/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.player;

import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.common.player.a;
import java.util.Random;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ClientFlamethrowerIgniteManager
extends a<BFClientPlayerData> {
    public ClientFlamethrowerIgniteManager(@NotNull BFClientPlayerData bFClientPlayerData) {
        super(bFClientPlayerData);
    }

    public void method_1102(@NotNull ClientLevel clientLevel, @NotNull Player player, @NotNull Random random) {
        if (!this.field_1145) {
            return;
        }
        Vec3 vec3 = new Vec3((double)-0.4f, 0.0, 0.0).yRot(-player.yBodyRot * ((float)Math.PI / 180) - 1.5707964f);
        Vec3 vec32 = player.getEyePosition().add(0.0, -0.25, 0.0).add(vec3);
        if (random.nextBoolean()) {
            clientLevel.addParticle((ParticleOptions)ParticleTypes.LAVA, vec32.x, vec32.y, vec32.z, 0.0, 0.0, 0.0);
        }
        clientLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, vec32.x, vec32.y, vec32.z, 0.0, 0.0, 0.0);
    }
}

