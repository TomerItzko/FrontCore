/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.Holder
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.world.damage;

import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BFDamageSource
extends DamageSource {
    @NotNull
    private final ItemStack itemUsing;

    public BFDamageSource(@NotNull Holder<DamageType> damageType, @Nullable Entity entity, @NotNull ItemStack itemUsing) {
        super(damageType, entity);
        this.itemUsing = itemUsing;
    }

    @NotNull
    public ItemStack getItemUsing() {
        return this.itemUsing;
    }

    public boolean scalesWithDifficulty() {
        return false;
    }
}

