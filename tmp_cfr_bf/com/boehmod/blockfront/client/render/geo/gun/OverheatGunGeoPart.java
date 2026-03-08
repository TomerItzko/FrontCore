/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.geo.gun;

import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.geo.BFWeaponItemRenderer;
import com.boehmod.blockfront.client.render.geo.gun.AbstractGunGeoPart;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.GeoBone;

public class OverheatGunGeoPart<T extends BFWeaponItem<T>>
extends AbstractGunGeoPart<T> {
    private static final ResourceLocation field_1737 = BFRes.loc("textures/misc/gun/overheat.png");

    public OverheatGunGeoPart(@NotNull BFWeaponItemRenderer<T> bFWeaponItemRenderer) {
        super(bFWeaponItemRenderer);
    }

    @Override
    public void method_1267(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, float f, float f2, float f3, float f4) {
    }

    @Override
    public boolean shouldSkipRendering(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, boolean bl, int n, int n2, int n3, float f, float f2, float f3) {
        return false;
    }

    @Override
    public boolean method_1265(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, @NotNull GeoBone geoBone, @NotNull T t, @NotNull ItemStack itemStack, @NotNull ResourceLocation resourceLocation, @NotNull ItemDisplayContext itemDisplayContext, @NotNull VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        if (itemDisplayContext == ItemDisplayContext.GUI || itemDisplayContext == ItemDisplayContext.GROUND) {
            return false;
        }
        if (!(itemStack.getItem() instanceof GunItem) || !geoBone.getName().contains("overheat")) {
            return false;
        }
        float f2 = 0.1f;
        float f3 = GunItem.getHeat(itemStack);
        if (f3 < 0.1f) {
            return false;
        }
        this.method_1263(poseStack, multiBufferSource, geoBone, f, n2, f3);
        return false;
    }

    @Override
    public boolean method_1266(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull ItemStack itemStack, @NotNull MutableInt mutableInt, @Nullable GunScopeConfig gunScopeConfig, @NotNull ItemDisplayContext itemDisplayContext, @NotNull BFClientPlayerData bFClientPlayerData) {
        return false;
    }

    private void method_1263(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, @NotNull GeoBone geoBone, float f, int n, float f2) {
        float f3 = (f2 - 0.1f) * 1.1111112f;
        float f4 = Mth.clamp((float)(f3 * 2.5f), (float)0.0f, (float)1.0f);
        int n2 = FastColor.ARGB32.colorFromFloat((float)f4, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderType renderType = RenderType.text((ResourceLocation)field_1737);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
        this.field_1738.method_1246(poseStack, geoBone, renderType, multiBufferSource, vertexConsumer, false, f, 0xF000F0, n, n2);
    }
}

