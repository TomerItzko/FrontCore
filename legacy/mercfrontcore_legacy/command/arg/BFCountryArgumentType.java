package red.vuis.mercfrontcore.command.arg;

import com.boehmod.blockfront.common.match.BFCountry;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import red.vuis.mercfrontcore.data.AddonCodecs;

public class BFCountryArgumentType extends EnumArgumentType<BFCountry> {
	private BFCountryArgumentType() {
		super(AddonCodecs.BF_COUNTRY, BFCountry::values);
	}
	
	public static BFCountryArgumentType country() {
		return new BFCountryArgumentType();
	}
	
	public static BFCountry getCountry(CommandContext<ServerCommandSource> context, String id) {
		return context.getArgument(id, BFCountry.class);
	}
}
