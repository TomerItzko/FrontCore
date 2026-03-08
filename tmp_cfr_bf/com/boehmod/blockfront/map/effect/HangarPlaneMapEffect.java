/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.util.RegistryUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class HangarPlaneMapEffect
extends AbstractMapEffect {
    private int field_3193;
    private int field_3194;
    @NotNull
    private Supplier<? extends Block> field_3195;

    public HangarPlaneMapEffect() {
        this(0, 0, (Supplier<? extends Block>)BFBlocks.BLACK_PLANE);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    public HangarPlaneMapEffect(int n, int n2, @NotNull Supplier<? extends Block> supplier) {
        this.field_3193 = n;
        this.field_3194 = n2;
        this.field_3195 = supplier;
    }

    @Override
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
        BlockState blockState = this.field_3195.get().defaultBlockState();
        double d = this.field_3193 * this.field_3194;
        int n = 250;
        Vec3 vec3 = player.position();
        double d2 = (double)(renderTime % (float)this.field_3193) + vec3.x - d / 2.0;
        double d3 = vec3.y + 200.0;
        double d4 = vec3.z;
        poseStack.translate(d2, d3, d4);
        this.method_3119(poseStack, level, dispatcher, buffer, blockState, 0.0, 0.0, 0.0);
        this.method_3119(poseStack, level, dispatcher, buffer, blockState, 60.0, -50.0, 250.0);
        this.method_3119(poseStack, level, dispatcher, buffer, blockState, 60.0, -50.0, -250.0);
        poseStack.translate(-d2, -d3, -d4);
    }

    @Override
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
    }

    @Override
    public boolean requiresFancyGraphics() {
        return true;
    }

    @Override
    public void reset() {
    }

    private void method_3119(@NotNull PoseStack poseStack, @NotNull ClientLevel clientLevel, @NotNull BlockRenderDispatcher blockRenderDispatcher, @NotNull MultiBufferSource.BufferSource bufferSource, @NotNull BlockState blockState, double d, double d2, double d3) {
        for (int i = 0; i < this.field_3194; ++i) {
            this.method_3120(clientLevel, blockRenderDispatcher, bufferSource, poseStack, blockState, d + (double)(this.field_3193 * i), d2, d3);
        }
    }

    private void method_3120(@NotNull ClientLevel clientLevel, @NotNull BlockRenderDispatcher blockRenderDispatcher, @NotNull MultiBufferSource.BufferSource bufferSource, @NotNull PoseStack poseStack, @NotNull BlockState blockState, double d, double d2, double d3) {
        BFRendering.block(clientLevel, blockRenderDispatcher, bufferSource, blockState, poseStack, d, d2, d3, 0.0f, -90.0f, 0.0f);
        BFRendering.block(clientLevel, blockRenderDispatcher, bufferSource, blockState, poseStack, d - 20.0, d2 + 25.0, d3 + 25.0, 0.0f, -90.0f, 0.0f);
        BFRendering.block(clientLevel, blockRenderDispatcher, bufferSource, blockState, poseStack, d - 20.0, d2 + 25.0, d3 - 25.0, 0.0f, -90.0f, 0.0f);
    }

    @Override
    public // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull RenderLevelStageEvent.Stage getRenderStage() {
        return RenderLevelStageEvent.Stage.AFTER_PARTICLES;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        fDSTagCompound.setInteger("radius", this.field_3193);
        fDSTagCompound.setInteger("repeat", this.field_3194);
        fDSTagCompound.setString("block", RegistryUtils.getBlockId(this.field_3195.get()));
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        this.field_3193 = fDSTagCompound.getInteger("radius");
        this.field_3194 = fDSTagCompound.getInteger("repeat");
        this.field_3195 = RegistryUtils.retrieveBlock(fDSTagCompound.getString("block"));
    }
}

