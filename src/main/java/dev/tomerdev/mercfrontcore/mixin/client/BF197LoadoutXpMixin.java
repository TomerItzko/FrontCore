package dev.tomerdev.mercfrontcore.mixin.client;

import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import dev.tomerdev.mercfrontcore.util.LoadoutXpCompat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.boehmod.blockfront.unnamed.BF_197")
public abstract class BF197LoadoutXpMixin {
    @Shadow @Final private MatchClass field_1241;

    @Redirect(
        method = "method_981",
        at = @At(
            value = "INVOKE",
            target = "Lcom/boehmod/blockfront/common/player/PlayerCloudData;getExpForClass(I)I"
        ),
        require = 0,
        remap = false
    )
    private int mercfrontcore$useEffectiveLoadoutXp(PlayerCloudData profile, int classOrdinal) {
        return LoadoutXpCompat.getEffectiveXp(profile, this.field_1241);
    }
}
