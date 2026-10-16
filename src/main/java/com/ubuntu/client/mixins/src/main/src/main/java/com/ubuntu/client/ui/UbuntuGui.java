package com.ubuntu.client.ui;

import com.ubuntu.client.UbuntuClient;
import com.ubuntu.client.auth.PremiumVerifier;
import com.ubuntu.client.managers.ModuleManager;
import com.ubuntu.client.modules.Module;
import com.ubuntu.client.premium.modules.PremiumModule;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags; // Import for window flags
import imgui.type.ImBoolean; // For checkbox state management

public class UbuntuGui {
    private static boolean guiOpen = false; // State for the GUI window

    public static void toggleGui() {
        guiOpen = !guiOpen;
        // You might want to pause the game or release mouse here
        // MinecraftClient.getInstance().mouse.lockCursor();
        // MinecraftClient.getInstance().setScreen(guiOpen ? new ImGuiScreen() : null);
    }

    public static void render() {
        if (!guiOpen) return;

        // Apply glowing orange style
        ImGui.pushStyleColor(ImGui.ImGuiCol_WindowBg, 0.05f, 0.05f, 0.05f, 0.9f); // Darker background
        ImGui.pushStyleColor(ImGui.ImGuiCol_Border, 1.0f, 0.5f, 0.0f, 1.0f); // Orange border
        ImGui.pushStyleColor(ImGui.ImGuiCol_TitleBg, 0.8f, 0.4f, 0.0f, 1.0f); // Orange title bar (inactive)
        ImGui.pushStyleColor(ImGui.ImGuiCol_TitleBgActive, 1.0f, 0.5f, 0.0f, 1.0f); // Orange title bar (active)
        ImGui.pushStyleColor(ImGui.ImGuiCol_Header, 0.8f, 0.4f, 0.0f, 0.8f); // Orange collapsing headers
        ImGui.pushStyleColor(ImGui.ImGuiCol_HeaderHovered, 1.0f, 0.5f, 0.0f, 1.0f); // Orange collapsing headers (hover)
        ImGui.pushStyleColor(ImGui.ImGuiCol_CheckMark, 1.0f, 0.5f, 0.0f, 1.0f); // Orange checkmark
        ImGui.pushStyleColor(ImGui.ImGuiCol_Button, 0.8f, 0.4f, 0.0f, 1.0f); // Orange buttons
        ImGui.pushStyleColor(ImGui.ImGuiCol_ButtonHovered, 1.0f, 0.5f, 0.0f, 1.0f); // Orange buttons (hover)
        ImGui.pushStyleColor(ImGui.ImGuiCol_ButtonActive, 0.6f, 0.3f, 0.0f, 1.0f); // Orange buttons (active)

        // Begin the main window
        // ImGuiWindowFlags.NoCollapse prevents the window from being minimized
        // ImGuiWindowFlags.AlwaysAutoResize makes the window fit its content
        if (ImGui.begin("Ubuntu Client by KebinTools", new ImBoolean(true), ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoCollapse)) {

            ImGui.text("Welcome, " + (UbuntuClient.isPremium ? "Premium User!" : "Free User."));
            ImGui.separator();

            // --- Free Features Section ---
            if (ImGui.collapsingHeader("Free Features")) {
                for (Module module : ModuleManager.modules) {
                    ImBoolean enabled = new ImBoolean(module.isEnabled());
                    if (ImGui.checkbox(module.getName() + " ##" + module.getName(), enabled)) { // Unique ID for checkbox
                        module.setEnabled(enabled.get());
                    }
                    if (ImGui.isItemHovered()) {
                        ImGui.setTooltip(module.getDescription());
                    }
                }
            }

            // --- Premium Features Section ---
            if (UbuntuClient.isPremium) {
                if (ImGui.collapsingHeader("Premium Features")) {
                    for (PremiumModule module : ModuleManager.premiumModules) {
                        ImBoolean enabled = new ImBoolean(module.isEnabled());
                        if (ImGui.checkbox(module.getName() + " ##" + module.getName(), enabled)) { // Unique ID
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
        ImGui.popStyleColor(10); // Pop all 10 pushed colors
    }
}