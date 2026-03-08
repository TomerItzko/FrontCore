/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance$Attenuation
 *  net.minecraft.client.resources.sounds.TickableSoundInstance
 *  net.minecraft.sounds.SoundSource
 */
package com.boehmod.blockfront.client.sound.menu;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.sounds.SoundSource;

public class BFMenuMusicSearching
extends SimpleSoundInstance
implements TickableSoundInstance {
    public BFMenuMusicSearching() {
        super(BFSounds.MUSIC_MENU_SEARCHING.getId(), SoundSource.MUSIC, 0.5f, 1.0f, SoundInstance.createUnseededRandom(), true, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
        this.volume = 0.0f;
    }

    public boolean isStopped() {
        return false;
    }

    public void tick() {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        this.volume = MathUtils.moveTowards(this.volume, bFClientManager.getMatchMaking().isSearching() ? 1.0f : 0.0f, 0.01f);
    }
}

