/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event.tick;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.gun.GunSpreadTarget;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GunSpreadTickable
extends ClientTickable {
    public static final float field_192 = 0.04f;
    public static final float field_193 = 0.31f;
    @NotNull
    public static GunSpreadTarget target = GunSpreadTarget.IDLE;

    @Override
    public void run(@NotNull ClientTickEvent.Post event, @NotNull Random random, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull BFClientManager manager, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull PlayerCloudData cloudData, @NotNull Vec3 pos, @NotNull BlockPos blockPos, @Nullable AbstractGame<?, ?, ?> game, boolean bl, float renderTime) {
        Vec3 vec3;
        BlockHitResult blockHitResult;
        if (player == null || level == null) {
            target = GunSpreadTarget.IDLE;
            return;
        }
        double d = player.walkDist - player.walkDistO;
        boolean bl2 = false;
        if (!player.onGround() && (blockHitResult = level.clip(new ClipContext(vec3 = player.position(), vec3.add(0.0, -0.75, 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()))) instanceof BlockHitResult) {
            BlockHitResult blockHitResult2 = blockHitResult;
            bl2 = blockHitResult2.getType() == HitResult.Type.MISS;
        }
        boolean bl3 = player.getVehicle() != null;
        boolean bl4 = d > (double)0.04f;
        boolean bl5 = bl4 && player.isSprinting();
        boolean bl6 = Math.abs(player.getDeltaMovement().y) >= (double)0.31f || bl2;
        GunSpreadTarget gunSpreadTarget = GunSpreadTarget.IDLE;
        if (!bl3) {
            if (player.isCrouching()) {
                gunSpreadTarget = GunSpreadTarget.CRAWLING;
            }
            if (bl4) {
                gunSpreadTarget = GunSpreadTarget.WALKING;
            }
            if (bl5) {
                gunSpreadTarget = GunSpreadTarget.SPRINTING;
            }
            if (bl6) {
                gunSpreadTarget = GunSpreadTarget.JUMPING;
            }
        }
        target = gunSpreadTarget;
    }
}

