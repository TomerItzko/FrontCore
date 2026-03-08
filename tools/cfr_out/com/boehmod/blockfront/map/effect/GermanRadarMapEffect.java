/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.Nullable
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.block.BlockRenderDispatcher
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LightningBolt
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.ConditionedMapEffect;
import com.boehmod.blockfront.map.effect.MapEffectCondition;
import com.boehmod.blockfront.registry.BFBlockEntityTypes;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class GermanRadarMapEffect
extends ConditionedMapEffect {
    private float field_3041;
    private float field_3044 = 0.0f;
    private float field_3045 = 0.05f;
    private boolean field_3042 = false;
    private boolean field_3043 = false;
    private int field_3046 = 20;
    private int field_3047 = 200;
    @Nullable
    private BFBlockEntity field_3040 = null;

    public GermanRadarMapEffect() {
        this(Vec3.ZERO, "Unknown", MapEffectCondition.DOMINATION_HALF_SCORE);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    public GermanRadarMapEffect(@NotNull Vec3 vec3, @NotNull String string, @NotNull MapEffectCondition mapEffectCondition) {
        super(vec3, string, mapEffectCondition);
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_3066(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, @NotNull LocalPlayer localPlayer) {
        ShakeManager.applyShake(ShakeNodePresets.field_1923, localPlayer, this.position, 64.0f);
        int n = 16;
        for (int i = -n; i < n; ++i) {
            for (int j = -n; j < n; ++j) {
                for (int k = 0; k < 5; ++k) {
                    if (!(Math.random() < (double)0.05f)) continue;
                    ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, (SimpleParticleType)BFParticleTypes.LONG_GRENADE_SMOKE.get(), this.position.x + (double)i, this.position.y - 6.0 + (double)k, this.position.z - (double)j, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        BFBlockEntity bFBlockEntity;
        super.updateGameClient(minecraft, manager, random, game, player, level, delta);
        if (this.field_3040 == null && (bFBlockEntity = (this.field_3040 = (BFBlockEntity)((BlockEntityType)BFBlockEntityTypes.GERMAN_RADAR.get()).create(BlockPos.ZERO, ((Block)BFBlocks.GERMAN_RADAR.get()).defaultBlockState()))) instanceof BFBlockEntity) {
            BFBlockEntity bFBlockEntity2 = bFBlockEntity;
            bFBlockEntity2.method_1887();
        }
        if (this.field_3043) {
            if (this.field_3046-- <= 0) {
                this.field_3046 = 80;
                level.playLocalSound(this.position.x, this.position.y + 5.0, this.position.z, (SoundEvent)BFSounds.AMBIENT_LSP_ALARM_2.get(), SoundSource.AMBIENT, 1.5f, 1.0f, false);
            }
            if (this.field_3047-- <= 0) {
                this.field_3047 = 200;
                level.playLocalSound(this.position.x - 12.0, this.position.y, this.position.z - 5.0, (SoundEvent)BFSounds.AMBIENT_LSP_WATER_DRIP_METAL_0.get(), SoundSource.AMBIENT, 2.5f, 1.0f, false);
                level.playLocalSound(this.position.x - 15.0, this.position.y, this.position.z + 5.0, (SoundEvent)BFSounds.AMBIENT_LSP_WATER_DRIP_METAL_1.get(), SoundSource.AMBIENT, 2.5f, 1.0f, false);
                level.playLocalSound(this.position.x - 20.0, this.position.y, this.position.z + 15.0, (SoundEvent)BFSounds.AMBIENT_LSP_WATER_DRIP_METAL_1.get(), SoundSource.AMBIENT, 2.5f, 1.0f, false);
            }
        }
        this.field_3044 = this.field_3041;
        if (this.field_3042 && !this.field_3043) {
            this.field_3041 += (this.field_3045 *= 1.05f);
            if (this.field_3041 >= 90.0f) {
                this.field_3043 = true;
                this.method_3066(minecraft, manager, level, player);
            }
        }
        if (this.field_3043 && player.tickCount % 12 == 0) {
            ClientUtils.spawnParticle(minecraft, manager, level, (SimpleParticleType)BFParticleTypes.FAR_SMOKE_PARTICLE.get(), this.position.x, this.position.y + 0.5, this.position.z - 8.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
        if (this.field_3040 == null) {
            return;
        }
        float f = MathUtils.lerpf1(this.field_3041, this.field_3044, delta);
        BFRendering.blockEntity(minecraft, level, poseStack, this.field_3040, this.position.x, this.position.y, this.position.z, f, 90.0f, delta);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
    }

    @Override
    public boolean requiresFancyGraphics() {
        return false;
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
    @OnlyIn(value=Dist.CLIENT)
    public void onConditionMet(@NotNull Minecraft minecraft, @NotNull ClientLevel clientLevel, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        clientLevel.setSkyFlashTime(3);
        clientLevel.playLocalSound(this.position.x, this.position.y + 5.0, this.position.z, (SoundEvent)BFSounds.BLOCK_POWERLINE_EXPLODE.get(), SoundSource.AMBIENT, 1.5f, 1.0f, false);
        clientLevel.playLocalSound(this.position.x, this.position.y + 5.0, this.position.z, (SoundEvent)BFSounds.AMBIENT_LSP_THUNDER.get(), SoundSource.AMBIENT, 25.0f, 1.0f, false);
        clientLevel.playLocalSound(this.position.x, this.position.y + 10.0, this.position.z, (SoundEvent)BFSounds.AMBIENT_LSP_RADAR_COLLAPSE.get(), SoundSource.AMBIENT, 15.0f, 1.0f, false);
        clientLevel.playLocalSound(this.position.x, this.position.y + 10.0, this.position.z, (SoundEvent)BFSounds.AMBIENT_LSP_RADAR_COLLAPSE.get(), SoundSource.AMBIENT, 15.0f, 1.0f, false);
        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, (Level)clientLevel);
        lightningBolt.setVisualOnly(true);
        lightningBolt.setPos(this.position.x, this.position.y + 5.0, this.position.z);
        lightningBolt.moveTo(this.position.x, this.position.y + 5.0, this.position.z, 0.0f, 0.0f);
        ClientUtils.spawnEntity((Entity)lightningBolt, clientLevel);
        this.field_3042 = true;
    }

    @Override
    public void resetInternal(@NotNull Minecraft minecraft) {
        this.field_3042 = false;
        this.field_3043 = false;
        this.field_3044 = 0.0f;
        this.field_3041 = 0.0f;
        this.field_3045 = 0.05f;
    }
}

