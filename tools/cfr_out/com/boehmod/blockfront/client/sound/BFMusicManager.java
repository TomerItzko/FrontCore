/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.sounds.SoundEvent
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.client.sound;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.sound.BFMusicSet;
import com.boehmod.blockfront.client.sound.BFMusicType;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameMusic;
import com.boehmod.blockfront.unnamed.BF_392;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.ArrayDeque;
import java.util.Queue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BFMusicManager {
    @NotNull
    private final ObjectList<BF_392> field_2019 = new ObjectArrayList();
    @NotNull
    private Queue<GameMusic.BF_440> field_2020 = new ArrayDeque<GameMusic.BF_440>();
    private int field_2022 = 0;
    @Nullable
    private BF_392 field_2018 = null;

    public void update(@NotNull BFClientManager bFClientManager) {
        this.field_2019.removeIf(bF_392 -> bF_392.method_1402() && bF_392.getVolume() <= 0.0f);
        this.method_1533(bFClientManager);
    }

    private void method_1533(@NotNull BFClientManager bFClientManager) {
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            this.method_1532();
        } else if (this.field_2022 > 0) {
            --this.field_2022;
        } else if (this.field_2018 == null || this.field_2018.method_1402()) {
            this.method_1534(Minecraft.getInstance());
        }
    }

    public void method_1531(@NotNull Minecraft minecraft, @NotNull BFMusicType bFMusicType, boolean bl, boolean bl2) {
        if (bl2) {
            this.method_1532();
        }
        this.field_2020.add(new GameMusic.BF_440(bFMusicType, bl, bl2, 0));
        if (this.field_2018 == null) {
            this.method_1534(minecraft);
        }
    }

    public void method_1530(@NotNull Queue<GameMusic.BF_440> queue) {
        this.field_2020 = queue;
    }

    public void method_1534(@NotNull Minecraft minecraft) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        BFMusicSet bFMusicSet = abstractGame.getMap().getMusicSet();
        if (bFMusicSet == null) {
            return;
        }
        if (this.field_2020.isEmpty()) {
            return;
        }
        GameMusic.BF_440 bF_440 = this.field_2020.poll();
        if (bF_440 == null) {
            return;
        }
        if (bF_440.field_2027 > 0) {
            this.field_2022 = bF_440.field_2027;
            return;
        }
        if (bF_440.field_2024 == null) {
            return;
        }
        SoundEvent soundEvent = bFMusicSet.get(bF_440.field_2024);
        if (soundEvent == null) {
            return;
        }
        BF_392 bF_392 = new BF_392(soundEvent, bF_440.field_2025, bF_440.field_2026);
        minecraft.getSoundManager().play((SoundInstance)bF_392);
        this.field_2018 = bF_392;
        for (BF_392 bF_3922 : this.field_2019) {
            bF_3922.method_1401(true);
        }
        this.field_2019.add((Object)bF_392);
    }

    public void method_1532() {
        if (this.field_2018 != null) {
            this.field_2018.method_1401(true);
            this.field_2018 = null;
        }
        this.field_2020.clear();
    }
}

