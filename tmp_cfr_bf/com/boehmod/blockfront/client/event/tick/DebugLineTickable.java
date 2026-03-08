/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event.tick;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFClientTickSubscriber;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.debug.DebugLine;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;

public class DebugLineTickable
extends ClientTickable {
    @Override
    public void run(@NotNull ClientTickEvent.Post event, @NotNull Random random, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull BFClientManager manager, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull PlayerCloudData cloudData, @NotNull Vec3 pos, @NotNull BlockPos blockPos, @Nullable AbstractGame<?, ?, ?> game, boolean bl, float renderTime) {
        BFClientTickSubscriber.DEBUG_LINES.removeIf(DebugLine::update);
    }
}

