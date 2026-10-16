package com.ubuntu.client.managers;

import com.ubuntu.client.modules.*;
import com.ubuntu.client.premium.modules.*;
import com.ubuntu.client.UbuntuClient;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static List<Module> modules = new ArrayList<>();
    public static List<PremiumModule> premiumModules = new ArrayList<>();

    public static void init() {
        UbuntuClient.LOGGER.info("Initializing modules...");


        modules.add(new Fly());
        modules.add(new KillAura());
        modules.add(new AimAssist());
        modules.add(new AutoWalk());
        modules.add(new AutoMine());
        modules.add(new AutoCrystal());
        modules.add(new AutoTotem());
        modules.add(new AutoRefill());
        modules.add(new ESP());
        modules.add(new StorageESP());
        modules.add(new BaseFinder());



        premiumModules.add(new Aimbot());
        premiumModules.add(new AutoAnchor());
        premiumModules.add(new DonutSmpAutoBuy());
        premiumModules.add(new PacketVelocity());
        premiumModules.add(new AntiBacktrack());
        premiumModules.add(new Phase());
        premiumModules.add(new PredictiveProjectileAim());
        premiumModules.add(new PacketMine());
        premiumModules.add(new IllegalStackFinder());

        UbuntuClient.LOGGER.info("Modules initialized: " + modules.size() + " free, " + premiumModules.size() + " premium.");
    }

    /**
    * Executes the onTick() method for all enabled modules.
    * Free modules always tick if enabled. Premium modules only tick if enabled AND premium is active.
    */
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