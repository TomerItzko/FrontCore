/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.gun;

import org.jetbrains.annotations.NotNull;

public record GunSpreadConfig(float jumpingSpread, float walkingSpread, float walkingSpreadAiming, float idleSpread, float idleSpreadAiming, float crawlingSpread, float crawlingSpreadAiming) {
    public static final float field_6773 = 0.6f;
    public static float currentSpread = 0.0f;
    public static float prevSpread = 0.0f;

    @NotNull
    public GunSpreadConfig method_3733(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        return new GunSpreadConfig(this.jumpingSpread * f7, this.walkingSpread * f3, this.walkingSpreadAiming * f4, this.idleSpread * f, this.idleSpreadAiming * f2, this.crawlingSpread * f5, this.crawlingSpreadAiming * f6);
    }
}

