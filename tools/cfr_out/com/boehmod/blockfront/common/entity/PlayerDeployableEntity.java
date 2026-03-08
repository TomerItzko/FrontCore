/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.phys.AABB
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.registry.BFSounds;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerDeployableEntity
extends GrenadeEntity {
    public boolean field_2781 = false;
    public boolean field_2782 = false;
    public int field_2784 = 1200;
    public int field_2785 = 0;
    public double field_2780 = 1.5;

    public PlayerDeployableEntity(@NotNull EntityType<? extends PlayerDeployableEntity> entityType, @NotNull Level level) {
        super((EntityType<? extends GrenadeEntity>)entityType, level);
    }

    @Override
    protected float method_1938() {
        return 0.4f;
    }

    @Override
    protected float method_1939() {
        return 0.4f;
    }

    @Override
    public void tick() {
        AbstractGame<?, ?, ?> abstractGame;
        super.tick();
        Level level = this.level();
        if (level.isClientSide()) {
            return;
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame2 = abstractGame = this.owner != null ? bFAbstractManager.getGameWithPlayer(this.owner) : null;
        if (this.field_2781 && this.field_2785-- <= 0 && abstractGame != null) {
            Object obj = abstractGame.getPlayerManager();
            this.field_2785 = 80;
            AABB aABB = this.getBoundingBox().inflate(this.field_2780);
            List list = level.getEntitiesOfClass(ServerPlayer.class, aABB);
            list.forEach(serverPlayer -> this.method_2516(bFAbstractManager, (ServerPlayer)serverPlayer, abstractGame, (AbstractGamePlayerManager<?>)obj));
        }
        if (this.field_2784-- <= 0) {
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.NEUTRAL, 1.0f, 1.0f);
            this.discard();
        }
    }

    @Override
    public float method_1940() {
        return 0.0f;
    }

    @Override
    protected DeferredHolder<SoundEvent, SoundEvent> method_2279(@NotNull SoundType soundType) {
        return BFSounds.ITEM_MEDICALBAG_BOUNCE;
    }

    @Override
    public void method_1957() {
        if (this.field_2340) {
            return;
        }
        this.field_2781 = true;
        this.field_2340 = true;
    }

    void method_2516(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerPlayer serverPlayer, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager) {
        this.method_2515(bFAbstractManager, serverPlayer, abstractGame, abstractGamePlayerManager);
    }

    abstract void method_2515(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull ServerPlayer var2, @NotNull AbstractGame<?, ?, ?> var3, @NotNull AbstractGamePlayerManager<?> var4);

    @Override
    public void method_2281() {
    }

    @Override
    protected void method_2282() {
        this.field_2782 = true;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("hasBurst", this.field_2781);
        compoundTag.putBoolean("hasStopped", this.field_2782);
        compoundTag.putInt("activeTimer", this.field_2784);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.field_2781 = compoundTag.getBoolean("hasBurst");
        this.field_2782 = compoundTag.getBoolean("hasStopped");
        this.field_2784 = compoundTag.getInt("activeTimer");
    }
}

