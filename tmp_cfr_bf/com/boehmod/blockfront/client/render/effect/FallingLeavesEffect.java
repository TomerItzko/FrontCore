/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.effect;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.effect.AbstractParticleEffect;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FallingLeavesEffect
extends AbstractParticleEffect {
    private static final int field_102 = 16;

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull RandomSource random, @NotNull Vec3 cameraPos, @NotNull BlockPos cameraBlockPos) {
        for (int i = -16; i < 16; ++i) {
            for (int j = -16; j < 16; ++j) {
                for (int k = -16; k < 16; ++k) {
                    BlockPos blockPos = cameraBlockPos.offset(i, j, k);
                    if (!(random.nextFloat() < 0.001f) || level.getBlockState(blockPos).canOcclude()) continue;
                    boolean bl = level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ()) > Mth.floor((float)j);
                    BlockPos blockPos2 = blockPos.offset(0, 1, 0);
                    BlockState blockState = level.getBlockState(blockPos2);
                    Vec3 vec3 = blockPos.getCenter();
                    if ((double)random.nextFloat() < 0.2) {
                        level.addParticle((ParticleOptions)ParticleTypes.SPORE_BLOSSOM_AIR, vec3.x, vec3.y + 0.5, vec3.z, 0.0, 0.0, 0.0);
                        continue;
                    }
                    if (!bl || !(blockState.getBlock() instanceof LeavesBlock)) continue;
                    ClientUtils.spawnParticle(minecraft, manager, level, (SimpleParticleType)BFParticleTypes.FALLING_LEAF_PARTICLE.get(), vec3.x, vec3.y + 0.5, vec3.z, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}

