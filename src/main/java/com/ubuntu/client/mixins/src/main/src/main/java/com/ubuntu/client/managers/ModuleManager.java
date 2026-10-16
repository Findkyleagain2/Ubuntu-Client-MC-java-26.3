package com.ubuntu.client.managers;

import com.ubuntu.client.modules.*; // Free modules
import com.ubuntu.client.premium.modules.*; // Premium modules
import com.ubuntu.client.UbuntuClient; // For isPremium check

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static List<Module> modules = new ArrayList<>();
    public static List<PremiumModule> premiumModules = new ArrayList<>();

    public static void init() {
        UbuntuClient.LOGGER.info("Initializing modules...");

        // --- FREE FEATURES ---
        modules.add(new Fly()); // Movement
        modules.add(new KillAura()); // Combat
        modules.add(new AimAssist()); // Combat Helper
        modules.add(new AutoWalk()); // Utility
        modules.add(new AutoMine()); // Utility
        modules.add(new AutoCrystal()); // Combat
        modules.add(new AutoTotem()); // Combat Protection
        modules.add(new AutoRefill()); // Inventory Management
        modules.add(new ESP()); // Visual
        modules.add(new StorageESP()); // Visual
        modules.add(new BaseFinder()); // Utility

        // --- PREMIUM FEATURES ---
        // These are examples, actual premium modules would be more complex and optimized
        premiumModules.add(new Aimbot()); // Advanced Combat: Predictive, smooth, less detectable aim
        premiumModules.add(new AutoAnchor()); // Advanced Combat: Instant charge and explode anchors
        premiumModules.add(new DonutSmpAutoBuy()); // Exclusive Utility: Auto-buy specific items on DonutSMP
        premiumModules.add(new PacketVelocity()); // Movement: Full packet-level velocity negation
        premiumModules.add(new AntiBacktrack()); // Combat: Manipulates hitbox desync for evasion
        premiumModules.add(new Phase()); // Movement: Allows phasing through blocks
        premiumModules.add(new PredictiveProjectileAim()); // Combat: Calculates projectile trajectory
        premiumModules.add(new PacketMine()); // Utility: Mines multiple blocks via packets
        premiumModules.add(new IllegalStackFinder()); // Utility: Scans for illegal items in chunks

        UbuntuClient.LOGGER.info("Modules initialized: " + modules.size() + " free, " + premiumModules.size() + " premium.");
    }

    /**
    * Executes the onTick() method for all enabled modules.
    * Free modules always tick if enabled. Premium modules only tick if enabled AND premium is active.
    */
    public static void onTick() {
        // Tick free modules
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }

        // Tick premium modules only if premium is active
        if (UbuntuClient.isPremium) {
            for (PremiumModule module : premiumModules) {
                if (module.isEnabled()) {
                    module.onTick();
                }
            }
        }
    }
}