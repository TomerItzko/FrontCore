package dev.tomerdev.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.match.Loadout;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.client.screen.match.MatchSelectClassScreen")
public abstract class MatchSelectClassScreenLoadoutLockMixin {
    @Shadow
    public abstract void showErrorMessage(@NotNull Text message);

    @Inject(
        method = "method_896",
        at = @At("HEAD"),
        cancellable = true,
        require = 0,
        remap = false
    )
    private void mercfrontcore$blockLockedLoadoutSelection(BF_197 selection, CallbackInfo ci) {
        if (selection == null) {
            return;
        }
        Loadout loadout = selection.method_905();
        if (loadout == null) {
            return;
        }
        int minimumXp = loadout.getMinimumXp();
        if (minimumXp <= 0) {
            return;
        }
        MinecraftClient minecraft = MinecraftClient.getInstance();
        BFClientManager manager = BFClientManager.getInstance();
        if (minecraft == null || manager == null) {
            return;
        }
        if (!(manager.getPlayerDataHandler() instanceof ClientPlayerDataHandler dataHandler)) {
            return;
        }
        PlayerCloudData profile = dataHandler.getCloudData(minecraft);
        int effectiveXp = LoadoutXpCompat.getEffectiveXp(profile, selection.method_906());
        if (effectiveXp >= minimumXp) {
            return;
        }
        showErrorMessage(LoadoutXpCompat.createMinimumXpMessage(minimumXp, effectiveXp));
        ci.cancel();
    }
}
