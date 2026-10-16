public class AutoCrystal extends Module {
    public void onTick() {
        if (!mc.player.isAlive()) return;
        BlockPos target = findBestCrystalPos();
        if (target != null) {
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(new Vec3d(target.getX(), target.getY(), target.getZ()), Direction.UP, target, false));
        }
    }
}
}