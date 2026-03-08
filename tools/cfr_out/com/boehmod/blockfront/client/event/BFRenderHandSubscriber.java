/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderDispatcher
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RenderHandEvent
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFRenderFrameSubscriber;
import com.boehmod.blockfront.client.event.tick.GunAimingTickable;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.entity.BFPlayerRenderer;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.client.world.BobbingPresets;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.common.item.BinocularsItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.MeleeItem;
import com.boehmod.blockfront.common.item.RadioItem;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFRenderHandSubscriber {
    public static final ResourceLocation SCOPEFLARE_TEXTURE = BFRes.loc("textures/misc/muzzleflash/scopeflare.png");
    public static final float field_357 = 1.2f;
    public static final float field_358 = 5.0f;
    public static Vec2 field_353 = Vec2.ZERO;
    public static Vec2 field_354 = Vec2.ZERO;
    public static Vec2 field_355 = Vec2.ZERO;
    public static float field_359 = 0.0f;
    public static float field_360 = 0.0f;
    public static float field_361 = 0.0f;
    public static float field_362;
    public static float field_363;
    public static float field_343;
    public static float field_344;
    public static float field_345;
    public static float field_346;
    public static float field_347;
    public static float field_348;
    private static float field_349;
    private static float field_350;
    private static float field_351;
    private static float field_6620;
    private static float field_6621;
    private static float field_6622;

    @SubscribeEvent
    public static void onRenderHand(@NotNull RenderHandEvent event) {
        float f;
        float f2;
        Item item;
        AbstractVehicleEntity abstractVehicleEntity;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        boolean bl = event.getHand() == InteractionHand.MAIN_HAND;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        float f3 = BFRendering.getRenderTime();
        if (localPlayer == null) {
            return;
        }
        EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        Object object = entityRenderDispatcher.getRenderer((Entity)localPlayer);
        if (!(object instanceof BFPlayerRenderer)) {
            return;
        }
        BFPlayerRenderer bFPlayerRenderer = (BFPlayerRenderer)object;
        object = clientPlayerDataHandler.getPlayerData(minecraft);
        if (BFUtils.isPlayerUnavailable((Player)localPlayer, object)) {
            event.setCanceled(true);
            return;
        }
        float f4 = event.getPartialTick();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource multiBufferSource = event.getMultiBufferSource();
        int n = event.getPackedLight();
        if (bFClientManager.getCinematics().isSequencePlaying()) {
            event.setCanceled(true);
            return;
        }
        if (localPlayer.isUsingItem() && localPlayer.getUseItem().getItem() instanceof BinocularsItem) {
            event.setCanceled(true);
            return;
        }
        Object object2 = localPlayer.getVehicle();
        if (object2 instanceof AbstractVehicleEntity && (object2 = (abstractVehicleEntity = (AbstractVehicleEntity)object2).method_2324((Entity)localPlayer)) != null && object2.field_2664) {
            event.setCanceled(true);
            return;
        }
        abstractVehicleEntity = localPlayer.getOffhandItem();
        object2 = event.getHand();
        if (object2.equals((Object)InteractionHand.MAIN_HAND) && !abstractVehicleEntity.isEmpty()) {
            poseStack.pushPose();
        } else if (object2.equals((Object)InteractionHand.OFF_HAND) && !abstractVehicleEntity.isEmpty()) {
            poseStack.popPose();
        }
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() instanceof MeleeItem) {
            poseStack.translate(0.0f, 0.4f, 0.0f);
        }
        if (itemStack.getItem() instanceof RadioItem) {
            poseStack.translate(0.0f, 0.6f, 0.0f);
        }
        if (itemStack.getItem() instanceof BFWeaponItem) {
            BFRenderHandSubscriber.method_422(minecraft, poseStack, f4);
        }
        if (!((item = itemStack.getItem()) instanceof BFWeaponItem)) {
            return;
        }
        BFWeaponItem bFWeaponItem = (BFWeaponItem)item;
        if (bFWeaponItem instanceof GunItem) {
            item = (GunItem)bFWeaponItem;
            poseStack.translate(0.0f, 0.6f, 0.0f);
        }
        float f5 = MathUtils.lerpf1(BFRenderHandSubscriber.field_353.x, BFRenderHandSubscriber.field_354.x, f4);
        float f6 = MathUtils.lerpf1(BFRenderHandSubscriber.field_353.y, BFRenderHandSubscriber.field_354.y, f4);
        float f7 = 0.0f;
        if (BFClientSettings.EXPERIMENTAL_TOGGLE_IMMERSIVE_BOBBING.isEnabled()) {
            f2 = BFClientSettings.EXPERIMENTAL_IMMERSIVE_BOBBING_SCALE.getValue();
            f = GunItem.field_4019 ? 80.0f : 20.0f;
            Vector3f vector3f = BobbingPresets.IMMERSIVE.getCameraVec(f3, f);
            poseStack.mulPose(Axis.XP.rotationDegrees(vector3f.x * f2));
            poseStack.mulPose(Axis.YP.rotationDegrees(vector3f.y * f2));
            poseStack.mulPose(Axis.ZP.rotationDegrees(vector3f.z * f2));
        }
        if (!GunItem.field_4019) {
            f2 = MathUtils.lerpf1(GunAimingTickable.field_172, GunAimingTickable.field_173, f4);
            Vector3f vector3f = BobbingPresets.field_1629.getCameraVec(f3, 1.0f);
            f5 += vector3f.x * f2;
            f6 += vector3f.y * f2;
            f7 += vector3f.z * f2;
        }
        f2 = 0.35f;
        f = -0.5f;
        float f8 = -0.8f;
        float f9 = 0.0f;
        float f10 = 0.0f;
        float f11 = 0.0f;
        float f12 = MathUtils.lerpf1(GunAimingTickable.field_175, GunAimingTickable.field_176, f4);
        if (f12 > 0.0f) {
            Vector3f vector3f = BobbingPresets.field_1628.getCameraVec(f3, 1.0f).mul(4.0f);
            f5 += vector3f.x * f12;
            f6 += vector3f.y * f12;
            f7 += vector3f.z * f12;
        }
        f12 = MathUtils.lerpf1(field_362, field_363, f4);
        float f13 = MathUtils.lerpf1(field_343, field_344, f4);
        float f14 = MathUtils.lerpf1(field_345, field_346, f4);
        f5 -= f12 * 3.0f;
        f6 -= f13 * 3.0f;
        f7 -= f14 * 3.0f;
        f12 = GunItem.field_4019 ? 1.0f : 1.75f;
        Vector3f vector3f = ShakeManager.getDelta(f4).mul(f12);
        f5 += vector3f.x;
        f6 += vector3f.y;
        f7 += vector3f.z;
        f12 = MathUtils.lerpf1(localPlayer.walkDist, localPlayer.walkDistO, f4) * 16.0f;
        float f15 = GunItem.field_4019 ? 0.1f : 1.0f;
        f14 = Mth.sin((float)(f12 / 3.0f)) * f15;
        float f16 = Mth.sin((float)(f12 / 3.4f)) * f15;
        float f17 = Mth.sin((float)(f12 / 3.8f)) * f15;
        float f18 = Mth.sin((float)(f12 / 5.0f));
        float f19 = Mth.sin((float)(f12 / 2.5f));
        float f20 = MathUtils.lerpf1(BFRenderFrameSubscriber.field_338, BFRenderFrameSubscriber.field_339, f4);
        float f21 = MathUtils.lerpf1(BFRenderFrameSubscriber.field_334, BFRenderFrameSubscriber.field_336, f4);
        f9 += 2.5f * f14 * f21;
        f10 += 0.7f * f17 * f21;
        poseStack.translate(-0.04f * f18 * f20, 0.0f, 0.0f);
        f5 += (f9 += 5.0f * f19 * f20);
        f6 += (f10 += 2.0f * f18 * f20);
        f7 += (f11 += 2.75f * f16 * f21);
        f12 = MathUtils.lerpf1(field_347, field_348, f4);
        Vec3 vec3 = bFWeaponItem.method_3778().scale((double)f12);
        poseStack.mulPose(Axis.XP.rotationDegrees((float)vec3.x));
        poseStack.mulPose(Axis.YP.rotationDegrees((float)vec3.y));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float)vec3.z));
        f14 = MathUtils.lerpf1(BFRenderFrameSubscriber.field_340, BFRenderFrameSubscriber.field_341, f4);
        poseStack.translate(0.0f, -0.5f * f14, 0.0f);
        poseStack.mulPose(Axis.XP.rotationDegrees(-10.0f * f14));
        poseStack.mulPose(Axis.YP.rotationDegrees(60.0f * f14));
        poseStack.mulPose(Axis.ZP.rotationDegrees(5.0f * f14));
        f16 = MathUtils.lerpf1(PlayerTickable.field_149, PlayerTickable.field_150, f4);
        Vector3f vector3f2 = new Vector3f(125.0f, 150.0f, -45.0f).mul(f16).mul(1.0f, bl ? 1.0f : -1.0f, bl ? 1.0f : -1.0f);
        f5 += vector3f2.x;
        f6 += vector3f2.y;
        f7 += vector3f2.z;
        poseStack.translate(-0.5f * f16, -0.1f * f16, 0.5f * f16);
        if (PlayerTickable.inspectionBlurPrev > 0.0f && PlayerTickable.inspectionBlur > 0.0f) {
            f16 = MathUtils.lerpf1(PlayerTickable.inspectionBlurPrev, PlayerTickable.inspectionBlur, f4);
            float f22 = Mth.sin((float)(f3 / 30.0f));
            f18 = Mth.sin((float)(f3 / 40.0f));
            f19 = 50.0f;
            f20 = Mth.sin((float)(f3 / 50.0f)) * (1.0f - 0.2f * f22 * f18);
            f5 += 45.0f * f16;
            f6 += 110.0f * f16;
            f7 += (-25.0f + 45.0f * f20) * f16;
            poseStack.translate(-0.2f * f16, 0.1f * f16, 0.15f * f16);
            poseStack.translate(-0.1f * f20 * f16, 0.0f, 0.1f * f20 * f16);
        }
        f16 = bl ? field_349 : field_6620;
        float f23 = bl ? field_350 : field_6621;
        f18 = bl ? field_351 : field_6622;
        f16 = Mth.lerp((float)0.2f, (float)f16, (float)f5);
        f23 = Mth.lerp((float)0.2f, (float)f23, (float)f6);
        f18 = Mth.lerp((float)0.2f, (float)f18, (float)f7);
        poseStack.translate(f2, f, f8);
        poseStack.mulPose(Axis.XP.rotationDegrees(f16 / 2.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(f23 / 2.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(f18 / 2.0f));
        poseStack.translate(-f2, -f, -f8);
        if (bl) {
            field_349 = f16;
            field_350 = f23;
            field_351 = f18;
        } else {
            field_6620 = f16;
            field_6621 = f23;
            field_6622 = f18;
        }
    }

    public static void method_422(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, float f) {
        if (!((Boolean)minecraft.options.bobView().get()).booleanValue()) {
            return;
        }
        Entity entity = minecraft.getCameraEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            float f2 = player.walkDist - player.walkDistO;
            float f3 = -(player.walkDist + f2 * f);
            float f4 = Mth.lerp((float)f, (float)player.oBob, (float)player.bob);
            poseStack.translate(-(Mth.sin((float)(f3 * (float)Math.PI)) * f4 * 0.5f), Math.abs(Mth.cos((float)(f3 * (float)Math.PI)) * f4), 0.0f);
            poseStack.mulPose(Axis.ZP.rotationDegrees(-Mth.sin((float)(f3 * (float)Math.PI)) * f4 * 3.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(-Math.abs(Mth.cos((float)(f3 * (float)Math.PI - 0.2f)) * f4) * 5.0f));
        }
    }

    public static void update(@Nullable LocalPlayer player) {
        field_354 = new Vec2(BFRenderHandSubscriber.field_353.x, BFRenderHandSubscriber.field_353.y);
        field_353 = new Vec2(Mth.lerp((float)0.4f, (float)BFRenderHandSubscriber.field_353.x, (float)0.0f), Mth.lerp((float)0.4f, (float)BFRenderHandSubscriber.field_353.y, (float)0.0f));
    }

    static {
        field_363 = 0.0f;
        field_344 = 0.0f;
        field_346 = 0.0f;
        field_348 = 0.0f;
        field_351 = 0.0f;
        field_6622 = 0.0f;
    }

    public static class BF_73
    extends BF_71 {
        private static final int field_377 = 8;
        private static final float field_367 = 0.12f;
        private static final float field_368 = 3.0f;
        private static final float field_369 = 3.0f;
        private static final float field_370 = 0.5f;
        private static final float field_371 = 0.45f;
        private final ResourceLocation field_378;
        private final boolean field_376 = Math.random() < 0.5;
        private float field_372 = 0.5f;
        private float field_373 = 0.5f;
        private float field_374 = 0.45f;
        private float field_375 = 0.45f;

        public BF_73(@NotNull Random random, int n) {
            super(n, random);
            this.field_378 = BFRes.loc("textures/misc/muzzleflash/smokepuff" + random.nextInt(0, 8) + ".png");
            this.method_426();
        }

        @Override
        public boolean method_426() {
            this.field_373 = this.field_372;
            this.field_372 = MathUtils.moveTowards(this.field_372, 1.0f, 0.12f);
            this.field_375 = this.field_374;
            this.field_374 = MathUtils.moveTowards(this.field_374, 0.0f, 0.12f);
            return this.field_374 + this.field_375 <= 0.0f;
        }

        @Override
        public void method_425(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, float f, float f2, float f3, float f4) {
            float f5 = MathUtils.lerpf1(this.field_364, this.field_365, f2);
            float f6 = MathUtils.lerpf1(this.field_372, this.field_373, f2);
            float f7 = MathUtils.lerpf1(this.field_374, this.field_375, f2) * f;
            float f8 = 3.0f * f6 * f4;
            poseStack.pushPose();
            BFRendering.centeredTexture(poseStack, guiGraphics, this.field_378, 0.0f, 0.0f, f8, f8, 0.0f, f7);
            poseStack.popPose();
        }
    }

    public static class BF_72
    extends BF_71 {
        @Nonnull
        private final ResourceLocation field_366;

        public BF_72(int n, @NotNull Random random) {
            super(n, random);
            int n2 = ThreadLocalRandom.current().nextInt(0, 4);
            this.field_366 = BFRes.loc("textures/misc/muzzleflash/firepuff" + n2 + ".png");
        }

        @Override
        public void method_425(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, float f, float f2, float f3, float f4) {
            float f5 = MathUtils.lerpf1(this.field_364, this.field_365, f2) * f;
            float f6 = 20.0f / f5 * f4;
            float f7 = Mth.sin((float)(f3 * 2.0f));
            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, 1.4f + 4.0f / f5);
            BFRendering.centeredTexture(poseStack, guiGraphics, this.field_366, 0.0f, 0.0f, f6, f6, 40.0f * f7, 0.1f * f5);
            poseStack.popPose();
        }
    }

    public static abstract class BF_71 {
        protected int field_364;
        protected int field_365;

        public BF_71(int n, @NotNull Random random) {
            this.field_364 = this.field_365 = n;
        }

        public boolean method_426() {
            return (this.field_365 = this.field_364--) <= 0;
        }

        public abstract void method_425(@NotNull PoseStack var1, @NotNull GuiGraphics var2, float var3, float var4, float var5, float var6);
    }

    public record BF_74(double posX, double posY, double posZ, float rotX, float rotY, float rotZ, float scale) {
        public BF_74(double d, double d2, double d3, float f, float f2, float f3) {
            this(d, d2, d3, f, f2, f3, 1.0f);
        }
    }
}

