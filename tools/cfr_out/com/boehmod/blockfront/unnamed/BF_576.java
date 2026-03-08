/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.phys.AABB
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.BotEntity;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class BF_576<E extends LivingEntity>
extends NearestAttackableTargetGoal<E> {
    public static final int field_2444 = 2;
    private static final int field_2445 = 1;
    @NotNull
    private final BotEntity field_2443;

    public BF_576(@NotNull BotEntity botEntity, @NotNull Class<E> clazz, boolean bl) {
        super((Mob)botEntity, clazz, 10, bl, false, null);
        this.field_2443 = botEntity;
    }

    @NotNull
    protected AABB getTargetSearchArea(double d) {
        return this.mob.getBoundingBox().inflate(d, 16.0, d);
    }

    protected void findTarget() {
        List list = this.mob.level().getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()), livingEntity -> this.mob.canAttack(livingEntity) && this.mob.hasLineOfSight((Entity)livingEntity));
        for (LivingEntity livingEntity2 : list) {
            if (this.field_2443.field_2390.containsKey(livingEntity2)) continue;
            this.field_2443.field_2390.put(livingEntity2, new BF_577(livingEntity2));
        }
    }

    public boolean canUse() {
        if (super.canUse()) {
            this.target = this.field_2443.field_2390.values().stream().filter(bF_577 -> bF_577.method_2061() >= 2).map(BF_577::method_2057).findFirst().orElse(null);
            return this.target != null;
        }
        return false;
    }

    public static class BF_577 {
        @NotNull
        private final LivingEntity field_2446;
        private int field_2447 = 0;
        private int field_2448 = 1;

        public BF_577(@NotNull LivingEntity livingEntity) {
            this.field_2446 = livingEntity;
        }

        @NotNull
        public LivingEntity method_2057() {
            return this.field_2446;
        }

        public int method_2061() {
            return this.field_2447;
        }

        public void method_2058() {
            ++this.field_2447;
        }

        public void method_2059() {
            --this.field_2448;
        }

        public void method_2060() {
            this.field_2448 = 1;
        }

        public boolean method_2062() {
            return this.field_2448 <= 0;
        }
    }
}

