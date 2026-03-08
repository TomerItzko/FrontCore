/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.NextbotEntity;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MichaelNextbotEntity
extends NextbotEntity {
    public MichaelNextbotEntity(EntityType<? extends MichaelNextbotEntity> entityType, Level level) {
        super((EntityType<? extends NextbotEntity>)entityType, level);
    }

    @Override
    public String getTexture() {
        return "michael";
    }

    @Override
    public Holder<SoundEvent> getFunnySound() {
        return BFSounds.MATCH_MEMES_HUH;
    }

    @Override
    public int getFunnySoundInterval() {
        return 90;
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

