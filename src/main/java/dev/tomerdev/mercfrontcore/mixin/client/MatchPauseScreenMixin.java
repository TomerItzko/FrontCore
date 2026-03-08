package dev.tomerdev.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.match.MatchPauseScreen;
import net.minecraft.text.Text;
import net.neoforged.neoforge.client.gui.ModListScreen;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MatchPauseScreen.class)
public abstract class MatchPauseScreenMixin extends BFMenuScreen {
    public MatchPauseScreenMixin(@NotNull Text component) {
        super(component);
    }

    @ModifyConstant(
        method = "method_758",
        constant = @Constant(intValue = 90),
        require = 0
    )
    private int mercfrontcore$lowerFriendsButton(int constant) {
        return 105;
    }

    @ModifyConstant(
        method = "method_758",
        constant = @Constant(intValue = 120),
        require = 0
    )
    private int mercfrontcore$lowerExitButton(int constant) {
        return 135;
    }

    @Inject(
        method = "method_758",
        at = @At("TAIL"),
        require = 0
    )
    private void mercfrontcore$addModsButton(CallbackInfo ci) {
        int midX = width / 2;
        int midY = height / 2;
        int startY = midY - 60;
        addDrawableChild(
            new BFButton(
                midX - 75, startY + 75, 150, 15,
                Text.translatable("fml.menu.mods"),
                button -> client.setScreen(new ModListScreen((MatchPauseScreen) (Object) this))
            )
                .alignment(BFButton.Alignment.LEFT)
                .displayType(BFButton.DisplayType.NONE)
        );
    }

    @ModifyConstant(
        method = "renderBackground",
        constant = @Constant(intValue = 145),
        require = 0
    )
    private int mercfrontcore$expandBackground(int constant) {
        return 160;
    }
}
