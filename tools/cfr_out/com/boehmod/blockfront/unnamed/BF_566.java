/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.BotEntity;
import javax.annotation.Nullable;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_566 {
    @NotNull
    private final BotEntity field_2397;
    @Nullable
    private Vec3 field_2399 = null;
    private int field_2398 = 0;
    private double field_2400 = 0.0;

    public BF_566(@NotNull BotEntity botEntity) {
        this.field_2397 = botEntity;
    }

    public void method_2033(@NotNull RandomSource randomSource) {
        LivingEntity livingEntity = this.field_2397.getTarget();
        if (livingEntity != null && (this.field_2397.getSensing().hasLineOfSight((Entity)livingEntity) || this.field_2397.hasLineOfSight((Entity)livingEntity))) {
            this.field_2400 = livingEntity.getEyeY();
            this.field_2399 = livingEntity.position();
            this.field_2398 = -1;
        } else {
            if (this.field_2398 == -1) {
                this.field_2398 = randomSource.nextInt(7) * 20;
            }
            if (this.field_2398-- == 0) {
                this.field_2399 = null;
            }
        }
    }

    @Nullable
    public Vec3 method_2035() {
        return this.field_2399;
    }

    public double method_2036() {
        return this.field_2400;
    }

    public boolean method_2034() {
        return this.field_2399 != null;
    }
}

