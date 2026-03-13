package dev.tomerdev.mercfrontcore.mixin.client;

import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_197;
import dev.tomerdev.mercfrontcore.util.LoadoutXpCompat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.client.screen.match.AbstractMatchSelectClassScreen")
public abstract class AbstractMatchSelectClassScreenLoadoutXpMixin {
    @Shadow protected BF_197 field_1214;
    @Shadow public abstract void showErrorMessage(@NotNull Text message);

    @Redirect(
        method = "method_5485",
        at = @At(
            value = "INVOKE",
            target = "Lcom/boehmod/blockfront/common/player/PlayerCloudData;getExpForClass(I)I"
        ),
        require = 0,
        remap = false
    )
    private int mercfrontcore$useEffectiveLoadoutXp(PlayerCloudData profile, int classOrdinal) {
        MatchClass matchClass = this.field_1214 != null ? this.field_1214.method_906() : null;
        return LoadoutXpCompat.getEffectiveXp(profile, matchClass);
    }

    @Inject(method = "tick", at = @At("TAIL"), require = 0, remap = false)
    private void mercfrontcore$showLockedLoadoutHint(CallbackInfo ci) {
        if (this.field_1214 == null) {
            return;
        }
        var loadout = this.field_1214.method_905();
        var matchClass = this.field_1214.method_906();
        if (loadout == null || matchClass == null) {
            return;
        }
        int minimumXp = loadout.getMinimumXp();
        if (minimumXp <= 0) {
            return;
        }
        var minecraft = MinecraftClient.getInstance();
        var manager = com.boehmod.blockfront.client.BFClientManager.getInstance();
        if (minecraft == null || manager == null) {
            return;
        }
        if (!(manager.getPlayerDataHandler() instanceof com.boehmod.blockfront.client.player.ClientPlayerDataHandler dataHandler)) {
            return;
        }
        PlayerCloudData profile = dataHandler.getCloudData(minecraft);
        int effectiveXp = LoadoutXpCompat.getEffectiveXp(profile, matchClass);
        if (effectiveXp >= minimumXp) {
            return;
        }
        showErrorMessage(LoadoutXpCompat.createMinimumXpMessage(minimumXp, effectiveXp));
    }
}
