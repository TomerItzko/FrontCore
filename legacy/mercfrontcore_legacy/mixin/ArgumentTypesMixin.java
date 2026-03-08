package red.vuis.mercfrontcore.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import red.vuis.mercfrontcore.command.arg.BFCountryArgumentType;
import red.vuis.mercfrontcore.command.arg.MatchClassArgumentType;

@Mixin(ArgumentTypes.class)
public abstract class ArgumentTypesMixin {
	@Shadow
	private static <A extends ArgumentType<?>, T extends ArgumentSerializer.ArgumentTypeProperties<A>> ArgumentSerializer<A, T> register(Registry<ArgumentSerializer<?, ?>> registry, String id, Class<? extends A> clazz, ArgumentSerializer<A, T> serializer) {
		return null;
	}
	
	@Inject(
		method = "register(Lnet/minecraft/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;",
		at = @At("HEAD")
	)
	private static void registerCustom(Registry<ArgumentSerializer<?, ?>> registry, CallbackInfoReturnable<ArgumentSerializer<?, ?>> cir) {
		register(registry, "mercfrontcore:bf_country", BFCountryArgumentType.class, ConstantArgumentSerializer.of(BFCountryArgumentType::country));
		register(registry, "mercfrontcore:match_class", MatchClassArgumentType.class, ConstantArgumentSerializer.of(MatchClassArgumentType::matchClass));
	}
}
