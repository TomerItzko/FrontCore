/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.world.ExplosionType;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.util.ExplosionUtils;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class ArtilleryExplosionMapEffect
extends AbstractMapEffect {
    private static final int field_3189 = 200;
    private Vec2 field_3191;
    private Vec2 field_3192;
    private int field_3190 = (int)(200.0 * Math.random());

    public ArtilleryExplosionMapEffect() {
        this(Vec2.ZERO, Vec2.ZERO);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    public ArtilleryExplosionMapEffect(@NotNull Vec2 vec2, @NotNull Vec2 vec22) {
        this.field_3191 = vec2;
        this.field_3192 = vec22;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        if (this.field_3190-- <= 0) {
            this.field_3190 = (int)(200.0 * Math.random());
            this.method_3118(minecraft, manager, random, level);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_3118(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull Random random, @NotNull ClientLevel clientLevel) {
        Vec2 vec2 = new Vec2(this.field_3191.x + random.nextFloat() * (this.field_3192.x - this.field_3191.x), this.field_3191.y + random.nextFloat() * (this.field_3192.y - this.field_3191.y));
        if (!clientLevel.isLoaded(new BlockPos((int)vec2.x, 255, (int)vec2.y))) {
            return;
        }
        Vec3 vec3 = new Vec3((double)vec2.x, 255.0, (double)vec2.y);
        ClipContext clipContext = new ClipContext(vec3, vec3.subtract(0.0, 255.0, 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.WATER, CollisionContext.empty());
        BlockHitResult blockHitResult = clientLevel.clip(clipContext);
        Vec3 vec32 = blockHitResult.getLocation();
        ExplosionUtils.explode(minecraft, bFClientManager, clientLevel, ExplosionType.ARTILLERY_EXPLOSION, vec32);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
        DebugRenderer.renderFilledBox((PoseStack)poseStack, (MultiBufferSource)buffer, (double)this.field_3191.x, (double)255.0, (double)this.field_3191.y, (double)this.field_3192.x, (double)255.0, (double)this.field_3192.y, (float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
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
        fDSTagCompound.setFloat("startX", this.field_3191.x);
        fDSTagCompound.setFloat("startY", this.field_3191.y);
        fDSTagCompound.setFloat("endX", this.field_3192.x);
        fDSTagCompound.setFloat("endY", this.field_3192.y);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        this.field_3191 = new Vec2(fDSTagCompound.getFloat("startX"), fDSTagCompound.getFloat("startY"));
        this.field_3192 = new Vec2(fDSTagCompound.getFloat("endX"), fDSTagCompound.getFloat("endY"));
    }
}

