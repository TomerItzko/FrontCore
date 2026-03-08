/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec2
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.client.gui.layer;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFRenderHandSubscriber;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.gui.BFCrosshair;
import com.boehmod.blockfront.client.gui.layer.BFAbstractGuiLayer;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.gun.GunSpreadConfig;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.MeleeItem;
import com.boehmod.blockfront.common.item.base.IHasCrosshair;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.unnamed.BF_180;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public final class CrosshairGuiLayer
extends BFAbstractGuiLayer {
    private static final ResourceLocation field_528 = BFRes.loc("textures/misc/hitmarker.png");
    private static final ResourceLocation field_529 = ResourceLocation.withDefaultNamespace((String)"textures/misc/spyglass_scope.png");
    private static final ResourceLocation field_530 = BFRes.loc("textures/misc/sights/scope_bg.png");
    private static final ResourceLocation field_531 = BFRes.loc("textures/misc/sights/scope_bg_blur.png");
    private static final float field_532 = 3.2f;
    private static final float field_6659 = 3.0f;
    public static int hitMarkerTimer = 0;
    public static boolean isKill = false;

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull DeltaTracker delta, @NotNull BFClientManager manager) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui) {
            return;
        }
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            return;
        }
        ItemStack itemStack = localPlayer.getMainHandItem();
        if (itemStack.isEmpty()) {
            return;
        }
        float f = delta.getGameTimeDeltaPartialTick(true);
        if (itemStack.getItem() instanceof IHasCrosshair) {
            ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)manager.getPlayerDataHandler();
            BFClientPlayerData bFClientPlayerData = clientPlayerDataHandler.getPlayerData(minecraft);
            boolean bl = BFUtils.isPlayerUnavailable((Player)localPlayer, bFClientPlayerData);
            if (!manager.getCinematics().isSequencePlaying() && !bl) {
                this.method_512(minecraft, clientPlayerDataHandler, manager, itemStack, graphics, f, graphics.guiWidth(), graphics.guiHeight());
            }
        }
    }

    private void method_512(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull BFClientManager bFClientManager, @NotNull ItemStack itemStack, @NotNull GuiGraphics guiGraphics, float f, int n, int n2) {
        Object object;
        Object object2;
        LocalPlayer localPlayer;
        PoseStack poseStack = guiGraphics.pose();
        int n3 = guiGraphics.guiWidth() / 2;
        int n4 = guiGraphics.guiHeight() / 2;
        poseStack.pushPose();
        boolean bl = true;
        BFClientPlayerData bFClientPlayerData = clientPlayerDataHandler.getPlayerData(minecraft);
        if (BFClientSettings.CROSSHAIR_DYNAMIC.isEnabled() && !GunItem.field_4019) {
            Vec2 vec2 = MathUtils.lerp(BFRenderHandSubscriber.field_353, BFRenderHandSubscriber.field_354, f);
            poseStack.translate(-vec2.y * 3.2f, 0.0f, 0.0f);
            poseStack.translate(0.0f, -vec2.x * 3.2f, 0.0f);
        }
        float f2 = MathUtils.lerpf1(GunSpreadConfig.currentSpread, GunSpreadConfig.prevSpread, f);
        if (bFClientPlayerData.isOutOfGame()) {
            bl = false;
        }
        if ((localPlayer = minecraft.player) != null && localPlayer.isUsingItem() && localPlayer.getUseItem().is(BFItems.BINOCULARS)) {
            bl = false;
        }
        if ((object2 = itemStack.getItem()) instanceof GunItem) {
            object = (GunItem)object2;
            if (GunItem.field_4019 && !BFClientSettings.CROSSHAIR_RENDER_AIMING.isEnabled()) {
                bl = false;
            }
            object2 = ((GunItem)object).getScopeConfig(itemStack);
            if (GunItem.field_4019 && minecraft.screen == null && object2.field_3843 != null) {
                CrosshairGuiLayer.method_5747(guiGraphics, f, n, n2, poseStack, n3, n4, (GunScopeConfig)object2);
                bl = false;
            }
            if (!object2.field_3846) {
                bl = false;
            }
        }
        poseStack.pushPose();
        object = (BFCrosshair)bFClientManager.getCrosshairResource().method_1647(BFClientSettings.CROSSHAIR_NAME.method_1525());
        if (BFClientSettings.CROSSHAIR_RENDER.isEnabled() && bl && minecraft.screen == null) {
            float f3 = BFClientSettings.CROSSHAIR_STATIC.isEnabled() ? 0.0f : 3.0f * f2;
            float f4 = MathUtils.lerpf1(PlayerTickable.field_151, PlayerTickable.field_152, f);
            float f5 = MathUtils.lerpf1(PlayerTickable.field_149, PlayerTickable.field_150, f);
            float f6 = MathUtils.lerpf1(PlayerTickable.inspectionBlurPrev, PlayerTickable.inspectionBlur, f);
            float f7 = MathUtils.lerpf1(PlayerTickable.field_6606, PlayerTickable.field_6607, f);
            float f8 = Mth.clamp((float)(f4 + f5 + f6 + f7), (float)0.0f, (float)1.0f) * 2.0f;
            float f9 = Mth.clamp((float)(1.0f - f8), (float)0.0f, (float)1.0f);
            float f10 = BFClientSettings.CROSSHAIR_ALPHA.getValue() * f9;
            BFRendering.crosshair(poseStack, guiGraphics, (BFCrosshair)object, n3, n4, f10, BFClientSettings.CROSSHAIR_DOT.isEnabled(), BFClientSettings.CROSSHAIR_LINES.isEnabled(), f3);
            if (itemStack.getItem() instanceof MeleeItem && minecraft.player != null) {
                float f11 = f3 < 15.0f ? f3 : 0.0f;
                BFRendering.vanillaCrosshair(minecraft, poseStack, guiGraphics, minecraft.player, n3, (float)n4 + f11);
            }
        }
        if (hitMarkerTimer > 0) {
            poseStack.pushPose();
            BFRendering.centeredTintedTextureScaled(poseStack, guiGraphics, field_528, n3, n4, 17.0f, 17.0f, Math.min(0.75f * (float)hitMarkerTimer, 1.0f), 1.0f, isKill ? 13846343 : 0xFFFFFF);
            poseStack.popPose();
        }
        poseStack.popPose();
        poseStack.popPose();
    }

    private static void method_5747(@NotNull GuiGraphics guiGraphics, float f, int n, int n2, PoseStack poseStack, int n3, int n4, GunScopeConfig gunScopeConfig) {
        if (gunScopeConfig.field_3843 == null) {
            return;
        }
        int n5 = Math.min(n2, n);
        boolean bl = n5 == n;
        poseStack.pushPose();
        float f2 = 25.0f;
        float f3 = Mth.clamp((float)MathUtils.lerpf1(PlayerTickable.field_157, PlayerTickable.field_126, f), (float)-25.0f, (float)25.0f);
        float f4 = Mth.clamp((float)MathUtils.lerpf1(PlayerTickable.field_158, PlayerTickable.field_127, f), (float)-25.0f, (float)25.0f);
        Vector3f vector3f = ShakeManager.getDelta(f).mul(2.0f);
        float f5 = (float)n3 + f4 + -6.0f * vector3f.x;
        float f6 = (float)n4 + f3 + -6.0f * vector3f.y;
        float f7 = -(f3 + f4) * 0.2f - vector3f.z;
        poseStack.pushPose();
        poseStack.translate(f5, f6, 0.0f);
        for (BF_180 bF_180 : PlayerTickable.field_160) {
            bF_180.method_741(poseStack, guiGraphics, f);
        }
        poseStack.popPose();
        BFRendering.centeredTexture(poseStack, guiGraphics, gunScopeConfig.field_3843, f5, f6, (float)n5, (float)n5, f7);
        BFRendering.centeredTexture(poseStack, guiGraphics, field_529, f5, f6, (float)n5, (float)n5, f7, 1.0f);
        BFRendering.centeredTexture(poseStack, guiGraphics, field_530, f5, f6, (float)n5 * 1.5f, (float)n5 * 1.5f, f7, 1.0f);
        BFRendering.centeredTexture(poseStack, guiGraphics, field_531, f5 + f4 * 6.0f, f6 + f3 * 6.0f, (float)(n5 * 3), (float)(n5 * 3), f7, 1.0f);
        if (bl) {
            int n6 = (n2 - n5) / 2;
            BFRendering.rectangle(guiGraphics, 0, 0, n, n6, ColorReferences.COLOR_BLACK_SOLID);
            BFRendering.rectangle(guiGraphics, 0, n6 + n5, n, n6 + 1, ColorReferences.COLOR_BLACK_SOLID);
        } else {
            int n7 = (n - n5) / 2;
            BFRendering.rectangle(guiGraphics, 0, 0, n7 + 5, n2, ColorReferences.COLOR_BLACK_SOLID);
            BFRendering.rectangle(guiGraphics, n7 + n5 - 5, 0, n7 + 10, n2, ColorReferences.COLOR_BLACK_SOLID);
        }
        poseStack.popPose();
    }
}

