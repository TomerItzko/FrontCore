/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.AbstractClanData
 *  com.boehmod.bflib.cloud.common.RequestType
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.discord.PacketDiscordUnlinkRequest
 *  com.boehmod.bflib.cloud.packet.common.friend.PacketFriendRemove
 *  com.boehmod.bflib.cloud.packet.common.friend.PacketFriendRequest
 *  com.boehmod.bflib.cloud.packet.common.patreon.PacketPatreonLinkRequest
 *  com.boehmod.bflib.cloud.packet.common.patreon.PacketPatreonUnlinkRequest
 *  com.boehmod.bflib.cloud.packet.common.profile.PacketCheckPrestige
 *  com.boehmod.bflib.common.ColorReferences
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.profile;

import com.boehmod.bflib.cloud.common.AbstractClanData;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.discord.PacketDiscordUnlinkRequest;
import com.boehmod.bflib.cloud.packet.common.friend.PacketFriendRemove;
import com.boehmod.bflib.cloud.packet.common.friend.PacketFriendRequest;
import com.boehmod.bflib.cloud.packet.common.patreon.PacketPatreonLinkRequest;
import com.boehmod.bflib.cloud.packet.common.patreon.PacketPatreonUnlinkRequest;
import com.boehmod.bflib.cloud.packet.common.profile.PacketCheckPrestige;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.player.ClientFriendManager;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.SidebarScreen;
import com.boehmod.blockfront.client.screen.profile.ClanScreen;
import com.boehmod.blockfront.client.screen.profile.ProfileAchievementsScreen;
import com.boehmod.blockfront.client.screen.profile.ProfileMainScreen;
import com.boehmod.blockfront.client.screen.profile.ProfileStatsScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.BFConfirmPromptScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.PrestigePromptScreen;
import com.boehmod.blockfront.client.screen.prompt.text.MoodPromptScreen;
import com.boehmod.blockfront.client.screen.title.sidebar.TitleSidebarScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ProfileScreen
extends BFMenuScreen {
    private static final Component field_1477 = Component.translatable((String)"bf.message.prestige").withStyle(ChatFormatting.BOLD);
    private static final Component field_1478 = Component.translatable((String)"bf.message.clan").withStyle(ChatFormatting.BOLD);
    private static final Component field_1479 = Component.translatable((String)"bf.message.profile.base.clan.tip");
    private static final Component field_1480 = Component.translatable((String)"bf.message.mood").withStyle(ChatFormatting.BOLD);
    private static final Component field_1481 = Component.translatable((String)"bf.message.add.friend").withStyle(ChatFormatting.BOLD);
    private static final Component field_1482 = Component.translatable((String)"bf.message.remove.friend").withStyle(ChatFormatting.BOLD);
    private static final Component field_1483 = Component.translatable((String)"bf.message.profile.base.mood.tip");
    private static final Component field_1484 = Component.translatable((String)"bf.message.unlink").withStyle(ChatFormatting.BOLD);
    private static final Component field_1485 = Component.translatable((String)"bf.message.profile.base.unlink.tip");
    private static final Component field_6969 = Component.translatable((String)"bf.message.patreon.link").withStyle(ChatFormatting.BOLD);
    private static final Component field_6970 = Component.translatable((String)"bf.message.profile.base.patreon.link.tip");
    private static final Component field_6971 = Component.translatable((String)"bf.message.patreon.unlink").withStyle(ChatFormatting.BOLD);
    private static final Component field_6972 = Component.translatable((String)"bf.message.profile.base.patreon.unlink.tip");
    private static final Component field_1486 = Component.translatable((String)"bf.message.profile.base.unlink.title");
    private static final Component field_1487 = Component.translatable((String)"bf.message.profile.base.unlink");
    private static final Component field_1488 = Component.translatable((String)"bf.message.profile.base.add.friend.tip");
    private static final Component field_1489 = Component.translatable((String)"bf.message.profile.base.remove.friend.tip");
    private static final Component field_1490 = Component.translatable((String)"bf.message.prompt.friend.remove.title");
    private static final Component field_1491 = Component.translatable((String)"bf.message.prompt.friend.remove");
    private static final Component field_1492 = Component.translatable((String)"bf.message.prompt.friend.add.title");
    private static final Component field_1493 = Component.translatable((String)"bf.message.prompt.friend.add");
    private static final Component field_6967 = Component.translatable((String)"bf.message.profile.base.patreon.unlink.title");
    private static final Component field_6968 = Component.translatable((String)"bf.message.profile.base.patreon.unlink");
    private static final Component field_1467 = Component.translatable((String)"bf.message.profile.base.prestige.title").setStyle(Style.EMPTY.withColor(ColorReferences.COLOR_THEME_PRESTIGE_SOLID));
    private static final Component field_1468 = Component.translatable((String)"bf.message.profile.base.prestige.tip");
    private static final Component field_1469 = Component.translatable((String)"bf.menu.button.back");
    private static final Component field_1470 = Component.translatable((String)"bf.dropdown.button.settings");
    private static final Component field_1471 = Component.translatable((String)"bf.message.main").withStyle(ChatFormatting.BOLD);
    private static final Component field_1472 = Component.translatable((String)"bf.message.profile.base.main.tip");
    private static final Component field_1473 = Component.translatable((String)"bf.message.statistics").withStyle(ChatFormatting.BOLD);
    private static final Component field_1474 = Component.translatable((String)"bf.message.profile.base.stats.tip");
    private static final Component field_1475 = Component.translatable((String)"bf.message.profile.base.achievements").withStyle(ChatFormatting.BOLD);
    private static final Component field_1476 = Component.translatable((String)"bf.message.profile.base.achievements.tip");
    private static final int field_1465 = 30;
    private static final ResourceLocation field_1454 = BFRes.loc("textures/gui/menu/icons/menu.png");
    private static final ResourceLocation field_1456 = BFRes.loc("textures/gui/menu/icons/addfriend.png");
    private static final ResourceLocation field_1457 = BFRes.loc("textures/gui/menu/icons/removefriend.png");
    private static final ResourceLocation field_1458 = BFRes.loc("textures/gui/menu/icons/profile_main.png");
    private static final ResourceLocation field_1459 = BFRes.loc("textures/gui/menu/icons/profile_stats.png");
    private static final ResourceLocation field_1460 = BFRes.loc("textures/gui/menu/icons/profile_achievements.png");
    private static final ResourceLocation field_1461 = BFRes.loc("textures/gui/menu/icons/profile_prestige.png");
    private static final ResourceLocation field_1462 = BFRes.loc("textures/gui/menu/icons/profile_clan.png");
    private static final ResourceLocation field_1463 = BFRes.loc("textures/gui/menu/icons/profile_mood.png");
    private static final ResourceLocation field_1464 = BFRes.loc("textures/gui/menu/icons/profile_unlink.png");
    private static final ResourceLocation field_6965 = BFRes.loc("textures/gui/menu/icons/profile_patreon_link.png");
    private static final ResourceLocation field_6966 = BFRes.loc("textures/gui/menu/icons/profile_patreon_unlink.png");
    @NotNull
    protected final Screen field_1494;
    public PlayerCloudData playerCloudData;
    private int field_1466 = 15;
    private final boolean field_1455;

    public ProfileScreen(@NotNull Screen screen, @NotNull PlayerCloudData playerCloudData, @NotNull Component component) {
        super(component);
        this.field_1494 = screen;
        this.playerCloudData = playerCloudData;
        this.field_1455 = playerCloudData.getUUID().equals(this.minecraft.getUser().getProfileId());
    }

    public boolean method_1063() {
        return this.field_1455;
    }

    public boolean method_1064() {
        AbstractClanData abstractClanData = this.playerCloudData.getClanData();
        if (abstractClanData == null || abstractClanData.isEmpty()) {
            return false;
        }
        return abstractClanData.getMembers().contains(this.minecraft.getUser().getProfileId());
    }

    @Override
    public void init() {
        super.init();
        int n = this.width / 2;
        int n2 = 40;
        int n3 = 2;
        int n4 = 15;
        int n5 = 0;
        boolean bl = this.minecraft.getUser().getProfileId().equals(this.playerCloudData.getUUID());
        int n6 = 9;
        int n7 = -2;
        int n8 = 4;
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(new BFButton(n, n2, 0, n4, field_1471, button -> this.minecraft.setScreen((Screen)new ProfileMainScreen(this.field_1494, this.playerCloudData))).texture(field_1458).size(9, 9).method_391(0, -2).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE).tip(field_1472));
        objectArrayList.add(new BFButton(n, n2, 0, n4, field_1473, button -> this.minecraft.setScreen((Screen)new ProfileStatsScreen(this.field_1494, this.playerCloudData))).texture(field_1459).size(9, 9).method_391(0, -2).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE).tip(field_1474));
        objectArrayList.add(new BFButton(n, n2, 0, n4, field_1475, button -> this.minecraft.setScreen((Screen)new ProfileAchievementsScreen(this.field_1494, this.playerCloudData))).texture(field_1460).size(9, 9).method_391(0, -2).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE).tip(field_1476));
        objectArrayList.add(new BFButton(n, n2, 0, n4, field_1478, button -> this.minecraft.setScreen((Screen)new ClanScreen(this.field_1494, this.playerCloudData))).texture(field_1462).size(9, 9).method_391(0, -2).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE).tip(field_1479));
        if (bl) {
            objectArrayList.add(new BFButton(n, n2, 0, n4, field_1477, button -> {
                PrestigePromptScreen prestigePromptScreen = new PrestigePromptScreen((Screen)this, field_1467, bl -> {
                    if (bl) {
                        BFLog.log("Attempting to send prestige request to cloud...", new Object[0]);
                        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
                        clientConnectionManager.sendPacket((IPacket)new PacketCheckPrestige());
                    }
                });
                int n = this.playerCloudData.getPrestigeLevel() + 1;
                MutableComponent mutableComponent = Component.literal((String)String.valueOf(n)).withColor(ColorReferences.COLOR_THEME_PRESTIGE_SOLID);
                MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.profile.base.prestige.par1", (Object[])new Object[]{mutableComponent});
                MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.profile.base.prestige.par2").withStyle(ChatFormatting.DARK_GRAY);
                prestigePromptScreen.method_1085(new Component[]{Component.empty(), mutableComponent2, Component.empty(), mutableComponent3});
                this.minecraft.setScreen((Screen)prestigePromptScreen);
            }).enabled(bFButton -> this.playerCloudData.canPrestige()).texture(field_1461).size(9, 9).method_391(0, -2).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).tip(field_1468).displayType(BFButton.DisplayType.NONE));
            objectArrayList.add(new BFButton(n, n2, 0, n4, field_1480, button -> this.minecraft.setScreen((Screen)new MoodPromptScreen(this))).texture(field_1463).size(9, 9).method_391(0, -2).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE).tip(field_1483));
            if (this.playerCloudData.method_5636()) {
                objectArrayList.add(new BFButton(n, n2, 0, n4, field_1484, button -> {
                    BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen((Screen)this, field_1486, bl -> {
                        if (bl) {
                            BFLog.log("Attempting to send unlink request to cloud...", new Object[0]);
                            ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
                            clientConnectionManager.sendPacket((IPacket)new PacketDiscordUnlinkRequest());
                        }
                    });
                    bFConfirmPromptScreen.method_1084(field_1487);
                    this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
                }).texture(field_1464).size(9, 9).method_391(0, -2).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE).tip(field_1485));
            }
            if (this.playerCloudData.method_5909()) {
                objectArrayList.add(new BFButton(n, n2, 0, n4, field_6971, button -> {
                    BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen((Screen)this, field_6967, bl -> {
                        if (bl) {
                            BFLog.log("Attempting to send Patreon unlink request to cloud...", new Object[0]);
                            ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
                            clientConnectionManager.sendPacket((IPacket)new PacketPatreonUnlinkRequest());
                        }
                    });
                    bFConfirmPromptScreen.method_1084(field_6968);
                    this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
                }).texture(field_6966).size(9, 9).method_391(0, -2).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE).tip(field_6972));
            } else {
                objectArrayList.add(new BFButton(n, n2, 0, n4, field_6969, button -> {
                    BFLog.log("Attempting to send Patreon link request to cloud...", new Object[0]);
                    ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
                    clientConnectionManager.sendPacket((IPacket)new PacketPatreonLinkRequest());
                }).texture(field_6965).size(9, 9).method_391(0, -2).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE).tip(field_6970));
            }
        }
        ClientFriendManager clientFriendManager = this.manager.getFriendManager();
        boolean bl2 = clientFriendManager.hasFriend(this.playerCloudData.getUUID());
        if (!bl) {
            if (!bl2) {
                objectArrayList.add(new BFButton(n, n2, 0, n4, field_1481, button -> {
                    BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen((Screen)this, field_1492, bl -> {
                        if (bl) {
                            BFLog.log("Attempting to add friend...", new Object[0]);
                            ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
                            clientConnectionManager.sendPacket((IPacket)new PacketFriendRequest(this.playerCloudData.getUsername()));
                        }
                    });
                    bFConfirmPromptScreen.method_1084(field_1493);
                    this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
                }).texture(field_1456).size(9, 9).method_391(0, -2).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE).tip(field_1488));
            } else {
                objectArrayList.add(new BFButton(n, n2, 0, n4, field_1482, button -> {
                    BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen((Screen)this, field_1490, bl -> {
                        if (bl) {
                            BFLog.log("Attempting to remove friend...", new Object[0]);
                            ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
                            clientConnectionManager.sendPacket((IPacket)new PacketFriendRemove(this.playerCloudData.getUUID()));
                        }
                    });
                    bFConfirmPromptScreen.method_1084(field_1491);
                    this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
                }).texture(field_1457).size(9, 9).method_391(0, -2).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE).tip(field_1489));
            }
        }
        for (BFButton bFButton2 : objectArrayList) {
            bFButton2.setWidth((this.font.width(bFButton2.getMessage().getString()) + 15) / 2 + 4);
            n5 += bFButton2.getWidth() + n3;
        }
        int n9 = n - n5 / 2;
        int n10 = 0;
        for (BFButton bFButton3 : objectArrayList) {
            bFButton3.setX(n9 + n10);
            this.addRenderableWidget((GuiEventListener)bFButton3);
            n10 += bFButton3.getWidth() + n3;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.field_1466++ > 30) {
            this.field_1466 = 0;
            UUID uUID = this.playerCloudData.getUUID();
            ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
            CloudRequestManager cloudRequestManager = clientConnectionManager.getRequester();
            cloudRequestManager.push(RequestType.PLAYER_DATA, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY, uUID);
            cloudRequestManager.push(RequestType.PLAYER_STATUS, uUID);
            UUID uUID2 = this.playerCloudData.getClanId();
            if (uUID2 != null) {
                cloudRequestManager.push(RequestType.CLAN_DATA, uUID2);
            }
        }
    }

    @Override
    public void method_758() {
        super.method_758();
        int n = 20;
        BFButton bFButton = new BFButton(5, 18, n, n, (Component)Component.empty(), button -> this.method_1057());
        this.addRenderableWidget((GuiEventListener)bFButton);
        bFButton.texture(RETURN_ICON);
        bFButton.size(n, n);
        bFButton.displayType(BFButton.DisplayType.NONE);
        bFButton.tip(field_1469);
        this.addRenderableWidget((GuiEventListener)bFButton);
        Button.OnPress onPress = button -> {
            this.minecraft.setScreen((Screen)new TitleSidebarScreen((Screen)this, false));
            this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_SIDEMENU_OPEN.get()), (float)((float)((double)0.9f + (double)0.1f * Math.random()))));
        };
        this.addRenderableWidget((GuiEventListener)new BFButton(this.width - (n + 5), 18, n, n, (Component)Component.empty(), onPress).texture(field_1454).size(n, n).displayType(BFButton.DisplayType.NONE).tip(field_1470));
    }

    public void method_1057() {
        Screen screen = this.field_1494;
        if (screen instanceof SidebarScreen) {
            SidebarScreen sidebarScreen = (SidebarScreen)screen;
            sidebarScreen.field_1030 = true;
        }
        this.minecraft.setScreen(this.field_1494);
    }
}

