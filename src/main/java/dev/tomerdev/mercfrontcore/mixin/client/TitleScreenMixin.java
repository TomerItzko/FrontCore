package dev.tomerdev.mercfrontcore.mixin.client;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.BFClient;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.net.ConnectionMode;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.tomerdev.mercfrontcore.client.screen.ForcedTitleScreen;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    private TitleScreenMixin(Text title) {
        super(title);
    }

    @Redirect(
        method = "initWidgetsNormal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;",
            ordinal = 2
        ),
        require = 0
    )
    private MutableText mercfrontcore$replaceRealmsButtonText(String key) {
        if (((Object) this).getClass() == TitleScreen.class
            || (Object) this instanceof ForcedTitleScreen
            || mercfrontcore$isOfflineConnectionMode()) {
            return Text.translatable(key);
        }
        return BlockFront.DISPLAY_NAME_COMPONENT.copyContentOnly();
    }

    @Inject(
        method = "lambda$createNormalMenuOptions$9",
        at = @At("HEAD"),
        cancellable = true,
        require = 0
    )
    private void mercfrontcore$openLobbyScreen(ButtonWidget button, CallbackInfo ci) {
        if (client == null) {
            return;
        }
        if (((Object) this).getClass() == TitleScreen.class
            || (Object) this instanceof ForcedTitleScreen
            || mercfrontcore$isOfflineConnectionMode()) {
            return;
        }
        client.setScreen(new LobbyTitleScreen());
        ci.cancel();
    }

    private static boolean mercfrontcore$isOfflineConnectionMode() {
        try {
            BFClientManager manager = BFClient.getManager();
            return manager != null
                && manager.getConnectionManager() != null
                && manager.getConnectionManager().getConnectionMode() == ConnectionMode.OFFLINE;
        } catch (Throwable ignored) {
        }
        return false;
    }
}
