/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event.tick;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ClientTickable {
    public abstract void run(@NotNull ClientTickEvent.Post var1, @NotNull Random var2, @NotNull Minecraft var3, @NotNull ClientPlayerDataHandler var4, @NotNull BFClientManager var5, @Nullable LocalPlayer var6, @Nullable ClientLevel var7, @NotNull BFClientPlayerData var8, @NotNull PlayerCloudData var9, @NotNull Vec3 var10, @NotNull BlockPos var11, @Nullable AbstractGame<?, ?, ?> var12, boolean var13, float var14);
}

