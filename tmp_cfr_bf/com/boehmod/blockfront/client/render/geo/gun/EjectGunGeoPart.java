/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.geo.gun;

import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.geo.BFWeaponItemRenderer;
import com.boehmod.blockfront.client.render.geo.gun.AbstractGunGeoPart;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.unnamed.BF_340;
import com.boehmod.blockfront.unnamed.BF_341;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;

public class EjectGunGeoPart<T extends BFWeaponItem<T>>
extends AbstractGunGeoPart<T> {
    @NotNull
    private static final List<BF_341> field_1731 = new ObjectArrayList();
    @NotNull
    private static final List<BF_340> field_1732 = new ObjectArrayList();

    public EjectGunGeoPart(@NotNull BFWeaponItemRenderer<T> bFWeaponItemRenderer) {
        super(bFWeaponItemRenderer);
    }

    public static void method_1262() {
        field_1731.removeIf(BF_341::method_1258);
        field_1732.removeIf(BF_340::method_1256);
    }

    public static void method_1261(@NotNull Random random, @NotNull String string, boolean bl) {
        BF_341 bF_341 = new BF_341(new Vector2f(1.0f, -1.0f + -0.2f * random.nextFloat()), string, bl);
        bF_341.method_1258();
        field_1731.add(bF_341);
        if (BFClientSettings.EXPERIMENTAL_BULLET_EJECT_PUFF.isEnabled()) {
            BF_340 bF_340 = new BF_340(random, bl);
            bF_340.method_1256();
            field_1732.add(bF_340);
        }
    }

    @Override
    public void method_1267(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, float f, float f2, float f3, float f4) {
    }

    @Override
    public boolean shouldSkipRendering(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, boolean bl, int n, int n2, int n3, float f, float f2, float f3) {
        if (!itemDisplayContext.firstPerson() || !geoBone.getName().equals("eject") || bl) {
            return false;
        }
        Vector3f vector3f = ((GeoCube)geoBone.getCubes().getFirst()).quads()[0].vertices()[0].position();
        poseStack.pushPose();
        poseStack.translate(vector3f.x, vector3f.y, vector3f.z);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
        for (BF_341 object : field_1731) {
            object.method_1260(poseStack, guiGraphics, f3);
        }
        for (BF_340 bF_340 : field_1732) {
            bF_340.method_1257(poseStack, guiGraphics, f, f3);
        }
        poseStack.popPose();
        return true;
    }

    @Override
    public boolean method_1265(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, @NotNull GeoBone geoBone, @NotNull T t, @NotNull ItemStack itemStack, @NotNull ResourceLocation resourceLocation, @NotNull ItemDisplayContext itemDisplayContext, @NotNull VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        return false;
    }

    @Override
    public boolean method_1266(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull ItemStack itemStack, @NotNull MutableInt mutableInt, @Nullable GunScopeConfig gunScopeConfig, @NotNull ItemDisplayContext itemDisplayContext, @NotNull BFClientPlayerData bFClientPlayerData) {
        return false;
    }
}

