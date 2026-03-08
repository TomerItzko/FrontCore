/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.ReportType
 *  com.boehmod.bflib.cloud.common.RequestType
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.PacketPunishPlayer
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.staff;

import com.boehmod.bflib.cloud.common.ReportType;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.PacketPunishPlayer;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.BFConfirmPromptScreen;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class PunishPlayerScreen
extends BFMenuScreen {
    public static final Component field_1295 = Component.translatable((String)"screen.staff.punish");
    public static final Component field_1296 = Component.translatable((String)"bf.menu.punish.reason.title");
    public static final Component field_1297 = Component.translatable((String)"bf.menu.punish.reason.tip").withStyle(ChatFormatting.GRAY);
    public static final Component field_1298 = Component.translatable((String)"bf.menu.button.back");
    public static final Component field_1299 = Component.translatable((String)"bf.menu.punish.prompt.title");
    public static final int field_1293 = 310;
    public static final int field_1294 = 100;
    @NotNull
    private final UUID field_1300;
    private ReportType field_1292;
    private BFButton field_1301;

    public PunishPlayerScreen(@NotNull Component component, @NotNull UUID uUID) {
        super(component, field_1295);
        this.field_1300 = uUID;
        ((ClientConnectionManager)this.manager.getConnectionManager()).getRequester().push(RequestType.PLAYER_DATA, this.field_1300);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        int n5 = n4 - 36;
        BFRendering.rectangleWithDarkShadow(guiGraphics, n3 - 155, n5 - 10, 310, 100, 0, 0.5f);
        super.render(guiGraphics, n, n2, f);
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudProfile(this.field_1300);
        int n6 = n3 - 68;
        int n7 = n5 - 35;
        int n8 = 18;
        BFRendering.playerHead(this.minecraft, this.manager, poseStack, guiGraphics, playerCloudData.getMcProfile(), n6, n7, 18);
        BFRendering.drawString(this.font, guiGraphics, (Component)Component.literal((String)playerCloudData.getUsername()), n6 + 18 + 4, n7 + 1);
        BFRendering.component2d(poseStack, this.font, guiGraphics, (Component)Component.literal((String)this.field_1300.toString()).withStyle(ChatFormatting.GRAY), n6 + 18 + 4, n7 + 12, 0.5f);
        BFRendering.centeredString(this.font, guiGraphics, field_1296, n3, n5);
        BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, field_1297, n3, n5 + 12, 0.5f);
        if (this.field_1301 != null) {
            boolean bl = this.field_1301.active = this.field_1292 != null;
        }
        if (this.field_1292 != null) {
            for (Renderable renderable : this.renderables) {
                if (!(renderable instanceof BFButton)) continue;
                BFButton bFButton = (BFButton)renderable;
                int n9 = bFButton.getX();
                int n10 = bFButton.getY();
                int n11 = bFButton.getWidth();
                int n12 = bFButton.getHeight();
                if (!bFButton.getMessage().getString().equals(this.field_1292.getTitle())) continue;
                BFRendering.rectangle(guiGraphics, n9 - 1, n10 - 1, n11 + 2, 1, ColorReferences.COLOR_WHITE_SOLID);
                BFRendering.rectangle(guiGraphics, n9 - 1, n10 - 1, 1, n12 + 2, ColorReferences.COLOR_WHITE_SOLID);
                BFRendering.rectangle(guiGraphics, n9 - 1, n10 + n12, n11 + 2, 1, ColorReferences.COLOR_WHITE_SOLID);
                BFRendering.rectangle(guiGraphics, n9 + n11, n10 - 1, 1, n12 + 2, ColorReferences.COLOR_WHITE_SOLID);
            }
        }
    }

    @Override
    public void method_758() {
        super.method_758();
        int n = this.width / 2;
        int n2 = this.height / 2;
        int n3 = n2 - 34;
        int n4 = n3 + 22;
        int n5 = 0;
        int n6 = 5;
        int n7 = 30;
        if (this.minecraft.level == null) {
            this.addRenderableWidget((GuiEventListener)new BFButton(5, 18, 20, 20, (Component)Component.empty(), button -> this.minecraft.setScreen((Screen)new LobbyTitleScreen())).texture(RETURN_ICON).size(20, 20).displayType(BFButton.DisplayType.NONE).tip(field_1298));
        }
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (Object object : ReportType.values()) {
            objectArrayList.add(new BFButton(0, n4, 30, 30, (Component)Component.literal((String)object.getTitle()), arg_0 -> this.method_939((ReportType)object, arg_0)).tip((Component)Component.literal((String)object.getDescription())).method_389(0.0f).method_392(BFRes.loc("textures/gui/reports/" + object.ordinal() + ".png")));
        }
        for (BFButton bFButton : objectArrayList) {
            n5 += bFButton.getWidth() + 5;
        }
        int n8 = n - n5 / 2;
        int n9 = 0;
        for (Object object : objectArrayList) {
            object.setX(n8 + n9);
            this.addRenderableWidget((GuiEventListener)object);
            n9 += object.getWidth() + 5;
        }
        int n10 = n4 + 30 + 5;
        int n11 = 250;
        int n12 = 20;
        PunishPlayerScreen punishPlayerScreen = this;
        this.field_1301 = new BFButton(n - 125, n10, 250, 20, (Component)Component.translatable((String)"bf.menu.button.report.confirm"), button -> {
            PlayerCloudData playerCloudData = this.playerDataHandler.getCloudProfile(this.field_1300);
            BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen((Screen)punishPlayerScreen, field_1299, bl -> {
                if (bl) {
                    ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketPunishPlayer(this.field_1300, this.field_1292));
                }
                this.minecraft.setScreen(null);
            });
            MutableComponent mutableComponent = Component.literal((String)playerCloudData.getUsername()).withStyle(ChatFormatting.GRAY);
            MutableComponent mutableComponent2 = Component.literal((String)this.field_1292.getTitle()).withStyle(ChatFormatting.GRAY);
            MutableComponent mutableComponent3 = Component.translatable((String)"bf.menu.punish.prompt", (Object[])new Object[]{mutableComponent, mutableComponent2});
            bFConfirmPromptScreen.method_1084((Component)mutableComponent3);
            this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
        });
        this.addRenderableWidget((GuiEventListener)this.field_1301);
    }

    private /* synthetic */ void method_939(ReportType reportType, Button button) {
        this.field_1292 = reportType;
    }
}

