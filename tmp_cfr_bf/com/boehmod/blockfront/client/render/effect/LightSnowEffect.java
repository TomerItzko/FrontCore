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
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LightSnowEffect
extends AbstractParticleEffect {
    private static final int field_104 = 8;

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull RandomSource random, @NotNull Vec3 cameraPos, @NotNull BlockPos cameraBlockPos) {
        for (int i = -8; i < 8; ++i) {
            for (int j = -8; j < 8; ++j) {
                for (int k = -8; k < 8; ++k) {
                    BlockPos blockPos = cameraBlockPos.offset(i, j, k);
                    if (!((double)random.nextFloat() < 0.005) || level.getBlockState(blockPos).canOcclude() || !level.canSeeSky(blockPos)) continue;
                    Vec3 vec3 = cameraPos.add((double)i, (double)j, (double)k);
                    ClientUtils.spawnParticle(minecraft, manager, level, (SimpleParticleType)BFParticleTypes.FALLING_SNOW_PARTICLE.get(), vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}

