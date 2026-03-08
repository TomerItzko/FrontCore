package red.vuis.mercfrontcore.util;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public final class AddonCommandUtils {
	private AddonCommandUtils() {
	}
	
	public static @Nullable ServerPlayerEntity getContextPlayer(CommandContext<ServerCommandSource> context) {
		ServerCommandSource stack = context.getSource();
		ServerPlayerEntity player = stack.getPlayer();
		if (player == null) {
			stack.output.sendMessage(Text.translatable("mercfrontcore.message.command.error.player"));
			return null;
		}
		return player;
	}
}
