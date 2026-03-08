package dev.tomerdev.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.screen.SidebarScreen;
import com.boehmod.blockfront.client.screen.title.sidebar.TitleSidebarScreen;
import com.boehmod.blockfront.util.BFStyles;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.neoforged.neoforge.client.gui.ModListScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleSidebarScreen.class)
public abstract class TitleSidebarScreenMixin extends SidebarScreen {
    public TitleSidebarScreenMixin(@Nullable Screen screen, @NotNull Text component) {
        super(screen, component);
    }

    @Inject(
        method = "method_758",
        at = @At("TAIL"),
        require = 0
    )
    private void mercfrontcore$addModsButton(CallbackInfo ci) {
        int width = Math.min(160, this.width - 16);
        int x = 8;
        int y = 44;
        addDrawableChild(
            new BFButton(
                x, y, width, 18,
                Text.translatable("fml.menu.mods").fillStyle(BFStyles.BOLD),
                button -> client.setScreen(new ModListScreen((TitleSidebarScreen) (Object) this))
            )
                .tip(Text.translatable("mercfrontcore.menu.button.mods.tip"))
                .alignment(BFButton.Alignment.LEFT)
        );
    }
}
