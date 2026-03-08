/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.CloudItemType;
import com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin;
import com.boehmod.bflib.cloud.common.player.PlayerDataType;
import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.env.FakeEnvironment;
import com.boehmod.blockfront.client.gui.BFCrosshair;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemRenderers;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.gun.bullet.BulletTracer;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.boot.BootcampTexturePoint;
import com.boehmod.blockfront.unnamed.BF_211;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.ClanUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;

public final class BFRendering {
    private static final float BYTE_TO_FLOAT_RATIO = 0.003921569f;
    private static final ResourceLocation CROSSHAIR_BACKGROUND = ResourceLocation.withDefaultNamespace((String)"hud/crosshair_attack_indicator_background");
    private static final ResourceLocation CROSSHAIR_PROGRESS = ResourceLocation.withDefaultNamespace((String)"hud/crosshair_attack_indicator_progress");
    @Unique
    public static final int field_6300 = 3;
    public static final int RED_CHAT_COLOR = ChatFormatting.RED.getColor();
    public static final float field_232 = 400.0f;
    public static final int field_220 = 16;
    public static final int field_221 = 8;
    @NotNull
    public static final ResourceLocation EMBER_TEXTURE = BFRes.loc("textures/gui/emitter/ember.png");
    @NotNull
    public static final ResourceLocation DEBUG_SKIN_TEXTURE = BFRes.loc("textures/skins/debug.png");
    @NotNull
    public static final ResourceLocation RANK_BG_TEXTURE = BFRes.loc("textures/misc/ranks/bg.png");
    @NotNull
    public static final ResourceLocation DEFAULT_CALLINGCARD_TEXTURE = BFRes.loc("textures/gui/callingcard/default.png");
    @NotNull
    private static final ResourceLocation SHADOWEFFECT_TEXTURE = BFRes.loc("textures/gui/shadoweffect.png");
    @NotNull
    public static final FakeEnvironment ENVIRONMENT = new FakeEnvironment(Minecraft.getInstance().getGameProfile());
    @NotNull
    private static final Component PARTY_LEADER_COMPONENT = Component.translatable((String)"bf.message.party.leader").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.AQUA);
    @NotNull
    private static final Vector3f GUI_LIGHTS_1 = new Vector3f(-0.5f, 1.0f, -0.7f).normalize();
    private static final Vector3f GUI_LIGHTS_2 = new Vector3f(-0.5f, 1.0f, 0.7f).normalize();
    private static final int TRANSLUCENT_BLACK_COLOR = 0x77000000;
    @NotNull
    private static final ResourceLocation RUNNING_TEXTURE = BFRes.loc("textures/gui/animated/running.png");
    @NotNull
    private static final Component PARTY_LEADER_PREFIX = Component.literal((String)"[PL]");
    private static final int field_223 = 256;
    private static final int field_224 = 15616;
    private static final int field_225 = 61;
    private static final Component MESSAGE_USERNAME = Component.translatable((String)"bf.message.username");
    private static final Vector3f ENTITY_LIGHTS_1 = new Vector3f(0.2f, -1.0f, 1.0f).normalize();
    private static final Vector3f ENTITY_LIGHTS_2 = new Vector3f(-0.2f, -1.0f, 0.0f).normalize();
    private static final float field_233 = 0.3f;
    @NotNull
    private static final ResourceLocation CORNER_TEXTURE = BFRes.loc("textures/gui/gamemode/corner.png");
    private static final int field_226 = 32;
    private static int field_227 = 0;

    public static int translucentBlack() {
        return 0x77000000;
    }

    public static void onUpdate() {
        if (field_227 > 0) {
            --field_227;
        }
    }

    public static float getRenderTime() {
        return (float)Util.getMillis() / 20.0f;
    }

    public static void promptBackground(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, int x, int y, int width, int height) {
        int n = 8;
        float f = 0.25f;
        BFRendering.texture(poseStack, graphics, CORNER_TEXTURE, x, y, 8, 8, 0.25f);
        BFRendering.texture(poseStack, graphics, CORNER_TEXTURE, x + width - 8, y, 8, 8, 90.0f, 0.25f);
        BFRendering.texture(poseStack, graphics, CORNER_TEXTURE, x, y + height - 8, 8, 8, -90.0f, 0.25f);
        BFRendering.texture(poseStack, graphics, CORNER_TEXTURE, x + width - 8, y + height - 8, 8, 8, 180.0f, 0.25f);
        BFRendering.rectangle(graphics, x + 8, y, width - 16, 1, ColorReferences.COLOR_WHITE_SOLID, 0.25f);
        BFRendering.rectangle(graphics, x + 8, y + height - 1, width - 16, 1, ColorReferences.COLOR_WHITE_SOLID, 0.25f);
        BFRendering.rectangle(graphics, x, y + 8, 1, height - 16, ColorReferences.COLOR_WHITE_SOLID, 0.25f);
        BFRendering.rectangle(graphics, x + width - 1, y + 8, 1, height - 16, ColorReferences.COLOR_WHITE_SOLID, 0.25f);
    }

    @Deprecated
    public static void component2d(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y) {
        BFRendering.component2d(poseStack, font, graphics, component, x, y, 0xFFFFFF);
    }

    @Deprecated
    public static void component2d(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component text, float x, float y, int color) {
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        graphics.drawString(font, text, 0, 0, color, false);
        poseStack.popPose();
    }

    public static void drawString(@NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, int x, int y) {
        BFRendering.drawString(font, graphics, component, x, y, 0xFFFFFF);
    }

    public static void drawString(@NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, int x, int y, int color) {
        graphics.drawString(font, component, x, y, color, false);
    }

    @Deprecated
    public static void renderTextLines(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull MultiBufferSource bufferSource, @NotNull List<FormattedCharSequence> charSequences, float x, float y, int lineHeight) {
        int n = 0;
        Matrix4f matrix4f = poseStack.last().pose();
        for (FormattedCharSequence formattedCharSequence : charSequences) {
            font.drawInBatch(formattedCharSequence, x, y + (float)n, 0xFFFFFF, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            n += lineHeight;
        }
    }

    public static void renderTextLines(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull MultiBufferSource bufferSource, @NotNull List<FormattedCharSequence> charSequences, int x, int y, int lineHeight) {
        int n = 0;
        Matrix4f matrix4f = poseStack.last().pose();
        for (FormattedCharSequence formattedCharSequence : charSequences) {
            font.drawInBatch(formattedCharSequence, (float)x, (float)(y + n), 0xFFFFFF, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            n += lineHeight;
        }
    }

    @Deprecated
    public static void renderTextLines(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull MultiBufferSource bufferSource, @NotNull List<FormattedCharSequence> charSequences, float x, float y, int lineHeight, boolean dropShadow) {
        float f = 0.0f;
        Matrix4f matrix4f = poseStack.last().pose();
        for (FormattedCharSequence formattedCharSequence : charSequences) {
            int n = font.width(formattedCharSequence);
            font.drawInBatch(formattedCharSequence, x - (float)n / 2.0f, y + f, 0xFFFFFF, dropShadow, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            f += (float)lineHeight;
        }
    }

    public static void renderTextLines(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull MultiBufferSource bufferSource, @NotNull List<FormattedCharSequence> charSequences, int x, int y, int lineHeight, boolean dropShadow) {
        int n = 0;
        Matrix4f matrix4f = poseStack.last().pose();
        for (FormattedCharSequence formattedCharSequence : charSequences) {
            int n2 = font.width(formattedCharSequence);
            font.drawInBatch(formattedCharSequence, (float)x - (float)n2 / 2.0f, (float)(y + n), 0xFFFFFF, dropShadow, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            n += lineHeight;
        }
    }

    public static void renderTextLines(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull MultiBufferSource bufferSource, @NotNull List<FormattedCharSequence> charSequences, float x, float y, int height, float scale) {
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(scale, scale, 0.1f);
        BFRendering.renderTextLines(poseStack, font, bufferSource, charSequences, 0, 0, (int)((float)height / scale));
        poseStack.popPose();
    }

    @Deprecated
    public static void renderTextLines(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull MultiBufferSource bufferSource, @NotNull List<FormattedCharSequence> charSequences, float x, float y, int height, float scale, boolean dropShadow) {
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(scale, scale, 0.1f);
        BFRendering.renderTextLines(poseStack, font, bufferSource, charSequences, 0, 0, (int)((float)height / scale), dropShadow);
        poseStack.popPose();
    }

    @Deprecated
    public static void component2dShadow(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y) {
        BFRendering.component2dWithShadow(poseStack, font, graphics, component, x, y, 0xFFFFFF);
    }

    public static void drawStringWithShadow(@NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, int x, int y) {
        BFRendering.drawStringWithShadow(font, graphics, component, x, y, 0xFFFFFF);
    }

    @Deprecated
    public static void component2dWithShadow(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, int color) {
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        graphics.drawString(font, component, 0, 0, color);
        poseStack.popPose();
    }

    public static void drawStringWithShadow(@NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, int x, int y, int color) {
        graphics.drawString(font, component, x, y, color);
    }

    @Deprecated
    public static void centeredComponent2d(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y) {
        BFRendering.centeredComponent2d(poseStack, font, graphics, component, x, y, 0xFFFFFF);
    }

    public static void centeredString(@NotNull Font font, @NotNull GuiGraphics guiGraphics, @NotNull Component component, int x, int y) {
        BFRendering.centeredString(font, guiGraphics, component, x, y, 0xFFFFFF);
    }

    @Deprecated
    public static void centeredComponent2d(@NotNull PoseStack poseStack, Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, int color) {
        BFRendering.component2d(poseStack, font, graphics, component, x - (float)font.width((FormattedText)component) / 2.0f, y, color);
    }

    public static void centeredString(Font font, @NotNull GuiGraphics graphics, @NotNull Component component, int x, int y, int color) {
        BFRendering.drawString(font, graphics, component, x - font.width((FormattedText)component) / 2, y, color);
    }

    @Deprecated
    public static void component2d(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, float scale) {
        BFRendering.component2d(poseStack, font, graphics, component, x, y, 0xFFFFFF, scale);
    }

    public static void component2d(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, int x, int y, float scale) {
        BFRendering.component2d(poseStack, font, graphics, component, x, y, 0xFFFFFF, scale);
    }

    public static void component2d(@NotNull PoseStack poseStack, Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, int color, float scale) {
        if (scale <= 0.0f) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(scale, scale, 1.0f);
        BFRendering.drawString(font, graphics, component, 0, 0, color);
        poseStack.popPose();
    }

    public static void component2dWithShadow(@NotNull PoseStack poseStack, Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, int color, float scale) {
        if (scale <= 0.0f) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(scale, scale, 1.0f);
        BFRendering.drawStringWithShadow(font, graphics, component, 0, 0, color);
        poseStack.popPose();
    }

    @Deprecated
    public static void centeredComponent2d(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, float scale) {
        BFRendering.centeredComponent2d(poseStack, font, graphics, component, x, y, 0xFFFFFF, scale);
    }

    public static void centeredComponent2d(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, int x, int y, float scale) {
        BFRendering.centeredComponent2d(poseStack, font, graphics, component, x, y, 0xFFFFFF, scale);
    }

    @Deprecated
    public static void centeredComponent2d(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, int color, float scale) {
        if (scale <= 0.0f) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(scale, scale, 1.0f);
        BFRendering.centeredString(font, graphics, component, 0, 0, color);
        poseStack.popPose();
    }

    @Deprecated
    public static void centeredComponent2dWithShadow(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, float scale) {
        BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, component, x, y, 0xFFFFFF, scale);
    }

    public static void centeredComponent2dWithShadow(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, int x, int y, float scale) {
        BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, component, x, y, 0xFFFFFF, scale);
    }

    @Deprecated
    public static void centeredComponent2dWithShadow(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, int color, float scale) {
        if (scale <= 0.0f) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(scale, scale, 1.0f);
        BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, component, 0, 0, color);
        poseStack.popPose();
    }

    @Deprecated
    public static void outlinedComponent2d(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, int outlineColor, int color, float scale) {
        if (scale <= 0.0f) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(scale, scale, 1.0f);
        BFRendering.outlinedComponent2d(poseStack, font, graphics, component, 0.0f, 0.0f, outlineColor, color);
        poseStack.popPose();
    }

    @Deprecated
    public static void outlinedComponent2d(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, int outlineColor, int color) {
        int n = font.width((FormattedText)component) / 2;
        BFRendering.component2d(poseStack, font, graphics, component, x - 1.0f - (float)n, y + 1.0f, color);
        BFRendering.component2d(poseStack, font, graphics, component, x - (float)n, y + 1.0f, color);
        BFRendering.component2d(poseStack, font, graphics, component, x + 1.0f - (float)n, y + 1.0f, color);
        BFRendering.component2d(poseStack, font, graphics, component, x - 1.0f - (float)n, y, color);
        BFRendering.component2d(poseStack, font, graphics, component, x + 1.0f - (float)n, y, color);
        BFRendering.component2d(poseStack, font, graphics, component, x - 1.0f - (float)n, y - 1.0f, color);
        BFRendering.component2d(poseStack, font, graphics, component, x - (float)n, y - 1.0f, color);
        BFRendering.component2d(poseStack, font, graphics, component, x + 1.0f - (float)n, y - 1.0f, color);
        BFRendering.component2d(poseStack, font, graphics, component, x - (float)n, y, outlineColor);
    }

    @Deprecated
    public static void centeredComponent2dWithShadow(@NotNull PoseStack poseStack, Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y) {
        BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, component, x, y, 0xFFFFFF);
    }

    public static void centeredComponent2dWithShadow(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, int x, int y) {
        BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, component, x, y, 0xFFFFFF);
    }

    @Deprecated
    public static void centeredComponent2dWithShadow(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, float x, float y, int color) {
        BFRendering.component2dWithShadow(poseStack, font, graphics, component, x - (float)font.width((FormattedText)component) / 2.0f, y, color);
    }

    public static void centeredComponent2dWithShadow(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, int x, int y, int color) {
        BFRendering.component2dWithShadow(poseStack, font, graphics, component, (float)x - (float)font.width((FormattedText)component) / 2.0f, y, color);
    }

    public static void orderedRectangle(@NotNull PoseStack poseStack, float x, float y, float width, float height, int color, int mode) {
        float f;
        width = x + width;
        height = y + height;
        if (x < width) {
            f = x;
            x = width;
            width = f;
        }
        if (y < height) {
            f = y;
            y = height;
            height = f;
        }
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        float f2 = (float)FastColor.ARGB32.red((int)color) * 0.003921569f;
        float f3 = (float)FastColor.ARGB32.green((int)color) * 0.003921569f;
        float f4 = (float)FastColor.ARGB32.blue((int)color) * 0.003921569f;
        float f5 = (float)FastColor.ARGB32.alpha((int)color) * 0.003921569f;
        switch (mode) {
            default: {
                bufferBuilder.addVertex(matrix4f, x, y, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, width, height, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, width, y, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, x, y, 0.0f).setColor(f2, f3, f4, f5);
                break;
            }
            case 1: {
                bufferBuilder.addVertex(matrix4f, x, height, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, width, height, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, width, y, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, width, height, 0.0f).setColor(f2, f3, f4, f5);
                break;
            }
            case 2: {
                bufferBuilder.addVertex(matrix4f, x, height, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, width, height, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, x, y, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, x, y, 0.0f).setColor(f2, f3, f4, f5);
                break;
            }
            case 3: {
                bufferBuilder.addVertex(matrix4f, x, height, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, width, y, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, width, y, 0.0f).setColor(f2, f3, f4, f5);
                bufferBuilder.addVertex(matrix4f, x, y, 0.0f).setColor(f2, f3, f4, f5);
            }
        }
        BufferUploader.drawWithShader((MeshData)bufferBuilder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    @Deprecated
    public static void rectangle(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, float x, float y, float width, float height, int color) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(width, height, 1.0f);
        graphics.fill(0, 0, 1, 1, color);
        poseStack.popPose();
        RenderSystem.disableBlend();
    }

    public static void rectangle(@NotNull GuiGraphics graphics, int x, int y, int width, int height, int color) {
        if (width <= 0 || height <= 0) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        graphics.fill(x, y, x + width, y + height, color);
        RenderSystem.disableBlend();
    }

    @Deprecated
    public static void rectangle(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, float x, float y, float width, float height, int color, float alpha) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(width, height, 1.0f);
        graphics.fill(0, 0, 1, 1, MathUtils.withAlphaf(color, alpha));
        poseStack.popPose();
        RenderSystem.disableBlend();
    }

    public static void rectangle(@NotNull GuiGraphics graphics, int x, int y, int width, int height, int color, float alpha) {
        if (width <= 0 || height <= 0) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        graphics.fill(x, y, x + width, y + height, MathUtils.withAlphaf(color, alpha));
        RenderSystem.disableBlend();
    }

    @Deprecated
    public static void rectangleGradient(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, float x, float y, float width, float height, int color1, int color2) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(width, height, 1.0f);
        graphics.fillGradient(0, 0, 1, 1, color1, color2);
        poseStack.popPose();
        RenderSystem.disableBlend();
    }

    public static void rectangleGradient(@NotNull GuiGraphics graphics, int x, int y, int width, int height, int color1, int color2) {
        if (width <= 0 || height <= 0) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        graphics.fillGradient(x, y, x + width, y + height, color1, color2);
        RenderSystem.disableBlend();
    }

    public static void rectangleGradientWithVertices(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, float x, float y, float width, float height, int color1, int color2) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(width, height, 1.0f);
        BFRendering.rectangleGradientFromOrigin(poseStack, graphics, 1.0f, 1.0f, color1, color2);
        poseStack.popPose();
        RenderSystem.disableBlend();
    }

    @Deprecated
    private static void rectangleGradientFromOrigin(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, float width, float height, int color1, int color2) {
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer vertexConsumer = graphics.bufferSource().getBuffer(RenderType.gui());
        float f = (float)FastColor.ARGB32.red((int)color1) * 0.003921569f;
        float f2 = (float)FastColor.ARGB32.green((int)color1) * 0.003921569f;
        float f3 = (float)FastColor.ARGB32.blue((int)color1) * 0.003921569f;
        float f4 = (float)FastColor.ARGB32.alpha((int)color1) * 0.003921569f;
        float f5 = (float)FastColor.ARGB32.red((int)color2) * 0.003921569f;
        float f6 = (float)FastColor.ARGB32.green((int)color2) * 0.003921569f;
        float f7 = (float)FastColor.ARGB32.blue((int)color2) * 0.003921569f;
        float f8 = (float)FastColor.ARGB32.alpha((int)color2) * 0.003921569f;
        vertexConsumer.addVertex(matrix4f, 0.0f, 0.0f, 0.0f).setColor(f, f2, f3, f4);
        vertexConsumer.addVertex(matrix4f, 0.0f, height, 0.0f).setColor(f, f2, f3, f4);
        vertexConsumer.addVertex(matrix4f, width, height, 0.0f).setColor(f5, f6, f7, f8);
        vertexConsumer.addVertex(matrix4f, width, 0.0f, 0.0f).setColor(f5, f6, f7, f8);
    }

    @Deprecated
    public static void rectangleGradient(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, float x, float y, float width, float height, int color1, int color2, float alpha1, float alpha2) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        alpha1 = Mth.clamp((float)alpha1, (float)0.0f, (float)1.0f);
        alpha2 = Mth.clamp((float)alpha2, (float)0.0f, (float)1.0f);
        int n = MathUtils.withAlphaf(color1, alpha1);
        int n2 = MathUtils.withAlphaf(color2, alpha2);
        BFRendering.rectangleGradient(poseStack, graphics, x, y, width, height, n, n2);
    }

    public static void rectangleGradient(@NotNull GuiGraphics graphics, int x, int y, int width, int height, int color1, int color2, float alpha1, float alpha2) {
        if (width <= 0 || height <= 0) {
            return;
        }
        alpha1 = Mth.clamp((float)alpha1, (float)0.0f, (float)1.0f);
        alpha2 = Mth.clamp((float)alpha2, (float)0.0f, (float)1.0f);
        int n = MathUtils.withAlphaf(color1, alpha1);
        int n2 = MathUtils.withAlphaf(color2, alpha2);
        BFRendering.rectangleGradient(graphics, x, y, width, height, n, n2);
    }

    @Deprecated
    public static void textureSquare(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float size) {
        BFRendering.texture(poseStack, graphics, texture, x, y, size, size);
    }

    public static void textureSquare(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int size) {
        BFRendering.texture(poseStack, graphics, texture, x, y, size, size);
    }

    @Deprecated
    public static void texture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height) {
        BFRendering.texture(poseStack, graphics, texture, x, y, width, height, 0.0f, 1.0f);
    }

    public static void texture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int width, int height) {
        BFRendering.texture(poseStack, graphics, texture, x, y, width, height, 0.0f, 1.0f);
    }

    @Deprecated
    public static void texture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height, float alpha) {
        BFRendering.texture(poseStack, graphics, texture, x, y, width, height, 0.0f, alpha);
    }

    public static void texture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int width, int height, float alpha) {
        BFRendering.texture(poseStack, graphics, texture, x, y, width, height, 0.0f, alpha);
    }

    @Deprecated
    public static void texture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height, float rotation, float alpha) {
        BFRendering.tintedTexture(poseStack, graphics, texture, x, y, width, height, rotation, alpha, 0xFFFFFF);
    }

    public static void texture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int width, int height, float rotation, float alpha) {
        BFRendering.tintedTexture(poseStack, graphics, texture, x, y, width, height, rotation, alpha, 0xFFFFFF);
    }

    @Deprecated
    public static void tintedTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height, float rotation, float alpha, int color) {
        BFRendering.tintedTexture(poseStack, graphics, texture, x, y, width, height, 0.0f, rotation, alpha, color);
    }

    public static void tintedTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int width, int height, float rotation, float alpha, int color) {
        BFRendering.tintedTexture(poseStack, graphics, texture, x, y, width, height, 0.0f, rotation, alpha, color);
    }

    @Deprecated
    public static void centeredTextureSquare(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float size) {
        BFRendering.centeredTexture(poseStack, graphics, texture, x, y, size, size);
    }

    public static void centeredTextureSquare(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int size) {
        BFRendering.centeredTexture(poseStack, graphics, texture, x, y, size, size);
    }

    @Deprecated
    public static void centeredTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height) {
        BFRendering.texture(poseStack, graphics, texture, x - width / 2.0f, y - height / 2.0f, width, height);
    }

    public static void centeredTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int width, int height) {
        BFRendering.texture(poseStack, graphics, texture, x - width / 2, y - height / 2, width, height);
    }

    @Deprecated
    public static void centeredTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height, float rotation) {
        BFRendering.texture(poseStack, graphics, texture, x - width / 2.0f, y - height / 2.0f, width, height, rotation, 1.0f);
    }

    public static void centeredTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int width, int height, float rotation) {
        BFRendering.texture(poseStack, graphics, texture, x - width / 2, y - height / 2, width, height, rotation, 1.0f);
    }

    @Deprecated
    public static void centeredTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height, float rotation, float alpha) {
        BFRendering.texture(poseStack, graphics, texture, x - width / 2.0f, y - height / 2.0f, width, height, rotation, alpha);
    }

    public static void centeredTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int width, int height, float rotation, float alpha) {
        BFRendering.texture(poseStack, graphics, texture, (float)x - (float)width / 2.0f, (float)y - (float)height / 2.0f, (float)width, (float)height, rotation, alpha);
    }

    @Deprecated
    public static void centeredTintedTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height, float rotation, float alpha, int color) {
        BFRendering.tintedTexture(poseStack, graphics, texture, x - width / 2.0f, y - height / 2.0f, width, height, rotation, alpha, color);
    }

    public static void centeredTintedTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int width, int height, float rotation, float alpha, int color) {
        BFRendering.tintedTexture(poseStack, graphics, texture, x - width / 2, y - height / 2, width, height, rotation, alpha, color);
    }

    public static void centeredTextureScaled(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height, float rotation) {
        BFRendering.centeredTintedTextureScaled(poseStack, graphics, texture, x, y, width, height, rotation, 1.0f, 0xFFFFFF);
    }

    public static void centeredTextureScaled(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height, float rotation, float alpha) {
        BFRendering.centeredTintedTextureScaled(poseStack, graphics, texture, x, y, width, height, rotation, alpha, 0xFFFFFF);
    }

    @Deprecated
    public static void centeredTintedTextureScaled(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height, float scale, float alpha, int color) {
        if (scale <= 0.0f) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(scale, scale, 1.0f);
        BFRendering.centeredTintedTexture(poseStack, graphics, texture, 0.0f, 0.0f, width, height, 0.0f, alpha, color);
        poseStack.popPose();
    }

    @Deprecated
    public static void tintedTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height, float rotationX, float rotationZ, float alpha, int color) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        RenderSystem.enableBlend();
        BFRendering.shaderColor(color, alpha);
        poseStack.pushPose();
        if (rotationX != 0.0f || rotationZ != 0.0f) {
            float f = x + width / 2.0f;
            float f2 = y + height / 2.0f;
            poseStack.translate(f, f2, 0.0f);
            poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ));
            poseStack.translate(-f, -f2, 0.0f);
        }
        poseStack.translate(x, y, 0.0f);
        poseStack.scale(width, height, 1.0f);
        graphics.blit(texture, 0, 0, 0.0f, 0.0f, 1, 1, 1, 1);
        poseStack.popPose();
        RenderSystem.disableBlend();
        BFRendering.resetShaderColor();
    }

    public static void tintedTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int width, int height, float rotationX, float rotationZ, float alpha, int color) {
        if (alpha <= 0.0f || width <= 0 || height <= 0) {
            return;
        }
        RenderSystem.enableBlend();
        BFRendering.shaderColor(color, alpha);
        poseStack.pushPose();
        if (rotationX != 0.0f || rotationZ != 0.0f) {
            int n = x + width / 2;
            int n2 = y + height / 2;
            poseStack.translate((float)n, (float)n2, 0.0f);
            poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ));
            poseStack.translate((float)(-n), (float)(-n2), 0.0f);
        }
        graphics.blit(texture, x, y, 0.0f, 0.0f, width, height, width, height);
        poseStack.popPose();
        RenderSystem.disableBlend();
        BFRendering.resetShaderColor();
    }

    @Deprecated
    public static void texture(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height) {
        BFRendering.texture(poseStack, textureId, x, y, width, height, 0.0f, 1.0f);
    }

    public static void texture(@NotNull PoseStack poseStack, int textureId, int x, int y, int width, int height) {
        BFRendering.texture(poseStack, textureId, (float)x, (float)y, (float)width, (float)height, 0.0f, 1.0f);
    }

    @Deprecated
    public static void texture(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height, float alpha) {
        BFRendering.texture(poseStack, textureId, x, y, width, height, 0.0f, alpha);
    }

    @Deprecated
    public static void texture(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height, float rotation, float alpha) {
        BFRendering.tintedTexture(poseStack, textureId, x, y, width, height, rotation, alpha, 0xFFFFFF);
    }

    @Deprecated
    public static void texture(@NotNull PoseStack poseStack, int textureId, float x, int y, int width, int height, int rotation, float alpha) {
        BFRendering.tintedTexture(poseStack, textureId, x, (float)y, (float)width, (float)height, (float)rotation, alpha, 0xFFFFFF);
    }

    @Deprecated
    public static void tintedTexture(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height, float rotation, float alpha, int color) {
        BFRendering.tintedTexture(poseStack, textureId, x, y, width, height, rotation, 0.0f, alpha, color);
    }

    public static void tintedTexture(@NotNull PoseStack poseStack, int textureId, int x, int y, int width, int height, float rotation, float alpha, int color) {
        BFRendering.tintedTexture(poseStack, textureId, x, y, width, height, rotation, 0.0f, alpha, color);
    }

    @Deprecated
    public static void centeredTextureSquare(@NotNull PoseStack poseStack, int textureId, float x, float y, float size) {
        BFRendering.centeredTexture(poseStack, textureId, x, y, size, size);
    }

    public static void centeredTextureSquare(@NotNull PoseStack poseStack, int textureId, int x, int y, int size) {
        BFRendering.centeredTexture(poseStack, textureId, x, y, size, size);
    }

    public static void centeredTextureSquare(@NotNull PoseStack poseStack, int textureId, int x, int y, float size) {
        BFRendering.centeredTexture(poseStack, textureId, (float)x, (float)y, size, size);
    }

    @Deprecated
    public static void centeredTexture(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height) {
        BFRendering.texture(poseStack, textureId, x - width / 2.0f, y - height / 2.0f, width, height);
    }

    public static void centeredTexture(@NotNull PoseStack poseStack, int textureId, int x, int y, int width, int height) {
        BFRendering.texture(poseStack, textureId, x - width / 2, y - height / 2, width, height);
    }

    @Deprecated
    public static void centeredTexture(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height, float rotation) {
        BFRendering.texture(poseStack, textureId, x - width / 2.0f, y - height / 2.0f, width, height, rotation, 1.0f);
    }

    @Deprecated
    public static void centeredTexture(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height, float rotation, float alpha) {
        BFRendering.texture(poseStack, textureId, x - width / 2.0f, y - height / 2.0f, width, height, rotation, alpha);
    }

    @Deprecated
    public static void centeredTintedTexture(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height, float rotation, float alpha, int color) {
        BFRendering.tintedTexture(poseStack, textureId, x - width / 2.0f, y - height / 2.0f, width, height, rotation, alpha, color);
    }

    @Deprecated
    public static void centeredTextureScaled(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height, float scale) {
        BFRendering.centeredTintedTextureScaled(poseStack, textureId, x, y, width, height, scale, 1.0f, 0xFFFFFF);
    }

    @Deprecated
    public static void centeredTextureScaled(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height, float scale, float alpha) {
        BFRendering.centeredTintedTextureScaled(poseStack, textureId, x, y, width, height, scale, alpha, 0xFFFFFF);
    }

    @Deprecated
    public static void centeredTintedTextureScaled(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height, float scale, float alpha, int color) {
        poseStack.pushPose();
        poseStack.translate(x - x * scale, y - y * scale, 0.0f);
        poseStack.scale(scale, scale, scale);
        BFRendering.centeredTintedTexture(poseStack, textureId, x, y, width, height, 0.0f, alpha, color);
        poseStack.popPose();
    }

    @Deprecated
    public static void tintedTexture(@NotNull PoseStack poseStack, int textureId, float x, float y, float width, float height, float rotationZ, float rotationX, float alpha, int color) {
        BFRendering.shaderColor(color, alpha);
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture((int)0, (int)textureId);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        poseStack.pushPose();
        if (rotationX != 0.0f || rotationZ != 0.0f) {
            float f = x + width / 2.0f;
            float f2 = y + height / 2.0f;
            poseStack.translate(f, f2, 0.0f);
            poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ));
            poseStack.translate(-f, -f2, 0.0f);
        }
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.addVertex(matrix4f, x, y + height, 0.0f).setUv(0.0f, 0.0f);
        bufferBuilder.addVertex(matrix4f, x + width, y + height, 0.0f).setUv(1.0f, 0.0f);
        bufferBuilder.addVertex(matrix4f, x + width, y, 0.0f).setUv(1.0f, 1.0f);
        bufferBuilder.addVertex(matrix4f, x, y, 0.0f).setUv(0.0f, 1.0f);
        BufferUploader.drawWithShader((MeshData)bufferBuilder.buildOrThrow());
        poseStack.popPose();
        BFRendering.resetShaderColor();
        RenderSystem.disableBlend();
    }

    public static void tintedTexture(@NotNull PoseStack poseStack, int textureId, int x, int y, int width, int height, float rotationZ, float rotationX, float alpha, int color) {
        BFRendering.shaderColor(color, alpha);
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture((int)0, (int)textureId);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        poseStack.pushPose();
        if (rotationX != 0.0f || rotationZ != 0.0f) {
            int n = x + width / 2;
            int n2 = y + height / 2;
            poseStack.translate((float)n, (float)n2, 0.0f);
            poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ));
            poseStack.translate((float)(-n), (float)(-n2), 0.0f);
        }
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.addVertex(matrix4f, (float)x, (float)(y + height), 0.0f).setUv(0.0f, 0.0f);
        bufferBuilder.addVertex(matrix4f, (float)(x + width), (float)(y + height), 0.0f).setUv(1.0f, 0.0f);
        bufferBuilder.addVertex(matrix4f, (float)(x + width), (float)y, 0.0f).setUv(1.0f, 1.0f);
        bufferBuilder.addVertex(matrix4f, (float)x, (float)y, 0.0f).setUv(0.0f, 1.0f);
        BufferUploader.drawWithShader((MeshData)bufferBuilder.buildOrThrow());
        poseStack.popPose();
        BFRendering.resetShaderColor();
        RenderSystem.disableBlend();
    }

    public static boolean isPointInRectangle(double rectX, double rectY, double rectWidth, double rectHeight, double x, double y) {
        return x >= rectX && y >= rectY && x <= rectX + rectWidth && y <= rectY + rectHeight;
    }

    public static void billboardLine(@NotNull Camera camera, @NotNull PoseStack poseStack, @NotNull Vec3 vec1, @NotNull Vec3 vec2, float thickness, int color) {
        BFRendering.billboardLine(camera, poseStack, vec1, vec2, thickness, color, 1.0f);
    }

    public static void billboardLine(@NotNull Camera camera, @NotNull PoseStack poseStack, @NotNull Vec3 vec1, @NotNull Vec3 vec2, float thickness, int color, float alpha) {
        RenderSystem.disableDepthTest();
        Vec3 vec3 = vec2.subtract(vec1).normalize();
        Vec3 vec32 = camera.getPosition();
        Vec3 vec33 = vec3.cross(new Vec3(vec32.x - vec1.x, vec32.y - vec1.y, vec32.z - vec1.z)).normalize().scale((double)(thickness * 0.01f));
        float f = (float)FastColor.ARGB32.red((int)color) * 0.003921569f;
        float f2 = (float)FastColor.ARGB32.green((int)color) * 0.003921569f;
        float f3 = (float)FastColor.ARGB32.blue((int)color) * 0.003921569f;
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        float f4 = (float)vec1.x;
        float f5 = (float)vec1.y;
        float f6 = (float)vec1.z;
        float f7 = (float)vec2.x;
        float f8 = (float)vec2.y;
        float f9 = (float)vec2.z;
        float f10 = (float)vec33.x;
        float f11 = (float)vec33.y;
        float f12 = (float)vec33.z;
        bufferBuilder.addVertex(matrix4f, f4 + f10, f5 + f11, f6 + f12).setColor(f, f2, f3, alpha);
        bufferBuilder.addVertex(matrix4f, f7 + f10, f8 + f11, f9 + f12).setColor(f, f2, f3, alpha);
        bufferBuilder.addVertex(matrix4f, f7 - f10, f8 - f11, f9 - f12).setColor(f, f2, f3, alpha);
        bufferBuilder.addVertex(matrix4f, f4 - f10, f5 - f11, f6 - f12).setColor(f, f2, f3, alpha);
        BufferUploader.drawWithShader((MeshData)bufferBuilder.buildOrThrow());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    public static void quad(@NotNull PoseStack poseStack, @NotNull Vec3[] points, float r, float g, float b, float a2) {
        if (points.length != 4) {
            throw new IllegalArgumentException("Must provide 4 corners for a face");
        }
        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        RenderSystem.depthMask((boolean)true);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i < 4; ++i) {
            Vec3 vec3 = points[i];
            bufferBuilder.addVertex(matrix4f, (float)vec3.x, (float)vec3.y, (float)vec3.z).setColor(r, g, b, a2);
        }
        BufferUploader.drawWithShader((MeshData)bufferBuilder.buildOrThrow());
        RenderSystem.depthMask((boolean)true);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
    }

    public static void playerHead(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull GameProfile profile, float x, float y, int size) {
        ResourceLocation resourceLocation = BFRendering.getSkinTexture(minecraft, manager, profile);
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        graphics.blit(resourceLocation, 0, 0, size, size, 8.0f, 8.0f, 8, 8, 64, 64);
        graphics.blit(resourceLocation, 0, 0, size, size, 40.0f, 8.0f, 8, 8, 64, 64);
        poseStack.popPose();
    }

    @Deprecated
    public static void runningTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, float x, float y, int size, int color, float time) {
        time = (long)time % 61L;
        int n = (int)(time * 256.0f);
        RenderSystem.enableBlend();
        BFRendering.shaderColor(color);
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0f);
        graphics.blit(RUNNING_TEXTURE, 0, 0, size, size, 0.0f, (float)n, 256, 256, 256, 15616);
        poseStack.popPose();
        RenderSystem.disableBlend();
        BFRendering.resetShaderColor();
    }

    public static void runningTexture(@NotNull GuiGraphics graphics, int x, int y, int size, int color, float time) {
        time = (long)time % 61L;
        int n = (int)(time * 256.0f);
        RenderSystem.enableBlend();
        BFRendering.shaderColor(color);
        graphics.blit(RUNNING_TEXTURE, x, y, size, size, 0.0f, (float)n, 256, 256, 256, 15616);
        BFRendering.resetShaderColor();
        RenderSystem.disableBlend();
    }

    @Deprecated
    public static void centeredGhostedTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, float x, float y, float width, float height, float gap, float repeats) {
        float f = 0.25f;
        float f2 = f / repeats;
        BFRendering.centeredTexture(poseStack, graphics, texture, x, y, width, height);
        int n = 0;
        while ((float)n < repeats) {
            BFRendering.centeredTexture(poseStack, graphics, texture, x + gap * (float)n, y, width, height, 0.0f, f -= f2);
            BFRendering.centeredTexture(poseStack, graphics, texture, x - gap * (float)n, y, width, height, 0.0f, f - f2);
            f = 0.25f;
            ++n;
        }
    }

    public static void centeredGhostedTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int width, int height, float gap, float repeats) {
        float f = 0.25f;
        float f2 = f / repeats;
        BFRendering.centeredTexture(poseStack, graphics, texture, x, y, width, height);
        int n = 0;
        while ((float)n < repeats) {
            BFRendering.centeredTexture(poseStack, graphics, texture, (float)x + gap * (float)n, (float)y, (float)width, (float)height, 0.0f, f -= f2);
            BFRendering.centeredTexture(poseStack, graphics, texture, (float)x - gap * (float)n, (float)y, (float)width, (float)height, 0.0f, f - f2);
            f = 0.25f;
            ++n;
        }
    }

    @Deprecated
    public static void centeredRectangleWithShadow(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, float x, float y, float width, float height, int color, float alpha) {
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(-(width / 2.0f), -(height / 2.0f), 0.0f);
        BFRendering.rectangle(poseStack, graphics, x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f, color, 0.3f);
        BFRendering.rectangle(poseStack, graphics, x, y, width, height, color, alpha);
        poseStack.popPose();
    }

    @Deprecated
    public static void rectangleWithDarkShadow(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, float x, float y, float width, float height, int color) {
        BFRendering.rectangleWithDarkShadow(poseStack, graphics, x, y, width, height, color, (float)FastColor.ARGB32.alpha((int)color) * 0.003921569f);
    }

    public static void rectangleWithDarkShadow(@NotNull GuiGraphics graphics, int x, int y, int width, int height, int color) {
        BFRendering.rectangleWithDarkShadow(graphics, x, y, width, height, color, (float)FastColor.ARGB32.alpha((int)color) * 0.003921569f);
    }

    @Deprecated
    public static void rectangleWithDarkShadow(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, float x, float y, float width, float height, int color, float alpha) {
        BFRendering.rectangle(poseStack, graphics, x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f, 0, 0.3f);
        BFRendering.rectangle(poseStack, graphics, x, y, width, height, color, alpha);
    }

    public static void rectangleWithDarkShadow(@NotNull GuiGraphics graphics, int x, int y, int width, int height, int color, float alpha) {
        BFRendering.rectangle(graphics, x - 1, y - 1, width + 2, height + 2, 0, 0.3f);
        BFRendering.rectangle(graphics, x, y, width, height, color, alpha);
    }

    public static void entity(@NotNull ClientPlayerDataHandler dataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Minecraft minecraft, @NotNull LivingEntity entity, int x, int y, float scale, float pitch, float yaw, float headYaw) {
        BFRendering.entity(dataHandler, poseStack, graphics, minecraft, entity, x, y, scale, pitch, yaw, headYaw, 1.0f);
    }

    public static void entity(@NotNull ClientPlayerDataHandler dataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Minecraft minecraft, @NotNull LivingEntity entity, float x, float y, float scale, float pitch, float yaw, float headYaw, float delta) {
        BFClientPlayerData bFClientPlayerData;
        if (entity instanceof Player) {
            Player player = (Player)entity;
            bFClientPlayerData = (BFClientPlayerData)dataHandler.getPlayerData(player);
        } else {
            bFClientPlayerData = (BFClientPlayerData)dataHandler.getPlayerData(entity.getUUID());
        }
        BFClientPlayerData bFClientPlayerData2 = bFClientPlayerData;
        boolean bl = bFClientPlayerData2.isOutOfGame();
        int n = bFClientPlayerData2.method_1132();
        boolean bl2 = entity.isInvisible();
        float f = entity.yBodyRotO;
        float f2 = entity.yBodyRot;
        float f3 = entity.yHeadRotO;
        float f4 = entity.yHeadRot;
        float f5 = entity.xRotO;
        bFClientPlayerData2.setOutOfGame(false);
        bFClientPlayerData2.method_1157(0);
        entity.setInvisible(false);
        EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        MultiBufferSource.BufferSource bufferSource = graphics.bufferSource();
        if (entityRenderDispatcher.camera == null) {
            entityRenderDispatcher.camera = new Camera();
        }
        poseStack.pushPose();
        poseStack.translate(x, y + (entity.isShiftKeyDown() ? -11.0f : 0.0f), 50.0f);
        poseStack.scale(scale, scale, -scale);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        entity.yBodyRotO = entity.yBodyRot = -yaw;
        entity.yHeadRotO = entity.yHeadRot = -yaw + -headYaw;
        entity.xRotO = pitch;
        entity.setXRot(pitch);
        Lighting.setupForEntityInInventory();
        entityRenderDispatcher.render((Entity)entity, 0.0, 0.0, 0.0, 1.0f, delta, poseStack, (MultiBufferSource)bufferSource, 0xF000F0);
        minecraft.gameRenderer.lightTexture().turnOffLightLayer();
        bufferSource.endBatch();
        poseStack.popPose();
        entity.yBodyRotO = f;
        entity.yBodyRot = f2;
        entity.yHeadRotO = f3;
        entity.yHeadRot = f4;
        entity.xRotO = f5;
        entity.setXRot(f5);
        bFClientPlayerData2.setOutOfGame(bl);
        bFClientPlayerData2.method_1157(n);
        entity.setInvisible(bl2);
    }

    public static void method_197(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull GuiGraphics guiGraphics, Font font, @NotNull UUID uUID, @NotNull BF_211.BF_213 bF_213, int n, int n2, int n3, boolean bl) {
        BF_211.SlotInfo slotInfo = bF_213.getSlotInfo();
        int n4 = slotInfo.method_966(clientPlayerDataHandler, uUID, bl);
        Component component = slotInfo.method_967(clientPlayerDataHandler, uUID, bl);
        Component component2 = slotInfo.method_965(clientPlayerDataHandler, uUID);
        BFRendering.rectangle(guiGraphics, n + 2, n2 + 1, 1, n3 - 2, n4);
        BFRendering.drawString(font, guiGraphics, component2, n + 5, n2 + 2);
        BFRendering.drawString(font, guiGraphics, component, n + 5, n2 + n3 - 9, n4);
    }

    public static void method_305(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics guiGraphics, @NotNull Component component, float f, float f2, int n, int n2) {
        int n3 = BFRendering.translucentBlack();
        float f3 = (float)n / 2.0f;
        float f4 = f - f3;
        float f5 = f + f3;
        BFRendering.orderedRectangle(poseStack, f4 - 5.0f, f2, 5.0f, n2, n3, 2);
        BFRendering.orderedRectangle(poseStack, f5, f2, 5.0f, n2, n3, 4);
        BFRendering.rectangle(poseStack, guiGraphics, f4, f2, (float)n, (float)n2, n3);
        BFRendering.centeredComponent2d(poseStack, font, guiGraphics, component, f, f2 + 1.5f);
    }

    public static void playerInfoTag(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, PlayerCloudData partyOwnerProfile, @NotNull PlayerCloudData profile, float x, float y, boolean highlight) {
        PlayerRank playerRank;
        Object object;
        CloudRegistry cloudRegistry = manager.getCloudRegistry();
        Optional optional = partyOwnerProfile.getParty();
        int n = profile.getPrestigeLevel();
        UUID uUID = profile.getUUID();
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)profile.getInventory();
        boolean bl = optional.map(matchParty -> matchParty.isHost(uUID)).orElse(false);
        MutableComponent mutableComponent = Component.literal((String)(n > 0 ? "P" + n + " " : "")).withColor(ColorReferences.COLOR_THEME_PRESTIGE_SOLID);
        MutableComponent mutableComponent2 = ClanUtils.getClanPrefix(profile);
        MutableComponent mutableComponent3 = mutableComponent2.append((Component)mutableComponent).append((Component)profile.method_1715());
        if (profile.getPlayerDataType() == PlayerDataType.DISPLAY) {
            mutableComponent3.withStyle(ChatFormatting.GRAY);
        }
        if (highlight) {
            mutableComponent3.withStyle(ChatFormatting.YELLOW);
        }
        int n2 = font.width((FormattedText)mutableComponent3);
        BFRendering.method_305(poseStack, font, graphics, (Component)mutableComponent3, x, y, n2, 10);
        CloudItemStack cloudItemStack = playerCloudInventory.method_1673(CloudItemType.COIN);
        if (cloudItemStack != null && (object = cloudItemStack.getCloudItem(cloudRegistry)) instanceof AbstractCloudItemCoin) {
            playerRank = (AbstractCloudItemCoin)object;
            object = CloudItemRenderers.getRenderer(playerRank);
            ((CloudItemRenderer)object).method_1745(playerRank, cloudItemStack, poseStack, minecraft, graphics, x - (float)n2 / 2.0f - 5.0f, y, 9.0f, 9.0f, 1.0f);
        }
        if (bl) {
            BFRendering.centeredComponent2d(poseStack, font, graphics, (Component)PARTY_LEADER_COMPONENT.copy(), x, y + 12.0f, 0.5f);
        }
        if (profile.getPlayerDataType() != PlayerDataType.DISPLAY) {
            playerRank = profile.getRank();
            int n3 = 16;
            int n4 = 8;
            int n5 = 15;
            int n6 = 7;
            float f = x - 8.0f;
            float f2 = y - 16.0f - 5.0f;
            ResourceLocation resourceLocation = BFRes.loc("textures/misc/ranks/" + playerRank.getTexture() + ".png");
            BFRendering.texture(poseStack, graphics, BFMenuScreen.BACKSHADOW, f - 7.0f, f2 - 7.0f, 31.0f, 31.0f);
            BFRendering.texture(poseStack, graphics, BFMenuScreen.BG_US, f, f2, 16.0f, 16.0f);
            BFRendering.texture(poseStack, graphics, resourceLocation, f, f2, 16.0f, 16.0f);
        }
    }

    public static void renderTabListEntry(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientPlayerDataHandler dataHandler, @NotNull PlayerCloudData cloudData, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull AbstractGame<?, ?, ?> game, @NotNull AbstractGameClient<?, ?> gameClient, @Nullable UUID uuid, @NotNull Style style, @NotNull Collection<String> collection, float f, float f2, float f3, float f4, int n) {
        MutableComponent mutableComponent;
        CloudRegistry cloudRegistry = manager.getCloudRegistry();
        Optional optional = cloudData.getParty();
        PlayerCloudData playerCloudData = null;
        PlayerCloudInventory playerCloudInventory = null;
        BFClientPlayerData bFClientPlayerData = null;
        if (uuid != null) {
            playerCloudData = dataHandler.getCloudProfile(uuid);
            playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
            bFClientPlayerData = (BFClientPlayerData)dataHandler.getPlayerData(uuid);
            if (uuid.equals(cloudData.getUUID())) {
                style = Style.EMPTY;
            }
        }
        int n2 = collection.size();
        int n3 = 30;
        int n4 = n2 * 30;
        float f5 = f3 - (float)n4;
        int n5 = (int)f;
        int n6 = (int)f2;
        int n7 = (int)f5;
        int n8 = (int)f4;
        BFRendering.rectangle(graphics, n5, n6, n7, n8, 0, 0.5f);
        if (uuid != null) {
            int n9;
            CloudItemStack cloudItemStack;
            MutableComponent mutableComponent2;
            int n10;
            float f6;
            float f7;
            int n11;
            CloudItem cloudItem;
            var30_30 = DEFAULT_CALLINGCARD_TEXTURE;
            CloudItemStack cloudItemStack2 = playerCloudInventory.method_1673(CloudItemType.CARD);
            if (cloudItemStack2 != null && (cloudItem = cloudItemStack2.getCloudItem(cloudRegistry)) != null) {
                var30_30 = BFRes.loc("textures/gui/callingcard/" + cloudItem.getSuffixForDisplay() + ".png");
            }
            BFRendering.enableScissor(graphics, n5 + 1, n6 + 1, n7 - 2, n8 - 2);
            float f8 = 220.0f;
            float f9 = 50.0f;
            BFRendering.texture(poseStack, graphics, var30_30, (float)n5, (float)n6 - 25.0f, 220.0f, 50.0f, 0.0f, 0.1f);
            graphics.disableScissor();
            int n12 = 0;
            mutableComponent = ClanUtils.getClanPrefix(playerCloudData);
            MutableComponent mutableComponent3 = mutableComponent.append((Component)playerCloudData.method_1715());
            optional.ifPresent(party -> {
                if (party.isPresent(uuid)) {
                    mutableComponent3.withStyle(ChatFormatting.AQUA);
                    if (party.isHost(uuid)) {
                        mutableComponent3.append(" ").append(PARTY_LEADER_PREFIX);
                    }
                }
            });
            PlayerRank playerRank = playerCloudData.getRank();
            GameTeam gameTeam = ((AbstractGamePlayerManager)game.getPlayerManager()).getPlayerTeam(uuid);
            if (gameTeam != null) {
                int n13 = gameTeam.getColor();
                ResourceLocation resourceLocation = BFRes.loc("textures/misc/ranks/" + playerRank.getTexture() + ".png");
                n11 = 7;
                f7 = f + 14.5f;
                f6 = f2 + (float)n + 0.5f;
                BFRendering.tintedTexture(poseStack, graphics, RANK_BG_TEXTURE, f7, f6, 7.0f, 7.0f, 0.0f, 1.0f, n13);
                BFRendering.texture(poseStack, graphics, resourceLocation, f7, f6, 7.0f, 7.0f);
                n10 = playerCloudData.getPrestigeLevel();
                if (n10 > 0) {
                    mutableComponent2 = Component.literal((String)String.valueOf(n10));
                    float f10 = f7 + 3.5f;
                    float f11 = f2 + 7.0f;
                    BFRendering.outlinedComponent2d(poseStack, font, graphics, (Component)mutableComponent2, f10, f11, 0xFFFFFF, 0, 0.5f);
                }
            }
            if ((cloudItemStack = playerCloudInventory.method_1673(CloudItemType.COIN)) != null) {
                int n14 = 8;
                n11 = 4;
                Object object = cloudItemStack.getCloudItem(cloudRegistry);
                if (object instanceof AbstractCloudItemCoin) {
                    AbstractCloudItemCoin abstractCloudItemCoin = (AbstractCloudItemCoin)object;
                    object = CloudItemRenderers.getRenderer(abstractCloudItemCoin);
                    float f12 = f + 24.0f + 4.0f;
                    float f13 = f2 + (float)n + 0.1f + 4.0f;
                    ((CloudItemRenderer)object).method_1746(abstractCloudItemCoin, cloudItemStack, minecraft, poseStack, graphics, f12, f13, 8.0f, 8.0f, 1.0f);
                }
                n12 += 10;
            }
            if ((n9 = game.getPlayerStatData(uuid).getInteger(BFStats.CLASS.getKey(), -1)) != -1) {
                MatchClass matchClass = MatchClass.values()[n9];
                int n15 = game.getPlayerStatData(uuid).getInteger(BFStats.CLASS_INDEX.getKey(), 0);
                f6 = f + f5 - f4;
                BFRendering.textureSquare(poseStack, graphics, matchClass.getIcon(), f6, f2, f4);
                n10 = matchClass == MatchClass.CLASS_COMMANDER ? ColorReferences.COLOR_THEME_YELLOW_SOLID : ColorReferences.COLOR_WHITE_SOLID;
                mutableComponent2 = Component.translatable((String)matchClass.getDisplayTitle()).withStyle(ChatFormatting.BOLD).withColor(n10);
                if (n15 > 0) {
                    MutableComponent mutableComponent4 = Component.translatable((String)("enchantment.level." + (n15 + 1))).withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
                    mutableComponent2 = mutableComponent2.append(" ").append((Component)mutableComponent4);
                }
                int n16 = font.width((FormattedText)mutableComponent2);
                BFRendering.component2d(poseStack, font, graphics, (Component)mutableComponent2, f6 - (float)n16 / 2.0f - 1.0f, f2 + 3.0f, 0.5f);
            }
            float f14 = f + 3.0f;
            f7 = f2 + (float)n + 0.1f;
            int n17 = 8;
            BFRendering.playerHead(minecraft, manager, poseStack, graphics, playerCloudData.getMcProfile(), f14, f7, 8);
            gameClient.method_2692(poseStack, graphics, bFClientPlayerData, f14, f7, 8);
            BFRendering.component2d(poseStack, font, graphics, (Component)mutableComponent3, f + 25.0f + (float)n12, f2 + (float)n + 0.5f);
        } else {
            var30_30 = MESSAGE_USERNAME.copy().withStyle(style);
            BFRendering.component2d(poseStack, font, graphics, (Component)var30_30, f + 2.0f, f2 + (float)n);
        }
        float f15 = f + (float)n7;
        int n18 = 0;
        for (String string : collection) {
            float f16 = f15 + (float)(n18 * 30) + 1.0f;
            mutableComponent = Component.literal((String)string).withStyle(style);
            BFRendering.rectangle(poseStack, graphics, f16, f2, 29.0f, f4, n18 % 2 == 0 ? 0x111111 : 0, 0.5f);
            BFRendering.centeredComponent2d(poseStack, font, graphics, (Component)mutableComponent, f16 + 15.0f, f2 + (float)n);
            ++n18;
        }
    }

    @Deprecated
    public static void item(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ItemStack itemStack, float x, float y) {
        BFRendering.item(poseStack, graphics, itemStack, x, y, 1.0f);
    }

    public static void item(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ItemStack itemStack, float x, float y, float scale) {
        RenderSystem.setupGuiFlatDiffuseLighting((Vector3f)GUI_LIGHTS_1, (Vector3f)GUI_LIGHTS_2);
        poseStack.pushPose();
        poseStack.translate(x - 8.0f * scale, y - 8.0f * scale, 0.0f);
        poseStack.scale(scale, scale, scale);
        graphics.renderItem(itemStack, 0, 0, 0, -125);
        poseStack.popPose();
    }

    public static void staticEffect(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, int x, int y, int width, int height, float alpha, float time) {
        int n = (int)(time / 2.0f) % 4;
        ResourceLocation resourceLocation = BFRes.loc("textures/misc/effects/static" + n + ".png");
        BFRendering.texture(poseStack, graphics, resourceLocation, x, y, width, height, 0.0f, alpha);
    }

    public static String formatTime(int seconds) {
        int n = seconds / 60;
        int n2 = seconds % 60;
        String string = n2 < 10 ? "0" : "";
        return n + ":" + string + n2;
    }

    public static void billboardTexture(@NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, double x, double y, double z, int scale) {
        BFRendering.tintedBillboardTexture(poseStack, camera, graphics, texture, x, y, z, scale, scale, 1.0f, 0xFFFFFF, true);
    }

    public static void billboardTexture(@NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, double x, double y, double z, int scale, boolean depthTest) {
        BFRendering.tintedBillboardTexture(poseStack, camera, graphics, texture, x, y, z, scale, scale, 1.0f, 0xFFFFFF, depthTest);
    }

    public static void billboardTexture(@NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, @NotNull Vec3 position, int scale, boolean depthTest) {
        BFRendering.tintedBillboardTexture(poseStack, camera, graphics, texture, position.x, position.y, position.z, scale, scale, 1.0f, 0xFFFFFF, depthTest);
    }

    public static void billboardTexture(@NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, @NotNull BlockPos blockPos, int scale, boolean depthTest) {
        BFRendering.tintedBillboardTexture(poseStack, camera, graphics, texture, blockPos.getX(), blockPos.getY(), blockPos.getZ(), scale, scale, 1.0f, 0xFFFFFF, depthTest);
    }

    public static void billboardTexture(@NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, double x, double y, double z, int width, int height) {
        BFRendering.tintedBillboardTexture(poseStack, camera, graphics, texture, x, y, z, width, height, 1.0f, 0xFFFFFF, true);
    }

    public static void billboardTexture(@NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, double x, double y, double z, int width, int height, boolean depthTest) {
        BFRendering.tintedBillboardTexture(poseStack, camera, graphics, texture, x, y, z, width, height, 1.0f, 0xFFFFFF, depthTest);
    }

    public static void billboardTexture(@NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, double x, double y, double z, int width, int height, float alpha) {
        BFRendering.tintedBillboardTexture(poseStack, camera, graphics, texture, x, y, z, width, height, alpha, 0xFFFFFF, true);
    }

    public static void billboardTexture(@NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, double x, double y, double z, int width, int height, float alpha, boolean depthTest) {
        BFRendering.tintedBillboardTexture(poseStack, camera, graphics, texture, x, y, z, width, height, alpha, 0xFFFFFF, depthTest);
    }

    public static void tintedBillboardTexture(@NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, @NotNull Vec3 position, int width, int height, float alpha, int color, boolean depthTest) {
        BFRendering.tintedBillboardTexture(poseStack, camera, graphics, texture, position.x, position.y, position.z, width, height, alpha, color, depthTest);
    }

    public static void tintedBillboardTexture(@NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, double x, double y, double z, int width, int height, float alpha, int color, boolean depthTest) {
        RenderSystem.depthMask((boolean)false);
        if (depthTest) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        poseStack.scale(-0.02f, -0.02f, 0.02f);
        BFRendering.tintedTexture(poseStack, graphics, texture, -width / 2, -height / 2, width, height, 0.0f, alpha, color);
        poseStack.popPose();
        if (!depthTest) {
            RenderSystem.enableDepthTest();
        }
        RenderSystem.depthMask((boolean)true);
    }

    public static void method_254(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull ResourceLocation resourceLocation, double d, double d2, double d3, float f, float f2, float f3, int n, boolean bl) {
        float f4 = -0.02f;
        RenderSystem.depthMask((boolean)false);
        if (bl) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
        poseStack.pushPose();
        poseStack.translate(d, d2, d3);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        poseStack.scale(-0.02f, -0.02f, -0.02f);
        BFRendering.tintedTexture(poseStack, guiGraphics, resourceLocation, -f / 2.0f, -f2 / 2.0f, f, f2, 0.0f, f3, n);
        poseStack.popPose();
        if (!bl) {
            RenderSystem.enableDepthTest();
        }
        RenderSystem.depthMask((boolean)true);
    }

    public static void worldTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, double x, double y, double z, float width, float height, float alpha, float rotationX, float rotationY) {
        BFRendering.tintedWorldTexture(poseStack, graphics, texture, x, y, z, width, height, alpha, 0xFFFFFF, true, rotationX, rotationY);
    }

    public static void tintedWorldTexture(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, double x, double y, double z, float width, float height, float alpha, int color, boolean depthTest, float rotationX, float rotationY) {
        float f = 0.02f;
        RenderSystem.depthMask((boolean)false);
        if (depthTest) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY));
        poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
        poseStack.scale(-0.02f, -0.02f, 0.02f);
        BFRendering.tintedTexture(poseStack, graphics, texture, -width / 2.0f, -height / 2.0f, width, height, 0.0f, alpha, color);
        poseStack.popPose();
        if (!depthTest) {
            RenderSystem.enableDepthTest();
        }
        RenderSystem.depthMask((boolean)true);
    }

    public static void enableScissor(@NotNull GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.enableScissor(x, y, x + width, y + height);
    }

    public static void component(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull Component component, double x, double y, double z) {
        BFRendering.component(poseStack, font, camera, graphics, component, x, y, z, 1.0f, 0xFFFFFF, true);
    }

    public static void billboardComponentWithShadow(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull Component component, double x, double y, double z) {
        BFRendering.billboardComponentWithShadow(poseStack, font, camera, graphics, component, x, y, z, 1.0f, 0xFFFFFF, true);
    }

    public static void component(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull Component component, double x, double y, double z, float scale) {
        BFRendering.component(poseStack, font, camera, graphics, component, x, y, z, scale, 0xFFFFFF, true);
    }

    public static void billboardComponentWithShadow(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull Component component, double x, double y, double z, float scale) {
        BFRendering.billboardComponentWithShadow(poseStack, font, camera, graphics, component, x, y, z, scale, 0xFFFFFF, true);
    }

    public static void component(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull Component component, double x, double y, double z, float scale, int color, boolean depthTest) {
        float f = 0.016f;
        RenderSystem.depthMask((boolean)false);
        if (depthTest) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        poseStack.scale(2.0f, 2.0f, 1.0f);
        poseStack.scale(-0.016f, -0.016f, 1.0f);
        poseStack.scale(scale, scale, 1.0f);
        BFRendering.component2d(poseStack, font, graphics, component, (float)(-font.width((FormattedText)component)) / 2.0f, 0.0f, color);
        poseStack.popPose();
        if (!depthTest) {
            RenderSystem.enableDepthTest();
        }
        RenderSystem.depthMask((boolean)true);
    }

    public static void billboardComponentWithShadow(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull Camera camera, @NotNull GuiGraphics graphics, @NotNull Component component, double x, double y, double z, float scale, int color, boolean depthTest) {
        float f = 0.016f;
        RenderSystem.depthMask((boolean)false);
        if (depthTest) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        poseStack.scale(2.0f, 2.0f, 1.0f);
        poseStack.scale(-0.016f, -0.016f, 1.0f);
        poseStack.scale(scale, scale, 1.0f);
        BFRendering.component2dWithShadow(poseStack, font, graphics, component, (float)(-font.width((FormattedText)component)) / 2.0f, 0.0f, color);
        poseStack.popPose();
        if (!depthTest) {
            RenderSystem.enableDepthTest();
        }
        RenderSystem.depthMask((boolean)true);
    }

    public static void billboardComponent(@NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Component component, double x, double y, double z) {
        float f = 0.016f;
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        poseStack.scale(2.0f, 2.0f, 1.0f);
        poseStack.translate(0.0f, 0.0f, -1.0f);
        poseStack.scale(-0.016f, -0.016f, 1.0f);
        BFRendering.component2d(poseStack, font, graphics, component, (float)(-font.width((FormattedText)component)) / 2.0f, 0.0f);
        poseStack.popPose();
    }

    public static void backgroundBlur(@NotNull Minecraft minecraft) {
        BFRendering.backgroundBlur(minecraft, 1.0f);
    }

    public static void backgroundBlur(@NotNull Minecraft minecraft, float delta) {
        BFRendering.backgroundBlur(minecraft, delta, minecraft.options.getMenuBackgroundBlurriness());
    }

    public static void backgroundBlur(@NotNull Minecraft minecraft, float delta, int blurriness) {
        Options options = minecraft.options;
        int n = options.getMenuBackgroundBlurriness();
        OptionInstance optionInstance = options.menuBackgroundBlurriness();
        optionInstance.set((Object)blurriness);
        options.getMenuBackgroundBlurriness();
        minecraft.gameRenderer.processBlurEffect(delta);
        minecraft.getMainRenderTarget().bindWrite(false);
        optionInstance.set((Object)n);
    }

    @NotNull
    public static ResourceLocation getSkinTexture(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull GameProfile profile) {
        return BFRendering.getSkinTexture(minecraft, manager, profile.getId());
    }

    @NotNull
    public static ResourceLocation getSkinTexture(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull UUID playerId) {
        return manager.getSkinFetcher().fetchSkinCached(minecraft, playerId);
    }

    public static void cameraOrigin(@NotNull Camera camera, @NotNull PoseStack poseStack) {
        Vec3 vec3 = camera.getPosition();
        poseStack.translate(-vec3.x, -vec3.y, -vec3.z);
    }

    public static void block(@NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull BlockState blockState, @NotNull PoseStack poseStack, double x, double y, double z) {
        BFRendering.block(level, dispatcher, buffer, blockState, poseStack, x, y, z, 0.0f, 0.0f, 0.0f);
    }

    public static void block(@NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull BlockState blockState, @NotNull PoseStack poseStack, double x, double y, double z, float rotationX, float rotationY, float rotationZ) {
        int n = LevelRenderer.getLightColor((BlockAndTintGetter)level, (BlockPos)BlockPos.containing((double)x, (double)y, (double)z).offset(0, 1, 0));
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationZ));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY));
        dispatcher.renderSingleBlock(blockState, poseStack, (MultiBufferSource)buffer, n, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutout());
        buffer.endBatch();
        poseStack.popPose();
    }

    public static void blockEntity(@NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull PoseStack poseStack, @NotNull BlockEntity blockEntity, double x, double y, double z, float rotationX, float rotationY, float delta) {
        BlockEntityRenderer blockEntityRenderer = minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity);
        if (blockEntityRenderer == null) {
            return;
        }
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        AABB aABB = blockEntityRenderer.getRenderBoundingBox(blockEntity).move(x, y, z);
        if (minecraft.getEntityRenderDispatcher().shouldRenderHitBoxes()) {
            LevelRenderer.renderLineBox((PoseStack)poseStack, (VertexConsumer)bufferSource.getBuffer(RenderType.lines()), (AABB)aABB, (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
        if (!blockEntityRenderer.shouldRenderOffScreen(blockEntity) && !minecraft.levelRenderer.getFrustum().isVisible(aABB)) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationY + 180.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(rotationX));
        blockEntityRenderer.render(blockEntity, delta, poseStack, (MultiBufferSource)bufferSource, LevelRenderer.getLightColor((BlockAndTintGetter)level, (BlockPos)BlockPos.containing((double)x, (double)y, (double)z).offset(0, 1, 0)), OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    public static void crosshair(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull BFCrosshair crosshair, float x, float y, float alpha, boolean renderDot, boolean renderLines, float spread) {
        if (renderDot) {
            BFRendering.centeredTexture(poseStack, graphics, crosshair.middle(), x, y, 16.0f, 16.0f, 0.0f, alpha);
        }
        if (renderLines) {
            float f = 2.0f;
            BFRendering.centeredTexture(poseStack, graphics, crosshair.top(), x, y - 8.0f - spread + 2.0f, 16.0f, 16.0f, 0.0f, alpha);
            BFRendering.centeredTexture(poseStack, graphics, crosshair.bottom(), x, y + 8.0f + spread - 2.0f, 16.0f, 16.0f, 0.0f, alpha);
            BFRendering.centeredTexture(poseStack, graphics, crosshair.left(), x - 8.0f - spread + 2.0f, y, 16.0f, 16.0f, 0.0f, alpha);
            BFRendering.centeredTexture(poseStack, graphics, crosshair.right(), x + 8.0f + spread - 2.0f, y, 16.0f, 16.0f, 0.0f, alpha);
        }
    }

    public static void vanillaCrosshair(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull LocalPlayer player, float x, float y) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        if (minecraft.options.attackIndicator().get() == AttackIndicatorStatus.CROSSHAIR) {
            float f = player.getAttackStrengthScale(0.0f);
            float f2 = y - 7.0f + 24.0f;
            float f3 = x - 8.0f;
            if (f < 1.0f) {
                int n = (int)(f * 17.0f);
                poseStack.translate(f3, f2, 0.0f);
                graphics.blitSprite(CROSSHAIR_BACKGROUND, 0, 0, 16, 4);
                graphics.blitSprite(CROSSHAIR_PROGRESS, 16, 4, 0, 0, 0, 0, n, 4);
                poseStack.translate(-f3, -f2, 0.0f);
            }
        }
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    public static void shaderColor(int color, float alpha) {
        float f = (float)FastColor.ARGB32.red((int)color) * 0.003921569f;
        float f2 = (float)FastColor.ARGB32.green((int)color) * 0.003921569f;
        float f3 = (float)FastColor.ARGB32.blue((int)color) * 0.003921569f;
        RenderSystem.setShaderColor((float)f, (float)f2, (float)f3, (float)alpha);
    }

    public static void shaderColor(int color) {
        float f = (float)FastColor.ARGB32.red((int)color) * 0.003921569f;
        float f2 = (float)FastColor.ARGB32.green((int)color) * 0.003921569f;
        float f3 = (float)FastColor.ARGB32.blue((int)color) * 0.003921569f;
        RenderSystem.setShaderColor((float)f, (float)f2, (float)f3, (float)1.0f);
    }

    public static void resetShaderColor() {
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void bootcampTexturePoint(@NotNull BootcampTexturePoint bootcampTexturePoint, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Camera camera, @NotNull ClientLevel clientLevel, @NotNull LocalPlayer localPlayer, float f) {
        boolean bl;
        Vec3 vec3 = localPlayer.getEyePosition();
        ClipContext clipContext = new ClipContext(vec3, bootcampTexturePoint.position, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)localPlayer);
        boolean bl2 = bl = clientLevel.clip(clipContext).getType() == HitResult.Type.MISS;
        if (!bl) {
            return;
        }
        int n = 16;
        int n2 = 8;
        float f2 = (float)(bootcampTexturePoint.position.x - localPlayer.getX());
        float f3 = (float)(bootcampTexturePoint.position.y - localPlayer.getY());
        float f4 = (float)(bootcampTexturePoint.position.z - localPlayer.getZ());
        float f5 = Mth.sqrt((float)(f2 * f2 + f3 * f3 + f4 * f4));
        float f6 = f5 * 100.0f / 8.0f;
        if (f5 > 8.0f) {
            f6 = 200.0f - f6;
        }
        float f7 = 0.01f * f6;
        float f8 = (float)(bootcampTexturePoint.position.x + bootcampTexturePoint.position.y + bootcampTexturePoint.position.z);
        float f9 = f + f8;
        float f10 = Mth.sin((float)(f9 / 15.0f));
        float f11 = Mth.sin((float)(f9 / 5.0f));
        if ((f7 *= 1.5f + f11) < 0.0f) {
            f7 = 0.0f;
        }
        BFRendering.tintedBillboardTexture(poseStack, camera, guiGraphics, bootcampTexturePoint.method_3187(), bootcampTexturePoint.position.x, bootcampTexturePoint.position.y + (double)(0.15f * f10), bootcampTexturePoint.position.z, 32, 32, f7, ColorReferences.COLOR_WHITE_SOLID, true);
    }

    public static void method_198(@NotNull BulletTracer bulletTracer, @NotNull PoseStack poseStack, float f) {
        int n;
        Vec3 vec3 = bulletTracer.getLerpedPosition(f);
        Vector3f vector3f = bulletTracer.method_1175().toVector3f();
        poseStack.pushPose();
        poseStack.translate(vec3.x, vec3.y, vec3.z);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        RenderSystem.lineWidth((float)bulletTracer.getTracerWidth());
        int n2 = n = bulletTracer.getColor();
        if (bulletTracer.method_1179()) {
            n2 = MathUtils.withAlphaf(n, 1.0f - bulletTracer.method_1176());
        }
        Matrix4f matrix4f = poseStack.last().pose();
        bufferBuilder.addVertex(matrix4f, 0.0f, 0.0f, 0.0f).setColor(n - ColorReferences.COLOR_BLACK_SOLID);
        bufferBuilder.addVertex(matrix4f, vector3f.x, vector3f.y, vector3f.z).setColor(n2);
        BufferUploader.drawWithShader((MeshData)bufferBuilder.buildOrThrow());
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.lineWidth((float)1.0f);
        poseStack.popPose();
    }

    public static void method_5483(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull ResourceLocation resourceLocation, int n, int n2, float f) {
        int n3;
        int n4;
        int n5 = n / 2;
        int n6 = n2 / 2;
        int n7 = 1920;
        int n8 = 1080;
        float f2 = (float)n / 1920.0f;
        float f3 = (float)n2 / 1080.0f;
        float f4 = 1080.0f * f2;
        if (f4 < (float)n2) {
            n4 = (int)(1080.0f * f3);
            n3 = (int)(1920.0f * f3);
        } else {
            n4 = (int)(1080.0f * f2);
            n3 = (int)(1920.0f * f2);
        }
        BFRendering.rectangle(guiGraphics, 0, 0, n, n2, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.centeredTextureScaled(poseStack, guiGraphics, resourceLocation, n5, n6, n3, n4, 1.2f, 0.5f);
        BFRendering.texture(poseStack, guiGraphics, SHADOWEFFECT_TEXTURE, 0, 0, n, n2);
        BFRendering.backgroundBlur(minecraft, f);
        int n9 = 5;
        BFRendering.promptBackground(poseStack, guiGraphics, 5, 5, n - 10, n2 - 10);
    }

    public static int lerpColor(int colorA, int colorB, double t) {
        t = Math.max(0.0, Math.min(1.0, t));
        int n = colorA >> 16 & 0xFF;
        int n2 = colorA >> 8 & 0xFF;
        int n3 = colorA & 0xFF;
        int n4 = colorB >> 16 & 0xFF;
        int n5 = colorB >> 8 & 0xFF;
        int n6 = colorB & 0xFF;
        int n7 = (int)Math.round((double)n + (double)(n4 - n) * t);
        int n8 = (int)Math.round((double)n2 + (double)(n5 - n2) * t);
        int n9 = (int)Math.round((double)n3 + (double)(n6 - n3) * t);
        return n7 << 16 | n8 << 8 | n9;
    }
}

