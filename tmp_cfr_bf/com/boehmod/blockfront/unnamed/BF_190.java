/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.util.math.MathUtils;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class BF_190 {
    private static final int field_1139 = 180;
    @NotNull
    private final String field_1134;
    @NotNull
    private final UUID field_1140;
    @NotNull
    private final String field_1135;
    private int field_1138 = 0;
    private float field_1136 = 1.0f;
    private float field_1137 = 1.0f;

    public BF_190(@NotNull UUID uUID, @NotNull String string, @NotNull String string2) {
        this.field_1140 = uUID;
        this.field_1134 = string;
        this.field_1135 = string2;
    }

    public boolean method_814(@NotNull Minecraft minecraft) {
        if (this.field_1138 == 0) {
            minecraft.getNarrator().sayNow((Component)Component.literal((String)(this.field_1134 + ". " + Component.translatable((String)this.field_1135).getString())));
        }
        if (this.field_1138++ >= 180) {
            return true;
        }
        this.field_1137 = this.field_1136;
        this.field_1136 = Mth.lerp((float)0.075f, (float)this.field_1136, (float)((double)this.field_1138 > 135.0 ? 1.0f : 0.0f));
        return false;
    }

    @NotNull
    public UUID getUUID() {
        return this.field_1140;
    }

    @NotNull
    public String getName() {
        return this.field_1134;
    }

    @NotNull
    public String method_816() {
        return this.field_1135;
    }

    public float method_815(float f) {
        return MathUtils.lerpf1(this.field_1136, this.field_1137, f);
    }
}

