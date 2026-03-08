/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.List;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class LingeringGrenadeEntity
extends GrenadeEntity {
    public int field_2575 = 300;
    public boolean field_2572 = false;
    public boolean field_2573 = false;
    public float field_2574 = 0.1f;

    public LingeringGrenadeEntity(EntityType<? extends GrenadeEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        if (this.field_2340) {
            this.field_2574 = MathUtils.moveTowards(this.field_2574, 1.0f, 0.025f);
            if (level.isClientSide()) {
                this.method_2267();
            }
            AABB aABB = this.getBoundingBox().inflate((double)(this.method_2265() * this.field_2574));
            List list = level.getEntitiesOfClass(Entity.class, aABB);
            for (Entity entity : list) {
                this.method_2266(entity);
            }
        }
        if (this.isUnderWater() && !this.field_2572) {
            this.method_2270();
        }
        if (this.tickCount > this.field_2575) {
            this.method_2268();
            this.discard();
        }
    }

    protected abstract float method_2265();

    protected abstract void method_2266(Entity var1);

    @OnlyIn(value=Dist.CLIENT)
    protected abstract void method_2267();

    protected abstract SoundEvent method_2271();

    protected abstract void method_2268();

    @OnlyIn(value=Dist.CLIENT)
    protected abstract void method_2269();

    @Override
    public float method_1940() {
        return 0.0f;
    }

    @Override
    public void method_1957() {
        if (this.field_2572) {
            return;
        }
        if (!this.field_2340) {
            this.field_2340 = true;
            this.playSound(this.method_2271(), 3.0f, 1.0f);
            if (this.level().isClientSide()) {
                this.method_2269();
            }
        }
    }

    public void method_2270() {
        this.field_2572 = true;
        this.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0f, 1.0f);
    }

    @Override
    protected void method_2282() {
        this.field_2573 = true;
    }
}

