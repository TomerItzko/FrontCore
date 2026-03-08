/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.gui.layer.CrosshairGuiLayer;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.registry.BFSounds;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;

public final class BF_33
extends ClientTickable {
    private static final int field_124 = 15;
    private static final float field_122 = 5.0f;
    private int field_125 = 0;

    @Override
    public void run(@NotNull ClientTickEvent.Post event, @NotNull Random random, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull BFClientManager manager, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull PlayerCloudData cloudData, @NotNull Vec3 pos, @NotNull BlockPos blockPos, @Nullable AbstractGame<?, ?, ?> game, boolean bl, float renderTime) {
        if (player != null && !playerData.isOutOfGame() && --this.field_125 <= 0) {
            float f;
            this.field_125 = 15;
            float f2 = player.getHealth();
            float f3 = f2 / (f = player.getMaxHealth());
            if (f3 > 0.0f && f2 <= 5.0f) {
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.MISC_HEARTBEAT.get()), (float)1.0f, (float)((1.0f - f3) / 2.0f)));
            }
        }
        if (CrosshairGuiLayer.hitMarkerTimer > 0) {
            --CrosshairGuiLayer.hitMarkerTimer;
        }
        if (GunItem.field_4059 > 0) {
            --GunItem.field_4059;
        }
    }
}

