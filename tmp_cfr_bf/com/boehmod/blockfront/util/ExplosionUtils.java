/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.layer.HealthEffectsGuiLayer;
import com.boehmod.blockfront.client.gui.layer.MatchGuiLayer;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.block.SandbagBlock;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.world.ExplosionAudioConfig;
import com.boehmod.blockfront.common.world.ExplosionType;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.math.ShakeNodeData;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class ExplosionUtils {
    private static final int field_2325 = 256;
    private static final int field_2326 = 16;
    private static final int field_2327 = 10;
    private static final int field_2328 = 4;
    private static final int field_2329 = 4;
    private static final float field_2322 = 1.0f;
    private static final float field_2323 = 10.0f;
    private static final float field_2324 = 5.0f;
    public static final int field_2330 = 16;
    public static final int field_2331 = 8;

    public static void explode(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull ExplosionType type, @NotNull Vec3 position) {
        ShakeNodeData shakeNodeData;
        int n;
        boolean bl;
        LocalPlayer localPlayer = minecraft.player;
        assert (localPlayer != null);
        float f = Mth.sqrt((float)((float)localPlayer.distanceToSqr(position)));
        if (f > 256.0f) {
            return;
        }
        BlockPos blockPos = BlockPos.containing((Position)position);
        RandomSource randomSource = RandomSource.create();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        Vec3 vec3 = localPlayer.getEyePosition();
        boolean bl2 = BFUtils.canSeeSky((BlockAndTintGetter)level, blockPos);
        ClientUtils.spawnParticle(minecraft, manager, level, (SimpleParticleType)BFParticleTypes.GRENADE_FLASH.get(), position.x, position.y, position.z, 0.0, 0.0, 0.0);
        boolean bl3 = Minecraft.useFancyGraphics();
        if (type == ExplosionType.GORE_EXPLOSION) {
            ExplosionUtils.method_1922(minecraft, manager, level, position, bl3);
        } else {
            blockPos.below();
            ExplosionUtils.method_1923(minecraft, manager, level, level.getBlockState(blockPos).isAir(), position, type);
        }
        MatchGuiLayer.BF_114.BF_115 bF_115 = MatchGuiLayer.BF_114.BF_115.DIRT;
        if (type.shouldDisplayBlockDamage() && bl3) {
            ExplosionUtils.method_1928(level, position, type.getBlockDamageMotion(), threadLocalRandom, type.getBlockDamageRadius());
        }
        boolean bl4 = bl = level.clip(new ClipContext(vec3, position, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)localPlayer)).getType() == HitResult.Type.MISS;
        if (bl && (n = (int)f) <= 10) {
            int n2 = 10 - n;
            for (int i = 0; i < n2; ++i) {
                HealthEffectsGuiLayer.field_534.add((Object)new MatchGuiLayer.BF_114(threadLocalRandom, bF_115));
            }
        }
        n = 0;
        float f2 = type.getMaxDistance();
        ShakeNodeData shakeNodeData2 = type.getShakeNodeData();
        if (shakeNodeData2 != null) {
            ShakeManager.applyShake(shakeNodeData2, localPlayer, position, f2);
        }
        if ((shakeNodeData = type.getShakeNodeFaintData()) != null) {
            ShakeManager.applyShake(shakeNodeData, localPlayer, position, f2 * 3.0f);
        }
        if (f <= f2) {
            n = 1;
        }
        ExplosionAudioConfig explosionAudioConfig = type.getAudioConfig();
        if (type == ExplosionType.VEHICLE_EXPLOSION) {
            level.playLocalSound(position.x, position.y, position.z, (SoundEvent)BFSounds.ENTITY_VEHICLE_EXPLODE.get(), SoundSource.AMBIENT, 10.0f, 1.0f, false);
        }
        if (type == ExplosionType.GORE_EXPLOSION) {
            ExplosionUtils.method_1927(level, position, threadLocalRandom);
        } else {
            ExplosionUtils.playSounds(minecraft, position, explosionAudioConfig, randomSource, n != 0, bl2);
            ExplosionUtils.queueDebrisSounds(level, blockPos, threadLocalRandom);
            ExplosionUtils.method_1929((Level)level, position);
        }
    }

    private static void method_1929(@NotNull Level level, @NotNull Vec3 vec3) {
        List list = level.getEntitiesOfClass(AbstractVehicleEntity.class, AABB.ofSize((Vec3)vec3, (double)16.0, (double)16.0, (double)16.0));
        for (AbstractVehicleEntity abstractVehicleEntity : list) {
            if (!level.clip(new ClipContext(abstractVehicleEntity.position(), vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)abstractVehicleEntity)).getType().equals((Object)HitResult.Type.MISS)) continue;
            Vec3 vec32 = abstractVehicleEntity.position();
            level.playLocalSound(vec32.x, vec32.y, vec32.z, (SoundEvent)BFSounds.AMBIENT_EXPLOSION_DEBRIS_VEHICLE.get(), SoundSource.AMBIENT, 5.0f, 1.0f, false);
        }
    }

    private static void playSounds(@NotNull Minecraft minecraft, @NotNull Vec3 position, @NotNull ExplosionAudioConfig config, @NotNull RandomSource random, boolean isClose, boolean isIndoors) {
        SoundEvent soundEvent;
        SoundManager soundManager = minecraft.getSoundManager();
        int n = 2;
        if (!isIndoors) {
            soundEvent = (SoundEvent)config.bass.get();
            soundManager.playDelayed((SoundInstance)new SimpleSoundInstance(soundEvent, SoundSource.AMBIENT, 6.0f, 1.0f, random, position.x, position.y, position.z), 2);
        }
        soundEvent = isIndoors ? (SoundEvent)config.explodeCloseIndoors.get() : (SoundEvent)config.explodeClose.get();
        SimpleSoundInstance simpleSoundInstance = new SimpleSoundInstance(soundEvent, SoundSource.AMBIENT, 5.0f, 1.0f, random, position.x, position.y, position.z);
        SimpleSoundInstance simpleSoundInstance2 = new SimpleSoundInstance((SoundEvent)config.explodeCloseStereo.get(), SoundSource.AMBIENT, 5.0f, 1.0f, random, position.x, position.y, position.z);
        SoundEvent soundEvent2 = isIndoors ? (SoundEvent)config.explodeDistantIndoors.get() : (SoundEvent)config.explodeDistant.get();
        SimpleSoundInstance simpleSoundInstance3 = new SimpleSoundInstance(soundEvent2, SoundSource.AMBIENT, 10.0f, 1.0f, random, position.x, position.y, position.z);
        soundManager.playDelayed((SoundInstance)(isClose ? simpleSoundInstance : simpleSoundInstance3), 2);
        if (!isIndoors && isClose) {
            soundManager.playDelayed((SoundInstance)simpleSoundInstance2, 6);
        }
        soundEvent = new SimpleSoundInstance((SoundEvent)config.ambience.get(), SoundSource.AMBIENT, 5.0f, 1.0f, random, position.x, position.y, position.z);
        simpleSoundInstance = new SimpleSoundInstance((SoundEvent)config.ambienceIndoors.get(), SoundSource.AMBIENT, 5.0f, 1.0f, random, position.x, position.y, position.z);
        simpleSoundInstance2 = new SimpleSoundInstance((SoundEvent)config.ambienceDistant.get(), SoundSource.AMBIENT, 10.0f, 1.0f, random, position.x, position.y, position.z);
        soundEvent2 = new SimpleSoundInstance((SoundEvent)config.ambienceIndoorsDistant.get(), SoundSource.AMBIENT, 10.0f, 1.0f, random, position.x, position.y, position.z);
        simpleSoundInstance3 = isIndoors ? (isClose ? simpleSoundInstance : soundEvent2) : (isClose ? soundEvent : simpleSoundInstance2);
        soundManager.playDelayed((SoundInstance)simpleSoundInstance3, 2);
        soundEvent = new SimpleSoundInstance((SoundEvent)config.nose.get(), SoundSource.AMBIENT, 5.0f, 1.0f, random, position.x, position.y, position.z);
        simpleSoundInstance = new SimpleSoundInstance((SoundEvent)config.noseDistant.get(), SoundSource.AMBIENT, 10.0f, 1.0f, random, position.x, position.y, position.z);
        for (int i = 0; i < 3; ++i) {
            soundManager.playDelayed((SoundInstance)(isClose ? soundEvent : simpleSoundInstance), 2);
        }
        soundEvent = new SimpleSoundInstance(isIndoors ? (SoundEvent)BFSounds.AMBIENT_EXPLOSION_DEBRIS_INDOORS.get() : (SoundEvent)BFSounds.AMBIENT_EXPLOSION_DEBRIS.get(), SoundSource.AMBIENT, 1.0f, 1.0f, random, position.x, position.y, position.z);
        soundManager.playDelayed((SoundInstance)soundEvent, 2);
    }

    private static void queueDebrisSounds(@NotNull ClientLevel level, @NotNull BlockPos pos, @NotNull Random random) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (int i = -4; i < 4; ++i) {
            for (int j = -4; j < 4; ++j) {
                for (int k = -4; k < 4; ++k) {
                    BlockPos blockPos = pos.offset(i, j, k);
                    BlockState blockState = level.getBlockState(blockPos);
                    if (blockState.isAir() || !ExplosionUtils.method_1925(level, blockPos)) continue;
                    SoundType soundType = blockState.getSoundType((LevelReader)level, pos, null);
                    Block block = blockState.getBlock();
                    DeferredHolder<SoundEvent, SoundEvent> deferredHolder = null;
                    if (soundType == SoundType.METAL) {
                        deferredHolder = BFSounds.AMBIENT_EXPLOSION_DEBRIS_METAL;
                    } else if (soundType == SoundType.STONE) {
                        deferredHolder = BFSounds.AMBIENT_EXPLOSION_DEBRIS_ROCK;
                    } else if (soundType == SoundType.GLASS) {
                        deferredHolder = BFSounds.AMBIENT_EXPLOSION_DEBRIS_GLASS;
                    } else if (soundType == SoundType.WOOD) {
                        deferredHolder = BFSounds.AMBIENT_EXPLOSION_DEBRIS_WOOD;
                    }
                    if (deferredHolder == null && random.nextFloat() < 0.1f) {
                        deferredHolder = BFSounds.AMBIENT_EXPLOSION_DEBRIS_ROCK;
                    }
                    if (block == Blocks.BRICKS || block == Blocks.BRICK_SLAB || block == Blocks.BRICK_STAIRS || block == Blocks.BRICK_WALL) {
                        deferredHolder = BFSounds.AMBIENT_EXPLOSION_DEBRIS_BRICK;
                    } else if (block instanceof SandbagBlock) {
                        deferredHolder = BFSounds.AMBIENT_EXPLOSION_DEBRIS_SANDBAG;
                    }
                    if (deferredHolder == null) continue;
                    objectArrayList.add(new DebrisSound(blockPos, deferredHolder));
                }
            }
        }
        while (objectArrayList.size() > 4) {
            objectArrayList.remove(random.nextInt(objectArrayList.size()));
        }
        for (DebrisSound debrisSound : objectArrayList) {
            Vec3 vec3 = debrisSound.blockPos.getCenter();
            float f = 0.9f + 0.2f * random.nextFloat();
            level.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)debrisSound.soundEvent.get(), SoundSource.AMBIENT, 1.0f, f, false);
        }
    }

    private static boolean method_1925(@NotNull ClientLevel clientLevel, @NotNull BlockPos blockPos) {
        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                for (int k = -1; k < 2; ++k) {
                    if (!clientLevel.getBlockState(blockPos.offset(i, j, k)).isAir()) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private static void method_1927(@NotNull ClientLevel clientLevel, @NotNull Vec3 vec3, @NotNull Random random) {
        SoundEvent soundEvent = (SoundEvent)BFSounds.MISC_GORE.get();
        clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, soundEvent, SoundSource.AMBIENT, 5.0f, 1.0f, false);
        clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, soundEvent, SoundSource.AMBIENT, 5.0f, 0.5f, false);
        float f = 0.85f + 0.2f * random.nextFloat();
        clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.MISC_GORE_NOSE.get(), SoundSource.AMBIENT, 5.0f, f, false);
    }

    private static void method_1922(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, @NotNull Vec3 vec3, boolean bl) {
        if (!BFClientSettings.CONTENT_GORE.isEnabled()) {
            return;
        }
        int n = bl ? 16 : 8;
        ClientUtils.spawnParticleCircle(minecraft, bFClientManager, clientLevel, (ParticleOptions)BFParticleTypes.BLOOD_SPLAT_PARTICLE.get(), vec3, 0.0f, 1.0f, n, 0.1f);
        ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, (SimpleParticleType)BFParticleTypes.BLOOD_IMPACT_SMOKE.get(), vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
    }

    private static void method_1923(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, boolean bl, @NotNull Vec3 vec3, @NotNull ExplosionType explosionType) {
        float f = explosionType.getPower();
        int n = (int)(5.0f * f);
        float f2 = 0.2f;
        float f3 = 2.0f * f;
        clientLevel.addParticle((ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, true, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        ParticleOptions[] particleOptionsArray = new ParticleOptions[]{(ParticleOptions)BFParticleTypes.POOF_PARTICLE.get(), ParticleTypes.CLOUD};
        float[] fArray = new float[]{0.2f, 0.2f};
        int n2 = particleOptionsArray.length;
        for (int i = 0; i < n2; ++i) {
            ParticleOptions particleOptions = particleOptionsArray[i];
            float f4 = fArray[i];
            if (bl) {
                ClientUtils.spawnParticleCircle(minecraft, bFClientManager, clientLevel, particleOptions, vec3, 1.0f, f3, n, f4);
                continue;
            }
            ClientUtils.spawnParticleDome(minecraft, bFClientManager, clientLevel, particleOptions, vec3, 1.0f, f3, n, f4);
        }
    }

    private static void method_1928(@NotNull ClientLevel clientLevel, @NotNull Vec3 vec3, @NotNull Vec2 vec2, Random random, int n) {
        float f = vec2.x;
        float f2 = vec2.y;
        for (int i = -n; i < n; ++i) {
            for (int j = -n; j < n; ++j) {
                for (int k = -n; k < n; ++k) {
                    BlockPos blockPos = BlockPos.containing((Position)vec3).offset(i, j - n, k);
                    BlockState blockState = clientLevel.getBlockState(blockPos);
                    BlockPos blockPos2 = blockPos.offset(0, 1, 0);
                    BlockState blockState2 = clientLevel.getBlockState(blockPos2);
                    int n2 = 0;
                    while (!blockState2.isAir() && n2 < n) {
                        ++n2;
                        blockPos2 = blockPos2.offset(0, 1, 0);
                        blockState2 = clientLevel.getBlockState(blockPos2);
                    }
                    if (!blockState.canOcclude() || blockState.isAir() || blockState.getFluidState().is(FluidTags.WATER) || blockState.getFluidState().is(FluidTags.LAVA) || !blockState2.isAir()) continue;
                    double d = (double)f + random.nextDouble() * (double)(f2 - f);
                    double d2 = (double)f + random.nextDouble() * (double)(f2 - f);
                    double d3 = (double)f + random.nextDouble() * (double)(f2 - f);
                    d2 *= 0.1;
                    d3 *= 0.1;
                    d2 *= (double)i;
                    d3 *= (double)k;
                    boolean bl = random.nextBoolean();
                    boolean bl2 = random.nextBoolean();
                    BlockPos blockPos3 = blockPos2.offset(0, 1, 0);
                    if (!clientLevel.getBlockState(blockPos3).isAir()) continue;
                    FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall((Level)clientLevel, (BlockPos)blockPos3, (BlockState)blockState);
                    fallingBlockEntity.disableDrop();
                    fallingBlockEntity.dropItem = false;
                    fallingBlockEntity.fallDistance = 5.0f;
                    fallingBlockEntity.setDeltaMovement(bl ? -d2 : d2, d, bl2 ? -d3 : d3);
                    fallingBlockEntity.setHurtsEntities(0.0f, 0);
                    ClientUtils.spawnEntity((Entity)fallingBlockEntity, clientLevel);
                }
            }
        }
    }

    private record DebrisSound(BlockPos blockPos, DeferredHolder<SoundEvent, SoundEvent> soundEvent) {
    }
}

