/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.block.BlockRenderDispatcher
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.RegistryUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LoopingSoundPointMapEffect
extends PositionedMapEffect {
    @NotNull
    private static final ResourceLocation DEBUG_TEXTURE = BFRes.loc("textures/misc/debug/sound_looping.png");
    private static final Component DEBUG_HEADER = Component.literal((String)"Looping Sound Point");
    @Nullable
    public Supplier<SoundEvent> sound;
    public int maxTime;
    public int time = 0;
    public float volume = 1.5f;
    public float pitch = 1.0f;
    public float activationDistance = 25.0f;

    public LoopingSoundPointMapEffect() {
        this((Supplier<SoundEvent>)BFSounds.AMBIENT_LSP_ALARM_0, Vec3.ZERO, 0);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    public LoopingSoundPointMapEffect(@NotNull Supplier<SoundEvent> supplier, @NotNull Vec3 vec3, int n) {
        super(vec3);
        this.sound = supplier;
        this.maxTime = n;
    }

    public LoopingSoundPointMapEffect setTime(int time) {
        this.time = time;
        return this;
    }

    public LoopingSoundPointMapEffect setVolume(float volume) {
        this.volume = volume;
        return this;
    }

    public LoopingSoundPointMapEffect setActivationDistance(float activationDistance) {
        this.activationDistance = activationDistance;
        return this;
    }

    public LoopingSoundPointMapEffect setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        if (this.time-- > 0) {
            return;
        }
        this.time = this.maxTime;
        if (this.sound != null && Mth.sqrt((float)((float)player.distanceToSqr(this.position))) <= this.activationDistance) {
            level.playLocalSound(this.position.x, this.position.y, this.position.z, this.sound.get(), SoundSource.AMBIENT, this.volume, this.pitch, false);
        }
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
    }

    @Override
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
        String string;
        BFRendering.billboardTexture(poseStack, camera, graphics, DEBUG_TEXTURE, this.position.add(0.0, 1.0, 0.0), 64, false);
        BFRendering.billboardComponent(poseStack, camera, font, graphics, DEBUG_HEADER, this.position.x, this.position.y, this.position.z);
        ResourceLocation resourceLocation = BuiltInRegistries.SOUND_EVENT.getKey((Object)this.sound.get());
        if (resourceLocation != null) {
            string = String.format("Sound: '%s'", String.valueOf(ChatFormatting.GRAY) + resourceLocation.toString() + String.valueOf(ChatFormatting.RESET));
            BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string), this.position.x, this.position.y - 0.5, this.position.z);
        }
        string = String.format("Volume: '%s', Pitch: '%s'", String.valueOf(ChatFormatting.GRAY) + this.volume + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + this.pitch + String.valueOf(ChatFormatting.RESET));
        BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string), this.position.x, this.position.y - 1.0, this.position.z);
        String string2 = String.format("Timer: '%s/%s'", String.valueOf(ChatFormatting.GRAY) + this.time + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + this.maxTime + String.valueOf(ChatFormatting.RESET));
        BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string2), this.position.x, this.position.y - 1.5, this.position.z);
    }

    @Override
    public boolean requiresFancyGraphics() {
        return true;
    }

    @Override
    public void reset() {
    }

    @Override
    public // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull RenderLevelStageEvent.Stage getRenderStage() {
        return RenderLevelStageEvent.Stage.AFTER_PARTICLES;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        String string;
        super.writeToFDS(fDSTagCompound);
        if (this.sound != null && (string = RegistryUtils.getSoundEventId(this.sound.get())) != null) {
            fDSTagCompound.setString("sound", string);
        }
        fDSTagCompound.setInteger("maxTime", this.maxTime);
        fDSTagCompound.setFloat("volume", this.volume);
        fDSTagCompound.setFloat("pitch", this.pitch);
        fDSTagCompound.setFloat("activationDistance", this.activationDistance);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        String string = fDSTagCompound.getString("sound");
        if (string != null) {
            this.sound = RegistryUtils.retrieveSoundEvent(string);
        }
        this.maxTime = fDSTagCompound.getInteger("maxTime");
        this.volume = fDSTagCompound.getFloat("volume");
        this.pitch = fDSTagCompound.getFloat("pitch");
        this.activationDistance = fDSTagCompound.getFloat("activationDistance");
    }
}

