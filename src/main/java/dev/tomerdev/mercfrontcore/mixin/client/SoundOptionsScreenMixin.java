package dev.tomerdev.mercfrontcore.mixin.client;

import net.minecraft.client.gui.screen.option.SoundOptionsScreen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;

@Mixin(SoundOptionsScreen.class)
public abstract class SoundOptionsScreenMixin extends net.minecraft.client.gui.screen.Screen {
    protected SoundOptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "addOptions", at = @At("TAIL"), require = 0)
    private void mercfrontcore$addMercFrontCoreVolumeControls(CallbackInfo ci) {
        int centerX = this.width / 2;
        int y = this.height - 74;

        this.addDrawableChild(new MercFrontCoreVolumeSlider(
            centerX - 155, y, 150, 20, true
        ));
        this.addDrawableChild(new MercFrontCoreVolumeSlider(
            centerX + 5, y, 150, 20, false
        ));
    }

    private static final class MercFrontCoreVolumeSlider extends SliderWidget {
        private final boolean gun;

        private MercFrontCoreVolumeSlider(int x, int y, int width, int height, boolean gun) {
            super(
                x,
                y,
                width,
                height,
                Text.empty(),
                toSliderValue(gun
                    ? MercFrontCoreConfigManager.get().audio.gunSfxVolume
                    : MercFrontCoreConfigManager.get().audio.grenadeSfxVolume)
            );
            this.gun = gun;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            float volume = fromSliderValue(this.value);
            int percent = Math.round(volume * 100.0f);
            Text label = gun
                ? Text.translatable("mercfrontcore.options.sound.gun_volume")
                : Text.translatable("mercfrontcore.options.sound.grenade_volume");
            setMessage(Text.literal(label.getString() + ": " + percent + "%"));
        }

        @Override
        protected void applyValue() {
            float volume = fromSliderValue(this.value);
            if (gun) {
                MercFrontCoreConfigManager.get().audio.gunSfxVolume = volume;
            } else {
                MercFrontCoreConfigManager.get().audio.grenadeSfxVolume = volume;
            }
            MercFrontCoreConfigManager.save();
        }

        private static double toSliderValue(float volume) {
            return MathHelper.clamp(volume, 0.0f, 1.0f);
        }

        private static float fromSliderValue(double slider) {
            return (float) MathHelper.clamp(slider, 0.0, 1.0);
        }
    }
}
