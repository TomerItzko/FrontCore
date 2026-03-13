package dev.tomerdev.mercfrontcore;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public final class AddonConstants {
    public static final String MOD_ID = "mercfrontcore";
    public static final String MOD_NAME = "FrontCore";
    public static final String MOD_VERSION = "1.0.8-0.7.1.2b";
    public static final Logger LOGGER = LogUtils.getLogger();

    private AddonConstants() {
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
