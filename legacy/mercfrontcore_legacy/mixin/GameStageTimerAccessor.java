package red.vuis.mercfrontcore.mixin;

import com.boehmod.blockfront.game.GameStageTimer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameStageTimer.class)
public interface GameStageTimerAccessor {
	@Accessor("warningThreshold")
	int getWarningThreshold();
}
