/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.bflib.cloud.common.discord.DiscordEvent;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFPopoutWidget;
import com.boehmod.blockfront.client.net.HttpTextureManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.FormatUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class EventsWidget
extends BFPopoutWidget {
    private static final SimpleDateFormat field_664 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ROOT);
    private static final int field_672 = 100;
    private static final ResourceLocation field_665 = BFRes.loc("textures/gui/menu/icons/friends_black.png");
    private static final ResourceLocation field_666 = BFRes.loc("textures/gui/menu/icons/events.png");
    private static final ResourceLocation field_667 = BFRes.loc("textures/gui/menu/icons/alarm.png");
    private static final ResourceLocation field_668 = BFRes.loc("textures/gui/effects/fade_green.png");
    private static final Component field_662 = Component.translatable((String)"bf.menu.events.title");
    private static final Component field_663 = Component.translatable((String)"bf.menu.events.description");
    private int field_669 = 0;
    private int field_670;
    private int field_671 = 100;

    public EventsWidget(int n, int n2, int n3, int n4, Screen screen) {
        super(n, n2, n3, n4, screen);
    }

    @Override
    public void method_537(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, float f) {
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)bFClientManager.getConnectionManager();
        ObjectList<DiscordEvent> objectList = clientConnectionManager.method_1387();
        if (objectList.isEmpty()) {
            this.field_669 = 0;
            this.field_670 = 0;
            return;
        }
        super.method_537(minecraft, bFClientManager, clientPlayerDataHandler, f);
        this.field_671 = this.field_670;
        if (this.field_670-- <= 0) {
            this.field_671 = this.field_670 = 100;
            ++this.field_669;
            if (this.field_669 >= objectList.size()) {
                this.field_669 = 0;
            }
        }
    }

    @Override
    public boolean method_603(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        return !((ClientConnectionManager)bFClientManager.getConnectionManager()).method_1387().isEmpty();
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)bFClientManager.getConnectionManager();
        ObjectList<DiscordEvent> objectList = clientConnectionManager.method_1387();
        if (objectList.isEmpty() || this.field_669 >= objectList.size()) {
            return;
        }
        float f8 = this.method_604(f2);
        int n3 = ColorReferences.COLOR_BLACK_SOLID + ChatFormatting.GREEN.getColor();
        boolean bl = clientConnectionManager.getStatus().isConnected();
        BFRendering.rectangle(guiGraphics, 0, 0, this.getScreen().width, this.getScreen().height, 0, f8 * 0.85f);
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 10.0f);
        float f9 = 220.0f;
        f9 *= 0.5f;
        float f10 = (f9 += 13.0f) * f8;
        float f11 = (float)this.field_564 - f10;
        ObjectList<String> objectList2 = FormatUtils.parseMarkup(font, field_663.getString(), (int)(f9 * 1.8f));
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, bl ? n3 : 0x77888888);
        int n4 = 8;
        if (bl) {
            BFRendering.texture(poseStack, guiGraphics, field_668, this.field_564 - 8, this.field_565 - 8, this.field_566 + 16, this.height + 16);
        }
        BFRendering.texture(poseStack, guiGraphics, field_666, this.field_564, this.field_565, this.field_566, this.height);
        BFRendering.enableScissor(guiGraphics, (int)f11 + 1, this.field_565, (int)f10, (int)f10);
        BFRendering.rectangle(poseStack, guiGraphics, f11, (float)this.field_565, f10, f10, n3);
        poseStack.pushPose();
        float f12 = f11 + 10.0f;
        float f13 = MathUtils.lerpf1(this.field_670, this.field_671, f2);
        float f14 = 1.0f - f13 / 100.0f;
        BFRendering.rectangle(poseStack, guiGraphics, f11, this.field_565, f9, 28.0f, 0, 0.4f * f8);
        BFRendering.rectangle(poseStack, guiGraphics, f11, this.field_565, f9 * f14, 28.0f, ColorReferences.COLOR_BLACK_SOLID, 0.4f * f14);
        BFRendering.component2d(poseStack, font, guiGraphics, (Component)field_662.copy().withStyle(BFStyles.BOLD), f11 + 10.0f, (float)this.field_565 + 5.0f, 0.75f);
        int n5 = 0;
        for (String string : objectList2) {
            f7 = (float)this.field_565 + 14.0f + 5.0f * (float)n5;
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)Component.literal((String)string), f12, f7, 0.5f);
            ++n5;
        }
        ObjectListIterator objectListIterator = (DiscordEvent)objectList.get(this.field_669);
        n5 = 0;
        if (objectListIterator != null) {
            float f15;
            int n6;
            float f16 = 0.5f;
            f7 = 5.0f;
            f6 = f11 + 5.0f;
            f5 = (float)(this.field_565 + 29) + 5.0f;
            f4 = f9 - 10.0f;
            f3 = 40.0f;
            BFRendering.rectangle(poseStack, guiGraphics, f6 - 1.0f, f5 - 1.0f, f4 + 2.0f, 42.0f, BFRendering.translucentBlack());
            BFRendering.texture(poseStack, guiGraphics, HttpTextureManager.getOrLoad(minecraft, objectListIterator.imageURL), f6, f5, f4, 40.0f, 0.5f, 1.0f);
            BFRendering.rectangle(poseStack, guiGraphics, f6, f5, f4, 15.0f, BFRendering.translucentBlack());
            ObjectList<String> objectList3 = FormatUtils.parseMarkup(font, objectListIterator.description, (int)(f9 * 1.8f));
            float f17 = objectListIterator.title.length() > 22 ? 0.65f : 1.0f;
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)Component.literal((String)objectListIterator.title).withStyle(BFStyles.BOLD), f11 + f9 / 2.0f, (float)(this.field_565 + 38), f17);
            float f18 = (float)this.field_565 + f9 - 45.0f;
            int n7 = objectList3.size();
            for (n6 = 0; n6 < n7 && n6 < 3; ++n6) {
                f15 = f18 + 5.0f * (float)n5;
                BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)Component.literal((String)((String)objectList3.get(n6))).withColor(0), f11 + f9 / 2.0f, f15, 0.5f);
                ++n5;
            }
            n6 = 10;
            float f19 = f11 + 5.0f;
            f15 = (float)this.field_565 + f9 - 5.0f - 10.0f - 4.0f;
            String string = StringUtils.formatLong(objectListIterator.interestedCount);
            MutableComponent mutableComponent = (objectListIterator.interestedCount == -1 ? Component.literal((String)"Unknown") : Component.literal((String)(string + " Interested"))).withStyle(ChatFormatting.BOLD);
            BFRendering.texture(poseStack, guiGraphics, field_665, f19, f15 - 3.0f, 10.0f, 10.0f);
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent, f19 + 10.0f + 3.0f, f15, ColorReferences.COLOR_BLACK_SOLID, 0.5f);
            long l = objectListIterator.time * 1000L;
            Date date = new Date(l);
            MutableComponent mutableComponent2 = Component.literal((String)field_664.format(date)).withStyle(ChatFormatting.BOLD);
            BFRendering.texture(poseStack, guiGraphics, field_667, f19, f15 + 7.0f, 10.0f, 10.0f);
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent2, f19 + 10.0f + 3.0f, f15 + 10.0f, ColorReferences.COLOR_BLACK_SOLID, 0.5f);
        }
        guiGraphics.disableScissor();
        int n8 = 8;
        int n9 = 4;
        f6 = f11 - 4.0f;
        f5 = this.field_565 - 4;
        f4 = Mth.sin((float)(f / 10.0f));
        f3 = 8.0f * (2.75f * f4);
        BFRendering.centeredTexture(poseStack, guiGraphics, field_668, f6 + 4.0f, f5 + 4.0f, f3, f3, 0.0f, f4);
        BFRendering.rectangle(poseStack, guiGraphics, f6, f5, 8.0f, 8.0f, n3);
        BFRendering.rectangle(poseStack, guiGraphics, f6, f5, 8.0f, 8.0f, ColorReferences.COLOR_WHITE_SOLID, f4);
        BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)Component.literal((String)String.valueOf(objectList.size())).withStyle(ChatFormatting.BOLD).withColor(0), f11, (float)(this.field_565 - 2), 0.5f);
        poseStack.popPose();
        poseStack.popPose();
    }

    @Override
    public boolean onPress(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, double d, double d2, int n) {
        if (super.onPress(minecraft, bFClientManager, clientPlayerDataHandler, d, d2, n)) {
            return true;
        }
        if (this.method_557()) {
            BFMenuScreen.openUrl(minecraft, "https://discord.blockfrontmc.com/");
        }
        return false;
    }
}

