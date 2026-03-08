package red.vuis.mercfrontcore.mixin.client;

import java.util.Set;
import java.util.UUID;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.game.CapturePointGameClient;
import com.boehmod.blockfront.game.impl.dom.DominationGame;
import com.boehmod.blockfront.game.impl.dom.DominationGameClient;
import com.boehmod.blockfront.game.impl.dom.DominationPlayerManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DominationGameClient.class)
public abstract class DominationGameClientMixin extends CapturePointGameClient<DominationGame, DominationPlayerManager> {
	public DominationGameClientMixin(@NotNull BFClientManager manager, @NotNull DominationGame game, @NotNull ClientPlayerDataHandler dataHandler) {
		super(manager, game, dataHandler);
	}
}
