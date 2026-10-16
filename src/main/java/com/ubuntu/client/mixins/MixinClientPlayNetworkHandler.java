package com.ubuntu.client.mixins;

import com.ubuntu.client.premium.modules.PacketVelocity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPlayNetworkHandler {
	@Inject(method = "handleSetEntityMotion", at = @At("HEAD"), cancellable = true)
	private void ubuntu$onSetEntityMotion(ClientboundSetEntityMotionPacket packet, CallbackInfo ci) {
		if (!PacketVelocity.shouldCancelVelocity()) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null && packet.id() == mc.player.getId()) {
			ci.cancel();
		}
	}
}
