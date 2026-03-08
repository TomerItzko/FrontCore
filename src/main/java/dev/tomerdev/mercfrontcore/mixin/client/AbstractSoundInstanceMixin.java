package dev.tomerdev.mercfrontcore.mixin.client;

import dev.tomerdev.mercfrontcore.client.sound.SoundVolumeResolver;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSoundInstance.class)
public abstract class AbstractSoundInstanceMixin {
    @Inject(method = "getVolume", at = @At("RETURN"), cancellable = true)
    private void mercfrontcore$scaleVolume(CallbackInfoReturnable<Float> cir) {
        float original = cir.getReturnValueF();
        if (original <= 0.0f) {
            return;
        }

        SoundInstance self = (SoundInstance) (Object) this;
        String soundName = resolveSoundName(self);
        float scale = SoundVolumeResolver.resolveScale(soundName);
        if (Math.abs(scale - 1.0f) < 0.001f) {
            return;
        }

        cir.setReturnValue(original * scale);
    }

    private static String resolveSoundName(SoundInstance sound) {
        try {
            Object value = sound.getClass().getMethod("getLocation").invoke(sound);
            if (value instanceof Identifier id) {
                return id.getNamespace() + ":" + id.getPath();
            }
        } catch (Throwable ignored) {
        }
        try {
            Object value = sound.getClass().getMethod("getId").invoke(sound);
            if (value instanceof Identifier id) {
                return id.getNamespace() + ":" + id.getPath();
            }
        } catch (Throwable ignored) {
        }
        return "";
    }
}
