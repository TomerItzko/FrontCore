/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.SoundType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.layer.HealthEffectsGuiLayer;
import com.boehmod.blockfront.client.gui.layer.MatchGuiLayer;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.cloud.BFFeatureFlags;
import com.boehmod.blockfront.common.block.base.IBarrierBlock;
import com.boehmod.blockfront.common.block.base.IShootAction;
import com.boehmod.blockfront.common.gun.GunSoundConfig;
import com.boehmod.blockfront.common.gun.bullet.BlockBulletCollision;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.particle.BulletParticleTypes;
import com.boehmod.blockfront.common.particle.BulletParticles;
import com.boehmod.blockfront.registry.BFBlockAttributes;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.registry.custom.BlockAttribute;
import com.boehmod.blockfront.registry.custom.BlockSoundAttribute;
import com.boehmod.blockfront.util.ClientUtils;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public final class BulletUtils {
    private static final float field_117 = 0.2f;
    private static final float field_118 = 16.0f;
    private static final int field_6471 = 128;
    private static final float field_6897 = 0.75f;
    private static final float field_6898 = 3.0f;
    private static final int field_6912 = 2;
    private static final int field_6913 = 1;
    private static final int field_6914 = 6;
    private static final int field_6915 = 32;
    private static final int field_6909 = 2;
    private static final float field_6911 = 16.0f;
    private static final int field_6910 = 3;
    private static final float field_7089 = 0.1f;

    public static void doBlockBulletImpact(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull RandomSource random, @NotNull LocalPlayer player, @Nullable LivingEntity entity, @NotNull BlockBulletCollision collision, int n) {
        IShootAction iShootAction;
        DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> deferredHolder;
        Object object;
        Object object2;
        Vec3 vec3 = collision.hitVec();
        Entity entity2 = minecraft.getCameraEntity();
        boolean bl = true;
        if (entity2 != null) {
            int n2 = (int)entity2.position().distanceTo(vec3);
            if (n2 > 128) {
                return;
            }
            bl = n2 <= 32;
        }
        Direction direction = collision.direction();
        BlockPos blockPos = collision.blockPos();
        BlockState blockState = level.getBlockState(blockPos);
        SoundType soundType = blockState.getSoundType((LevelReader)level, blockPos, (Entity)player);
        Block block = blockState.getBlock();
        ItemStack itemStack = ItemStack.EMPTY;
        GunItem gunItem = null;
        if (entity != null) {
            itemStack = entity.getMainHandItem();
            object2 = itemStack.getItem();
            gunItem = object2 instanceof GunItem ? (object = (GunItem)object2) : null;
        }
        Object object3 = object = gunItem != null ? gunItem.getBulletParticles() : BulletParticleTypes.BASIC;
        if (blockState.liquid()) {
            ClientUtils.spawnParticle(minecraft, manager, level, ParticleTypes.SPLASH, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
            ClientUtils.spawnParticle(minecraft, manager, level, ParticleTypes.BUBBLE_POP, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
            ClientUtils.spawnParticle(minecraft, manager, level, (SimpleParticleType)BFParticleTypes.POOF_PARTICLE.get(), vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
            if (bl) {
                BulletUtils.method_5596(level, vec3, random);
            }
        }
        if (bl) {
            object2 = (SoundEvent)BFSounds.ITEM_GUN_BULLET_IMPACT_COREBASS.get();
            level.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)object2, SoundSource.BLOCKS, 2.5f, 1.0f, false);
        }
        if (soundType == SoundType.EMPTY || blockState.isAir()) {
            return;
        }
        if (bl) {
            level.playLocalSound((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), soundType.getBreakSound(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
        }
        BlockSoundAttribute blockSoundAttribute = (deferredHolder = ((BlockAttribute)(object2 = BFBlockAttributes.method_4614((Level)level, blockState, blockPos))).method_4246()) != null ? (BlockSoundAttribute)deferredHolder.get() : null;
        Vec3 vec32 = new Vec3((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()).add(0.5, 0.5, 0.5);
        boolean bl2 = block instanceof IShootAction || !(block instanceof IBarrierBlock);
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        if (block instanceof IShootAction) {
            iShootAction = (IShootAction)block;
            iShootAction.onBulletHit(minecraft, manager, (Level)level, n, vec3, blockPos, blockState, direction);
        }
        if (blockSoundAttribute != null && bl) {
            BulletUtils.method_5595(level, blockSoundAttribute, vec32, threadLocalRandom);
        }
        if (bl2) {
            iShootAction = player.getEyePosition();
            if (bl && gunItem != null) {
                BulletUtils.method_5594(level, gunItem, itemStack, vec32, vec3, (Vec3)iShootAction, threadLocalRandom);
            }
            BulletUtils.method_157(level, (LivingEntity)player, (BlockAttribute)object2, (Vec3)iShootAction, vec3, random, threadLocalRandom, vec32);
            if (blockSoundAttribute != null) {
                int n3;
                int n4;
                SimpleParticleType simpleParticleType;
                BlockSoundAttribute.BF_999 bF_9992;
                Iterator<BlockSoundAttribute.BF_999> iterator;
                Object object4;
                if (blockSoundAttribute.method_4254()) {
                    ClientUtils.spawnBlockParticles(minecraft, manager, level, blockPos, vec3, direction, blockState, 1.0f);
                }
                if (blockSoundAttribute.method_4252() && BFClientSettings.EXPERIMENTAL_BULLET_HOLES.isEnabled() && (!(block instanceof IShootAction) || (object4 = (IShootAction)block).showParticle())) {
                    iterator = ((BulletParticles)object).getImpactSmoke();
                    ClientUtils.spawnParticle(minecraft, manager, level, (SimpleParticleType)iterator.get(), vec3.x, vec3.y, vec3.z, direction.ordinal(), 0.0, 0.0);
                }
                object4 = blockSoundAttribute.method_5934();
                iterator = object4.iterator();
                while (iterator.hasNext()) {
                    bF_9992 = iterator.next();
                    simpleParticleType = (SimpleParticleType)bF_9992.particleType().get();
                    n4 = bF_9992.count();
                    for (n3 = 0; n3 < n4; ++n3) {
                        float f = 0.1f;
                        Vec3 vec33 = vec3.add((double)Mth.nextFloat((RandomSource)random, (float)-0.1f, (float)0.1f), (double)Mth.nextFloat((RandomSource)random, (float)-0.1f, (float)0.1f), (double)Mth.nextFloat((RandomSource)random, (float)-0.1f, (float)0.1f));
                        ClientUtils.spawnParticleDirection(minecraft, manager, level, vec33, direction, (ParticleOptions)simpleParticleType, 0.45f, 0.1f);
                    }
                }
                if (BFFeatureFlags.field_7091 && !object4.isEmpty()) {
                    float f = 0.1f;
                    bF_9992 = vec3.add((double)Mth.nextFloat((RandomSource)random, (float)-0.1f, (float)0.1f), (double)Mth.nextFloat((RandomSource)random, (float)-0.1f, (float)0.1f), (double)Mth.nextFloat((RandomSource)random, (float)-0.1f, (float)0.1f));
                    ClientUtils.spawnParticleDirection(minecraft, manager, level, (Vec3)bF_9992, direction, (ParticleOptions)BFParticleTypes.BULLET_IMPACT_LARGE_SMOKE.get(), 0.45f, 0.1f);
                }
                for (BlockSoundAttribute.BF_999 bF_9992 : blockSoundAttribute.method_4247()) {
                    simpleParticleType = (SimpleParticleType)bF_9992.particleType().get();
                    n4 = bF_9992.count();
                    for (n3 = 0; n3 < n4; ++n3) {
                        ClientUtils.spawnParticle(minecraft, manager, level, simpleParticleType, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
                    }
                }
                Iterator<BlockSoundAttribute.BF_999> iterator2 = ((BulletParticles)object).getImpactFlash();
                ClientUtils.spawnParticleDirection(minecraft, manager, level, vec3, direction, (ParticleOptions)iterator2.get(), 0.0f, 0.25f);
            }
        }
    }

    private static void method_5596(@NotNull ClientLevel clientLevel, @NotNull Vec3 vec3, @NotNull RandomSource randomSource) {
        float f = 0.9f + 0.2f * randomSource.nextFloat();
        SoundEvent soundEvent = (SoundEvent)BFSounds.ITEM_GUN_BULLET_IMPACT_WATER.get();
        for (int i = 0; i < 3; ++i) {
            clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, soundEvent, SoundSource.BLOCKS, 2.5f, f, false);
        }
        clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, SoundEvents.AXOLOTL_SPLASH, SoundSource.BLOCKS, 2.5f, 0.9f + 0.2f * randomSource.nextFloat(), false);
    }

    private static void method_5595(@NotNull ClientLevel clientLevel, @NotNull BlockSoundAttribute blockSoundAttribute, @NotNull Vec3 vec3, @NotNull ThreadLocalRandom threadLocalRandom) {
        if (blockSoundAttribute.method_4253()) {
            float f = 0.9f + 0.4f * threadLocalRandom.nextFloat();
            clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.ITEM_GUN_BULLET_IMPACT.get(), SoundSource.BLOCKS, 1.5f, f, false);
            clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.ITEM_GUN_BULLET_IMPACT_NOSE.get(), SoundSource.BLOCKS, 1.5f, 1.0f, false);
        }
        SoundEvent soundEvent = (SoundEvent)blockSoundAttribute.method_4256().get();
        clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, soundEvent, SoundSource.BLOCKS, 1.0f, 0.8f + 0.1f * (float)threadLocalRandom.nextInt(4), false);
        SoundEvent soundEvent2 = (SoundEvent)blockSoundAttribute.method_4257().get();
        clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, soundEvent2, SoundSource.BLOCKS, 1.0f, 0.8f + 0.1f * (float)threadLocalRandom.nextInt(4), false);
    }

    private static void method_5594(@NotNull ClientLevel clientLevel, @NotNull GunItem gunItem, @NotNull ItemStack itemStack, @NotNull Vec3 vec3, @NotNull Vec3 vec32, @NotNull Vec3 vec33, @NotNull ThreadLocalRandom threadLocalRandom) {
        GunSoundConfig gunSoundConfig = gunItem.getSoundConfig(itemStack);
        if (gunSoundConfig != null) {
            float f = 0.95f + 0.1f * threadLocalRandom.nextFloat();
            if (threadLocalRandom.nextFloat() < gunSoundConfig.method_3985()) {
                DeferredHolder<SoundEvent, SoundEvent> deferredHolder;
                DeferredHolder<SoundEvent, SoundEvent> deferredHolder2 = gunSoundConfig.getImpactMono();
                if (deferredHolder2 != null) {
                    clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)deferredHolder2.get(), SoundSource.BLOCKS, 1.5f, f, false);
                }
                if ((deferredHolder = gunSoundConfig.getImpactStereo()) != null) {
                    float f2 = 16.0f;
                    float f3 = (float)vec33.distanceTo(vec32);
                    if (f3 <= 16.0f) {
                        float f4 = 1.0f - f3 / 16.0f;
                        clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)deferredHolder.get(), SoundSource.BLOCKS, f4, f, false);
                    }
                }
            }
        }
    }

    private static void method_157(@NotNull ClientLevel clientLevel, @NotNull LivingEntity livingEntity, @Nullable BlockAttribute blockAttribute, @NotNull Vec3 vec3, @NotNull Vec3 vec32, @NotNull RandomSource randomSource, @NotNull Random random, @NotNull Vec3 vec33) {
        int n;
        boolean bl;
        if (blockAttribute == null || blockAttribute.method_4246() == null) {
            return;
        }
        BlockSoundAttribute blockSoundAttribute = (BlockSoundAttribute)blockAttribute.method_4246().get();
        MatchGuiLayer.BF_114.BF_115 bF_115 = blockSoundAttribute.method_4248();
        boolean bl2 = bl = clientLevel.clip(new ClipContext(vec3, vec32, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)livingEntity)).getType() == HitResult.Type.MISS;
        if (bl && bF_115 != null && randomSource.nextFloat() < 0.2f && (n = (int)vec3.distanceTo(vec33)) <= 6) {
            int n2 = 6 - n;
            for (int i = 0; i < n2; ++i) {
                HealthEffectsGuiLayer.field_534.add((Object)new MatchGuiLayer.BF_114(random, bF_115));
            }
        }
    }

    public static void method_155(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, @NotNull RandomSource randomSource, @NotNull Random random, @NotNull LivingEntity livingEntity, @NotNull LivingEntity livingEntity2, @NotNull Vec3 vec3, @NotNull Direction direction, boolean bl) {
        float f = 0.9f + randomSource.nextFloat() * 0.2f;
        Vec3 vec32 = livingEntity2.position();
        Vec3 vec33 = livingEntity.getEyePosition();
        float f2 = (float)vec33.distanceTo(vec3);
        if (f2 >= 128.0f) {
            return;
        }
        clientLevel.playLocalSound(vec32.x, vec32.y, vec32.z, (SoundEvent)BFSounds.ITEM_GUN_BULLET_IMPACT_FLESH.get(), SoundSource.BLOCKS, 1.5f, f, false);
        SoundEvent soundEvent = (SoundEvent)BFSounds.ITEM_GUN_BULLET_IMPACT_FLESH_NOSE.get();
        for (int i = 0; i < 2; ++i) {
            clientLevel.playLocalSound(vec32.x, vec32.y, vec32.z, soundEvent, SoundSource.BLOCKS, 1.5f, 1.0f, false);
        }
        if (f2 <= 16.0f) {
            float f3 = 1.0f - f2 / 16.0f;
            clientLevel.playLocalSound(vec32.x, vec32.y, vec32.z, (SoundEvent)BFSounds.ITEM_GUN_BULLET_IMPACT_FLESH_STEREO.get(), SoundSource.BLOCKS, f3, f, false);
        }
        if (bl) {
            clientLevel.playLocalSound(vec32.x, vec32.y, vec32.z, (SoundEvent)BFSounds.ITEM_GUN_BULLET_IMPACT_FLESH_HEAD.get(), SoundSource.BLOCKS, 1.5f, f, false);
            clientLevel.playLocalSound(vec32.x, vec32.y, vec32.z, (SoundEvent)BFSounds.ITEM_GUN_BULLET_IMPACT_FLESH_HEAD_HELMET.get(), SoundSource.BLOCKS, 1.5f, f, false);
        }
        if (BFClientSettings.CONTENT_GORE.isEnabled()) {
            int n;
            SimpleParticleType simpleParticleType = (SimpleParticleType)BFParticleTypes.BLOOD_SPLAT_PARTICLE.get();
            int n2 = n = bl ? 2 : 1;
            for (int i = 0; i < n; ++i) {
                ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, simpleParticleType, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
            }
            ClientUtils.spawnParticleDirection(minecraft, bFClientManager, clientLevel, vec3, direction, (ParticleOptions)BFParticleTypes.BLOOD_IMPACT_SMOKE.get(), -0.45f);
            if (randomSource.nextFloat() < 0.75f && Mth.sqrt((float)((float)vec33.distanceToSqr(vec3))) <= 3.0f) {
                HealthEffectsGuiLayer.field_534.add((Object)new MatchGuiLayer.BF_114(random, MatchGuiLayer.BF_114.BF_115.BLOOD));
            }
        }
        if (randomSource.nextBoolean()) {
            ClientUtils.spawnParticleDirection(minecraft, bFClientManager, clientLevel, vec3, direction, (ParticleOptions)BFParticleTypes.POOF_PARTICLE.get(), 0.1f);
        }
    }
}

