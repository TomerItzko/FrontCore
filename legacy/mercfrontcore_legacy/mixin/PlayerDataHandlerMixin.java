package red.vuis.mercfrontcore.mixin;

import java.util.Map;
import java.util.UUID;

import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import red.vuis.mercfrontcore.data.AddonCommonData;

@Mixin(PlayerDataHandler.class)
public abstract class PlayerDataHandlerMixin {
	@Inject(
		method = "getCloudProfile(Ljava/util/UUID;)Lcom/boehmod/blockfront/common/player/PlayerCloudData;",
		at = @At("HEAD"),
		cancellable = true
	)
	private void handleProfileOverrides(UUID uuid, CallbackInfoReturnable<PlayerCloudData> cir) {
		Map<UUID, PlayerCloudData> profileOverrides = AddonCommonData.getInstance().profileOverrides;
		if (profileOverrides.containsKey(uuid)) {
			cir.setReturnValue(profileOverrides.get(uuid));
		}
	}
}
