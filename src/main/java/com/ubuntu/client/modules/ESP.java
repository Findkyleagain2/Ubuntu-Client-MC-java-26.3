package com.ubuntu.client.modules;

public class ESP extends Module {
	public ESP() {
		super("ESP", "Highlights entities through walls.");
	}

	@Override
	public void onTick() {
		// Rendering happens in the render hooks; nothing to do per tick.
	}
}
