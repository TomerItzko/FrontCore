package red.vuis.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.common.net.packet.BFGamePacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.llamalad7.mixinextras.sugar.Local;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import red.vuis.mercfrontcore.client.data.config.AddonClientConfig;
import red.vuis.mercfrontcore.ex.AbstractGameEx;

@Mixin(BFClientPacketHandlers.class)
public abstract class PacketAppliersMixin {
	@Inject(
		method = "game",
		at = @At("TAIL")
	)
	private static void gameCustom(BFGamePacket packet, IPayloadContext context, CallbackInfo ci, @Local(ordinal = 0) AbstractGame<?, ?, ?> game) {
		if (game == null) {
			AddonClientConfig.INSTANCE.forced = null;
			return;
		}
		
		AbstractGameEx gameEx = (AbstractGameEx) game;
		if (gameEx.mercfrontcore$isForceClientConfig()) {
			AddonClientConfig.INSTANCE.forced = gameEx.mercfrontcore$getClientConfig();
		}
	}
}
