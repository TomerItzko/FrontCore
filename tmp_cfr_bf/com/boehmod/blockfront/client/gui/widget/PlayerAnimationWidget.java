/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.bflib.cloud.common.player.PlayerDataType;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.profile.ProfileMainScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.floats.FloatFloatImmutablePair;
import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerAnimationWidget
extends BFWidget {
    private static final float field_7030 = 128.0f;
    @NotNull
    private final PlayerCloudData playerCloudData;
    private final int field_632;
    private float field_630;
    private float field_631;
    private final boolean field_628;

    public PlayerAnimationWidget(int n, int n2, int n3, int n4, @NotNull Screen screen, boolean bl, int n5, @NotNull UUID uUID) {
        super(n, n2, n3, n4, screen);
        this.field_628 = bl;
        this.field_632 = n5;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        this.playerCloudData = ((ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler()).getCloudProfile(uUID);
        if (this.playerCloudData.getPlayerDataType() != PlayerDataType.DISPLAY) {
            MutableComponent mutableComponent = Component.literal((String)this.playerCloudData.getUsername());
            this.method_546((Component)Component.translatable((String)"bf.dropdown.text.profile.tip", (Object[])new Object[]{mutableComponent}));
            this.method_531();
        }
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        FloatFloatPair floatFloatPair;
        float f3;
        float f4;
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        int n3 = this.field_566 / 2;
        int n4 = this.height / 2;
        int n5 = this.field_564 + n3;
        int n6 = this.field_565 + n4;
        float f5 = MathUtils.lerpf1(this.field_630, this.field_631, f2);
        float f6 = (float)(this.field_632 % 2 == 1 ? 45 : 60) + 10.0f * f5;
        float f7 = (float)(this.field_632 % 2 == 1 ? 5 : 20) + 10.0f * f5;
        float f8 = this.field_632 % 2 == 1 ? 0 : 30;
        FakePlayer fakePlayer = BFRendering.ENVIRONMENT.getPlayerCached(minecraft, this.playerCloudData.getMcProfile());
        float f9 = fakePlayer.walkAnimation.position(f2);
        float f10 = Mth.sin((float)(f9 * 1.5f));
        float f11 = (float)(this.field_565 + this.height - 45) + f10 + f7;
        BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, n5, n6, 100, 100, 0.0f, 0.5f);
        BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.FADE, n5, n6, 120, 150, 0.0f, f5);
        BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.FADE, n5, n6, 80, 100, 0.0f, f5);
        if (this.field_628) {
            f4 = (float)n2 / (float)minecraft.getWindow().getScreenHeight() - 0.25f;
            f3 = (float)n / (float)minecraft.getWindow().getScreenWidth();
            floatFloatPair = new FloatFloatImmutablePair(55.0f * f4, 125.0f * f3);
        } else {
            floatFloatPair = fakePlayer.method_471(f2);
        }
        f4 = this.field_632 % 2 == 1 ? 0.75f : 1.0f;
        f3 = 75.0f * f4;
        float f12 = 20.0f * f4;
        float f13 = 0.55f + 0.05f * f10;
        BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, (float)n5, (float)n6 + f7 + 40.0f, f3, f12, 0.0f, f13);
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, f8);
        BFRendering.entity(clientPlayerDataHandler, poseStack, guiGraphics, minecraft, (LivingEntity)fakePlayer, n5, (float)n6 + f7 + 38.0f + f10, f6, floatFloatPair.firstFloat(), -35.0f, floatFloatPair.secondFloat(), f2);
        if (this.playerCloudData.getPlayerDataType() != PlayerDataType.DISPLAY) {
            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, 128.0f - f8);
            BFRendering.playerInfoTag(minecraft, bFClientManager, poseStack, font, guiGraphics, playerCloudData, this.playerCloudData, n5, f11, this.method_594());
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    @Override
    public void method_537(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, float f) {
        super.method_537(minecraft, bFClientManager, clientPlayerDataHandler, f);
        this.field_631 = this.field_630;
        this.field_630 = Mth.lerp((float)0.5f, (float)this.field_630, (float)(this.method_594() ? 1.0f : 0.0f));
    }

    @Override
    public boolean onPress(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, double d, double d2, int n) {
        if (this.method_594()) {
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.0f));
            minecraft.setScreen((Screen)new ProfileMainScreen(this.getScreen(), this.playerCloudData));
            return true;
        }
        return super.onPress(minecraft, bFClientManager, clientPlayerDataHandler, d, d2, n);
    }

    private boolean method_594() {
        return this.method_557() && this.playerCloudData.getPlayerDataType() != PlayerDataType.DISPLAY;
    }
}

