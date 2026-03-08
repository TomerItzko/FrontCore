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
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class WatchlightMapEffect
extends PositionedMapEffect {
    private static final float field_3204 = 61.0f;
    private static final float field_3205 = 6.0f;
    private static final float field_3206 = 35.0f;
    private static final int field_3209 = 1024;
    private static final int field_3210 = 128;
    @NotNull
    private static final ResourceLocation field_3203 = BFRes.loc("textures/misc/world/watchlight.png");
    public float field_3207;
    public float field_3208 = 0.0f;
    public float field_3201;
    public float field_3202 = 0.0f;
    private final int field_3211 = ThreadLocalRandom.current().nextInt(1, 3000);

    public WatchlightMapEffect() {
        this(Vec3.ZERO);
    }

    public WatchlightMapEffect(@NotNull Vec3 vec3) {
        super(vec3);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    @Override
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        this.field_3208 = this.field_3207;
        this.field_3207 = 1.0f + Mth.sin((float)((delta += (float)this.field_3211) / 160.0f));
        this.field_3202 = this.field_3201;
        this.field_3201 = Mth.sin((float)(delta / 30.0f));
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
        Vec3 vec3 = player.getPosition(delta);
        float f = (float)(vec3.x - this.position.x);
        float f2 = (float)(vec3.z - this.position.z);
        float f3 = (float)(Math.atan2(f2, f) * 57.29577951308232);
        float f4 = MathUtils.lerpf1(this.field_3207, this.field_3208, delta);
        float f5 = MathUtils.lerpf1(this.field_3201, this.field_3202, delta);
        float f6 = 768.0f;
        float f7 = 6144.0f;
        poseStack.pushPose();
        poseStack.translate(this.position.x, this.position.y, this.position.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(-f3 - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-35.0f + f4 * 35.0f));
        BFRendering.worldTexture(poseStack, graphics, field_3203, 0.0, 61.0, 0.0, 768.0f, 6144.0f, 1.0f + f5 / 2.0f, 0.0f, 0.0f);
        poseStack.popPose();
    }

    @Override
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
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
    }
}

