package red.vuis.mercfrontcore.client.data.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

import com.boehmod.blockfront.game.BFGameType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;

import red.vuis.mercfrontcore.data.AddonPacketCodecs;

@AllArgsConstructor
public enum MatchHudStyle {
	MODERN(Set.of(), false, false, false, false),
	LESS_MODERN(Set.of(), false, false, false, false),
	OLD(Set.of(BFGameType.DOMINATION, BFGameType.TEAM_DEATHMATCH, BFGameType.GUN_GAME, BFGameType.FREE_FOR_ALL), true, false, true, true),
	DAY_OF_DEFEAT(Set.of(BFGameType.DOMINATION), true, true, true, true),
	DAY_OF_INFAMY(Set.of(BFGameType.DOMINATION), true, false, true, true);
	
	public static final PacketCodec<ByteBuf, MatchHudStyle> PACKET_CODEC = AddonPacketCodecs.enumOrdinal(MatchHudStyle.values());
	
	@Getter
	private final Set<BFGameType> disabledGameElementTypes;
	@Getter
	private final boolean oldKillFeed;
	@Getter
	private final boolean rightKillFeed;
	@Getter
	private final boolean oldWaitingText;
	@Getter
	private final boolean oldCapturingText;
}
