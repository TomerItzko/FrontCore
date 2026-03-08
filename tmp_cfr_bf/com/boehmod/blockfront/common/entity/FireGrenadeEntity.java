/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.common.entity.LingeringGrenadeEntity;
import com.boehmod.blockfront.common.world.damage.BFDamageSource;
import com.boehmod.blockfront.registry.BFSounds;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class FireGrenadeEntity
extends LingeringGrenadeEntity {
    public FireGrenadeEntity(EntityType<? extends GrenadeEntity> entityType, Level level) {
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
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            BFDamageSource bFDamageSource = new BFDamageSource((Holder<DamageType>)this.level().damageSources().inFire().typeHolder(), (Entity)this.owner, this.getItem());
            livingEntity.hurt((DamageSource)bFDamageSource, 3.0f);
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected void method_2267() {
        Level level = this.level();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        Vec3 vec3 = this.position();
        for (int i = 0; i < 10; ++i) {
            float f = this.method_2265() * this.field_2574 * threadLocalRandom.nextFloat();
            f = Math.random() < 0.5 ? f : -f;
            float f2 = f + threadLocalRandom.nextFloat();
            float f3 = (float)((double)threadLocalRandom.nextFloat() * Math.PI);
            float f4 = (float)(Math.cos(f3) * (double)f2);
            float f5 = (float)(Math.sin(f3) * (double)f2);
            level.addParticle((ParticleOptions)ParticleTypes.FLAME, true, vec3.x + (double)f4, vec3.y + 0.1, vec3.z + (double)f5, 0.0, Math.random() < 0.05 ? 0.05 : 0.0, 0.0);
            if (!(Math.random() < 0.1)) continue;
            level.addParticle((ParticleOptions)ParticleTypes.LAVA, true, vec3.x + (double)f4, vec3.y + 0.1, vec3.z + (double)f5, 0.0, Math.random() < 0.05 ? 0.05 : 0.0, 0.0);
        }
    }

    @Override
    protected SoundEvent method_2271() {
        return (SoundEvent)BFSounds.ITEM_GRENADE_FIRE_EXPLODE.get();
    }

    @Override
    protected void method_2268() {
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected void method_2269() {
        Level level = this.level();
        if (!level.isClientSide()) {
            return;
        }
        Vec3 vec3 = this.position();
        double d = 1.0;
        double d2 = 14.0 * (double)this.field_2574 + 0.1;
        level.addParticle((ParticleOptions)ParticleTypes.FLASH, true, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        for (int i = 0; i < 40; ++i) {
            double d3 = ThreadLocalRandom.current().nextDouble(d, d2);
            d3 = Math.random() < 0.5 ? d3 : -d3;
            double d4 = (d3 + this.random.nextDouble()) / 5.0;
            double d5 = this.random.nextDouble() * Math.PI;
            double d6 = Math.cos(d5) * d4;
            double d7 = Math.sin(d5) * d4;
            level.addParticle((ParticleOptions)ParticleTypes.FLAME, true, vec3.x + d6 * 0.1, vec3.y + 0.1, vec3.z + d7 * 0.1, d6, 0.0, d7);
        }
    }

    @Override
    public void method_2281() {
        if (!this.field_2572) {
            this.level().addParticle((ParticleOptions)ParticleTypes.FLAME, true, this.getX(), this.getY() + (double)this.getEyeHeight(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }
}

