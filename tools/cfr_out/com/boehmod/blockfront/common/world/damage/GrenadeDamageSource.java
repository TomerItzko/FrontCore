/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.world.damage;

import com.boehmod.blockfront.common.world.damage.BFDamageSource;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class GrenadeDamageSource
extends BFDamageSource {
    @NotNull
    private final Vec3 sourcePosition;

    public GrenadeDamageSource(@NotNull Level level, @Nullable Entity entity, @NotNull ItemStack itemUsing, @NotNull Vec3 sourcePosition) {
        super((Holder<DamageType>)level.damageSources().explosion(null).typeHolder(), entity, itemUsing);
        this.sourcePosition = sourcePosition;
    }

    public Vec3 getSourcePosition() {
        return this.sourcePosition;
    }

    @NotNull
    public Component getLocalizedDeathMessage(@NotNull LivingEntity entity) {
        Entity entity2 = this.getDirectEntity();
        if (entity2 == null) {
            return Component.translatable((String)"death.attack.weapon.grenade", (Object[])new Object[]{entity.getDisplayName(), this.getItemUsing().getDisplayName()});
        }
        return Component.translatable((String)"death.attack.weapon.grenade.entity", (Object[])new Object[]{entity.getDisplayName(), this.getDirectEntity().getDisplayName(), this.getItemUsing().getDisplayName()});
    }
}

