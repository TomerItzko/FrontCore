/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.common.entity.base.IProducedProjectileEntity;
import com.boehmod.blockfront.common.world.damage.BFDamageSource;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FlamethrowerFireEntity
extends ThrowableProjectile
implements IProducedProjectileEntity {
    protected float field_2790 = 0.6f;
    protected int field_2791 = 0;
    protected boolean field_2788 = true;
    protected boolean field_2789 = false;
    protected Player field_2786;
    private ItemStack field_2787 = null;

    public FlamethrowerFireEntity(EntityType<FlamethrowerFireEntity> entityType, Level level) {
        super(entityType, level);
    }

    public void tick() {
        super.tick();
        Level level = this.level();
        Vec3 vec3 = this.position();
        if (this.field_2789) {
            this.setDeltaMovement(0.0, 0.0, 0.0);
        }
        if (this.isInWater()) {
            level.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
            if (level.isClientSide()) {
                level.playLocalSound(vec3.x, vec3.y, vec3.z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f, false);
            }
            this.discard();
            return;
        }
        if (this.tickCount > 80) {
            this.discard();
            return;
        }
        level.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        level.addParticle((ParticleOptions)ParticleTypes.FLAME, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        level.addParticle((ParticleOptions)ParticleTypes.SMALL_FLAME, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        if (!this.field_2789 && Math.random() < (double)0.1f) {
            level.addParticle((ParticleOptions)BFParticleTypes.BULLET_IMPACT_SMOKE.get(), vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        }
        if (this.field_2788 && this.tickCount > 5) {
            if (level.isClientSide()) {
                if (Math.random() < (double)0.05f) {
                    level.playLocalSound(vec3.x, vec3.y, vec3.z, Math.random() < 0.5 ? SoundEvents.FIRE_AMBIENT : (SoundEvent)BFSounds.ENTITY_FLAME_IGNITE.get(), SoundSource.PLAYERS, 1.5f, (float)(1.0 + (double)0.2f * Math.random()), false);
                }
                if (Math.random() < 0.25) {
                    level.addParticle((ParticleOptions)ParticleTypes.LAVA, true, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
                }
            } else {
                GrenadeEntity grenadeEntity;
                Object object2;
                AABB aABB = this.getBoundingBox().inflate(0.75, 0.75, 0.75);
                List list = level.getEntitiesOfClass(FlamethrowerFireEntity.class, aABB);
                for (Object object2 : list) {
                    if (!(Math.random() < 0.75) || ((FlamethrowerFireEntity)object2).field_2788) continue;
                    ((FlamethrowerFireEntity)object2).method_2518();
                }
                List list2 = level.getEntitiesOfClass(GrenadeEntity.class, aABB);
                object2 = list2.iterator();
                while (object2.hasNext()) {
                    grenadeEntity = (GrenadeEntity)((Object)object2.next());
                    if (grenadeEntity.method_1946()) continue;
                    grenadeEntity.method_1957();
                }
                object2 = level.getEntitiesOfClass(LivingEntity.class, aABB.inflate(2.0, 2.0, 2.0));
                grenadeEntity = this.field_2787 != null ? this.field_2787 : new ItemStack((ItemLike)BFItems.GRENADE_FIRE.get());
                BFDamageSource bFDamageSource = new BFDamageSource((Holder<DamageType>)level.damageSources().inFire().typeHolder(), (Entity)this.field_2786, (ItemStack)grenadeEntity);
                Iterator iterator = object2.iterator();
                while (iterator.hasNext()) {
                    LivingEntity livingEntity = (LivingEntity)iterator.next();
                    if (!(Math.random() < (double)0.2f)) continue;
                    livingEntity.hurt((DamageSource)bFDamageSource, 4.0f);
                }
            }
        }
    }

    protected void onHit(@NotNull HitResult hitResult) {
        Level level = this.level();
        this.field_2790 *= 0.8f;
        if (hitResult instanceof BlockHitResult) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            if (hitResult.getType().equals((Object)HitResult.Type.BLOCK)) {
                Vec3 vec3 = this.getDeltaMovement();
                vec3 = this.field_2791 >= 3 ? vec3.scale(0.25) : vec3.scale(0.5);
                double d = vec3.x;
                double d2 = vec3.y;
                double d3 = vec3.z;
                Vec3 vec32 = this.position();
                level.addParticle((ParticleOptions)ParticleTypes.LAVA, vec32.x, vec32.y, vec32.z, 0.0, 0.0, 0.0);
                switch (blockHitResult.getDirection()) {
                    case UP: {
                        this.setDeltaMovement(vec3.x, (double)(-this.field_2790) * d2, vec3.z);
                        break;
                    }
                    case DOWN: {
                        this.setDeltaMovement(vec3.x, (double)(-this.field_2790) * d2, vec3.z);
                        break;
                    }
                    case NORTH: {
                        this.setDeltaMovement(vec3.x, vec3.y, (double)(-this.field_2790) * d3);
                        break;
                    }
                    case EAST: {
                        this.setDeltaMovement((double)(-this.field_2790) * d, vec3.y, vec3.z);
                        break;
                    }
                    case SOUTH: {
                        this.setDeltaMovement(vec3.x, vec3.y, (double)(-this.field_2790) * d3);
                        break;
                    }
                    case WEST: {
                        this.setDeltaMovement((double)(-this.field_2790) * d, vec3.y, vec3.z);
                    }
                }
                this.method_2519();
            }
        }
    }

    public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
    }

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    @Override
    public void method_1934(@NotNull Player player, float f, @NotNull ItemStack itemStack, float f2, float f3) {
        this.field_2786 = player;
        this.field_2787 = itemStack;
        double d = f / 2.5f;
        Vec3 vec3 = player.position();
        double d2 = -Mth.sin((float)((player.getYRot() + 15.0f) * ((float)Math.PI / 180)));
        double d3 = Mth.cos((float)((player.getYRot() + 15.0f) * ((float)Math.PI / 180)));
        this.method_2517(vec3.add(d2 / 2.0, (double)player.getEyeHeight(), d3 / 2.0), player.getYRot() + f2 / 5.0f, player.getXRot() + f3 / 5.0f, d, d);
    }

    @Override
    public void method_1935(@NotNull Vec3 vec3, float f, @NotNull ItemStack itemStack, float f2, float f3) {
    }

    public void method_2518() {
        this.field_2788 = true;
    }

    protected void method_2519() {
        if (Math.random() < (double)0.2f) {
            this.field_2789 = true;
        }
    }

    protected void method_2517(@NotNull Vec3 vec3, float f, float f2, double d, double d2) {
        float f3 = f * ((float)Math.PI / 180);
        float f4 = f2 * ((float)Math.PI / 180);
        this.setRot(f, 0.0f);
        float f5 = -Mth.sin((float)f3);
        float f6 = Mth.cos((float)f3);
        float f7 = Mth.cos((float)f4);
        float f8 = Mth.sin((float)f4);
        if (d2 == 0.0 && d > 0.0) {
            d2 = d;
        }
        Vec3 vec32 = this.getDeltaMovement();
        this.setDeltaMovement(vec32.x + d * (double)f5 * (double)f7, vec32.y + -d2 * (double)f8, vec32.z + d * (double)f6 * (double)f7);
        double d3 = -((int)f2);
        d3 = d3 * 2.0 / 90.0;
        this.setPos(vec3.x + (double)f5 * 0.8, vec3.y + d3, vec3.z + (double)f6 * 0.8);
        Vec3 vec33 = this.position();
        this.xOld = vec33.x;
        this.yOld = vec33.y;
        this.zOld = vec33.z;
    }
}

