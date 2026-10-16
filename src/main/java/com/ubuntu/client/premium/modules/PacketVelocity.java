package com.ubuntu.client.premium.modules;

import com.ubuntu.client.UbuntuClient;

public class PacketVelocity extends PremiumModule {
	private static volatile boolean active;

	public PacketVelocity() {
		super("PacketVelocity", "Cancels incoming velocity packets to reduce knockback.");
	}

	public static boolean shouldCancelVelocity() {
		return active && UbuntuClient.isPremium;
	}

	@Override
	protected void onEnable() {
		active = true;
	}

	@Override
	protected void onDisable() {
		active = false;
	}
}
