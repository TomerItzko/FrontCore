package red.vuis.mercfrontcore.util.function;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface BiArgCommand<S, T, U> {
	int run(CommandContext<S> context, T t, U u) throws CommandSyntaxException;
}
