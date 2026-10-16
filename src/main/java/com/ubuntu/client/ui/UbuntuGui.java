package com.ubuntu.client.ui;

import com.ubuntu.client.UbuntuClient;
import com.ubuntu.client.auth.PremiumVerifier;
import com.ubuntu.client.managers.ModuleManager;
import com.ubuntu.client.modules.Module;
import com.ubuntu.client.premium.modules.PremiumModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class UbuntuGui extends Screen {
	private static final int COLUMNS = 2;
	private static final int BUTTON_WIDTH = 150;
	private static final int BUTTON_HEIGHT = 20;
	private static final int PADDING = 5;

	public UbuntuGui(Minecraft minecraft) {
		super(minecraft, minecraft.font, Component.literal("Ubuntu Client"));
	}

	public static void toggleGui() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.gui.screen() instanceof UbuntuGui) {
			mc.gui.setScreen(null);
		} else {
			mc.gui.setScreen(new UbuntuGui(mc));
		}
	}

	@Override
	protected void init() {
		List<Module> modules = new ArrayList<>(ModuleManager.modules);
		if (UbuntuClient.isPremium) {
			modules.addAll(ModuleManager.premiumModules);
		}

		int rows = (modules.size() + COLUMNS - 1) / COLUMNS;
		int totalWidth = COLUMNS * BUTTON_WIDTH + (COLUMNS - 1) * PADDING;
		int totalHeight = rows * (BUTTON_HEIGHT + PADDING);
		int startX = this.width / 2 - totalWidth / 2;
		int startY = Math.max(40, (this.height - totalHeight) / 2);

		for (int i = 0; i < modules.size(); i++) {
			Module module = modules.get(i);
			int x = startX + (i % COLUMNS) * (BUTTON_WIDTH + PADDING);
			int y = startY + (i / COLUMNS) * (BUTTON_HEIGHT + PADDING);
			addRenderableWidget(Button.builder(label(module), button -> {
				module.toggle();
				button.setMessage(label(module));
			}).bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
		}
	}

	private static Component label(Module module) {
		return Component.literal(module.getName() + ": " + (module.isEnabled() ? "ON" : "OFF"));
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float partialTick) {
		super.extractRenderState(extractor, mouseX, mouseY, partialTick);
		extractor.centeredText(this.font, this.title, this.width / 2, 15, 0xFFFFFF);

		if (!UbuntuClient.isPremium) {
			int footerY = this.height - 50;
			extractor.centeredText(this.font, "Unlock Premium for more features!", this.width / 2, footerY, 0xFFAA00);
			extractor.centeredText(this.font, "BTC Wallet: " + PremiumVerifier.WALLET_ADDRESS, this.width / 2, footerY + 12, 0xAAAAAA);
			extractor.centeredText(this.font, "Put transaction hash in config/ubuntu_key.txt", this.width / 2, footerY + 24, 0xAAAAAA);
			extractor.centeredText(this.font,
					"Minimum amount: " + String.format("%.8f BTC", PremiumVerifier.MIN_PREMIUM_AMOUNT_BTC),
					this.width / 2, footerY + 36, 0xAAAAAA);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
