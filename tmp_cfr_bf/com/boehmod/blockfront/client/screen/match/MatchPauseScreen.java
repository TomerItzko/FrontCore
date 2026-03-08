/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.match;

import com.boehmod.bflib.cloud.common.PopupType;
import com.boehmod.bflib.cloud.common.player.PlayerGroup;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.DailyChallengesWidget;
import com.boehmod.blockfront.client.gui.widget.PopupButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.prompt.BFPromptScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.BFConfirmPromptScreen;
import com.boehmod.blockfront.client.screen.prompt.text.StaffPunishPromptScreen;
import com.boehmod.blockfront.client.screen.settings.BFModSettingsScreen;
import com.boehmod.blockfront.client.screen.title.BFTitleScreen;
import com.boehmod.blockfront.client.screen.title.sidebar.FriendsSidebarScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.net.packet.BFSwitchTeamPacket;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.tag.ICanSwitchTeams;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.PacketUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public final class MatchPauseScreen
extends BFMenuScreen {
    private static final Component field_1198 = Component.translatable((String)"bf.message.okay");
    private static final Component field_1199 = Component.translatable((String)"bf.cloud.popup.discord.button");
    private static final Component field_1200 = Component.translatable((String)"bf.cloud.popup.discord.button.tip", (Object[])new Object[]{"BlockFront"});
    private static final Component field_1201 = Component.translatable((String)"bf.screen.ingame");
    private static final Component field_1202 = Component.translatable((String)"bf.message.ingame.resume");
    private static final Component field_1203 = Component.translatable((String)"bf.message.ingame.team.switch");
    private static final Component field_1204 = Component.translatable((String)"bf.message.prompt.ingame.switch.teams.title");
    private static final Component field_1205 = Component.translatable((String)"bf.message.prompt.ingame.switch.teams");
    private static final Component field_1206 = Component.translatable((String)"bf.message.ingame.team.switch.tip");
    private static final Component field_1207 = Component.translatable((String)"bf.message.ingame.settings.mod");
    private static final Component field_1208 = Component.translatable((String)"bf.message.ingame.settings.mod.tip", (Object[])new Object[]{"BlockFront"});
    private static final Component field_1186 = Component.translatable((String)"bf.message.ingame.settings.minecraft");
    private static final Component field_1187 = Component.translatable((String)"bf.message.ingame.settings.minecraft.tip");
    private static final Component field_1189 = Component.translatable((String)"bf.message.ingame.friends");
    private static final Component field_1190 = Component.translatable((String)"bf.message.ingame.friends.tip");
    private static final Component field_1191 = Component.translatable((String)"bf.message.prompt.ingame.exit.title");
    private static final Component field_1192 = Component.translatable((String)"bf.message.ingame.exit");
    private static final Component field_1193 = Component.translatable((String)"bf.message.prompt.ingame.exit");
    private static final Component field_1194 = Component.translatable((String)"menu.savingLevel");
    private static final Component field_1195 = Component.literal((String)"#players").withStyle(BFStyles.BLUE);
    private static final Component field_1196 = Component.translatable((String)"bf.screen.report.discord.title");
    private static final Component field_1197 = Component.translatable((String)"bf.screen.report.discord.message", (Object[])new Object[]{field_1195});
    public static final int field_1188 = 15;
    public boolean field_1185 = false;
    @Nullable
    private final Screen field_1209;

    public MatchPauseScreen() {
        this((Screen)null);
    }

    public MatchPauseScreen(@Nullable Screen screen) {
        super(field_1201);
        this.field_1209 = screen;
    }

    @Override
    public void method_758() {
        Object object;
        super.method_758();
        AbstractGame<?, ?, ?> abstractGame = this.manager.getGame();
        int n = this.width / 2;
        int n2 = this.height / 2;
        int n3 = n2 - 60;
        int n4 = 150;
        int n5 = 15;
        MatchPauseScreen matchPauseScreen = this;
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        BFButton bFButton = new BFButton(n - 75, n3, 150, 15, field_1202, button -> {
            this.minecraft.setScreen(this.field_1209);
            this.minecraft.setWindowActive(true);
            this.minecraft.getSoundManager().resume();
        });
        bFButton.alignment(BFButton.Alignment.LEFT);
        bFButton.displayType(BFButton.DisplayType.NONE);
        this.addRenderableWidget((GuiEventListener)bFButton);
        if (abstractGame instanceof ICanSwitchTeams) {
            bFButton = new BFButton(n - 75, n3 + 15, 150, 15, field_1203, button -> {
                BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen((Screen)matchPauseScreen, field_1204, bl -> {
                    if (bl) {
                        PacketUtils.sendToServer(new BFSwitchTeamPacket());
                    }
                    this.minecraft.setWindowActive(true);
                    this.minecraft.getSoundManager().resume();
                    this.minecraft.setScreen(null);
                });
                ((BFPromptScreen)((Object)((Object)((Object)bFConfirmPromptScreen.method_1084(field_1205))))).method_1082();
                this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
            }).tip(field_1206);
            bFButton.alignment(BFButton.Alignment.LEFT);
            bFButton.displayType(BFButton.DisplayType.NONE);
            object = abstractGame.getPlayerStatData(playerCloudData.getUUID());
            int bl = object.getInteger(BFStats.TEAM_SWITCH.getKey());
            bFButton.active = bl <= 0;
            this.addRenderableWidget((GuiEventListener)bFButton);
        }
        bFButton = new BFButton(n - 75, n3 + 30, 150, 15, field_1207, button -> this.minecraft.setScreen((Screen)new BFModSettingsScreen(matchPauseScreen))).tip(field_1208);
        bFButton.alignment(BFButton.Alignment.LEFT);
        bFButton.displayType(BFButton.DisplayType.NONE);
        this.addRenderableWidget((GuiEventListener)bFButton);
        this.addRenderableWidget((GuiEventListener)bFButton);
        bFButton = new BFButton(n - 75, n3 + 45, 150, 15, field_1186, button -> this.minecraft.setScreen((Screen)new OptionsScreen((Screen)matchPauseScreen, this.minecraft.options))).tip(field_1187);
        bFButton.alignment(BFButton.Alignment.LEFT);
        bFButton.displayType(BFButton.DisplayType.NONE);
        this.addRenderableWidget((GuiEventListener)bFButton);
        object = playerCloudData.getGroup();
        boolean bl = ((Optional)object).isPresent() && ((PlayerGroup)((Optional)object).get()).hasPermission("staff.punish");
        MutableComponent mutableComponent = Component.translatable((String)("bf.message.ingame." + (bl ? "punish" : "report"))).withStyle(bl ? ChatFormatting.BLUE : ChatFormatting.RED);
        bFButton = new BFButton(n - 75, n3 + 60, 150, 15, (Component)mutableComponent, button2 -> {
            if (bl) {
                this.minecraft.setScreen((Screen)new StaffPunishPromptScreen(matchPauseScreen));
            } else {
                BFClientManager.showPopup(field_1196, field_1197, PopupType.DISCORD_LINK, new PopupButton(field_1198, null, button -> this.minecraft.setScreen((Screen)this)), new PopupButton(field_1199, field_1200, button -> BFTitleScreen.openUrl(this.minecraft, "https://discord.blockfrontmc.com/")));
            }
        }).tip((Component)Component.translatable((String)("bf.message.ingame." + (bl ? "punish" : "report") + ".tip")));
        bFButton.active = ((ClientConnectionManager)this.manager.getConnectionManager()).getStatus().isConnected();
        bFButton.alignment(BFButton.Alignment.LEFT);
        bFButton.displayType(BFButton.DisplayType.NONE);
        this.addRenderableWidget((GuiEventListener)bFButton);
        BFButton bFButton2 = new BFButton(n - 75, n3 + 90, 150, 15, field_1189, button -> {
            this.minecraft.setScreen((Screen)new FriendsSidebarScreen((Screen)this, false));
            this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_SIDEMENU_OPEN.get()), (float)((float)((double)0.9f + (double)0.1f * Math.random()))));
        }).tip(field_1190);
        bFButton2.alignment(BFButton.Alignment.LEFT);
        bFButton2.displayType(BFButton.DisplayType.NONE);
        bFButton2.method_371(() -> this.manager.getFriendManager().getFriendRequests().size());
        this.addRenderableWidget((GuiEventListener)bFButton2);
        bFButton = new BFButton(n - 75, n3 + 120, 150, 15, (Component)Component.empty(), button -> {
            Object t = new BFConfirmPromptScreen((Screen)matchPauseScreen, field_1191, bl -> {
                if (bl) {
                    ClientLevel clientLevel = this.minecraft.level;
                    if (clientLevel != null) {
                        boolean bl2 = this.minecraft.isLocalServer();
                        clientLevel.disconnect();
                        if (bl2) {
                            this.minecraft.disconnect((Screen)new GenericMessageScreen(field_1194));
                        } else {
                            this.minecraft.disconnect();
                        }
                        this.minecraft.setScreen((Screen)new TitleScreen());
                    }
                } else {
                    this.minecraft.setScreen((Screen)this);
                }
            }).method_1084(field_1193);
            this.minecraft.setScreen(t);
        });
        bFButton.setMessage(field_1192);
        bFButton.alignment(BFButton.Alignment.LEFT);
        bFButton.displayType(BFButton.DisplayType.NONE);
        this.addRenderableWidget((GuiEventListener)bFButton);
    }

    @Override
    public void method_774() {
        super.method_774();
        this.method_764(new DailyChallengesWidget(this.width - 15 - 10, 55, 15, 15, this));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        if (this.field_1209 != null) {
            this.field_1209.render(guiGraphics, n, n2, f);
            poseStack.translate(0.0f, 0.0f, 100.0f);
        }
        super.render(guiGraphics, n, n2, f);
        if (MatchPauseScreen.method_769(this.minecraft)) {
            if (this.field_1185) {
                this.minecraft.setScreen(this.field_1209);
            }
        } else {
            this.field_1185 = true;
        }
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        BFRendering.centeredTexture(poseStack, guiGraphics, LOGO, n3, n4 - 82, 150, 25);
        poseStack.popPose();
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        if (this.field_1209 != null) {
            this.field_1209.renderBackground(guiGraphics, n, n2, f);
        } else {
            super.renderBackground(guiGraphics, n, n2, f);
        }
        int n3 = 145;
        int n4 = this.width / 2;
        int n5 = this.height / 2;
        int n6 = n5 - 70;
        int n7 = 150;
        int n8 = n4 - 75 - 5;
        int n9 = n6 + 5;
        int n10 = 160;
        int n11 = BFRendering.translucentBlack();
        BFRendering.rectangle(guiGraphics, n8 + 1, n9 - 1, 158, 1, n11);
        BFRendering.rectangle(guiGraphics, n8, n9, 160, 145, n11);
        BFRendering.rectangle(guiGraphics, n8 + 1, n9 + 145, 158, 1, n11);
        poseStack.popPose();
    }
}

