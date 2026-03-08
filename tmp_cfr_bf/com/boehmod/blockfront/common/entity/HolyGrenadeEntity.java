/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.registry.BFSounds;
import java.util.List;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class HolyGrenadeEntity
extends GrenadeEntity {
    public HolyGrenadeEntity(EntityType<? extends HolyGrenadeEntity> entityType, Level level) {
        super((EntityType<? extends GrenadeEntity>)entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        if (!level.isClientSide) {
            int n = this.method_2278();
            if (n >= 20) {
                List list = level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(6.0));
                if (n == 20) {
                    Vec3 vec3 = this.position();
                    this.playSound((SoundEvent)BFSounds.ITEM_GRENADE_HOLYHANDGRENADE.get(), 4.0f, 1.0f);
                    for (LivingEntity livingEntity : list) {
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40, -3));
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 5));
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 40));
                    }
                    level.addParticle((ParticleOptions)ParticleTypes.FLASH, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
                }
            }
        } else if (Math.random() < (double)0.4f) {
            Vec3 vec3 = this.position();
            level.addParticle((ParticleOptions)ParticleTypes.CLOUD, true, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public int method_2284() {
        return 60;
    }

    @Override
    protected float method_1942() {
        return 6.5f;
    }
}

