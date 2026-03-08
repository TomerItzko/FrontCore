package red.vuis.mercfrontcore.mixin.client;

import java.util.Set;
import java.util.UUID;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGame;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGameClient;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchPlayerManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import red.vuis.mercfrontcore.client.data.config.AddonClientConfig;
import red.vuis.mercfrontcore.client.render.game.TeamDeathmatchAddonRendering;

@Mixin(TeamDeathmatchGameClient.class)
public abstract class TeamDeathmatchGameClientMixin extends AbstractGameClient<TeamDeathmatchGame, TeamDeathmatchPlayerManager> {
	public TeamDeathmatchGameClientMixin(@NotNull BFClientManager manager, @NotNull TeamDeathmatchGame game, @NotNull ClientPlayerDataHandler dataHandler) {
		super(manager, game, dataHandler);
	}
	
	@Inject(
		method = "method_2722",
		at = @At("TAIL")
	)
	private void renderOldHud(
		@NotNull MinecraftClient client,
		@NotNull BFClientManager manager,
		@NotNull ClientPlayerEntity player,
		@NotNull ClientWorld world,
		@NotNull BFClientPlayerData playerData,
		@NotNull DrawContext context,
		@NotNull TextRenderer textRenderer,
		@NotNull MatrixStack matrices,
		@NotNull VertexConsumerProvider vertexConsumers,
		@NotNull Set<UUID> players,
		int width,
		int height,
		int midX,
		int midY,
		float renderTime,
		float delta,
		CallbackInfo ci
	) {
		switch (AddonClientConfig.getMatchHudStyle()) {
			case OLD -> TeamDeathmatchAddonRendering.oldMatchHud(client, manager, dataHandler, game, method_2678(), context, textRenderer, matrices, midX);
		}
	}
}
