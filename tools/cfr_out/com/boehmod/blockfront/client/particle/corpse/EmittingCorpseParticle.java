/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.core.particles.SimpleParticleType
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3dc
 */
package com.boehmod.blockfront.client.particle.corpse;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.corpse.physics.CorpsePartPhysics;
import com.boehmod.blockfront.client.particle.corpse.CorpseParticle;
import com.boehmod.blockfront.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

public class EmittingCorpseParticle
extends CorpseParticle {
    private final int maxTicks;
    private final int spawnInterval;
    private final int instances;
    @NotNull
    private final SimpleParticleType particleType;
    private int ticks = 0;
    private int spawnTimer = 0;

    public EmittingCorpseParticle(int maxTicks, int spawnInterval, int instances, @NotNull SimpleParticleType particleType) {
        this.maxTicks = maxTicks;
        this.spawnInterval = spawnInterval;
        this.instances = instances;
        this.particleType = particleType;
    }

    @Override
    public boolean update(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull CorpsePartPhysics partPhysics, @NotNull Vector3dc position) {
        if (this.spawnTimer++ >= this.spawnInterval) {
            this.spawnTimer = 0;
            for (int i = 0; i < this.instances; ++i) {
                ClientUtils.spawnParticle(minecraft, manager, level, this.particleType, position.x(), position.y(), position.z(), 0.0, 0.0, 0.0);
            }
        }
        return this.ticks++ >= this.maxTicks;
    }
}

