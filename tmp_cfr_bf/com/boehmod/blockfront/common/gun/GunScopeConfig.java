/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun;

import com.boehmod.blockfront.util.BFRes;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GunScopeConfig {
    public static final GunScopeConfig DEFAULT = new GunScopeConfig();
    public static final float field_3847 = 0.3f;
    public static final float field_3848 = 0.25f;
    public static final float field_3849 = 0.4f;
    public static final float field_3850 = 0.1f;
    public final boolean field_3845;
    public final float field_3851;
    public final float field_3852;
    public final boolean field_3846;
    @Nullable
    public ResourceLocation field_3843;
    public float field_6808 = 1.0f;

    public GunScopeConfig() {
        this(true, 0.3f, 0.25f, null, true);
    }

    public GunScopeConfig(float f) {
        this(true, f, 0.25f, null, true);
    }

    public GunScopeConfig(float f, float f2) {
        this(true, f, f2, null, true);
    }

    public GunScopeConfig(boolean bl, float f, float f2) {
        this(bl, f, f2, null, true);
    }

    public GunScopeConfig(boolean bl, float f, float f2, boolean bl2) {
        this(bl, f, f2, null, bl2);
    }

    public GunScopeConfig(boolean bl, String string, boolean bl2) {
        this(bl, 0.3f, 0.25f, string, bl2);
    }

    public GunScopeConfig(boolean bl, float f, String string, boolean bl2) {
        this(bl, f, 0.25f, string, bl2);
    }

    public GunScopeConfig(boolean bl, float f, float f2, String string, boolean bl2) {
        this.field_3845 = bl;
        this.field_3851 = f;
        this.field_3852 = f2;
        this.field_3846 = bl2;
        this.field_3843 = string != null ? BFRes.loc("textures/misc/sights/" + string + ".png") : null;
    }

    @NotNull
    public GunScopeConfig method_5748(float f) {
        this.field_6808 = f;
        return this;
    }

    public static enum Type {
        DEFAULT,
        SCOPE;

    }
}

