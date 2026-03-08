/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event;

import java.util.Locale;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.sound.PlaySoundSourceEvent;
import net.neoforged.neoforge.client.event.sound.PlayStreamingSourceEvent;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFSoundSubscriber {
    @SubscribeEvent
    public static void onPlaySoundSource(@NotNull PlaySoundSourceEvent event) {
        String string = event.getName().toLowerCase(Locale.ROOT);
        SoundInstance soundInstance = event.getSound();
        SoundEngine soundEngine = event.getEngine();
        if (string.equals("entity.generic.explode")) {
            soundEngine.stop(soundInstance);
        }
    }

    @SubscribeEvent
    public static void onPlayStreamingSource(@NotNull PlayStreamingSourceEvent event) {
        SoundInstance soundInstance = event.getSound();
        SoundEngine soundEngine = event.getEngine();
        if (soundInstance.getSource() == SoundSource.MUSIC && !soundInstance.getLocation().getNamespace().equals("bf")) {
            soundEngine.stop(soundInstance);
        }
    }

    @SubscribeEvent
    public static void onPlaySoundAtPosition(@NotNull PlayLevelSoundEvent.AtPosition event) {
        Holder holder = event.getSound();
        if (holder == null) {
            return;
        }
        String string = (String)holder.unwrap().map(key -> key.location().getPath(), sound -> sound.getLocation().getPath());
        BFSoundSubscriber.setStepVolume((PlayLevelSoundEvent)event, string);
    }

    @SubscribeEvent
    public static void onPlaySoundAtEntity(@NotNull PlayLevelSoundEvent.AtEntity event) {
        Holder holder = event.getSound();
        if (holder == null) {
            return;
        }
        String string = (String)holder.unwrap().map(key -> key.location().getPath(), sound -> sound.getLocation().getPath());
        BFSoundSubscriber.setStepVolume((PlayLevelSoundEvent)event, string);
    }

    private static void setStepVolume(@NotNull PlayLevelSoundEvent event, String soundId) {
        if (event.getOriginalVolume() > 0.0f && soundId.endsWith(".step") && event.getSource() == SoundSource.PLAYERS) {
            event.setNewVolume(0.3f);
        }
    }
}

