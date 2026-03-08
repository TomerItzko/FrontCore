package red.vuis.mercfrontcore.mixin;

import com.boehmod.blockfront.game.impl.inf.InfectedDoor;
import com.boehmod.blockfront.registry.BFBlocks;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InfectedDoor.class)
public abstract class InfectedDoorMixin {
	@Definition(id = "getBlock", method = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;")
	@Definition(id = "IRON_DOOR", field = "Lnet/minecraft/block/Blocks;IRON_DOOR:Lnet/minecraft/block/Block;")
	@Expression("?.getBlock() != IRON_DOOR")
	@ModifyExpressionValue(
		method = "method_3697",
		at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private boolean addBlastDoor(boolean original, @Local BlockState target) {
		return original && target.getBlock() != BFBlocks.DOOR_BLAST.get();
	}
}
