/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.MatchEntity;
import com.boehmod.blockfront.common.net.packet.BFExplosionPacket;
import com.boehmod.blockfront.common.world.ExplosionType;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_809;
import com.boehmod.blockfront.util.PacketUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class BombEntity
extends MatchEntity {
    private static final EntityDataAccessor<Integer> field_2344 = SynchedEntityData.defineId(BombEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> field_2345 = SynchedEntityData.defineId(BombEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final int field_2347 = 800;
    private boolean field_2346;
    private int field_2348 = 20;

    public BombEntity(@NotNull EntityType<? extends BombEntity> entityType, Level level) {
        super(entityType, level);
    }

    private void method_1966(@NotNull Vec3 vec3) {
        Level level = this.level();
        level.playSound(null, vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.ITEM_BOMB_EXPLODE.get(), SoundSource.NEUTRAL, 20.0f, 1.0f);
        if (!this.field_2346) {
            if (this.game != null) {
                BFExplosionPacket bFExplosionPacket = new BFExplosionPacket(ExplosionType.ARTILLERY_EXPLOSION, this.position());
                PacketUtils.sendToGamePlayers(bFExplosionPacket, this.game);
                AbstractGame abstractGame = this.game;
                if (abstractGame instanceof BF_809) {
                    BF_809 bF_809 = (BF_809)((Object)abstractGame);
                    bF_809.method_3382(this, level);
                }
            }
            this.field_2346 = true;
        }
        this.discard();
    }

    public void method_1963() {
        this.method_1961(true);
        int n = this.method_1962();
        float f = (float)n / 45.0f;
        float f2 = 1.0f - f / 20.0f;
        float f3 = 1.0f + 5.0f * f2;
        this.playSound((SoundEvent)BFSounds.ITEM_BOMB_DEFUSE.get(), f3, 1.0f);
    }

    public int method_1962() {
        return (Integer)this.entityData.get(field_2344);
    }

    public void method_1967(int n) {
        this.entityData.set(field_2344, (Object)n);
    }

    public void method_1964() {
        int n = this.method_1962();
        if (n > 0) {
            this.entityData.set(field_2344, (Object)(n - 1));
        }
    }

    public boolean method_1965() {
        return (Boolean)this.entityData.get(field_2345);
    }

    public void method_1961(boolean bl) {
        this.entityData.set(field_2345, (Object)bl);
    }

    @Override
    public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(field_2344, (Object)800).define(field_2345, (Object)false);
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        Vec3 vec3 = this.position();
        int n = this.method_1962();
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, (double)-0.1f, 0.0));
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.method_1965()) {
            return;
        }
        if (this.field_2348-- <= 0) {
            float f = (float)n / 45.0f;
            float f2 = 1.0f - f / 20.0f;
            float f3 = 2.0f + 5.0f * f2;
            float f4 = 1.0f + f2;
            this.field_2348 = (int)(f * 2.0f);
            level.playSound(null, vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.ITEM_BOMB_BEEP.get(), SoundSource.NEUTRAL, f3, f4);
        }
        this.method_1964();
        if (this.method_1962() <= 0) {
            this.method_1966(vec3);
        }
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    }

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

