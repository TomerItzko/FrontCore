/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.RegistryUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class ParticleEmitterMapEffect
extends PositionedMapEffect {
    @NotNull
    private SimpleParticleType field_3165;
    private int field_3168;
    private int field_3169 = 0;
    @Nullable
    private Supplier<SoundEvent> field_3167 = null;
    private float field_3166;

    public ParticleEmitterMapEffect() {
        this((SimpleParticleType)BFParticleTypes.FAR_SMOKE_PARTICLE.get(), Vec3.ZERO, 0);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    public ParticleEmitterMapEffect(@NotNull SimpleParticleType simpleParticleType, @NotNull Vec3 vec3, int n) {
        super(vec3);
        this.field_3165 = simpleParticleType;
        this.field_3168 = n;
    }

    public ParticleEmitterMapEffect method_3111(@NotNull Supplier<SoundEvent> supplier, float f) {
        this.field_3167 = supplier;
        this.field_3166 = f;
        return this;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        if (this.field_3169++ <= this.field_3168) {
            return;
        }
        this.field_3169 = 0;
        ClientUtils.spawnParticle(minecraft, manager, level, this.field_3165, this.position.x + (double)random.nextInt(2), this.position.y, this.position.z + (double)random.nextInt(2), 0.0, 0.0, 0.0);
        if (this.field_3167 != null) {
            level.playLocalSound(this.position.x, this.position.y, this.position.z, this.field_3167.get(), SoundSource.AMBIENT, this.field_3166, 1.0f, false);
        }
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
    }

    @Override
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
        BFRendering.billboardTexture(poseStack, camera, graphics, BFRes.loc("textures/gui/menu/icons/console.png"), this.position, 64, false);
        BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)"Particle Emitter Map Effect"), this.position.x, this.position.y - (double)0.6f, this.position.z);
        String string = String.format("X: '%s', Y: '%s', Z: '%s'", String.valueOf(ChatFormatting.GRAY) + this.position.x + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + this.position.y + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + this.position.z + String.valueOf(ChatFormatting.RESET));
        BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string), this.position.x, this.position.y - (double)1.1f, this.position.z);
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
        String string2 = RegistryUtils.getParticleTypeId(this.field_3165);
        if (string2 != null) {
            fDSTagCompound.setString("particleType", string2);
        }
        fDSTagCompound.setInteger("maxTick", this.field_3168);
        if (this.field_3167 != null && (string = RegistryUtils.getSoundEventId(this.field_3167.get())) != null) {
            fDSTagCompound.setString("sound", string);
        }
        fDSTagCompound.setFloat("soundVolume", this.field_3166);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        String string = fDSTagCompound.getString("particleType");
        if (string != null) {
            this.field_3165 = (SimpleParticleType)RegistryUtils.retrieveParticleType(string).get();
        }
        this.field_3168 = fDSTagCompound.getInteger("maxTick");
        String string2 = fDSTagCompound.getString("sound");
        if (string2 != null) {
            this.field_3167 = RegistryUtils.retrieveSoundEvent(string2);
        }
        this.field_3166 = fDSTagCompound.getFloat("soundVolume");
    }
}

