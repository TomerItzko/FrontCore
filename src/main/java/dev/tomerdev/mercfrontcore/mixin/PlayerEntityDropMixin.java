package dev.tomerdev.mercfrontcore.mixin;

import dev.tomerdev.mercfrontcore.util.MatchCompat;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityDropMixin {
    @Inject(
        method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;",
        at = @At("HEAD"),
        cancellable = true,
        require = 0
    )
    private void mercfrontcore$blockItemDropInMatch(
        ItemStack stack,
        boolean throwRandomly,
        boolean retainOwnership,
        CallbackInfoReturnable<ItemEntity> cir
    ) {
        if (!((Object) this instanceof ServerPlayerEntity player)) {
            return;
        }
        if (!player.isAlive() || !MatchCompat.isInGameSession(player)) {
            return;
        }

        player.getInventory().markDirty();
        player.currentScreenHandler.sendContentUpdates();
        player.playerScreenHandler.sendContentUpdates();
        cir.setReturnValue(null);
    }
}
