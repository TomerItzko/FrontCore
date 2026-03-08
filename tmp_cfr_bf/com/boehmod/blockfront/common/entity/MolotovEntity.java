/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.FireGrenadeEntity;
import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MolotovEntity
extends FireGrenadeEntity {
    public MolotovEntity(EntityType<? extends GrenadeEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.field_2581 > 0) {
            this.method_1957();
        }
    }

    @Override
    protected SoundEvent method_2271() {
        return (SoundEvent)BFSounds.ITEM_GRENADE_MOLOTOV_EXPLODE.get();
    }

    @Override
    public int method_2283() {
        return 1;
    }
}

