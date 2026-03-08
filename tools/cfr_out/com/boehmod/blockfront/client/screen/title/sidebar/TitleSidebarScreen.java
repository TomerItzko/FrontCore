/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.PopupType
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.options.OptionsScreen
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.title.sidebar;

import com.boehmod.bflib.cloud.common.PopupType;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.PopupButton;
import com.boehmod.blockfront.client.net.ConnectionMode;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFCreditsScreen;
import com.boehmod.blockfront.client.screen.SidebarScreen;
import com.boehmod.blockfront.client.screen.intro.ModeIntroScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.BFConfirmPromptScreen;
import com.boehmod.blockfront.client.screen.prompt.text.StaffPunishPromptScreen;
import com.boehmod.blockfront.client.screen.settings.BFModSettingsScreen;
import com.boehmod.blockfront.client.screen.title.BFTitleScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_211;
import com.boehmod.blockfront.util.BFStyles;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class TitleSidebarScreen
extends SidebarScreen {
    private static final Component field_1102 = Component.translatable((String)"bf.message.okay");
    private static final Component field_1103 = Component.translatable((String)"bf.cloud.popup.discord.button");
    private static final Component field_1104 = Component.translatable((String)"bf.cloud.popup.discord.button.tip", (Object[])new Object[]{"BlockFront"});
    private static final Component field_1105 = Component.translatable((String)"bf.dropdown.text.my.options").withStyle(BFStyles.BOLD);
    private static final Component field_1106 = Component.translatable((String)"bf.screen.overlay.settings");
    private static final Component field_1107 = Component.translatable((String)"bf.menu.button.modsettings").withStyle(BFStyles.BOLD);
    private static final Component field_1108 = Component.translatable((String)"bf.menu.button.modsettings.tip", (Object[])new Object[]{"BlockFront"});
    private static final Component field_1109 = Component.translatable((String)"bf.menu.button.minecraftsettings").withStyle(BFStyles.BOLD);
    private static final Component field_1110 = Component.translatable((String)"bf.menu.button.minecraftsettings.tip");
    private static final Component field_1111 = Component.translatable((String)"bf.menu.button.nav.text.staff.punish").withStyle(BFStyles.BOLD).withStyle(ChatFormatting.BLUE);
    private static final Component field_1112 = Component.translatable((String)"bf.message.ingame.punish.tip");
    private static final Component field_1113 = Component.literal((String)"#players").withStyle(BFStyles.BLUE);
    private static final Component field_1114 = Component.translatable((String)"bf.screen.report.discord.message", (Object[])new Object[]{field_1113});
    private static final Component field_1115 = Component.translatable((String)"bf.screen.report.discord.title");
    private static final Component field_1116 = Component.translatable((String)"bf.menu.button.report.player.tip");
    private static final Component field_1117 = Component.translatable((String)"bf.menu.button.support").withStyle(BFStyles.BOLD);
    private static final Component field_1118 = Component.translatable((String)"bf.menu.button.support.tip");
    private static final Component field_1119 = Component.translatable((String)"bf.menu.button.credits").withStyle(BFStyles.BOLD);
    private static final Component field_1120 = Component.translatable((String)"bf.menu.button.credits.tip");
    private static final Component field_1121 = Component.translatable((String)"bf.menu.button.rules").withStyle(BFStyles.BOLD);
    private static final Component field_1122 = Component.translatable((String)"bf.menu.button.rules.tip");
    private static final Component field_1123 = Component.translatable((String)"bf.menu.button.discord").withStyle(BFStyles.BOLD);
    private static final Component field_1124 = Component.translatable((String)"bf.menu.button.discord.tip").withColor(13721420).withColor(5793266);
    private static final Component field_1125 = Component.translatable((String)"bf.menu.button.exit").withStyle(BFStyles.BOLD);
    private static final Component field_1126 = Component.translatable((String)"bf.menu.button.exit.tip").withColor(13721420);
    private static final Component field_1127 = Component.translatable((String)"bf.menu.button.exit.prompt");
    private static final Component field_1128 = Component.translatable((String)"bf.menu.button.exit.prompt.title");
    private static final Component field_1129 = Component.translatable((String)"bf.menu.button.report.player").withStyle(BFStyles.BOLD);
    private static final Component field_1130 = Component.translatable((String)"bf.menu.button.play.online").withStyle(BFStyles.BOLD);
    private static final Component field_1131 = Component.translatable((String)"bf.menu.button.play.online.tip", (Object[])new Object[]{"BlockFront"}).withColor(ColorReferences.COLOR_TEAM_ALLIES_SOLID);
    private static final Component field_1132 = Component.translatable((String)"bf.menu.button.play.offline").withStyle(BFStyles.BOLD);
    private static final Component field_1133 = Component.translatable((String)"bf.menu.button.play.offline.tip", (Object[])new Object[]{"BlockFront"}).withColor(ColorReferences.COLOR_TEAM_ALLIES_SOLID);

    public TitleSidebarScreen(Screen screen, boolean bl) {
        super(screen, field_1106, bl);
    }

    @Override
    public void method_754(@NotNull GuiGraphics guiGraphics, float f) {
        super.method_754(guiGraphics, f);
        int n = this.width / 4;
        int n2 = this.width - n;
        int n3 = 20;
        int n4 = 18;
        PoseStack poseStack = guiGraphics.pose();
        BFRendering.rectangleWithDarkShadow(guiGraphics, n2, 0, n, this.height, BFRendering.translucentBlack());
        BFRendering.method_197(this.playerDataHandler, guiGraphics, this.font, this.minecraft.getUser().getProfileId(), BF_211.BF_213.FRIEND, n2, 12, 20, false);
        BFRendering.playerHead(this.minecraft, this.manager, poseStack, guiGraphics, this.minecraft.getGameProfile(), n2 + (n - 18) - 4, 14.0f, 18);
        BFRendering.centeredString(this.font, guiGraphics, field_1105, n2 + n / 2, 4);
    }

    @Override
    public boolean method_753(double d, double d2) {
        return d <= (double)this.width * 0.75;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void method_758() {
        super.method_758();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        int n = this.width / 4;
        int n2 = this.width - n;
        int n3 = n - 4;
        int n4 = 40;
        int n5 = 18;
        int n6 = 20;
        TitleSidebarScreen titleSidebarScreen = this;
        this.addRenderableWidget((GuiEventListener)new BFButton(n2 + 4, n4, n3, 18, field_1107, button -> this.minecraft.setScreen((Screen)new BFModSettingsScreen(titleSidebarScreen))).tip(field_1108).alignment(BFButton.Alignment.LEFT));
        this.addRenderableWidget((GuiEventListener)new BFButton(n2 + 4, n4 += 20, n3, 18, field_1109, button -> this.minecraft.setScreen((Screen)new OptionsScreen((Screen)titleSidebarScreen, this.minecraft.options))).tip(field_1110).alignment(BFButton.Alignment.LEFT));
        if (playerCloudData.hasPermission("staff.punish")) {
            this.addRenderableWidget((GuiEventListener)new BFButton(n2 + 4, n4 += 20, n3, 18, field_1111, button -> this.minecraft.setScreen((Screen)new StaffPunishPromptScreen(titleSidebarScreen))).tip(field_1112).alignment(BFButton.Alignment.LEFT));
        } else {
            this.addRenderableWidget((GuiEventListener)new BFButton(n2 + 4, n4 += 20, n3, 18, field_1129, button2 -> BFClientManager.showPopup(field_1115, field_1114, PopupType.DISCORD_LINK, new PopupButton(field_1102, null, button -> this.minecraft.setScreen((Screen)this)), new PopupButton(field_1103, field_1104, button -> BFTitleScreen.openUrl(this.minecraft, "https://discord.blockfrontmc.com/")))).method_369(13721420).method_390(0xFFFFFF).tip(field_1116).alignment(BFButton.Alignment.LEFT));
        }
        this.addRenderableWidget((GuiEventListener)new BFButton(n2 + 4, n4 += 20, n3, 18, field_1117, button -> {}).tip(field_1118).alignment(BFButton.Alignment.LEFT));
        this.addRenderableWidget((GuiEventListener)new BFButton(n2 + 4, n4 += 20, n3, 18, field_1119, button -> this.minecraft.setScreen((Screen)new BFCreditsScreen())).tip(field_1120).alignment(BFButton.Alignment.LEFT));
        this.addRenderableWidget((GuiEventListener)new BFButton(n2 + 4, n4 += 20, n3, 18, field_1121, button -> BFTitleScreen.openUrl(this.minecraft, "https://www.blockfrontmc.com/rules")).tip(field_1122).alignment(BFButton.Alignment.LEFT));
        ConnectionMode connectionMode = ((ClientConnectionManager)this.manager.getConnectionManager()).getConnectionMode();
        if (connectionMode == ConnectionMode.OFFLINE) {
            this.addRenderableWidget((GuiEventListener)new BFButton(n2 + 4, n4 += 20, n3, 18, field_1130, button -> {
                ((ClientConnectionManager)this.manager.getConnectionManager()).setConnectionMode(ConnectionMode.UNDECIDED);
                this.minecraft.setScreen((Screen)new ModeIntroScreen());
            }).method_395(ColorReferences.COLOR_TEAM_ALLIES_SOLID).method_397(ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID).method_390(0xFFFFFF).tip(field_1131).alignment(BFButton.Alignment.LEFT));
        }
        if (connectionMode == ConnectionMode.ONLINE) {
            this.addRenderableWidget((GuiEventListener)new BFButton(n2 + 4, n4 += 20, n3, 18, field_1132, button -> {
                ((ClientConnectionManager)this.manager.getConnectionManager()).setConnectionMode(ConnectionMode.UNDECIDED);
                ((ClientConnectionManager)this.manager.getConnectionManager()).disconnect("User requested disconnect", false);
                this.minecraft.setScreen((Screen)new ModeIntroScreen());
            }).method_395(ColorReferences.COLOR_TEAM_ALLIES_SOLID).method_397(ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID).method_390(0xFFFFFF).tip(field_1133).alignment(BFButton.Alignment.LEFT));
        }
        this.addRenderableWidget((GuiEventListener)new BFButton(n2 + 4, n4 += 20, n3, 18, field_1123, button -> BFTitleScreen.openUrl(this.minecraft, "https://discord.blockfrontmc.com/")).method_395(ColorReferences.COLOR_THEME_DISCORD_SOLID).method_397(ColorReferences.COLOR_THEME_DISCORD_HOVERED_SOLID).method_390(0xFFFFFF).tip(field_1124).alignment(BFButton.Alignment.LEFT));
        this.addRenderableWidget((GuiEventListener)new BFButton(n2 + 4, n4 + 20, n3, 18, field_1125, button -> {
            BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen((Screen)titleSidebarScreen, field_1128, bl -> {
                if (bl) {
                    this.minecraft.stop();
                }
            });
            bFConfirmPromptScreen.method_1084(field_1127);
            this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
        }).method_395(ColorReferences.COLOR_THEME_RED_SOLID).method_397(ColorReferences.COLOR_THEME_RED_HOVERED_SOLID).method_369(0xFFFFFF).method_390(0xFFFFFF).tip(field_1126).alignment(BFButton.Alignment.LEFT));
    }
}

