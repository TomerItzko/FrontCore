package dev.tomerdev.mercfrontcore.data;

import com.boehmod.blockfront.common.gun.GunFireMode;
import com.boehmod.blockfront.common.gun.GunTriggerSpawnType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Arrays;
import java.util.function.Function;

public final class AddonCodecs {
    public static final Codec<GunFireMode> GUN_FIRE_MODE = stringKey(
        GunFireMode.values(),
        mode -> mode.getName().toLowerCase(),
        key -> "Invalid fire mode: " + key
    );

    public static final Codec<GunTriggerSpawnType> GUN_TRIGGER_SPAWN_TYPE = stringKey(
        GunTriggerSpawnType.values(),
        mode -> mode.name().toLowerCase(),
        key -> "Invalid fire type: " + key
    );

    private AddonCodecs() {
    }

    public static <T> Codec<T> stringKey(T[] values, Function<T, String> keyFunc, Function<String, String> errorMsg) {
        return Codec.STRING.comapFlatMap(
            str -> Arrays.stream(values)
                .filter(value -> keyFunc.apply(value).equals(str))
                .findFirst()
                .map(DataResult::success)
                .orElseGet(() -> DataResult.error(() -> errorMsg.apply(str))),
            keyFunc
        );
    }
}
