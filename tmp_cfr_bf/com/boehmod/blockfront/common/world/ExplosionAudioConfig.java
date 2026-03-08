/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.world;

import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExplosionAudioConfig {
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> ambience;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> ambienceDistant;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> nose;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> noseDistant;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> ambienceIndoors;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> ambienceIndoorsDistant;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> explodeClose;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> explodeCloseIndoors;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> explodeCloseStereo;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> explodeDistant;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> explodeDistantIndoors;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> bass;

    @NotNull
    public ExplosionAudioConfig ambience(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder2, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder3, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder4) {
        this.ambience = deferredHolder;
        this.ambienceDistant = deferredHolder2;
        this.ambienceIndoors = deferredHolder3;
        this.ambienceIndoorsDistant = deferredHolder4;
        return this;
    }

    @NotNull
    public ExplosionAudioConfig explode(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder2, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder3, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder4, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder5) {
        this.explodeClose = deferredHolder;
        this.explodeCloseIndoors = deferredHolder2;
        this.explodeCloseStereo = deferredHolder3;
        this.explodeDistant = deferredHolder4;
        this.explodeDistantIndoors = deferredHolder5;
        return this;
    }

    @NotNull
    public ExplosionAudioConfig nose(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder2) {
        this.nose = deferredHolder;
        this.noseDistant = deferredHolder2;
        return this;
    }

    @NotNull
    public ExplosionAudioConfig bass(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        this.bass = deferredHolder;
        return this;
    }
}

