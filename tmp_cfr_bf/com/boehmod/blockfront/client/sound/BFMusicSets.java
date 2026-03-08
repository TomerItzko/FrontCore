/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.sound;

import com.boehmod.blockfront.client.sound.BFMusicSet;
import com.boehmod.blockfront.client.sound.BFMusicType;
import com.boehmod.blockfront.registry.BFSounds;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class BFMusicSets {
    @NotNull
    public static final Map<String, BFMusicSet> ENTRIES = new HashMap<String, BFMusicSet>();

    @NotNull
    public static String[] getIds() {
        return ENTRIES.keySet().toArray(new String[0]);
    }

    static {
        ENTRIES.put("europe", new BFMusicSet().register(BFMusicType.CONCLUDE, (SoundEvent)BFSounds.MUSIC_SETS_EUROPE_CONCLUDE.get()).register(BFMusicType.CONCLUDE_GENERIC, (SoundEvent)BFSounds.MUSIC_SETS_EUROPE_CONCLUDE_GENERIC.get()).register(BFMusicType.START, (SoundEvent)BFSounds.MUSIC_SETS_EUROPE_START.get()).register(BFMusicType.START_GENERIC, (SoundEvent)BFSounds.MUSIC_SETS_EUROPE_START_GENERIC.get()).register(BFMusicType.SUSPENSE_ONE, (SoundEvent)BFSounds.MUSIC_SETS_EUROPE_SUSPENSE_ONE.get()).register(BFMusicType.SUSPENSE_TWO, (SoundEvent)BFSounds.MUSIC_SETS_EUROPE_SUSPENSE_TWO.get()).register(BFMusicType.SUSPENSE_THREE, (SoundEvent)BFSounds.MUSIC_SETS_EUROPE_SUSPENSE_THREE.get()).register(BFMusicType.WARMUP, (SoundEvent)BFSounds.MUSIC_SETS_EUROPE_WARMUP.get()));
    }
}

