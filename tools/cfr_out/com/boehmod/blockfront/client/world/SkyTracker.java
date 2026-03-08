/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.common.NeoForgeMod
 *  net.neoforged.neoforge.fluids.FluidType
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.world;

import com.boehmod.blockfront.client.event.BFClientTickSubscriber;
import com.boehmod.blockfront.util.debug.DebugLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

public class SkyTracker {
    public static final float field_6828 = 8.0f;
    private int timer = 0;
    private float percentage = 0.0f;
    private float field_6829 = 0.0f;

    public void update(@NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull LocalPlayer player) {
        if (this.timer-- >= 0) {
            return;
        }
        this.timer = 5;
        Entity entity = minecraft.getCameraEntity();
        if (entity == null) {
            return;
        }
        BlockPos blockPos = entity.getOnPos();
        Vec3 vec3 = entity.position();
        if (!player.isEyeInFluidType((FluidType)NeoForgeMod.WATER_TYPE.value())) {
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            int n4 = 4;
            for (int i = -4; i < 4; ++i) {
                for (int j = -4; j < 4; ++j) {
                    BlockPos blockPos2 = blockPos.offset(i, 2, j);
                    Vec3 vec32 = blockPos2.getCenter();
                    if (!level.getBlockState(blockPos2).isAir()) continue;
                    if (level.canSeeSky(blockPos2)) {
                        ++n2;
                    } else {
                        Vec3 vec33;
                        ClipContext clipContext;
                        BlockHitResult blockHitResult;
                        Vec3 vec34;
                        float f;
                        Vec3 vec35 = vec32.add((double)i, 32.0, (double)j);
                        ClipContext clipContext2 = new ClipContext(vec32, vec35, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)player);
                        BlockHitResult blockHitResult2 = level.clip(clipContext2);
                        Vec3 vec36 = blockHitResult2.getLocation();
                        float f2 = (float)vec36.distanceTo(vec32);
                        float f3 = f2 + (f = (float)(vec34 = (blockHitResult = level.clip(clipContext = new ClipContext(vec32, vec33 = vec32.add((double)i, -32.0, (double)j), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)player))).getLocation()).distanceTo(vec32));
                        if (f3 >= 8.0f) {
                            ++n3;
                        }
                        if (minecraft.getDebugOverlay().showDebugScreen()) {
                            BFClientTickSubscriber.DEBUG_LINES.add(new DebugLine(vec32, vec36, 10, true, -16711936));
                            BFClientTickSubscriber.DEBUG_LINES.add(new DebugLine(vec32, vec34, 10, true, -16776961));
                        }
                    }
                    ++n;
                }
            }
            this.percentage = n > 0 ? (float)n2 / (float)n : 0.0f;
            this.field_6829 = n > 0 ? (float)n3 / (float)n : 0.0f;
        }
    }

    public float getPercentage() {
        return this.percentage;
    }

    public float method_5781() {
        return this.field_6829;
    }
}

