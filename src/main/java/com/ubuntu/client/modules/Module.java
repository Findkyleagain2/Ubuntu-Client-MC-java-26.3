package com.ubuntu.client.modules;

import com.ubuntu.client.UbuntuClient;
import net.minecraft.client.Minecraft;

public abstract class Module {
	private final String name;
	private final String description;
	private boolean enabled;

	protected Module(String name, String description) {
		this.name = name;
		this.description = description;
	}

	protected static Minecraft mc() {
		return Minecraft.getInstance();
	}

	public final String getName() {
		return name;
	}

	public final String getDescription() {
		return description;
	}

	public final boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean value) {
		if (enabled == value) {
			return;
		}
		enabled = value;
		try {
			if (enabled) {
				onEnable();
			} else {
				onDisable();
			}
		} catch (RuntimeException e) {
			UbuntuClient.LOGGER.severe("Module " + name + " failed to change state: " + e);
		}
	}

	public final void toggle() {
		setEnabled(!enabled);
	}

	public void onTick() {
	}

	protected void onEnable() {
	}

	protected void onDisable() {
	}
}
