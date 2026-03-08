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
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.block.BlockRenderDispatcher
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Mth
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.util.BFRes;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class FloorMistMapEffect
extends AbstractMapEffect {
    @NotNull
    private static final ResourceLocation field_3104 = BFRes.loc("textures/misc/world/floormist.png");
    public int field_3105;
    public int field_3106 = 15;

    public FloorMistMapEffect() {
        this(0);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    public FloorMistMapEffect(int n) {
        this.field_3105 = n;
    }

    @NotNull
    public FloorMistMapEffect method_3095(int n) {
        this.field_3106 = n;
        return this;
    }

    private void method_3096(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull GuiGraphics guiGraphics, PoseStack poseStack, double d, double d2) {
        if (d % 2.0 != 0.0 || d2 % 2.0 != 0.0) {
            return;
        }
        float f = Mth.sqrt((float)((float)localPlayer.distanceToSqr(d, (double)this.field_3105, d2)));
        float f2 = 1.0f - Mth.clamp((float)(f / 100.0f * (float)this.field_3106), (float)0.1f, (float)1.0f);
        Vec3 vec3 = new Vec3(d, (double)((float)this.field_3105 + (0.5f + f2) * 0.2f), d2);
        BlockPos blockPos = BlockPos.containing((Position)vec3);
        boolean bl = clientLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ()) > Mth.floor((float)blockPos.getY());
        MapEnvironment mapEnvironment = abstractGame.getMapEnvironment();
        if (!bl) {
            BFRendering.method_254(poseStack, guiGraphics, field_3104, vec3.x, vec3.y, vec3.z, 256.0f, 256.0f, f2 * 0.25f, mapEnvironment.getCustomFogColor(), true);
        }
    }

    @Override
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
        AbstractGame<?, ?, ?> abstractGame = manager.getGame();
        if (abstractGame == null) {
            return;
        }
        MapAsset mapAsset = abstractGame.getMap();
        double d = player.getX();
        double d2 = player.getZ();
        for (int i = -this.field_3106; i < this.field_3106; ++i) {
            for (int j = -this.field_3106; j < this.field_3106; ++j) {
                this.method_3096(abstractGame, player, level, graphics, poseStack, d + (double)i, d2 + (double)j);
            }
        }
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

    @Override
    public // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull RenderLevelStageEvent.Stage getRenderStage() {
        return RenderLevelStageEvent.Stage.AFTER_PARTICLES;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        fDSTagCompound.setInteger("yPos", this.field_3105);
        fDSTagCompound.setInteger("radius", this.field_3106);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        this.field_3105 = fDSTagCompound.getInteger("yPos", 0);
        this.field_3106 = fDSTagCompound.getInteger("radius", this.field_3106);
    }
}

