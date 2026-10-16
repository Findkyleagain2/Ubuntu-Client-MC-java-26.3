package com.ubuntu.client.modules;

import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Items;

public class AutoTotem extends Module {
	public AutoTotem() {
		super("AutoTotem", "Moves a totem into your offhand when it is empty.");
	}

	@Override
	public void onTick() {
		if (mc().player == null || mc().gameMode == null) {
			return;
		}
		if (mc().player.containerMenu != mc().player.inventoryMenu) {
			return;
		}
		if (mc().player.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING) {
			return;
		}

		int totemSlot = -1;
		for (int i = 9; i < 45; i++) {
			if (mc().player.inventoryMenu.getSlot(i).getItem().getItem() == Items.TOTEM_OF_UNDYING) {
				totemSlot = i;
				break;
			}
		}
		if (totemSlot == -1) {
			return;
		}

		int containerId = mc().player.inventoryMenu.containerId;
		mc().gameMode.handleContainerInput(containerId, totemSlot, 0, ContainerInput.PICKUP, mc().player);
		mc().gameMode.handleContainerInput(containerId, 45, 0, ContainerInput.PICKUP, mc().player);
		mc().gameMode.handleContainerInput(containerId, totemSlot, 0, ContainerInput.PICKUP, mc().player);
	}
}
