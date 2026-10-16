public class AutoTotem extends Module {
    public void onTick() {
        if (mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
            int slot = findItemInInventory(Items.TOTEM_OF_UNDYING);
            if (slot != -1) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);
            }
        }
    }
}
}