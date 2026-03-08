/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.block.BlockRenderDispatcher
 *  net.minecraft.client.renderer.debug.DebugRenderer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.AirstrikeRocketEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.util.ClientUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class FallingArtilleryMapEffect
extends AbstractMapEffect {
    private static final int field_3048 = 600;
    private Vec2 field_3050;
    private Vec2 field_3051;
    private int field_3049 = (int)(600.0 * Math.random());

    public FallingArtilleryMapEffect() {
        this(Vec2.ZERO, Vec2.ZERO);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    public FallingArtilleryMapEffect(@NotNull Vec2 vec2, @NotNull Vec2 vec22) {
        this.field_3050 = vec2;
        this.field_3051 = vec22;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        if (this.field_3049-- <= 0) {
            this.field_3049 = (int)(600.0 * Math.random());
            this.method_3067(random, level);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_3067(@NotNull Random random, @NotNull ClientLevel clientLevel) {
        Vec2 vec2 = new Vec2(this.field_3050.x + random.nextFloat() * (this.field_3051.x - this.field_3050.x), this.field_3050.y + random.nextFloat() * (this.field_3051.y - this.field_3050.y));
        if (!clientLevel.isLoaded(new BlockPos((int)vec2.x, 255, (int)vec2.y))) {
            return;
        }
        Vec3 vec3 = new Vec3((double)vec2.x, 255.0, (double)vec2.y);
        AirstrikeRocketEntity airstrikeRocketEntity = (AirstrikeRocketEntity)((EntityType)BFEntityTypes.AIRSTRIKE_ROCKET.get()).create((Level)clientLevel);
        if (airstrikeRocketEntity != null) {
            airstrikeRocketEntity.method_1935(vec3, 2.0f, new ItemStack((ItemLike)BFItems.GUN_PANZERSCHRECK.get()), 0.0f, 0.0f);
            airstrikeRocketEntity.setDeltaMovement(0.0, -1.0, 0.0);
            airstrikeRocketEntity.setPos(vec3.x, vec3.y, vec3.z);
            ClientUtils.spawnEntity((Entity)airstrikeRocketEntity, clientLevel);
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
        DebugRenderer.renderFilledBox((PoseStack)poseStack, (MultiBufferSource)buffer, (double)this.field_3050.x, (double)255.0, (double)this.field_3050.y, (double)this.field_3051.x, (double)255.0, (double)this.field_3051.y, (float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
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
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        fDSTagCompound.setFloat("startX", this.field_3050.x);
        fDSTagCompound.setFloat("startY", this.field_3050.y);
        fDSTagCompound.setFloat("endX", this.field_3051.x);
        fDSTagCompound.setFloat("endY", this.field_3051.y);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        this.field_3050 = new Vec2(fDSTagCompound.getFloat("startX"), fDSTagCompound.getFloat("startY"));
        this.field_3051 = new Vec2(fDSTagCompound.getFloat("endX"), fDSTagCompound.getFloat("endY"));
    }
}

