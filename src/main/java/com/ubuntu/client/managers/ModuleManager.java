package com.ubuntu.client.managers;

import com.ubuntu.client.UbuntuClient;
import com.ubuntu.client.modules.Module;
import com.ubuntu.client.premium.modules.PremiumModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
	public static final List<Module> modules = new ArrayList<>();
	public static final List<PremiumModule> premiumModules = new ArrayList<>();

	public static void init() {
		UbuntuClient.LOGGER.info("Initializing modules...");

		modules.add(new com.ubuntu.client.modules.Fly());
		modules.add(new com.ubuntu.client.modules.KillAura());
		modules.add(new com.ubuntu.client.modules.AimAssist());
		modules.add(new com.ubuntu.client.modules.AutoWalk());
		modules.add(new com.ubuntu.client.modules.AutoMine());
		modules.add(new com.ubuntu.client.modules.AutoCrystal());
		modules.add(new com.ubuntu.client.modules.AutoTotem());
		modules.add(new com.ubuntu.client.modules.AutoRefill());
		modules.add(new com.ubuntu.client.modules.ESP());
		modules.add(new com.ubuntu.client.modules.StorageESP());
		modules.add(new com.ubuntu.client.modules.BaseFinder());

		premiumModules.add(new com.ubuntu.client.premium.modules.Aimbot());
		premiumModules.add(new com.ubuntu.client.premium.modules.AutoAnchor());
		premiumModules.add(new com.ubuntu.client.premium.modules.DonutSmpAutoBuy());
		premiumModules.add(new com.ubuntu.client.premium.modules.PacketVelocity());
		premiumModules.add(new com.ubuntu.client.premium.modules.AntiBacktrack());
		premiumModules.add(new com.ubuntu.client.premium.modules.Phase());
		premiumModules.add(new com.ubuntu.client.premium.modules.PredictiveProjectileAim());
		premiumModules.add(new com.ubuntu.client.premium.modules.PacketMine());
		premiumModules.add(new com.ubuntu.client.premium.modules.IllegalStackFinder());

		UbuntuClient.LOGGER.info("Modules initialized: " + modules.size() + " free, " + premiumModules.size() + " premium.");
	}

	public static void onTick() {
		for (Module module : modules) {
			if (module.isEnabled()) {
				module.onTick();
			}
		}

		if (UbuntuClient.isPremium) {
			for (PremiumModule module : premiumModules) {
				if (module.isEnabled()) {
					module.onTick();
				}
			}
		}
	}
}
