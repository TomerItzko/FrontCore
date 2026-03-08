/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block.base;

import com.boehmod.blockfront.client.BFClientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public interface IShootAction {
    public void onBulletHit(@NotNull Minecraft var1, @NotNull BFClientManager var2, @NotNull Level var3, int var4, Vec3 var5, @NotNull BlockPos var6, @NotNull BlockState var7, Direction var8);

    public boolean showParticle();
}

