package red.vuis.mercfrontcore.mixin;

import com.boehmod.blockfront.common.command.AssetsCommand;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import red.vuis.mercfrontcore.command.bf.AssetsCommandEx;

@Mixin(AssetsCommand.class)
public abstract class AssetsCommandMixin {
	@Redirect(
		method = "register",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/command/CommandManager;argument(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;",
			ordinal = 2
		)
	)
	private static RequiredArgumentBuilder<ServerCommandSource, ?> addEditSuggestions(String name, ArgumentType<?> type) {
		return CommandManager.argument(name, type).suggests(AssetsCommandEx::suggestAssetArgs);
	}
}
