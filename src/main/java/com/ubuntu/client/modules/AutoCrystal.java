package com.ubuntu.client.modules;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class AutoCrystal extends Module {
	public AutoCrystal() {
		super("AutoCrystal", "Automatically places and detonates end crystals.");
	}

	@Override
	public void onTick() {
		if (mc().player == null || mc().gameMode == null || !mc().player.isAlive()) {
			return;
		}
		BlockPos target = findBestCrystalPos();
		if (target == null) {
			return;
		}
		mc().gameMode.useItemOn(mc().player, InteractionHand.MAIN_HAND,
				new BlockHitResult(new Vec3(target.getX(), target.getY(), target.getZ()), Direction.UP, target, false));
	}

	private BlockPos findBestCrystalPos() {
		// TODO: implement crystal target finding.
		return null;
	}
}
