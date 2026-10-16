package com.ubuntu.client.ui;

import com.ubuntu.client.UbuntuClient;
import com.ubuntu.client.auth.PremiumVerifier;
import com.ubuntu.client.managers.ModuleManager;
import com.ubuntu.client.modules.Module;
import com.ubuntu.client.premium.modules.PremiumModule;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

public class UbuntuGui {
    private static boolean guiOpen = false;

    public static void toggleGui() {
        guiOpen = !guiOpen;



    }

    public static void render() {
        if (!guiOpen) return;


        ImGui.pushStyleColor(ImGui.ImGuiCol_WindowBg, 0.05f, 0.05f, 0.05f, 0.9f);
        ImGui.pushStyleColor(ImGui.ImGuiCol_Border, 1.0f, 0.5f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGui.ImGuiCol_TitleBg, 0.8f, 0.4f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGui.ImGuiCol_TitleBgActive, 1.0f, 0.5f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGui.ImGuiCol_Header, 0.8f, 0.4f, 0.0f, 0.8f);
        ImGui.pushStyleColor(ImGui.ImGuiCol_HeaderHovered, 1.0f, 0.5f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGui.ImGuiCol_CheckMark, 1.0f, 0.5f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGui.ImGuiCol_Button, 0.8f, 0.4f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGui.ImGuiCol_ButtonHovered, 1.0f, 0.5f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGui.ImGuiCol_ButtonActive, 0.6f, 0.3f, 0.0f, 1.0f);




        if (ImGui.begin("Ubuntu Client by KebinTools", new ImBoolean(true), ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoCollapse)) {

            ImGui.text("Welcome, " + (UbuntuClient.isPremium ? "Premium User!" : "Free User."));
            ImGui.separator();


            if (ImGui.collapsingHeader("Free Features")) {
                for (Module module : ModuleManager.modules) {
                    ImBoolean enabled = new ImBoolean(module.isEnabled());
                    if (ImGui.checkbox(module.getName() + " ##" + module.getName(), enabled)) {
                        module.setEnabled(enabled.get());
                    }
                    if (ImGui.isItemHovered()) {
                        ImGui.setTooltip(module.getDescription());
                    }
                }
            }


            if (UbuntuClient.isPremium) {
                if (ImGui.collapsingHeader("Premium Features")) {
                    for (PremiumModule module : ModuleManager.premiumModules) {
                        ImBoolean enabled = new ImBoolean(module.isEnabled());
                        if (ImGui.checkbox(module.getName() + " ##" + module.getName(), enabled)) {
                            module.setEnabled(enabled.get());
                        }
                        if (ImGui.isItemHovered()) {
                            ImGui.setTooltip(module.getDescription());
                        }
                    }
                }
            } else {
                ImGui.text("Unlock Premium for more features!");
                ImGui.text("BTC Wallet: " + PremiumVerifier.WALLET_ADDRESS);
                ImGui.text("Put transaction hash in config/ubuntu_key.txt");
                ImGui.text("Minimum amount: " + String.format("%.8f BTC", PremiumVerifier.MIN_PREMIUM_AMOUNT_BTC));
            }

            ImGui.end();
        }
        ImGui.popStyleColor(10);
    }
}