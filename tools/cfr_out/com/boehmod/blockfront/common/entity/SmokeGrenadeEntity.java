/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.entity.FireGrenadeEntity;
import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.common.entity.LingeringGrenadeEntity;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.ClientUtils;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class SmokeGrenadeEntity
extends LingeringGrenadeEntity {
    public static final int field_2586 = 32;

    public SmokeGrenadeEntity(EntityType<? extends GrenadeEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected float method_2265() {
        return 2.0f;
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    @Override
    protected void method_2266(Entity entity) {
        FireGrenadeEntity fireGrenadeEntity;
        if (entity instanceof FireGrenadeEntity && (fireGrenadeEntity = (FireGrenadeEntity)entity).method_1946() && !fireGrenadeEntity.field_2572) {
            fireGrenadeEntity.method_2270();
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected void method_2267() {
        Minecraft minecraft = Minecraft.getInstance();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        if (minecraft.level != null) {
            Vec3 vec3 = this.position().add(0.0, 0.5, 0.0);
            ClientUtils.spawnParticleCircle(minecraft, bFClientManager, minecraft.level, (ParticleOptions)BFParticleTypes.POOF_PARTICLE.get(), vec3, 0.0f, 2.0f, 32, 0.075f);
        }
    }

    @Override
    protected SoundEvent method_2271() {
        return (SoundEvent)BFSounds.ITEM_GRENADE_SMOKE_EXPLODE.get();
    }

    @Override
    protected void method_2268() {
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected void method_2269() {
        Level level = this.level();
        Vec3 vec3 = this.position();
        Minecraft minecraft = Minecraft.getInstance();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientLevel clientLevel = minecraft.level;
        if (clientLevel == null) {
            return;
        }
        double d = 1.0;
        double d2 = Math.max(1.1f, this.method_2265() * this.field_2574);
        level.addParticle((ParticleOptions)ParticleTypes.FLASH, true, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        for (int i = 0; i < 25; ++i) {
            double d3 = ThreadLocalRandom.current().nextDouble(1.0, d2);
            d3 = Math.random() < 0.5 ? d3 : -d3;
            double d4 = (d3 + this.random.nextDouble()) / 5.0;
            double d5 = this.random.nextDouble() * Math.PI;
            double d6 = Math.cos(d5) * d4;
            double d7 = Math.sin(d5) * d4;
            ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, (SimpleParticleType)BFParticleTypes.CLOUD_PARTICLE.get(), vec3.x + d6 * 0.1, vec3.y + 0.1, vec3.z + d7 * 0.1, d6, 0.0, d7);
        }
    }

    @Override
    public void method_2281() {
        if (!this.field_2572) {
            this.level().addParticle((ParticleOptions)ParticleTypes.CLOUD, true, this.getX(), this.getY() + (double)this.getEyeHeight(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }
}

