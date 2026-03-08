package red.vuis.mercfrontcore.mixin;

import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameStageManager;
import com.boehmod.blockfront.game.impl.conq.ConquestCapturePoint;
import com.boehmod.blockfront.game.impl.conq.ConquestGame;
import com.boehmod.blockfront.game.impl.conq.ConquestPlayerManager;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import red.vuis.mercfrontcore.command.bf.GameCommands;

@Mixin(ConquestGame.class)
public abstract class ConquestGameMixin extends AbstractGame<ConquestGame, ConquestPlayerManager, GameStageManager<ConquestGame, ConquestPlayerManager>> {
	@Shadow
	@Final
	@NotNull
	public ObjectList<ConquestCapturePoint> capturePoints;
	@Shadow
	@Final
	private AssetCommandBuilder command;
	
	public ConquestGameMixin(@NotNull BFAbstractManager<?, ?, ?> manager) {
		super(manager);
	}
	
	@Inject(
		method = "<init>",
		at = @At("TAIL")
	)
	private void addCommands(BFAbstractManager<?, ?, ?> par1, CallbackInfo ci) {
		GameCommands.capturePointCommands(
			command.subCommands.get("cpoint"),
			capturePoints,
			(player, name) -> new ConquestCapturePoint(playerManager, player, name)
		);
	}
}
