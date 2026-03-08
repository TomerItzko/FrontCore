/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Holder
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageType
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.entity.base.IProducedProjectileEntity;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.world.damage.BFDamageSource;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MelonRocketEntity
extends BFProjectileEntity
implements IProducedProjectileEntity {
    private int field_2761 = 0;

    public MelonRocketEntity(EntityType<? extends MelonRocketEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void method_1934(@NotNull Player player, float f, @NotNull ItemStack itemStack, float f2, float f3) {
        this.owner = player;
        this.method_1951(itemStack);
        float f4 = f;
        int n = (int)(player.getDeltaMovement().x * 100.0) + 7;
        if (n < 0) {
            f4 += 0.3f;
        }
        this.method_1955(player.getEyePosition(), player.getYRot(), player.getXRot(), f, f4);
    }

    @Override
    public void method_1935(@NotNull Vec3 vec3, float f, @NotNull ItemStack itemStack, float f2, float f3) {
        this.method_1951(itemStack);
        this.method_1955(vec3, f2, f3, f, f);
    }

    public boolean canCollideWith(@NotNull Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player)entity;
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            assert (bFAbstractManager != null) : "Mod manager is null!";
            Object obj = bFAbstractManager.getPlayerDataHandler();
            Object d = ((PlayerDataHandler)obj).getPlayerData(player);
            return !((BFAbstractPlayerData)d).isOutOfGame();
        }
        return true;
    }

    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if (entity.isRemoved()) {
            return;
        }
        if (entity instanceof Player) {
            Player player = (Player)entity;
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            assert (bFAbstractManager != null) : "Mod manager is null!";
            Object obj = bFAbstractManager.getPlayerDataHandler();
            if (((BFAbstractPlayerData)((PlayerDataHandler)obj).getPlayerData(player)).isOutOfGame()) {
                return;
            }
        }
        this.method_1957();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.field_2761++ >= this.method_2499()) {
            this.method_1957();
        }
    }

    @Override
    public float method_1940() {
        return 1.0f;
    }

    @Override
    public float method_1941() {
        return 0.0f;
    }

    public int method_2499() {
        return 60;
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        EntityHitResult entityHitResult;
        Entity entity;
        Level level = this.level();
        if (hitResult instanceof EntityHitResult && (entity = (entityHitResult = (EntityHitResult)hitResult).getEntity()) instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            livingEntity.hurt((DamageSource)new BFDamageSource((Holder<DamageType>)level.damageSources().generic().typeHolder(), (Entity)this.owner, this.getItem()), 40.0f);
        }
        this.setDeltaMovement(0.0, 0.0, 0.0);
        if (!this.field_2340) {
            this.method_1957();
        }
    }

    @Override
    public void method_1957() {
        this.discard();
    }

    @Override
    protected boolean method_1943() {
        return false;
    }
}

