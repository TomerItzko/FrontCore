/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.geo.gun;

import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.geo.BFWeaponItemRenderer;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.GeoBone;

public abstract class AbstractGunGeoPart<T extends BFWeaponItem<T>> {
    @NotNull
    final BFWeaponItemRenderer<T> field_1738;

    public AbstractGunGeoPart(@NotNull BFWeaponItemRenderer<T> bFWeaponItemRenderer) {
        this.field_1738 = bFWeaponItemRenderer;
    }

    public abstract void method_1267(@NotNull Minecraft var1, @NotNull PoseStack var2, @NotNull GeoBone var3, @NotNull ItemStack var4, @NotNull ItemDisplayContext var5, @NotNull T var6, float var7, float var8, float var9, float var10);

    public abstract boolean shouldSkipRendering(@NotNull Minecraft var1, @NotNull PoseStack var2, @NotNull GuiGraphics var3, @NotNull GeoBone var4, @NotNull ItemStack var5, @NotNull ItemDisplayContext var6, @NotNull T var7, boolean var8, int var9, int var10, int var11, float var12, float var13, float var14);

    public abstract boolean method_1265(@NotNull Minecraft var1, @NotNull PoseStack var2, @NotNull MultiBufferSource var3, @NotNull GeoBone var4, @NotNull T var5, @NotNull ItemStack var6, @NotNull ResourceLocation var7, @NotNull ItemDisplayContext var8, @NotNull VertexConsumer var9, boolean var10, float var11, int var12, int var13, int var14);

    public abstract boolean method_1266(@NotNull Minecraft var1, @NotNull PoseStack var2, @NotNull ItemStack var3, @NotNull MutableInt var4, @Nullable GunScopeConfig var5, @NotNull ItemDisplayContext var6, @NotNull BFClientPlayerData var7);
}

