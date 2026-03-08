/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.item.GrenadeFragItem;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class LandmineItem
extends GrenadeFragItem {
    public LandmineItem(@NotNull String string, Item.Properties properties) {
        super(string, properties);
    }

    @Override
    protected BFProjectileEntity method_4090(@NotNull Level level) {
        return (BFProjectileEntity)((EntityType)BFEntityTypes.LANDMINE.get()).create(level);
    }

    @Override
    @NotNull
    public GrenadeFragItem.Type method_4087() {
        return GrenadeFragItem.Type.LANDMINE;
    }

    @Override
    public boolean method_4093() {
        return false;
    }

    @Override
    @NotNull
    public SoundEvent method_4095() {
        return (SoundEvent)BFSounds.ITEM_LANDMINE_DEPLOY.get();
    }
}

