/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.client.sound;

import com.boehmod.blockfront.client.sound.BFMusicType;
import java.util.EnumMap;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BFMusicSet {
    @NotNull
    private final EnumMap<BFMusicType, SoundEvent> sounds = new EnumMap(BFMusicType.class);

    @NotNull
    public BFMusicSet register(@NotNull BFMusicType type, @NotNull SoundEvent sound) {
        this.sounds.put(type, sound);
        return this;
    }

    @Nullable
    public SoundEvent get(@NotNull BFMusicType type) {
        return this.sounds.get((Object)type);
    }
}

