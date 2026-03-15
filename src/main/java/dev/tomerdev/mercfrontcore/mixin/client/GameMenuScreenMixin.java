package dev.tomerdev.mercfrontcore.mixin.client;

import dev.tomerdev.mercfrontcore.client.widget.PauseMenuIconWidget;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets", at = @At("TAIL"))
    private void mercfrontcore$addPauseMenuButtons(CallbackInfo ci) {
        if (client == null || client.player == null) {
            return;
        }

        int size = 24;
        int x = this.width / 2 + 142;
        int y = this.height / 2 - 94;
        GameMenuScreen parent = (GameMenuScreen) (Object) this;
        addDrawableChild(new PauseMenuIconWidget(x, y, size, parent, PauseMenuIconWidget.Action.LEADERBOARD));
        addDrawableChild(new PauseMenuIconWidget(x, y + 28, size, parent, PauseMenuIconWidget.Action.GUN_SKINS));
        addDrawableChild(new PauseMenuIconWidget(x, y + 56, size, parent, PauseMenuIconWidget.Action.LEAVE_MATCH));
    }
}
