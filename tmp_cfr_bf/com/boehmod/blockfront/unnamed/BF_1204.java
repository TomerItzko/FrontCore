/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.event.tick.GunAimingTickable;
import com.boehmod.blockfront.client.event.tick.GunSpreadTickable;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.gun.GunSpreadConfig;
import com.boehmod.blockfront.common.gun.GunSpreadTarget;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BF_1204 {
    public static final float field_6940 = 0.5f;
    public static final float field_6941 = 1.0E-4f;
    public static final float field_6942 = 0.0f;

    public static float method_5875(@NotNull GunSpreadConfig gunSpreadConfig, @NotNull GunSpreadTarget gunSpreadTarget, boolean bl, @Nullable GunScopeConfig gunScopeConfig, float f, float f2) {
        float f3 = switch (gunSpreadTarget) {
            default -> throw new MatchException(null, null);
            case GunSpreadTarget.IDLE -> {
                if (bl) {
                    yield gunSpreadConfig.idleSpreadAiming();
                }
                yield gunSpreadConfig.idleSpread();
            }
            case GunSpreadTarget.WALKING -> {
                if (bl) {
                    yield gunSpreadConfig.walkingSpreadAiming();
                }
                yield gunSpreadConfig.walkingSpread();
            }
            case GunSpreadTarget.CRAWLING -> {
                if (bl) {
                    yield gunSpreadConfig.crawlingSpreadAiming();
                }
                yield gunSpreadConfig.crawlingSpread();
            }
            case GunSpreadTarget.SPRINTING, GunSpreadTarget.JUMPING -> gunSpreadConfig.jumpingSpread();
        };
        f3 *= 1.0f - f * 0.5f;
        if (bl && gunScopeConfig != null) {
            float f4 = gunScopeConfig.field_6808;
            float f5 = 1.0f - (1.0f - f4) * f2;
            f3 *= f5;
        }
        return Math.max(0.0f, f3);
    }

    @OnlyIn(value=Dist.CLIENT)
    public static float method_5876(@NotNull GunSpreadConfig gunSpreadConfig, @Nullable GunScopeConfig gunScopeConfig, boolean bl) {
        return BF_1204.method_5875(gunSpreadConfig, GunSpreadTickable.target, bl, gunScopeConfig, PlayerTickable.field_153, GunAimingTickable.field_167);
    }

    public static float method_5877(@NotNull GunSpreadConfig gunSpreadConfig, @Nullable GunScopeConfig gunScopeConfig, boolean bl, boolean bl2) {
        float f = bl ? 1.0f : 0.0f;
        float f2 = 1.0f;
        return BF_1204.method_5875(gunSpreadConfig, GunSpreadTarget.CRAWLING, bl2, gunScopeConfig, f, 1.0f);
    }

    public static float method_5878(@Nullable GunScopeConfig gunScopeConfig, @NotNull GunSpreadConfig gunSpreadConfig) {
        return BF_1204.method_5875(gunSpreadConfig, GunSpreadTarget.JUMPING, false, gunScopeConfig, 0.0f, 1.0f);
    }

    public static boolean method_5874(float f, float f2, float f3) {
        return f >= f2 - 1.0E-4f && f <= f3 + 1.0E-4f;
    }
}

