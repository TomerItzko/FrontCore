/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.RocketEntity;
import com.boehmod.blockfront.common.entity.base.IProducedProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AntiAirRocketEntity
extends RocketEntity
implements IProducedProjectileEntity {
    public AntiAirRocketEntity(EntityType<? extends AntiAirRocketEntity> entityType, Level level) {
        super((EntityType<? extends RocketEntity>)entityType, level);
    }

    @Override
    public float method_1940() {
        return 0.05f;
    }

    @Override
    public int method_2508() {
        return 15;
    }

    @Override
    protected boolean method_2506() {
        return false;
    }
}

