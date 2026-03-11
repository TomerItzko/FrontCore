package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.common.BFAbstractManager;
import dev.tomerdev.mercfrontcore.data.WinnerSkinDropManager;
import java.util.UUID;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.game.AbstractGame")
public abstract class AbstractGameWinnerSkinDropMixin {
    @Inject(method = "onPlayerMatchFinished", at = @At("TAIL"))
    private void mercfrontcore$maybeAwardWinnerSkinDrop(
        BFAbstractManager<?, ?, ?> manager,
        boolean awardAchievement,
        UUID uuid,
        Text subtitle,
        boolean victory,
        CallbackInfo ci
    ) {
        WinnerSkinDropManager.maybeAwardWinnerSkin(this, manager, uuid, victory);
    }
}
