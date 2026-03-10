package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.common.match.Loadout;
import dev.tomerdev.mercfrontcore.data.PlayerGunSkinStore;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.util.BFUtils")
public abstract class BFUtilsLoadoutSkinMixin {
    @Inject(
        method = "giveLoadout",
        at = @At("TAIL"),
        require = 0,
        remap = false
    )
    private static void mercfrontcore$reapplySelectedGunSkins(
        ServerWorld world,
        ServerPlayerEntity player,
        Loadout loadout,
        boolean keepUnset,
        CallbackInfo ci
    ) {
        PlayerGunSkinStore.getInstance().applyToPlayer(player);
    }
}
