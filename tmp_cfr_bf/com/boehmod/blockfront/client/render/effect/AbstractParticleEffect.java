/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.effect;

import com.boehmod.blockfront.client.BFClientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractParticleEffect {
    public abstract void render(@NotNull Minecraft var1, @NotNull BFClientManager var2, @NotNull ClientLevel var3, @NotNull RandomSource var4, @NotNull Vec3 var5, @NotNull BlockPos var6);
}

