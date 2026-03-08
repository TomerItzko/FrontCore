/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.gun.bullet.LivingBulletCollision;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record BF_1223(long shootTick, @NotNull ServerPlayer serverPlayer, @NotNull BFServerManager serverModManager, @NotNull ServerPlayerDataHandler playerDataHandler, @NotNull List<LivingBulletCollision> clientHits, @NotNull Vec3 position, @NotNull Vec2 direction, float reportedItemSpread, float shootBackPitch, long randomSeed) {
}

