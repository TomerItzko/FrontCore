/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ttt;

import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public enum TTTPlayerTeam {
    NEUTRAL("Neutrals", 0xFFFFFF, BFSounds.MATCH_GAMEMODE_TTT_WIN_INNOCENT),
    GOOD("Innocents", 1755161, BFSounds.MATCH_GAMEMODE_TTT_WIN_INNOCENT),
    BAD("Traitors", 13048088, BFSounds.MATCH_GAMEMODE_TTT_WIN_TRAITOR);

    @NotNull
    private final String name;
    private final int color;
    @NotNull
    private final DeferredHolder<SoundEvent, SoundEvent> winSound;

    private TTTPlayerTeam(@NotNull String name, int color, DeferredHolder<SoundEvent, SoundEvent> winSound) {
        this.name = name;
        this.color = color;
        this.winSound = winSound;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public int getColor() {
        return this.color;
    }

    @NotNull
    public DeferredHolder<SoundEvent, SoundEvent> getWinSound() {
        return this.winSound;
    }
}

