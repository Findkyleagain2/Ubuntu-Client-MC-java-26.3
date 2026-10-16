package com.ubuntu.client.modules;

public class StorageESP extends Module {
	public StorageESP() {
		super("StorageESP", "Highlights containers through walls.");
	}

	@Override
	public void onTick() {
		// Rendering happens in the render hooks; nothing to do per tick.
	}
}
