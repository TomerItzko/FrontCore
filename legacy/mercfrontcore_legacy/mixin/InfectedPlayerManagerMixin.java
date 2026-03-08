package red.vuis.mercfrontcore.mixin;

import com.boehmod.blockfront.game.impl.inf.InfectedPlayerManager;
import com.boehmod.blockfront.registry.BFBlocks;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InfectedPlayerManager.class)
public class InfectedPlayerManagerMixin {
	@Definition(id = "block", local = @Local(argsOnly = true, type = Block.class))
	@Definition(id = "IRON_DOOR", field = "Lnet/minecraft/block/Blocks;IRON_DOOR:Lnet/minecraft/block/Block;")
	@Expression("block == IRON_DOOR")
	@ModifyExpressionValue(
		method = "method_2750",
		at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private boolean addBlastDoor(boolean original, @Local(argsOnly = true) Block block) {
		return original || block == BFBlocks.DOOR_BLAST.get();
	}
}
