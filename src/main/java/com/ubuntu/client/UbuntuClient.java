package com.ubuntu.client;

import com.ubuntu.client.auth.PremiumVerifier;
import com.ubuntu.client.managers.ModuleManager;
import com.ubuntu.client.ui.UbuntuGui;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class UbuntuClient implements ClientModInitializer {
	public static final String MOD_ID = "ubuntuclient";
	public static final Logger LOGGER = Logger.getLogger(MOD_ID);

	public static volatile boolean isPremium = false;

	// Rotation spoofing hooks, consumed by combat modules.
	public static float targetYaw = 0.0f;
	public static float targetPitch = 0.0f;
	public static boolean shouldSpoofRotations = false;

	private static KeyMapping toggleGuiKey;

	@Override
	public void onInitializeClient() {
		LOGGER.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		LOGGER.addHandler(handler);
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");

		LOGGER.info("Initializing Ubuntu Client by KebinTools...");

		ModuleManager.init();
		verifyPremiumAsync();

		Thread bridge = new SocketBridge();
		bridge.setName("ubuntu-socket-bridge");
		bridge.setDaemon(true);
		bridge.start();

		toggleGuiKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.ubuntuclient.gui", InputConstants.Type.KEYBOARD, InputConstants.KEY_RSHIFT, KeyMapping.Category.MISC));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleGuiKey.consumeClick()) {
				UbuntuGui.toggleGui();
			}
			ModuleManager.onTick();
		});

		LOGGER.info("Ubuntu Client initialized. Press Right Shift to open the menu.");
	}

	private void verifyPremiumAsync() {
		try {
			Path configDir = Paths.get("config");
			if (!Files.exists(configDir)) {
				Files.createDirectories(configDir);
			}
			Path txFile = configDir.resolve("ubuntu_key.txt");
			if (!Files.exists(txFile)) {
				Files.writeString(txFile, "# Put your Bitcoin transaction hash here to unlock Premium features.\n" +
						"# Minimum amount required: " + String.format("%.8f BTC", PremiumVerifier.MIN_PREMIUM_AMOUNT_BTC) + "\n");
				LOGGER.info("No 'ubuntu_key.txt' found. Running in Free Mode. Create this file with your BTC transaction hash to unlock Premium.");
				return;
			}
			String txHash = readTxHash(txFile);
			if (txHash == null) {
				LOGGER.info("No transaction hash set in 'ubuntu_key.txt'. Running in Free Mode.");
				return;
			}
			Thread verifier = new Thread(() -> {
				LOGGER.info("Attempting to verify premium status...");
				if (PremiumVerifier.verifyPayment(txHash)) {
					isPremium = true;
					LOGGER.info("Premium status VERIFIED! Enjoy all features.");
				} else {
					LOGGER.warning("Premium status NOT verified. Transaction hash invalid or not found. Running in Free Mode.");
				}
			}, "ubuntu-premium-verifier");
			verifier.setDaemon(true);
			verifier.start();
		} catch (IOException e) {
			LOGGER.severe("Failed to prepare config directory: " + e.getMessage());
		}
	}

	private static String readTxHash(Path file) throws IOException {
		for (String line : Files.readAllLines(file)) {
			String trimmed = line.trim();
			if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
				return trimmed;
			}
		}
		return null;
	}
}
