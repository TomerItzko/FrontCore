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

public class InfectedNezhitEffect
extends AbstractParticleEffect {
    public static final int field_103 = 16;

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull RandomSource random, @NotNull Vec3 cameraPos, @NotNull BlockPos cameraBlockPos) {
        for (int i = -16; i < 16; ++i) {
            for (int j = -3; j < 10; ++j) {
                for (int k = -16; k < 16; ++k) {
                    boolean bl;
                    BlockPos blockPos = cameraBlockPos.offset(i, j, k);
                    if (!((double)random.nextFloat() < 0.005) || level.getBlockState(blockPos).canOcclude()) continue;
                    boolean bl2 = bl = level.getHeight(Heightmap.Types.MOTION_BLOCKING, i, k) > Mth.floor((float)j);
                    if (bl) continue;
                    level.addParticle((ParticleOptions)((double)random.nextFloat() < 0.5 ? ParticleTypes.ASH : ParticleTypes.WHITE_ASH), (double)i, (double)j, (double)k, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}

