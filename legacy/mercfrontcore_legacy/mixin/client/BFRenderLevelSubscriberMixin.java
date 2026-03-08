package red.vuis.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.event.BFRenderLevelSubscriber;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import red.vuis.mercfrontcore.client.data.config.AddonClientConfig;

@Mixin(BFRenderLevelSubscriber.class)
public abstract class BFRenderLevelSubscriberMixin {
	@Redirect(
		method = "renderAfterLevel",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Math;max(FF)F",
			ordinal = 0
		)
	)
	private static float disableDeathBlur(float a, float b) {
		return AddonClientConfig.getEnableDeathFade() ? Math.max(a, b) : 0;
	}
}
