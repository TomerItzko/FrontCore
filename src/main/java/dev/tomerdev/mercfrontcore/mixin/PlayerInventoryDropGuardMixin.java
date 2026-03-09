package dev.tomerdev.mercfrontcore.mixin;

import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.util.MatchCompat;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryDropGuardMixin {
    @Shadow
    @Final
    public PlayerEntity player;

    @Inject(method = "removeStack(II)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true, require = 0)
    private void mercfrontcore$preventDropRemoval(int slot, int amount, CallbackInfoReturnable<ItemStack> cir) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }
        if (!serverPlayer.isAlive() || !MatchCompat.isInGameSession(serverPlayer)) {
            return;
        }
        if (!mercfrontcore$isDropCall()) {
            return;
        }

        MercFrontCore.LOGGER.info("Blocked inventory removeStack drop path for {}", serverPlayer.getNameForScoreboard());
        serverPlayer.getInventory().markDirty();
        serverPlayer.currentScreenHandler.sendContentUpdates();
        serverPlayer.playerScreenHandler.sendContentUpdates();
        cir.setReturnValue(ItemStack.EMPTY);
    }

    private static boolean mercfrontcore$isDropCall() {
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            String c = ste.getClassName();
            String m = ste.getMethodName();
            if (m.contains("dropSelectedItem")
                || m.contains("dropItem")
                || m.contains("onPlayerAction")
                || c.contains("PacketListenerPlayerAction")
                || c.contains("ServerPlayNetworkHandler")) {
                return true;
            }
        }
        return false;
    }
}
