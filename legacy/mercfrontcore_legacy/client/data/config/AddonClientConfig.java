package red.vuis.mercfrontcore.client.data.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import red.vuis.mercfrontcore.util.math.IntBounds;
import red.vuis.mercfrontcore.util.property.PropertyEntry;
import red.vuis.mercfrontcore.util.property.PropertyHandler;
import red.vuis.mercfrontcore.util.property.PropertyRegistry;

public final class AddonClientConfig {
	public static final AddonClientConfig INSTANCE;
	public static final ModConfigSpec SPEC;
	
	private final ModConfigSpec.EnumValue<MatchHudStyle> matchHudStyle;
	private final ModConfigSpec.BooleanValue renderCorpses;
	private final ModConfigSpec.BooleanValue enableDeathFade;
	private final ModConfigSpec.IntValue killFeedLines;
	
	public Data forced = null;
	
	private AddonClientConfig(ModConfigSpec.Builder builder) {
		matchHudStyle = builder.defineEnum("match_hud_style", Data.MATCH_HUD_STYLE_DEFAULT);
		renderCorpses = builder.define("render_corpses", Data.RENDER_CORPSES_DEFAULT);
		enableDeathFade = builder.define("enable_death_fade", Data.ENABLE_DEATH_FADE_DEFAULT);
		killFeedLines = builder.defineInRange("kill_feed_lines", Data.KILL_FEED_LINES_DEFAULT, 0, Integer.MAX_VALUE);
	}
	
	public static MatchHudStyle getMatchHudStyle() {
		return INSTANCE.forced != null ? INSTANCE.forced.matchHudStyle : INSTANCE.matchHudStyle.get();
	}
	
	public static boolean getRenderCorpses() {
		return INSTANCE.forced != null ? INSTANCE.forced.renderCorpses : INSTANCE.renderCorpses.getAsBoolean();
	}
	
	public static boolean getEnableDeathFade() {
		return INSTANCE.forced != null ? INSTANCE.forced.enableDeathFade : INSTANCE.enableDeathFade.getAsBoolean();
	}
	
	public static int getKillFeedLines() {
		return INSTANCE.forced != null ? INSTANCE.forced.killFeedLines : INSTANCE.killFeedLines.getAsInt();
	}
	
	static {
		Pair<AddonClientConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(AddonClientConfig::new);
		INSTANCE = pair.getLeft();
		SPEC = pair.getRight();
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	public static final class Data {
		private MatchHudStyle matchHudStyle = MATCH_HUD_STYLE_DEFAULT;
		private boolean renderCorpses = RENDER_CORPSES_DEFAULT;
		private boolean enableDeathFade = ENABLE_DEATH_FADE_DEFAULT;
		private int killFeedLines = KILL_FEED_LINES_DEFAULT;
		
		public static final MatchHudStyle MATCH_HUD_STYLE_DEFAULT = MatchHudStyle.MODERN;
		public static final boolean RENDER_CORPSES_DEFAULT = true;
		public static final boolean ENABLE_DEATH_FADE_DEFAULT = true;
		public static final int KILL_FEED_LINES_DEFAULT = 5;
		
		public static final IntBounds KILL_FEED_LINES_BOUNDS = IntBounds.ofMin(0);
		
		public static final PropertyRegistry PROPERTIES = new PropertyRegistry(
			new PropertyHandler<>(Data.class, Map.of(
				"matchHudStyle", new PropertyEntry<>(MatchHudStyle::valueOf, Data::setMatchHudStyle),
				"renderCorpses", new PropertyEntry<>(Boolean::parseBoolean, Data::setRenderCorpses),
				"enableDeathFade", new PropertyEntry<>(Boolean::parseBoolean, Data::setEnableDeathFade),
				"killFeedLines", new PropertyEntry<>(KILL_FEED_LINES_BOUNDS::parse, Data::setKillFeedLines)
			))
		);
		
		public static final PacketCodec<ByteBuf, Data> PACKET_CODEC = PacketCodec.tuple(
			MatchHudStyle.PACKET_CODEC, Data::getMatchHudStyle,
			PacketCodecs.BOOL, Data::isRenderCorpses,
			PacketCodecs.BOOL, Data::isEnableDeathFade,
			PacketCodecs.VAR_INT, Data::getKillFeedLines,
			Data::new
		);
	}
}
