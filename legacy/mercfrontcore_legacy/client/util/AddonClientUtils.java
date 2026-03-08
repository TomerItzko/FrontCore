package red.vuis.mercfrontcore.client.util;

import java.util.Locale;

import com.boehmod.blockfront.client.event.BFRenderFrameSubscriber;
import com.boehmod.blockfront.game.GameTeam;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class AddonClientUtils {
	private AddonClientUtils() {
	}
	
	public static Identifier getTeamIcon(@Nullable GameTeam team) {
		if (team == null) {
			return BFRenderFrameSubscriber.NEUTRAL_ICON_TEXTURE;
		}
		return switch (team.getName().toLowerCase(Locale.ROOT)) {
			case "allies" -> BFRenderFrameSubscriber.ALLIES_ICON_TEXTURE;
			case "axis" -> BFRenderFrameSubscriber.AXIS_ICON_TEXTURE;
			default -> BFRenderFrameSubscriber.NEUTRAL_ICON_TEXTURE;
		};
	}
}
