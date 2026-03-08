/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry.custom;

import com.boehmod.blockfront.util.BFRes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.NotNull;

public record BotVoice(@NotNull DeferredHolder<SoundEvent, SoundEvent> goSound, @NotNull DeferredHolder<SoundEvent, SoundEvent> grenadeSound, @NotNull DeferredHolder<SoundEvent, SoundEvent> hurtSound, @NotNull DeferredHolder<SoundEvent, SoundEvent> reloadingSound, @NotNull DeferredHolder<SoundEvent, SoundEvent> spottedSound, @NotNull DeferredHolder<SoundEvent, SoundEvent> tauntSound, @NotNull DeferredHolder<SoundEvent, SoundEvent> introSound) {
    @NotNull
    public static final ResourceKey<Registry<BotVoice>> KEY = ResourceKey.createRegistryKey((ResourceLocation)BFRes.loc("bot_voice"));
    @NotNull
    public static final Registry<BotVoice> REGISTRY = new RegistryBuilder(KEY).sync(true).create();
}

