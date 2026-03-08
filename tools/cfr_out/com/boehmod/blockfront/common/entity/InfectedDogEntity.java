/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class InfectedDogEntity
extends InfectedEntity {
    public InfectedDogEntity(EntityType<InfectedDogEntity> entityType, Level level) {
        super((EntityType<? extends InfectedEntity>)entityType, level);
    }

    public static AttributeSupplier.Builder getMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 128.0).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.MOVEMENT_SPEED, 0.6).add(Attributes.MAX_HEALTH, 100.0);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        Level level = this.level();
        if (!level.isClientSide && this.isAlive() && Math.random() < 0.05) {
            this.playAmbientSound();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return (SoundEvent)BFSounds.ENTITY_INFECTED_DOG_BARK.get();
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    @Override
    @NotNull
    public String getScoreboardName() {
        return String.valueOf(ChatFormatting.RED) + "Infected Hound";
    }
}

