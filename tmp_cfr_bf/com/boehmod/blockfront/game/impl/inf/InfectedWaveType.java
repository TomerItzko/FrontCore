/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.game.impl.inf.InfectedPlayerManager;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public enum InfectedWaveType {
    GENERIC(1.0f, BFSounds.MATCH_GAMEMODE_INF_NEWROUND, BFSounds.MATCH_GAMEMODE_INF_ENDROUND, new RoundCallable(){

        @Override
        void method_3683(InfectedGame infectedGame, Level level, int n, Set<UUID> set) {
            MutableComponent mutableComponent = Component.literal((String)String.valueOf(infectedGame.currentRound)).withStyle(ChatFormatting.RED);
            MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(infectedGame.numInfectedToSpawn)).withStyle(ChatFormatting.RED);
            MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.infected.wave.begun", (Object[])new Object[]{mutableComponent, mutableComponent2}).withStyle(ChatFormatting.DARK_RED);
            BFUtils.sendNoticeMessage(set, (Component)Component.translatable((String)"bf.message.gamemode.infected.popup.wave", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD));
            BFUtils.sendNoticeMessage(set, (Component)mutableComponent3);
            FDSPose fDSPose = ((InfectedPlayerManager)infectedGame.getPlayerManager()).getLobbySpawn();
            if (fDSPose != null && infectedGame.currentRound >= 6) {
                Vec3 vec3 = fDSPose.position;
                SoundEvent soundEvent = (SoundEvent)BFSounds.MATCH_GAMEMODE_INF_HOARD.get();
                BFUtils.playPositionedSound(set, soundEvent, SoundSource.AMBIENT, 30.0f, vec3.add(100.0, 0.0, 100.0));
                BFUtils.playPositionedSound(set, soundEvent, SoundSource.AMBIENT, 30.0f, vec3.add(100.0, 0.0, -100.0));
                BFUtils.playPositionedSound(set, soundEvent, SoundSource.AMBIENT, 30.0f, vec3.add(-100.0, 0.0, -100.0));
                BFUtils.playPositionedSound(set, soundEvent, SoundSource.AMBIENT, 30.0f, vec3.add(-100.0, 0.0, 100.0));
            }
        }

        @Override
        void method_3682(@NotNull PlayerDataHandler<?> playerDataHandler, InfectedGame infectedGame, InfectedEntity infectedEntity, Level level) {
            float f = 0.2f + 0.025f * (float)infectedGame.method_3426();
            infectedEntity.method_2065(playerDataHandler, f);
        }
    }),
    DOGS(0.25f, BFSounds.MATCH_GAMEMODE_INF_DOGROUND, BFSounds.MATCH_GAMEMODE_INF_DOGROUND_END, new RoundCallable(){

        @Override
        void method_3683(InfectedGame infectedGame, Level level, int n, Set<UUID> set) {
            MutableComponent mutableComponent = Component.literal((String)String.valueOf(infectedGame.currentRound)).withStyle(ChatFormatting.RED);
            MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(infectedGame.numInfectedToSpawn)).withStyle(ChatFormatting.RED);
            MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.infected.wave.begun.dogs", (Object[])new Object[]{mutableComponent, mutableComponent2}).withStyle(ChatFormatting.DARK_RED);
            FDSPose fDSPose = ((InfectedPlayerManager)infectedGame.getPlayerManager()).getLobbySpawn();
            if (fDSPose != null) {
                level.playSound(null, fDSPose.asBlockPos(), (SoundEvent)BFSounds.ENTITY_INFECTED_DOG_HOWL.get(), SoundSource.AMBIENT, 35.0f, 1.0f);
            }
            BFUtils.sendNoticeMessage(set, Values.DOGS_POPUP);
            BFUtils.sendNoticeMessage(set, (Component)mutableComponent3);
        }

        @Override
        void method_3682(@NotNull PlayerDataHandler<?> playerDataHandler, InfectedGame infectedGame, InfectedEntity infectedEntity, Level level) {
            float f = 0.4f + 0.05f * (float)infectedGame.method_3426();
            infectedEntity.method_2065(playerDataHandler, f);
        }
    });

    private final float countMultiplier;
    private final DeferredHolder<SoundEvent, SoundEvent> roundStartSound;
    private final DeferredHolder<SoundEvent, SoundEvent> roundEndSound;
    private final RoundCallable roundCallable;

    private InfectedWaveType(float countMultiplier, DeferredHolder<SoundEvent, SoundEvent> roundStartSound, DeferredHolder<SoundEvent, SoundEvent> roundEndSound, RoundCallable roundCallable) {
        this.countMultiplier = countMultiplier;
        this.roundStartSound = roundStartSound;
        this.roundEndSound = roundEndSound;
        this.roundCallable = roundCallable;
    }

    public RoundCallable getRoundCallable() {
        return this.roundCallable;
    }

    public float getCountMultiplier() {
        return this.countMultiplier;
    }

    public final DeferredHolder<SoundEvent, SoundEvent> getRoundStartSound() {
        return this.roundStartSound;
    }

    public final DeferredHolder<SoundEvent, SoundEvent> getRoundEndSound() {
        return this.roundEndSound;
    }

    public static abstract class RoundCallable {
        abstract void method_3683(InfectedGame var1, Level var2, int var3, Set<UUID> var4);

        abstract void method_3682(@NotNull PlayerDataHandler<?> var1, InfectedGame var2, InfectedEntity var3, Level var4);
    }

    private static class Values {
        public static final Component DOGS_POPUP = Component.translatable((String)"bf.message.gamemode.infected.popup.wave.dogs").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD);

        private Values() {
        }
    }
}

