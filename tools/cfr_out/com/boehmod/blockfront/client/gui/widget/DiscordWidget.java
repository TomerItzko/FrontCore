/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.discord.PacketDiscordLinkRequest
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.discord.PacketDiscordLinkRequest;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFPopoutWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.FormatUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class DiscordWidget
extends BFPopoutWidget {
    private static final ResourceLocation field_656 = BFRes.loc("textures/gui/discord.png");
    private static final ResourceLocation field_657 = BFRes.loc("textures/gui/discord_offline.png");
    private static final ResourceLocation field_658 = BFRes.loc("textures/gui/discord_background.png");
    private static final ResourceLocation field_659 = BFRes.loc("textures/gui/menu/icons/copy_discord.png");
    private static final ResourceLocation field_660 = BFRes.loc("textures/gui/effects/fade_blue.png");
    private static final ResourceLocation field_661 = BFRes.loc("textures/gui/menu/icons/copy.png");
    private static final Component field_653 = Component.translatable((String)"bf.menu.discord.title").withStyle(BFStyles.BOLD);
    private static final Component field_654 = Component.translatable((String)"bf.menu.discord.description");
    private static final Component field_655 = Component.translatable((String)"bf.menu.discord.copy").withStyle(BFStyles.BOLD);

    public DiscordWidget(int n, int n2, int n3, int n4, Screen screen) {
        super(n, n2, n3, n4, screen);
    }

    @Override
    public boolean method_603(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        return ((ClientConnectionManager)bFClientManager.getConnectionManager()).getStatus().isConnected();
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)bFClientManager.getConnectionManager();
        float f3 = this.method_604(f2);
        int n3 = ColorReferences.COLOR_THEME_DISCORD_SOLID;
        boolean bl = clientConnectionManager.getStatus().isConnected();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        BFRendering.rectangle(guiGraphics, 0, 0, this.getScreen().width, this.getScreen().height, 0, f3 * 0.85f);
        poseStack.pushPose();
        float f4 = 200.0f;
        f4 *= 0.5f;
        float f5 = (f4 += 13.0f) * f3;
        float f6 = (float)this.field_564 - f5;
        ObjectList<String> objectList = FormatUtils.parseMarkup(font, field_654.getString(), (int)(f4 * 1.8f));
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, bl ? n3 : 0x77888888);
        int n4 = 8;
        if (bl) {
            BFRendering.texture(poseStack, guiGraphics, field_660, this.field_564 - 8, this.field_565 - 8, this.field_566 + 16, this.height + 16);
            BFRendering.texture(poseStack, guiGraphics, field_656, this.field_564, this.field_565, this.field_566, this.height);
        } else {
            BFRendering.texture(poseStack, guiGraphics, field_657, this.field_564, this.field_565, this.field_566, this.height);
        }
        float f7 = 1.0f + 0.2f * f3;
        float f8 = f6 + f4 / 2.0f;
        float f9 = (float)this.field_565 + f4 / 2.0f;
        RenderSystem.enableDepthTest();
        BFRendering.enableScissor(guiGraphics, this.field_564 - (int)f4, this.field_565, (int)f4, (int)f5);
        BFRendering.rectangle(poseStack, guiGraphics, f6, (float)this.field_565, f5, f5, n3);
        poseStack.pushPose();
        float f10 = Mth.sin((float)(f / 150.0f));
        BFRendering.centeredTextureScaled(poseStack, guiGraphics, field_658, f8, f9 + 4.0f * f10, f4 * 8.0f, f4 * 8.0f, f7, 0.2f * f3);
        float f11 = Mth.sin((float)(f / 10.0f));
        float f12 = Mth.sin((float)(f / 15.0f));
        float f13 = Math.max(0.0f, f12);
        float f14 = 64.0f + 10.0f * f13;
        BFRendering.centeredTexture(poseStack, guiGraphics, field_659, f8, f9, f14, f14, 5.0f * f11);
        BFRendering.centeredTexture(poseStack, guiGraphics, field_661, f8, f9, f14, f14, 5.0f * f11, f13);
        float f15 = f9 + f14 / 2.0f + 3.0f;
        BFRendering.rectangle(poseStack, guiGraphics, f6, f15 - 4.0f, f4, 15.0f, 0, 0.4f * f3);
        float f16 = Mth.sin((float)(f / 10.0f));
        poseStack.pushPose();
        poseStack.translate(f8, f15, 0.0f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(10.0f * (f16 * f13)));
        poseStack.translate(-f8, -f15, 0.0f);
        BFRendering.centeredComponent2d(poseStack, font, guiGraphics, field_655, f8, f15, 1.0f + 0.2f * f13);
        poseStack.popPose();
        float f17 = f6 + 10.0f;
        BFRendering.rectangle(poseStack, guiGraphics, f6, this.field_565, f4, 28.0f, 0, 0.4f * f3);
        BFRendering.component2d(poseStack, font, guiGraphics, field_653, f6 + 10.0f, (float)this.field_565 + 5.0f, 0.75f);
        int n5 = 0;
        for (String string : objectList) {
            float f18 = (float)this.field_565 + 14.0f + 5.0f * (float)n5;
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)Component.literal((String)string), f17, f18, 0.5f);
            ++n5;
        }
        guiGraphics.disableScissor();
        poseStack.popPose();
        RenderSystem.disableDepthTest();
        poseStack.popPose();
        poseStack.popPose();
    }

    @Override
    public boolean onPress(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, double d, double d2, int n) {
        if (super.onPress(minecraft, bFClientManager, clientPlayerDataHandler, d, d2, n)) {
            return true;
        }
        if (this.method_557() && ((ClientConnectionManager)bFClientManager.getConnectionManager()).getStatus().isConnected()) {
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.0f));
            ((ClientConnectionManager)bFClientManager.getConnectionManager()).sendPacket((IPacket)new PacketDiscordLinkRequest());
        }
        return false;
    }
}

