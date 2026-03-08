/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  javax.annotation.Nonnull
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.gui.layer;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.layer.BFAbstractGuiLayer;
import com.boehmod.blockfront.client.gui.layer.MatchGuiLayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.unnamed.BF_39;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import javax.annotation.Nonnull;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class HealthEffectsGuiLayer
extends BFAbstractGuiLayer {
    @Nonnull
    public static final ObjectList<MatchGuiLayer.BF_114> field_534 = new ObjectArrayList();
    @Nonnull
    public static final ObjectList<DamageIndicator> DAMAGE_INDICATORS = new ObjectArrayList();
    @Nonnull
    private static final ResourceLocation field_536 = BFRes.loc("textures/gui/overlay/bloodoverlay.png");
    @Nonnull
    private static final ResourceLocation field_537 = BFRes.loc("textures/gui/overlay/tunnelvision.png");
    @Nonnull
    private static final ResourceLocation field_538 = BFRes.loc("textures/misc/muzzleflash/whiteflash_big.png");
    @Nonnull
    private static final ResourceLocation field_539 = BFRes.loc("textures/gui/overlay/damage.png");
    private static final int field_540 = 16;
    private static final int field_541 = 128;
    @Nonnull
    private static final Vec3 field_542 = new Vec3(94.0, 0.0, 0.0);

    private static void method_515(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull LocalPlayer localPlayer, int n, int n2, float f) {
        if (!BFClientSettings.UI_RENDER_DAMAGEINDICATORS.isEnabled()) {
            return;
        }
        float f2 = -localPlayer.getViewYRot(f);
        for (DamageIndicator damageIndicator : DAMAGE_INDICATORS) {
            float f3 = 180.0f + damageIndicator.getAngle(localPlayer, f) + f2;
            Vec3 vec3 = field_542.yRot((float)Math.toRadians(-f3));
            BFRendering.centeredTexture(poseStack, guiGraphics, field_539, (float)((double)((float)n / 2.0f) + vec3.x), (float)((double)((float)n2 / 2.0f) + vec3.z), 16.0f, 128.0f, 90.0f + f3, damageIndicator.getIntensity(f));
        }
    }

    private void method_517(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        for (MatchGuiLayer.BF_114 bF_114 : field_534) {
            bF_114.method_510(poseStack, guiGraphics, n, n2, f);
        }
    }

    private void method_514(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, float f, float f2, float f3, float f4) {
        float f5;
        if (BFClientSettings.CONTENT_GORE.isEnabled()) {
            f5 = MathUtils.lerpf1(BF_39.field_185, BF_39.field_186, f4);
            BFRendering.texture(poseStack, guiGraphics, field_536, 0, 0, n, n2, 0.0f, f5);
            if (f <= 8.0f) {
                float f6 = (0.5f - 0.1f * Mth.sin((float)(f3 / 5.0f))) * (1.0f - f2);
                BFRendering.texture(poseStack, guiGraphics, field_536, 0, 0, n, n2, 0.0f, f6);
            }
        }
        f5 = MathUtils.lerpf1(BF_39.field_187, BF_39.field_188, f4);
        BFRendering.texture(poseStack, guiGraphics, field_537, 0, 0, n, n2, 0.0f, f5);
    }

    private void method_516(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2) {
        if (!BFClientSettings.EXPERIMENTAL_TOGGLE_MUZZLEFLASH.isEnabled() || !minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }
        int n3 = n / 2;
        int n4 = n2 / 2;
        ItemStack itemStack = localPlayer.getMainHandItem();
        Object object = itemStack.getItem();
        if (object instanceof GunItem) {
            GunItem gunItem = (GunItem)object;
            object = gunItem.getScopeConfig(itemStack);
            int n5 = GunItem.field_4019 ? 0 : 1;
            int n6 = n3 / 4 * n5;
            int n7 = n4 / 4 * n5;
            if (minecraft.getDebugOverlay().showDebugScreen() || GunItem.field_4055 > 0 && (!GunItem.field_4019 || object.field_3843 == null)) {
                BFRendering.centeredTexture(poseStack, guiGraphics, field_538, n3 + n6, n4 + n7, 500, 500, 0.0f, 0.5f);
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull DeltaTracker delta, @NotNull BFClientManager manager) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            return;
        }
        PoseStack poseStack = graphics.pose();
        float f = BFRendering.getRenderTime();
        int n = graphics.guiWidth();
        int n2 = graphics.guiHeight();
        float f2 = localPlayer.getMaxHealth();
        float f3 = localPlayer.getHealth();
        float f4 = f3 / f2;
        float f5 = delta.getGameTimeDeltaPartialTick(true);
        HealthEffectsGuiLayer.method_515(poseStack, graphics, localPlayer, n, n2, f5);
        this.method_516(minecraft, localPlayer, poseStack, graphics, n, n2);
        this.method_517(poseStack, graphics, n, n2, f5);
        this.method_514(poseStack, graphics, n, n2, f3, f4, f, f5);
    }

    public static class DamageIndicator {
        private final Vec3 origin;
        private float alpha;
        private float prevAlpha;

        public DamageIndicator(float intensity, Vec3 origin) {
            this.prevAlpha = this.alpha = intensity;
            this.origin = origin;
        }

        public boolean update() {
            this.prevAlpha = this.alpha;
            this.alpha -= 0.05f;
            return this.alpha + this.prevAlpha <= 0.0f;
        }

        public float getAngle(LocalPlayer player, float delta) {
            return MathUtils.yaw(player.getPosition(delta), this.origin);
        }

        public float getIntensity(float delta) {
            return MathUtils.lerpf1(this.alpha, this.prevAlpha, delta);
        }
    }
}

