/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  org.apache.commons.lang3.mutable.MutableInt
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.cache.object.GeoCube
 */
package com.boehmod.blockfront.client.render.geo.gun;

import com.boehmod.blockfront.client.event.BFClientTickSubscriber;
import com.boehmod.blockfront.client.event.BFRenderHandSubscriber;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.geo.BFWeaponItemRenderer;
import com.boehmod.blockfront.client.render.geo.gun.AbstractGunGeoPart;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;

public class MuzzleFlashGunGeoPart<T extends BFWeaponItem<T>>
extends AbstractGunGeoPart<T> {
    @NotNull
    private static final ResourceLocation field_1736 = BFRes.loc("textures/misc/muzzleflash/whiteflash.png");
    @NotNull
    public static final List<ResourceLocation> TEXTURES = new ObjectArrayList();
    @NotNull
    public static final List<BFRenderHandSubscriber.BF_71> field_1734 = new ObjectArrayList();
    boolean field_1735 = false;

    public MuzzleFlashGunGeoPart(BFWeaponItemRenderer<T> bFWeaponItemRenderer) {
        super(bFWeaponItemRenderer);
    }

    @Override
    public void method_1267(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, float f, float f2, float f3, float f4) {
    }

    @Override
    public boolean shouldSkipRendering(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, boolean bl, int n, int n2, int n3, float f, float f2, float f3) {
        if (itemDisplayContext == ItemDisplayContext.GUI || itemDisplayContext == ItemDisplayContext.GROUND) {
            return false;
        }
        if (!geoBone.getName().equals("muzzleflash") || bl || !(t instanceof GunItem)) {
            return false;
        }
        GunItem gunItem = (GunItem)t;
        Vector3f vector3f = ((GeoCube)geoBone.getCubes().getFirst()).quads()[0].vertices()[0].position();
        boolean bl2 = itemDisplayContext.firstPerson();
        float f4 = gunItem.getMuzzleFlashSize();
        if (!bl2) {
            RenderSystem.enableDepthTest();
        }
        poseStack.pushPose();
        poseStack.translate(vector3f.x, vector3f.y, vector3f.z);
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, -0.1f);
        if (this.field_1735) {
            BFRendering.centeredTexture(poseStack, guiGraphics, TEXTURES.get(BFClientTickSubscriber.muzzleFlashIndex), 0.0f, 0.0f, f4, f4, 0.0f, f);
        }
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
        poseStack.translate(0.0f, 0.0f, 0.2f);
        if (bl2) {
            for (BFRenderHandSubscriber.BF_71 bF_71 : field_1734) {
                bF_71.method_425(poseStack, guiGraphics, f, f3, f2, f4);
            }
        }
        if (this.field_1735) {
            poseStack.translate(0.0f, 0.0f, -0.1f);
            BFRendering.centeredTexture(poseStack, guiGraphics, TEXTURES.get(BFClientTickSubscriber.muzzleFlashIndex), 0.0f, 0.0f, f4, f4, 0.0f, f);
            if (bl2) {
                poseStack.translate(0.0f, 0.0f, -0.1f);
                BFRendering.centeredTexture(poseStack, guiGraphics, field_1736, 0.0f, 0.0f, 4.0f * f4, 4.0f * f4, 0.0f, 0.5f * f);
            }
        }
        poseStack.popPose();
        poseStack.popPose();
        RenderSystem.disableDepthTest();
        return true;
    }

    @Override
    public boolean method_1265(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, @NotNull GeoBone geoBone, @NotNull T t, @NotNull ItemStack itemStack, @NotNull ResourceLocation resourceLocation, @NotNull ItemDisplayContext itemDisplayContext, @NotNull VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        return false;
    }

    @Override
    public boolean method_1266(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull ItemStack itemStack, @NotNull MutableInt mutableInt, @Nullable GunScopeConfig gunScopeConfig, @NotNull ItemDisplayContext itemDisplayContext, @NotNull BFClientPlayerData bFClientPlayerData) {
        boolean bl = itemDisplayContext.firstPerson() ? GunItem.field_4055 > 0 : GunItem.hasMuzzleFlash(itemStack);
        boolean bl2 = this.field_1735 = minecraft.getDebugOverlay().showDebugScreen() || BFClientSettings.EXPERIMENTAL_TOGGLE_MUZZLEFLASH.isEnabled() && bl;
        if ((!GunItem.field_4019 || gunScopeConfig != null && gunScopeConfig.field_3843 == null) && this.field_1735 || minecraft.getDebugOverlay().showDebugScreen()) {
            mutableInt.setValue(0xF000F0);
        }
        return false;
    }

    static {
        int n = 3;
        for (int i = 0; i <= 3; ++i) {
            TEXTURES.add(BFRes.loc("textures/misc/muzzleflash/flash" + i + ".png"));
        }
    }
}

