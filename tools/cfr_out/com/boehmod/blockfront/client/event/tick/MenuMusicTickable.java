/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.event.tick;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.sound.menu.BFMenuMusic;
import com.boehmod.blockfront.client.sound.menu.BFMenuMusicSearching;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;

public class MenuMusicTickable
extends ClientTickable {
    public static boolean play = false;
    @NotNull
    private static final BFMenuMusic MUSIC = new BFMenuMusic();
    @NotNull
    private static final BFMenuMusicSearching MUSIC_SEARCHING = new BFMenuMusicSearching();

    @Override
    public void run(@NotNull ClientTickEvent.Post event, @NotNull Random random, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull BFClientManager manager, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull PlayerCloudData cloudData, @NotNull Vec3 pos, @NotNull BlockPos blockPos, @Nullable AbstractGame<?, ?, ?> game, boolean bl, float renderTime) {
        SoundManager soundManager = minecraft.getSoundManager();
        MUSIC.tick();
        MUSIC_SEARCHING.tick();
        if (level != null) {
            if (soundManager.isActive((SoundInstance)MUSIC)) {
                soundManager.stop((SoundInstance)MUSIC);
            }
            if (soundManager.isActive((SoundInstance)MUSIC_SEARCHING)) {
                soundManager.stop((SoundInstance)MUSIC_SEARCHING);
            }
        } else if (play) {
            if (!soundManager.isActive((SoundInstance)MUSIC)) {
                soundManager.play((SoundInstance)MUSIC);
            }
            if (!soundManager.isActive((SoundInstance)MUSIC_SEARCHING)) {
                soundManager.play((SoundInstance)MUSIC_SEARCHING);
            }
        }
    }
}

