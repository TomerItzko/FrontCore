/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.projectile.ThrowableItemProjectile
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.EntityHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class AcidBallEntity
extends ThrowableItemProjectile {
    public AcidBallEntity(EntityType<AcidBallEntity> entityType, Level level) {
        super(entityType, level);
    }

    public void baseTick() {
        super.baseTick();
        Level level = this.level();
        if (Math.random() < 0.5) {
            level.addParticle((ParticleOptions)BFParticleTypes.ACID_SPLAT_PARTICLE.get(), this.getX(), this.getEyeY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Level level = this.level();
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.AMBIENT, 1.0f, 1.0f);
        entityHitResult.getEntity().hurt(level.damageSources().thrown((Entity)this, null), 3.0f);
    }

    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        Level level = this.level();
        if (!level.isClientSide()) {
            level.broadcastEntityEvent((Entity)this, (byte)3);
            this.discard();
            level.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)BFSounds.ENTITY_ACIDBALL_SPLAT.get(), SoundSource.AMBIENT, 1.0f, 1.0f);
        }
    }

    @NotNull
    public Item getDefaultItem() {
        return (Item)BFItems.ACID_BALL.get();
    }

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    @OnlyIn(value=Dist.CLIENT)
    public void handleEntityEvent(byte by) {
        Level level = this.level();
        if (by == 3) {
            for (int i = 0; i < 8; ++i) {
                level.addParticle((ParticleOptions)BFParticleTypes.ACID_GROUND_SPLAT_PARTICLE.get(), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }
}

