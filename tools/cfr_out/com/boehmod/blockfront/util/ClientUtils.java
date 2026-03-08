/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.particle.BFParticleManager;
import com.boehmod.blockfront.unnamed.BF_1143;
import com.boehmod.blockfront.unnamed.BF_1150;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class ClientUtils {
    @NotNull
    private static final AtomicInteger NEXT_ENTITY_ID = new AtomicInteger(0x3FFFFFFF);

    public static void spawnEntity(@NotNull Entity entity, @NotNull ClientLevel level) {
        try {
            int n = NEXT_ENTITY_ID.getAndIncrement();
            entity.setId(n);
            level.addEntity(entity);
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            BFLog.logThrowable("Failed to spawn client-side entity!", arrayIndexOutOfBoundsException, new Object[0]);
        }
    }

    public static boolean isBfEntity(@NotNull Entity entity) {
        return entity.getId() > 0x3FFFFFFF;
    }

    public static void setCameraEntity(@NotNull Minecraft minecraft, @NotNull Entity entity) {
        if (minecraft.getCameraEntity() != entity) {
            BFLog.log("Setting camera entity to %s", entity);
            minecraft.setCameraEntity(entity);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public static void spawnParticleCircle(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull ParticleOptions options, @NotNull Vec3 center, float minDist, float maxDist, int numParticles, float speedMagnitude) {
        for (int i = 0; i < numParticles; ++i) {
            double d = (double)minDist + (double)(maxDist - minDist) * Math.cbrt(level.random.nextDouble());
            double d2 = Math.PI * 2 * level.random.nextDouble();
            double d3 = Math.acos(2.0 * level.random.nextDouble() - 1.0);
            double d4 = center.x + d * Math.sin(d3) * Math.cos(d2);
            double d5 = center.y + d * Math.sin(d3) * Math.sin(d2);
            double d6 = center.z + d * Math.cos(d3);
            double d7 = (double)speedMagnitude * Math.sin(d3) * Math.cos(d2);
            double d8 = (double)speedMagnitude * Math.sin(d3) * Math.sin(d2);
            double d9 = (double)speedMagnitude * Math.cos(d3);
            ClientUtils.spawnParticle(minecraft, manager, level, options, d4, d5, d6, d7, d8, d9);
        }
    }

    public static void spawnParticleDome(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull ParticleOptions options, @NotNull Vec3 center, float minDist, float maxDist, int numParticles, float speedMagnitude) {
        for (int i = 0; i < numParticles; ++i) {
            double d = (double)minDist + (double)(maxDist - minDist) * Math.cbrt(level.random.nextDouble());
            double d2 = Math.PI * 2 * level.random.nextDouble();
            double d3 = Math.acos(level.random.nextDouble());
            double d4 = center.x + d * Math.sin(d3) * Math.cos(d2);
            double d5 = center.y + d * Math.sin(d3) * Math.sin(d2);
            double d6 = center.z + d * Math.cos(d3);
            double d7 = (double)speedMagnitude * Math.sin(d3) * Math.cos(d2);
            double d8 = (double)speedMagnitude * Math.sin(d3) * Math.sin(d2);
            double d9 = (double)speedMagnitude * Math.cos(d3);
            ClientUtils.spawnParticle(minecraft, manager, level, options, d4, d5, d6, d7, d8, d9);
        }
    }

    public static <T extends ParticleOptions> void spawnParticle(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull T options, double posX, double posY, double posZ, double velX, double velY, double velZ) {
        ParticleProvider particleProvider = (ParticleProvider)minecraft.particleEngine.providers.get(BuiltInRegistries.PARTICLE_TYPE.getKey((Object)options.getType()));
        Particle particle = particleProvider.createParticle(options, level, posX, posY, posZ, velX, velY, velZ);
        BFParticleManager bFParticleManager = manager.getParticleManager();
        if (particle instanceof BF_1150) {
            BF_1150 bF_1150 = (BF_1150)particle;
            bFParticleManager.method_5587(bF_1150);
            return;
        }
        if (particle instanceof BF_1143) {
            BF_1143 bF_1143 = (BF_1143)particle;
            bFParticleManager.method_5582(bF_1143);
            return;
        }
        level.addParticle(options, true, posX, posY, posZ, velX, velY, velZ);
    }

    public static void spawnParticleDirection(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull Vec3 origin, @NotNull Direction direction, @NotNull ParticleOptions options, float speedMagnitude) {
        ClientUtils.spawnParticleDirection(minecraft, manager, level, origin, direction, options, speedMagnitude, 0.0f);
    }

    public static void spawnParticleDirection(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull Vec3 origin, @NotNull Direction direction, @NotNull ParticleOptions options, float speedMagnitude, float offsetMagnitude) {
        Vec3 vec3 = origin.add(new Vec3((double)direction.getStepX(), (double)direction.getStepY(), (double)direction.getStepZ()).scale((double)offsetMagnitude));
        Vec3 vec32 = MathUtils.directionVec(direction, speedMagnitude);
        ClientUtils.spawnParticle(minecraft, manager, level, options, vec3.x, vec3.y, vec3.z, vec32.x, vec32.y, vec32.z);
    }

    public static void spawnBlockParticles(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull BlockPos blockPos, @NotNull Vec3 origin, @NotNull Direction direction, @NotNull BlockState blockState, float speedMagnitude) {
        Vec3 vec3 = MathUtils.directionVec(direction, speedMagnitude);
        if (!blockState.isAir()) {
            BlockParticleOption blockParticleOption = new BlockParticleOption(ParticleTypes.BLOCK, blockState).setPos(blockPos);
            for (int i = 0; i < 5; ++i) {
                ClientUtils.spawnParticle(minecraft, manager, level, blockParticleOption, origin.x, origin.y, origin.z, vec3.x, vec3.y, vec3.z);
            }
        }
    }
}

