package dev.tomerdev.mercfrontcore.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientScreenRedirectMixin {
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true, require = 0)
    private void mercfrontcore$forceVanillaTitleForBfOfflineScreens(Screen screen, CallbackInfo ci) {
        if (screen == null || screen.getClass() == TitleScreen.class) {
            return;
        }

        MinecraftClient client = (MinecraftClient) (Object) this;
        if (client.world != null) {
            return;
        }

        String className = screen.getClass().getName();
        if (mercfrontcore$isBfIntroOrTitleLikeScreen(className)) {
            client.setScreen(new TitleScreen());
            ci.cancel();
        }
    }

    private static boolean mercfrontcore$isBfIntroOrTitleLikeScreen(String className) {
        return className.startsWith("com.boehmod.blockfront.client.screen.intro.")
            || className.contains(".client.screen.title.")
            || className.endsWith("LobbyTitleScreen")
            || className.endsWith("BFIntroScreen")
            || className.contains("Offline")
            || className.equals("com.boehmod.blockfront.cZ");
    }
}
