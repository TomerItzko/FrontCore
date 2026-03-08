/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.item.GrenadeFragItem;
import com.boehmod.blockfront.registry.BFSounds;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class GrenadeEntity
extends BFProjectileEntity {
    private static final EntityDataAccessor<Integer> field_2582 = SynchedEntityData.defineId(GrenadeEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> field_2583 = SynchedEntityData.defineId(GrenadeEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    protected int field_2581 = 0;
    private boolean field_2579 = true;
    private int field_2580 = 0;

    public GrenadeEntity(@NotNull EntityType<? extends GrenadeEntity> entityType, @NotNull Level level) {
        super(entityType, level);
    }

    private float method_2277() {
        return 0.25f;
    }

    protected void method_2280(@NotNull BlockHitResult blockHitResult) {
        Level level = this.level();
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        SoundType soundType = blockState.getSoundType((LevelReader)level, blockPos, (Entity)this);
        Vec3 vec3 = this.getDeltaMovement();
        if (blockHitResult.getDirection() == Direction.UP) {
            this.setXRot(0.0f);
        }
        if (vec3.y > (double)-0.15f) {
            level.playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)this.method_2279(soundType).get(), SoundSource.BLOCKS, 1.0f, (float)((double)0.9f + (double)0.1f * Math.random()));
        }
    }

    protected DeferredHolder<SoundEvent, SoundEvent> method_2279(@NotNull SoundType soundType) {
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = BFSounds.ITEM_GRENADE_BOUNCE_DIRT;
        if (soundType.equals(SoundType.METAL)) {
            deferredHolder = BFSounds.ITEM_GRENADE_BOUNCE_METAL;
        } else if (soundType.equals(SoundType.WOOD)) {
            deferredHolder = BFSounds.ITEM_GRENADE_BOUNCE_WOOD;
        }
        return deferredHolder;
    }

    protected void method_2287() {
    }

    protected void method_2282() {
    }

    public void method_2281() {
        this.level().addParticle((ParticleOptions)ParticleTypes.SMOKE, true, this.getX(), this.getY() + (double)this.getEyeHeight(), this.getZ(), 0.0, 0.0, 0.0);
    }

    public int method_2285() {
        this.entityData.set(field_2582, (Object)(this.method_2278() + 1));
        return (Integer)this.entityData.get(field_2582);
    }

    public int method_2278() {
        return (Integer)this.entityData.get(field_2582);
    }

    public void method_2288(int n) {
        this.entityData.set(field_2582, (Object)n);
    }

    public int method_2286() {
        return (Integer)this.entityData.get(field_2583);
    }

    public void method_2289(int n) {
        this.entityData.set(field_2583, (Object)n);
    }

    public int method_2284() {
        return 80;
    }

    public int method_2283() {
        return 4;
    }

    @Override
    protected boolean method_1943() {
        ItemStack itemStack = this.getItem();
        if (itemStack.isEmpty()) {
            return false;
        }
        Item item = itemStack.getItem();
        if (item instanceof GrenadeFragItem) {
            GrenadeFragItem grenadeFragItem = (GrenadeFragItem)item;
            return grenadeFragItem.method_4094();
        }
        return false;
    }

    @Override
    public float method_1940() {
        ItemStack itemStack = this.getItem();
        if (itemStack.isEmpty()) {
            return 0.0f;
        }
        Item item = itemStack.getItem();
        if (item instanceof GrenadeFragItem) {
            GrenadeFragItem grenadeFragItem = (GrenadeFragItem)item;
            return grenadeFragItem.method_4085();
        }
        return 0.0f;
    }

    @Override
    public float method_1941() {
        ItemStack itemStack = this.getItem();
        if (itemStack.isEmpty()) {
            return 0.0f;
        }
        Item item = itemStack.getItem();
        if (item instanceof GrenadeFragItem) {
            GrenadeFragItem grenadeFragItem = (GrenadeFragItem)item;
            return grenadeFragItem.method_4086();
        }
        return 0.0f;
    }

    protected void updateRotation() {
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void method_1949(@NotNull ServerPlayer serverPlayer, float f, @NotNull ItemStack itemStack) {
        super.method_1949(serverPlayer, f, itemStack);
        this.method_2289(this.method_2284());
    }

    @Override
    public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(field_2582, (Object)0).define(field_2583, (Object)28);
    }

    @OnlyIn(value=Dist.CLIENT)
    public void lerpMotion(double d, double d2, double d3) {
        this.setDeltaMovement(d, d2, d3);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.method_2281();
        } else {
            boolean bl = this.onGround();
            Vec3 vec3 = this.getDeltaMovement();
            if (bl && vec3.x == 0.0 && vec3.y == 0.0 && vec3.z == 0.0) {
                this.method_2282();
            }
            vec3 = vec3.scale(0.98).scale(bl ? 0.7 : 1.0);
            this.setDeltaMovement(vec3);
            if (this.field_2580 > 0) {
                --this.field_2580;
            }
        }
        if (this.method_2286() != -1 && this.method_2285() >= this.method_2286()) {
            this.method_1957();
        }
    }

    @Override
    protected float method_1942() {
        return 4.5f;
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        if (hitResult instanceof BlockHitResult) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            if (hitResult.getType().equals((Object)HitResult.Type.BLOCK)) {
                Direction direction = blockHitResult.getDirection();
                if (this.field_2579) {
                    this.method_2287();
                }
                if (this.field_2581 >= this.method_2283()) {
                    this.setDeltaMovement(0.0, 0.0, 0.0);
                    if (this.field_2579) {
                        this.method_2280(blockHitResult);
                    }
                } else {
                    float f = this.method_2277();
                    Vec3 vec3 = this.getDeltaMovement();
                    switch (direction) {
                        case UP: {
                            this.setDeltaMovement(vec3.x, (double)(-f) * vec3.y, vec3.z);
                            ++this.field_2581;
                            break;
                        }
                        case DOWN: {
                            this.setDeltaMovement(vec3.x, (double)(-f) * vec3.y, vec3.z);
                            break;
                        }
                        case NORTH: {
                            this.setDeltaMovement(vec3.x, vec3.y, (double)(-f) * vec3.z);
                            break;
                        }
                        case EAST: {
                            this.setDeltaMovement((double)(-f) * vec3.x, vec3.y, vec3.z);
                            break;
                        }
                        case SOUTH: {
                            this.setDeltaMovement(vec3.x, vec3.y, (double)(-f) * vec3.z);
                            break;
                        }
                        case WEST: {
                            this.setDeltaMovement((double)(-f) * vec3.x, vec3.y, vec3.z);
                        }
                    }
                    this.method_2280(blockHitResult);
                }
                this.field_2579 = false;
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("fuse", this.method_2278());
        compoundTag.putInt("bounceCount", this.field_2581);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.method_2288(compoundTag.getInt("fuse"));
        this.field_2581 = compoundTag.getInt("bounceCount");
    }

    public boolean canCollideWith(@NotNull Entity entity) {
        return false;
    }
}

