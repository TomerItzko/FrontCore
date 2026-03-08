/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.effect;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.effect.AbstractParticleEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FallingAshEffect
extends AbstractParticleEffect {
    public static final int field_101 = 16;

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull RandomSource random, @NotNull Vec3 cameraPos, @NotNull BlockPos cameraBlockPos) {
        for (int i = -16; i < 16; ++i) {
            for (int j = -3; j < 10; ++j) {
                for (int k = -16; k < 16; ++k) {
                    boolean bl;
                    BlockPos blockPos = cameraBlockPos.offset(i, j, k);
                    if (!((double)random.nextFloat() < 5.0E-4) || level.getBlockState(blockPos).canOcclude()) continue;
                    boolean bl2 = bl = level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ()) > Mth.floor((float)blockPos.getY());
                    if (bl) continue;
                    Vec3 vec3 = blockPos.getCenter();
                    level.addParticle((ParticleOptions)((double)random.nextFloat() < 0.5 ? ParticleTypes.ASH : ParticleTypes.WHITE_ASH), vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}

