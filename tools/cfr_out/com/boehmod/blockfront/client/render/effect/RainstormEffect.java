/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.effect;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.effect.AbstractParticleEffect;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RainstormEffect
extends AbstractParticleEffect {
    private static final int field_318 = 600;
    private static final int field_6955 = 1;
    private static final int field_319 = 20;
    private static final int field_320 = 10;
    private int field_321 = 20;
    private int field_322 = 2;
    private int field_323 = 3;

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull RandomSource random, @NotNull Vec3 cameraPos, @NotNull BlockPos cameraBlockPos) {
        if (this.field_323 > 0 && this.field_322-- <= 0) {
            --this.field_323;
            this.field_322 = 2;
            if (this.field_323 <= 0) {
                float f = 32.0f;
                float f2 = random.nextFloat() * 360.0f;
                Vec3 vec3 = cameraPos.add((double)(Mth.cos((float)f2) * 32.0f), 0.0, (double)(Mth.sin((float)f2) * 32.0f));
                float f3 = 0.9f + 0.2f * random.nextFloat();
                level.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.AMBIENT_LSP_THUNDER.get(), SoundSource.WEATHER, 15.0f, f3, false);
            }
            level.setSkyFlashTime(1);
        }
        if (this.field_321-- <= 0) {
            this.field_321 = 600;
            if (random.nextFloat() < 0.6f) {
                this.field_323 = 3;
            }
        }
        SimpleParticleType simpleParticleType = (SimpleParticleType)BFParticleTypes.RAINDROP.get();
        for (int i = -10; i < 10; ++i) {
            for (int j = -10; j < 10; ++j) {
                for (int k = -10; k < 10; ++k) {
                    boolean bl;
                    BlockPos blockPos = cameraBlockPos.offset(i, j + 20, k);
                    Vec3 vec3 = cameraPos.add((double)i, (double)(j + 20), (double)k);
                    if (!((double)random.nextFloat() < 0.0015) || level.getBlockState(blockPos).canOcclude()) continue;
                    boolean bl2 = bl = level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ()) > Mth.floor((double)vec3.y);
                    if (bl) continue;
                    ClientUtils.spawnParticle(minecraft, manager, level, simpleParticleType, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}

