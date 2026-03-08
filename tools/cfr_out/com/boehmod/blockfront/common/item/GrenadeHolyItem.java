/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.item.GrenadeFragItem;
import com.boehmod.blockfront.registry.BFEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class GrenadeHolyItem
extends GrenadeFragItem {
    public GrenadeHolyItem(@NotNull String string, Item.Properties properties) {
        super(string, properties);
    }

    @Override
    protected BFProjectileEntity method_4090(@NotNull Level level) {
        return (BFProjectileEntity)((EntityType)BFEntityTypes.GRENADE_HOLY.get()).create(level);
    }
}

