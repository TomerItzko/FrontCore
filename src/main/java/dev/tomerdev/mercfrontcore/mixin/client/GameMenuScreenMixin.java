package dev.tomerdev.mercfrontcore.mixin.client;

import dev.tomerdev.mercfrontcore.client.screen.OwnedGunSkinsScreen;
import java.util.Comparator;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
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
    private void mercfrontcore$addGunSkinsButton(CallbackInfo ci) {
        if (client == null || client.player == null) {
            return;
        }

        ClickableWidget topButton = children().stream()
            .filter(ClickableWidget.class::isInstance)
            .map(ClickableWidget.class::cast)
            .filter(widget -> widget.getWidth() >= 200)
            .min(Comparator.comparingInt(ClickableWidget::getY))
            .orElse(null);
        if (topButton == null) {
            return;
        }

        int gunSkinsY = topButton.getY() + 24;

        for (Element element : children()) {
            if (element instanceof ClickableWidget widget && widget.getY() > topButton.getY()) {
                widget.setY(widget.getY() + 24);
            }
        }

        addDrawableChild(ButtonWidget.builder(
            Text.literal("Gun Skins"),
            button -> client.setScreen(new OwnedGunSkinsScreen((GameMenuScreen) (Object) this))
        ).dimensions(this.width / 2 - 102, gunSkinsY, 204, 20).build());
    }
}
