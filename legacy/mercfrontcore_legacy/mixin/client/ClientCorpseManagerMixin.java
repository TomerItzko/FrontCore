package red.vuis.mercfrontcore.mixin.client;

import java.util.List;
import java.util.function.Predicate;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.corpse.ClientCorpseManager;
import com.boehmod.blockfront.client.corpse.render.RagdollRenderer;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import red.vuis.mercfrontcore.client.data.config.AddonClientConfig;

@Mixin(ClientCorpseManager.class)
public abstract class ClientCorpseManagerMixin {
	@WrapOperation(
		method = "update",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;removeIf(Ljava/util/function/Predicate;)Z"
		)
	)
	private boolean checkFrustrumIsNull(List<RagdollRenderer> instance, Predicate<RagdollRenderer> predicate, Operation<Boolean> operation, @Local Frustum frustum) {
		if (frustum != null) {
			return operation.call(instance, predicate);
		}
		return false;
	}
	
	@Inject(
		method = "render",
		at = @At("HEAD"),
		cancellable = true
	)
	private void checkCorpsesEnabled(MinecraftClient client, BFClientManager manager, BFClientPlayerData playerData, ClientWorld world, ClientPlayerEntity player, MatrixStack matrices, float delta, CallbackInfo ci) {
		if (!AddonClientConfig.getRenderCorpses()) {
			ci.cancel();
		}
	}
}
