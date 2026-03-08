package red.vuis.mercfrontcore.command.arg;

import com.boehmod.blockfront.common.match.MatchClass;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import red.vuis.mercfrontcore.util.match.IdentifiableMatchClass;

public class MatchClassArgumentType extends EnumArgumentType<IdentifiableMatchClass> {
	private MatchClassArgumentType() {
		super(IdentifiableMatchClass.CODEC, IdentifiableMatchClass::values);
	}
	
	public static MatchClassArgumentType matchClass() {
		return new MatchClassArgumentType();
	}
	
	public static MatchClass getMatchClass(CommandContext<ServerCommandSource> context, String id) {
		return context.getArgument(id, IdentifiableMatchClass.class).getValue();
	}
}
