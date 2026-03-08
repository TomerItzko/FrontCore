package red.vuis.mercfrontcore.util.match;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.boehmod.blockfront.common.match.MatchClass;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum IdentifiableMatchClass implements StringIdentifiable {
	RIFLEMAN(MatchClass.CLASS_RIFLEMAN.getKey(), MatchClass.CLASS_RIFLEMAN),
	LIGHT_INFANTRY(MatchClass.CLASS_LIGHT_INFANTRY.getKey(), MatchClass.CLASS_LIGHT_INFANTRY),
	ASSAULT(MatchClass.CLASS_ASSAULT.getKey(), MatchClass.CLASS_ASSAULT),
	SUPPORT(MatchClass.CLASS_SUPPORT.getKey(), MatchClass.CLASS_SUPPORT),
	MEDIC(MatchClass.CLASS_MEDIC.getKey(), MatchClass.CLASS_MEDIC),
	SNIPER(MatchClass.CLASS_SNIPER.getKey(), MatchClass.CLASS_SNIPER),
	GUNNER(MatchClass.CLASS_GUNNER.getKey(), MatchClass.CLASS_GUNNER),
	ANTI_TANK(MatchClass.CLASS_ANTI_TANK.getKey(), MatchClass.CLASS_ANTI_TANK),
	SPECIALIST(MatchClass.CLASS_SPECIALIST.getKey(), MatchClass.CLASS_SPECIALIST),
	COMMANDER(MatchClass.CLASS_COMMANDER.getKey(), MatchClass.CLASS_COMMANDER);
	
	public static final Codec<IdentifiableMatchClass> CODEC = StringIdentifiable.createCodec(IdentifiableMatchClass::values);
	
	private final String key;
	@Getter
	private final MatchClass value;
	
	@Override
	public String asString() {
		return key;
	}
}
