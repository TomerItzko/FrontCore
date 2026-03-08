package red.vuis.mercfrontcore.command;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

public record ArgumentTypePair<T>(
	Supplier<ArgumentType<T>> argumentType,
	BiFunction<CommandContext<?>, String, T> getter
) {
	public static ArgumentTypePair<Integer> integer() {
		return new ArgumentTypePair<>(IntegerArgumentType::integer, IntegerArgumentType::getInteger);
	}
	
	public static ArgumentTypePair<Integer> integer(final int min) {
		return new ArgumentTypePair<>(() -> IntegerArgumentType.integer(min), IntegerArgumentType::getInteger);
	}
	
	public static ArgumentTypePair<Integer> integer(final int min, final int max) {
		return new ArgumentTypePair<>(() -> IntegerArgumentType.integer(min, max), IntegerArgumentType::getInteger);
	}
}
