package com.ubuntu.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;

import java.nio.file.*;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

import com.ubuntu.client.auth.PremiumVerifier;
import com.ubuntu.client.managers.ModuleManager;
import com.ubuntu.client.ui.UbuntuGui;


public class UbuntuClient implements ClientModInitializer {
    public static final String MOD_ID = "ubuntuclient";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);
    public static boolean isPremium = false;
    public static boolean isGuiOpen = false;


    public static float targetYaw = 0.0f;
    public static float targetPitch = 0.0f;
    public static boolean shouldSpoofRotations = false;

    @Override
    public void onInitializeClient() {

        LOGGER.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(handler);
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        LOGGER.info("Initializing Ubuntu Client by KebinTools...");


        try {
            Path configDir = Paths.get("config");
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            Path txFile = configDir.resolve("ubuntu_key.txt");
            if (Files.exists(txFile)) {
                String txHash = Files.readString(txFile).trim();
                LOGGER.info("Attempting to verify premium status...");
                isPremium = PremiumVerifier.verifyPayment(txHash);
                if (isPremium) {
                    LOGGER.info("Premium status VERIFIED! Enjoy all features.");
                } else {
                    LOGGER.warning("Premium status NOT verified. Transaction hash invalid or not found. Running in Free Mode.");
                }
            } else {
                LOGGER.info("No 'ubuntu_key.txt' found. Running in Free Mode. Create this file with your BTC transaction hash to unlock Premium.");

                Files.writeString(txFile, "# Put your Bitcoin transaction hash here to unlock Premium features.\n" +
                "# Minimum amount required: " + String.format("%.8f BTC", PremiumVerifier.MIN_PREMIUM_