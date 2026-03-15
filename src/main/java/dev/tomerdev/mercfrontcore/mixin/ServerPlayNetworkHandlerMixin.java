package dev.tomerdev.mercfrontcore.mixin;

import dev.tomerdev.mercfrontcore.util.MatchCompat;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onPlayerAction", at = @At("HEAD"), cancellable = true)
    private void mercfrontcore$blockDropAndOffhandInMatch(PlayerActionC2SPacket packet, CallbackInfo ci) {
        if (!player.isAlive() || !MatchCompat.isInGameSession(player)) {
            return;
        }
        PlayerActionC2SPacket.Action action = packet.getAction();
        if (action == PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND) {
            mercfrontcore$resyncInventory();
            ci.cancel();
        }
    }

    @Inject(method = "onPlayerInteractEntity", at = @At("HEAD"), cancellable = true)
    private void mercfrontcore$dropInvalidEntityInteract(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
        if (player == null || packet == null || player.getServerWorld() == null) {
            return;
        }

        if (!player.isAlive()) {
            ci.cancel();
            return;
        }

        Entity target = packet.getEntity(player.getServerWorld());
        if (target == null || !target.isAlive() || target.isRemoved()) {
            ci.cancel();
            return;
        }

        if (target.getId() == player.getId()) {
            ci.cancel();
        }
    }

    private void mercfrontcore$resyncInventory() {
        player.getInventory().markDirty();
        player.currentScreenHandler.sendContentUpdates();
        player.playerScreenHandler.sendContentUpdates();
    }
}
