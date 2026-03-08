/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.world.damage;

import com.boehmod.blockfront.common.world.damage.BFDamageSource;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class FirearmDamageSource
extends BFDamageSource {
    private final boolean isHeadshot;
    private final boolean isWallbang;
    private final boolean isCollateral;
    private final boolean isNoScope;

    public FirearmDamageSource(@NotNull Level level, @NotNull Entity entity, @NotNull ItemStack itemUsing, boolean isHeadshot, boolean isWallbang, boolean isCollateral, boolean isNoScope) {
        super((Holder<DamageType>)level.damageSources().generic().typeHolder(), entity, itemUsing);
        this.isHeadshot = isHeadshot;
        this.isWallbang = isWallbang;
        this.isCollateral = isCollateral;
        this.isNoScope = isNoScope;
    }

    public boolean isHeadshot() {
        return this.isHeadshot;
    }

    public boolean isWallbang() {
        return this.isWallbang;
    }

    public boolean isNoScope() {
        return this.isNoScope;
    }

    public boolean isCollateral() {
        return this.isCollateral;
    }

    @NotNull
    public Component getLocalizedDeathMessage(@NotNull LivingEntity entity) {
        ItemStack itemStack = this.getItemUsing();
        Entity entity2 = this.getEntity();
        String string = "death.attack.weapon.firearm" + (this.isWallbang ? ".wallbang" : "") + (this.isHeadshot ? ".headshot" : "") + (entity2 != null ? ".entity" : "");
        if (entity2 != null) {
            return Component.translatable((String)string, (Object[])new Object[]{entity.getDisplayName(), entity2.getDisplayName(), itemStack.getDisplayName()});
        }
        return Component.translatable((String)string, (Object[])new Object[]{entity.getDisplayName(), itemStack.getDisplayName()});
    }
}

