/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Quaterniond
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.unnamed.BF_643;
import com.boehmod.blockfront.util.BFLog;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;

public class CameraEntity
extends Entity {
    @NotNull
    private BF_643 field_2752 = BF_643.NONE;
    @NotNull
    public Quaterniond field_2753 = new Quaterniond();

    public CameraEntity(@NotNull EntityType<? extends CameraEntity> entityType, @NotNull Level level) {
        super(entityType, level);
    }

    public void method_2490(@NotNull BF_643 bF_643) {
        this.field_2752 = bF_643;
    }

    public BF_643 method_2489() {
        return this.field_2752;
    }

    public void tick() {
        super.tick();
        if (this.field_2752 == BF_643.NONE) {
            BFLog.log("Camera not assigned a type! Discarding.", new Object[0]);
            this.discard();
        }
    }

    public boolean isNoGravity() {
        return true;
    }

    public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
    }

    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    }

    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    }

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

