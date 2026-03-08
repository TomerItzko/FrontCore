/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemSticker
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  com.mojang.blaze3d.vertex.BufferUploader
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.MeshData
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.Tesselator
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  org.apache.commons.lang3.mutable.MutableInt
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Matrix4f
 *  org.joml.Vector3f
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.cache.object.GeoCube
 *  software.bernie.geckolib.util.RenderUtil
 */
package com.boehmod.blockfront.client.render.geo.gun;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.types.CloudItemSticker;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.geo.BFWeaponItemRenderer;
import com.boehmod.blockfront.client.render.geo.gun.AbstractGunGeoPart;
import com.boehmod.blockfront.client.screen.armory.ArmoryInspectScreen;
import com.boehmod.blockfront.client.screen.armory.ArmoryStickerPlaceScreen;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.util.RenderUtil;

public class StickersGunGeoPart<T extends BFWeaponItem<T>>
extends AbstractGunGeoPart<T> {
    private static final ResourceLocation field_1739 = BFRes.loc("textures/stickers/debug.png");
    private static final ResourceLocation field_1740 = BFRes.loc("textures/stickers/debug_clear.png");

    public StickersGunGeoPart(BFWeaponItemRenderer<T> bFWeaponItemRenderer) {
        super(bFWeaponItemRenderer);
    }

    private static void method_1268(PoseStack poseStack, ResourceLocation resourceLocation, Vector3f vector3f, Vector3f vector3f2, Vector3f vector3f3, Vector3f vector3f4, int n, int n2, int n3) {
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)resourceLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR);
        bufferBuilder.addVertex(matrix4f, vector3f.x, vector3f.y, vector3f.z).setUv(1.0f, 0.0f).setUv2(n, n2).setColor(n3);
        bufferBuilder.addVertex(matrix4f, vector3f2.x, vector3f2.y, vector3f2.z).setUv(0.0f, 0.0f).setUv2(n, n2).setColor(n3);
        bufferBuilder.addVertex(matrix4f, vector3f3.x, vector3f3.y, vector3f3.z).setUv(0.0f, 1.0f).setUv2(n, n2).setColor(n3);
        bufferBuilder.addVertex(matrix4f, vector3f4.x, vector3f4.y, vector3f4.z).setUv(1.0f, 1.0f).setUv2(n, n2).setColor(n3);
        BufferUploader.drawWithShader((MeshData)bufferBuilder.buildOrThrow());
        BFRendering.resetShaderColor();
    }

    @Override
    public void method_1267(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, float f, float f2, float f3, float f4) {
    }

    @Override
    public boolean shouldSkipRendering(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, boolean bl, int n, int n2, int n3, float f, float f2, float f3) {
        return geoBone.getName().startsWith("sticker") && !bl;
    }

    @Override
    public boolean method_1265(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, @NotNull GeoBone geoBone, @NotNull T t, @NotNull ItemStack itemStack, @NotNull ResourceLocation resourceLocation, @NotNull ItemDisplayContext itemDisplayContext, @NotNull VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        ArmoryInspectScreen armoryInspectScreen;
        CloudItem cloudItem;
        CloudItem cloudItem2;
        ArmoryStickerPlaceScreen armoryStickerPlaceScreen;
        if (itemDisplayContext == ItemDisplayContext.GUI || itemDisplayContext == ItemDisplayContext.GROUND) {
            return false;
        }
        if (!geoBone.getName().startsWith("sticker") || bl) {
            return false;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
        ResourceLocation resourceLocation2 = null;
        ResourceLocation resourceLocation3 = null;
        if (minecraft.getDebugOverlay().showDebugScreen()) {
            resourceLocation3 = field_1739;
        }
        int n4 = Integer.parseInt(geoBone.getName().replace("sticker", ""));
        Screen screen = minecraft.screen;
        if (screen instanceof ArmoryStickerPlaceScreen) {
            armoryStickerPlaceScreen = (ArmoryStickerPlaceScreen)screen;
            if (n4 == armoryStickerPlaceScreen.field_958) {
                screen = armoryStickerPlaceScreen.method_721();
                resourceLocation3 = field_1739;
                int n5 = screen.getSticker(n4);
                if (n5 != -1 && (cloudItem2 = cloudRegistry.getItem(n5)) instanceof CloudItemSticker) {
                    cloudItem = (CloudItemSticker)cloudItem2;
                    resourceLocation2 = BFRes.loc("textures/stickers/" + cloudItem.getSuffixForDisplay() + ".png");
                }
            }
        } else {
            screen = minecraft.screen;
            if (screen instanceof ArmoryInspectScreen) {
                armoryInspectScreen = (ArmoryInspectScreen)screen;
                int n6 = (screen = armoryInspectScreen.method_717()).getSticker(n4);
                if (n6 != -1 && (cloudItem2 = cloudRegistry.getItem(n6)) instanceof CloudItemSticker) {
                    cloudItem = (CloudItemSticker)cloudItem2;
                    resourceLocation2 = BFRes.loc("textures/stickers/" + cloudItem.getSuffixForDisplay() + ".png");
                }
            } else {
                int n7 = BFWeaponItem.getStickerIndex(itemStack, n4);
                if (n7 != -1 && (cloudItem = cloudRegistry.getItem(n7)) instanceof CloudItemSticker) {
                    CloudItemSticker cloudItemSticker = (CloudItemSticker)cloudItem;
                    resourceLocation2 = BFRes.loc("textures/stickers/" + cloudItemSticker.getSuffixForDisplay() + ".png");
                }
            }
        }
        if (resourceLocation2 != null && resourceLocation3 != null) {
            resourceLocation3 = field_1740;
        }
        RenderSystem.enableDepthTest();
        poseStack.pushPose();
        armoryStickerPlaceScreen = (GeoCube)geoBone.getCubes().getFirst();
        armoryInspectScreen = armoryStickerPlaceScreen.quads()[0];
        Vector3f vector3f = armoryInspectScreen.vertices()[0].position();
        Vector3f vector3f2 = armoryInspectScreen.vertices()[1].position();
        cloudItem = armoryInspectScreen.vertices()[2].position();
        cloudItem2 = armoryInspectScreen.vertices()[3].position();
        Vector3f vector3f3 = MathUtils.averageJoml(new Vector3f[]{vector3f, vector3f2, cloudItem, cloudItem2});
        poseStack.translate(vector3f3.x, vector3f3.y, vector3f3.z);
        RenderUtil.rotateMatrixAroundBone((PoseStack)poseStack, (GeoBone)geoBone);
        poseStack.translate(-vector3f3.x, -vector3f3.y, -vector3f3.z);
        if (resourceLocation3 != null) {
            StickersGunGeoPart.method_1268(poseStack, resourceLocation3, vector3f, vector3f2, (Vector3f)cloudItem, (Vector3f)cloudItem2, n, n2, n3);
        }
        if (resourceLocation2 != null) {
            StickersGunGeoPart.method_1268(poseStack, resourceLocation2, vector3f, vector3f2, (Vector3f)cloudItem, (Vector3f)cloudItem2, n, n2, n3);
        }
        RenderSystem.disableDepthTest();
        poseStack.popPose();
        return true;
    }

    @Override
    public boolean method_1266(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull ItemStack itemStack, @NotNull MutableInt mutableInt, @Nullable GunScopeConfig gunScopeConfig, @NotNull ItemDisplayContext itemDisplayContext, @NotNull BFClientPlayerData bFClientPlayerData) {
        return false;
    }
}

