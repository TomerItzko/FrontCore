/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.item.GrenadeFragItem;
import com.boehmod.blockfront.registry.BFEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class GrenadeFlashItem
extends GrenadeFragItem {
    public GrenadeFlashItem(@NotNull String string, Item.Properties properties) {
        super(string, properties);
    }

    @Override
    protected BFProjectileEntity method_4090(@NotNull Level level) {
        return (BFProjectileEntity)((EntityType)BFEntityTypes.GRENADE_FLASH.get()).create(level);
    }

    @Override
    @NotNull
    public GrenadeFragItem.Type method_4087() {
        return GrenadeFragItem.Type.FLASH;
    }
}

