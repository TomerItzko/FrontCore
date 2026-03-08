/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.phys.Vec2
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.world;

import com.boehmod.blockfront.common.world.ExplosionAudioConfig;
import com.boehmod.blockfront.common.world.ExplosionAudioConfigs;
import com.boehmod.blockfront.util.math.ShakeNodeData;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import javax.annotation.Nullable;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

public enum ExplosionType {
    GENERIC(16.0f, 2.0f, 0, new Vec2(0.1f, 1.5f), ExplosionAudioConfigs.GENERIC, ShakeNodePresets.EXPLOSION, ShakeNodePresets.EXPLOSION_FAINT, true),
    GORE_EXPLOSION(6.0f, 2.0f, 0, Vec2.ZERO, ExplosionAudioConfigs.GENERIC, null, null, false),
    ARTILLERY_EXPLOSION(32.0f, 4.0f, 2, new Vec2(0.2f, 1.5f), ExplosionAudioConfigs.ARTILLERY, ShakeNodePresets.EXPLOSION, ShakeNodePresets.EXPLOSION_FAINT, true),
    VEHICLE_EXPLOSION(16.0f, 4.0f, 2, new Vec2(0.2f, 1.5f), ExplosionAudioConfigs.GENERIC, ShakeNodePresets.EXPLOSION, ShakeNodePresets.EXPLOSION_FAINT, true),
    LANDMINE_EXPLOSION(16.0f, 3.0f, 2, new Vec2(0.2f, 1.5f), ExplosionAudioConfigs.GENERIC, ShakeNodePresets.EXPLOSION, ShakeNodePresets.EXPLOSION_FAINT, true);

    private final boolean shouldDisplayBlockDamage;
    private final float maxDistance;
    private final float power;
    private final int blockDamageRadius;
    @NotNull
    final Vec2 blockDamageMotion;
    @NotNull
    final ExplosionAudioConfig audioConfig;
    @Nullable
    final ShakeNodeData shakeNodeData;
    @Nullable
    final ShakeNodeData shakeNodeFaintData;

    private ExplosionType(float maxDistance, @NotNull float power, @NotNull int blockDamageRadius, @Nullable Vec2 blockDamageMotion, @Nullable ExplosionAudioConfig audioConfig, ShakeNodeData shakeNodeData, ShakeNodeData shakeNodeFaintData, boolean shouldDisplayBlockDamage) {
        this.maxDistance = maxDistance;
        this.power = power;
        this.blockDamageRadius = blockDamageRadius;
        this.blockDamageMotion = blockDamageMotion;
        this.audioConfig = audioConfig;
        this.shakeNodeData = shakeNodeData;
        this.shakeNodeFaintData = shakeNodeFaintData;
        this.shouldDisplayBlockDamage = shouldDisplayBlockDamage;
    }

    public float getMaxDistance() {
        return this.maxDistance;
    }

    public float getPower() {
        return this.power;
    }

    public int getBlockDamageRadius() {
        return this.blockDamageRadius;
    }

    @NotNull
    public Vec2 getBlockDamageMotion() {
        return this.blockDamageMotion;
    }

    @NotNull
    public ExplosionAudioConfig getAudioConfig() {
        return this.audioConfig;
    }

    @Nullable
    public ShakeNodeData getShakeNodeData() {
        return this.shakeNodeData;
    }

    @Nullable
    public ShakeNodeData getShakeNodeFaintData() {
        return this.shakeNodeFaintData;
    }

    public boolean shouldDisplayBlockDamage() {
        return this.shouldDisplayBlockDamage;
    }
}

