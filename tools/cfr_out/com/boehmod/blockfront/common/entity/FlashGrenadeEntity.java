/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.common.net.packet.BFGrenadeFlashPacket;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.PacketUtils;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class FlashGrenadeEntity
extends GrenadeEntity {
    public FlashGrenadeEntity(EntityType<? extends FlashGrenadeEntity> entityType, Level level) {
        super((EntityType<? extends GrenadeEntity>)entityType, level);
    }

    private static boolean method_2274(Entity entity, LivingEntity livingEntity) {
        float f = livingEntity.getYRot();
        float f2 = livingEntity.getXRot();
        FlashGrenadeEntity.method_2275(livingEntity, entity, 360.0f, 360.0f);
        float f3 = livingEntity.getYRot();
        float f4 = livingEntity.getXRot();
        livingEntity.setYRot(f);
        livingEntity.setXRot(f2);
        f = f3;
        f2 = f4;
        float f5 = 60.0f;
        float f6 = 60.0f;
        float f7 = livingEntity.getYRot() - f5;
        float f8 = livingEntity.getYRot() + f5;
        float f9 = livingEntity.getXRot() - f6;
        float f10 = livingEntity.getXRot() + f6;
        boolean bl = f < f8 && f > f7;
        boolean bl2 = f9 <= -180.0f && (f2 >= f9 + 360.0f || f2 <= f10) || f10 > 180.0f && (f2 <= f10 - 360.0f || f2 >= f9) || f10 < 180.0f && f9 >= -180.0f && f2 <= f10 && f2 >= f9;
        return bl && bl2 && livingEntity.hasLineOfSight(entity);
    }

    public static void method_2275(LivingEntity livingEntity, Entity entity, float f, float f2) {
        double d;
        Vec3 vec3 = livingEntity.position();
        Vec3 vec32 = entity.position();
        double d2 = vec32.x - vec3.x;
        double d3 = vec32.z - vec3.z;
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity2 = (LivingEntity)entity;
            d = vec32.y + (double)livingEntity2.getEyeHeight() - (vec3.y + (double)livingEntity.getEyeHeight());
        } else {
            d = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0 - (vec3.y + (double)livingEntity.getEyeHeight());
        }
        double d4 = Mth.sqrt((float)((float)(d2 * d2 + d3 * d3)));
        float f3 = (float)(Math.atan2(d3, d2) * 180.0 / Math.PI) - 90.0f;
        float f4 = (float)(-(Math.atan2(d, d4) * 180.0 / Math.PI));
        livingEntity.setXRot(FlashGrenadeEntity.method_2276(livingEntity.getXRot(), f4, f2));
        livingEntity.setYRot(FlashGrenadeEntity.method_2276(livingEntity.getYRot(), f3, f));
    }

    private static float method_2276(float f, float f2, float f3) {
        float f4 = Mth.wrapDegrees((float)(f2 - f));
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }

    @Override
    public int method_2284() {
        return 20;
    }

    @Override
    public float method_1940() {
        return 0.0f;
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    @Override
    public void method_1957() {
        Level level = this.level();
        if (!this.field_2340) {
            Vec3 vec3 = this.position();
            double d = 40.0;
            if (level.isClientSide()) {
                level.addParticle((ParticleOptions)ParticleTypes.FLASH, true, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
            }
            AABB aABB = this.getBoundingBox().inflate(40.0);
            List list = level.getEntitiesOfClass(Player.class, aABB);
            for (Player player : list) {
                float f;
                if (!player.hasLineOfSight((Entity)this) || !((double)(f = this.distanceTo((Entity)player)) < 40.0)) continue;
                float f2 = 0.0f;
                if (FlashGrenadeEntity.method_2274((Entity)this, (LivingEntity)player)) {
                    f2 = (40.0f - f) * 0.6f;
                    if (f2 > 11.0f) {
                        f2 = 11.0f;
                    }
                } else if ((double)f < 10.0) {
                    f2 = 4.0f;
                }
                if (level.isClientSide() || !(player instanceof ServerPlayer)) continue;
                ServerPlayer serverPlayer = (ServerPlayer)player;
                if (!(f2 > 0.0f)) continue;
                PacketUtils.sendToPlayer(new BFGrenadeFlashPacket(f2), serverPlayer);
            }
            ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
            this.playSound((SoundEvent)BFSounds.ITEM_GRENADE_FLASH_EXPLODE.get(), 3.0f, (1.0f + (threadLocalRandom.nextFloat() - threadLocalRandom.nextFloat()) * 0.2f) * 0.7f);
            this.field_2340 = true;
        }
        this.discard();
    }
}

