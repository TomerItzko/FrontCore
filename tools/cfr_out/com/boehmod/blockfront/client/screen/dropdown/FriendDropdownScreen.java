/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.AbstractClanData
 *  com.boehmod.bflib.cloud.common.mm.MatchParty
 *  com.boehmod.bflib.cloud.common.player.status.OnlineStatus
 *  com.boehmod.bflib.cloud.common.player.status.PartyStatus
 *  com.boehmod.bflib.cloud.common.player.status.PlayerStatus
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.clan.PacketClanInvite
 *  com.boehmod.bflib.cloud.packet.common.friend.PacketFriendJoinGame
 *  com.boehmod.bflib.cloud.packet.common.friend.PacketFriendRemove
 *  com.boehmod.bflib.cloud.packet.common.party.PacketPartyInvite
 *  com.boehmod.bflib.cloud.packet.common.party.PacketPartyJoin
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.dropdown;

import com.boehmod.bflib.cloud.common.AbstractClanData;
import com.boehmod.bflib.cloud.common.mm.MatchParty;
import com.boehmod.bflib.cloud.common.player.status.OnlineStatus;
import com.boehmod.bflib.cloud.common.player.status.PartyStatus;
import com.boehmod.bflib.cloud.common.player.status.PlayerStatus;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.clan.PacketClanInvite;
import com.boehmod.bflib.cloud.packet.common.friend.PacketFriendJoinGame;
import com.boehmod.bflib.cloud.packet.common.friend.PacketFriendRemove;
import com.boehmod.bflib.cloud.packet.common.party.PacketPartyInvite;
import com.boehmod.bflib.cloud.packet.common.party.PacketPartyJoin;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.BFNotification;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.dropdown.DropdownScreen;
import com.boehmod.blockfront.client.screen.profile.ProfileMainScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.BFConfirmPromptScreen;
import com.boehmod.blockfront.client.screen.prompt.text.FriendPokePromptScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_211;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public final class FriendDropdownScreen
extends DropdownScreen {
    private static final Component field_684 = Component.translatable((String)"bf.message.friend.error.party.in");
    private static final Component field_685 = Component.translatable((String)"bf.message.friend.error.party.null");
    private static final Component field_686 = Component.translatable((String)"bf.message.prompt.friend.remove.title");
    private static final Component field_687 = Component.translatable((String)"bf.message.prompt.friend.remove");
    private static final Component field_688 = Component.translatable((String)"bf.dropdown.text.poke");
    private static final Component field_689 = Component.translatable((String)"bf.dropdown.text.poke.tip");
    private static final Component field_690 = Component.translatable((String)"bf.dropdown.text.profile");
    private static final Component field_691 = Component.translatable((String)"bf.dropdown.text.clan.invite.tip");
    private static final Component field_692 = Component.translatable((String)"bf.dropdown.text.party.invite");
    private static final Component field_693 = Component.translatable((String)"bf.dropdown.text.party.invite.tip");
    private static final Component field_694 = Component.translatable((String)"bf.dropdown.text.party.join");
    private static final Component field_695 = Component.translatable((String)"bf.dropdown.text.party.join.tip");
    private static final Component field_696 = Component.translatable((String)"bf.dropdown.text.game.join");
    private static final Component field_697 = Component.translatable((String)"bf.dropdown.text.game.join.tip");
    private static final Component field_698 = Component.translatable((String)"bf.dropdown.text.remove").withStyle(ChatFormatting.RED);
    private static final Component field_699 = Component.translatable((String)"bf.dropdown.text.remove.tip").withStyle(ChatFormatting.RED);
    private static final Component field_700 = Component.translatable((String)"bf.dropdown.text.clan.invite");
    @NotNull
    private final UUID field_702;
    @NotNull
    private final PlayerCloudData field_701;

    public FriendDropdownScreen(@NotNull Screen screen, int n, int n2, int n3, int n4, @NotNull PlayerCloudData playerCloudData) {
        super(screen, n, n2, n3, n4, (Component)Component.translatable((String)"bf.screen.dropdown.friend", (Object[])new Object[]{playerCloudData.getUsername()}));
        this.field_702 = playerCloudData.getUUID();
        PlayerCloudData playerCloudData2 = this.playerDataHandler.getCloudData(this.minecraft);
        PlayerStatus playerStatus = playerCloudData2.getStatus();
        UUID uUID = playerStatus.getServerOn();
        Optional optional = playerCloudData2.getParty();
        AbstractClanData abstractClanData = playerCloudData2.getClanData();
        boolean bl = abstractClanData != null && !abstractClanData.isEmpty() && abstractClanData.getOwner().equals(playerCloudData2.getUUID());
        this.field_701 = playerCloudData;
        PlayerStatus playerStatus2 = this.field_701.getStatus();
        Component component = field_700;
        if (bl) {
            component = component.copy().withColor(ColorReferences.COLOR_THEME_CLANS_SOLID);
        }
        PlayerStatus playerStatus3 = this.field_701.getStatus();
        OnlineStatus onlineStatus = playerStatus3.getOnlineStatus();
        UUID uUID2 = playerStatus3.getServerOn();
        MutableComponent mutableComponent = Component.literal((String)playerCloudData.getUsername());
        this.method_746(field_688, field_689, onlineStatus == OnlineStatus.ONLINE);
        this.method_745(field_690, (Component)Component.translatable((String)"bf.dropdown.text.profile.tip", (Object[])new Object[]{mutableComponent}));
        this.method_746(component, field_691, bl);
        this.method_746(field_692, field_693, uUID == null && optional.isPresent() && onlineStatus == OnlineStatus.ONLINE);
        this.method_746(field_694, field_695, playerStatus2.getPartyStatus() != PartyStatus.NONE);
        this.method_746(field_696, field_697, uUID2 != null);
        this.method_745(field_698, field_699);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        int n3 = 22;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        BFRendering.rectangle(guiGraphics, this.field_1015, this.field_1016 - 22, this.field_1017, 22, BFRendering.translucentBlack());
        BFRendering.rectangle(guiGraphics, this.field_1015, this.field_1016 - 22, this.field_1017, 22, BFRendering.translucentBlack());
        BFRendering.method_197(this.playerDataHandler, guiGraphics, this.font, this.field_702, BF_211.BF_213.FRIEND, this.field_1015, this.field_1016 - 22, 22, false);
        int n4 = 2;
        int n5 = 18;
        BFRendering.playerHead(this.minecraft, this.manager, poseStack, guiGraphics, this.field_701.getMcProfile(), this.field_1015 + this.field_1017 - 20, this.field_1016 - 22 + 2, 18);
        poseStack.popPose();
    }

    @Override
    public void method_748(int n) {
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        PlayerCloudData playerCloudData2 = this.playerDataHandler.getCloudProfile(this.field_702);
        PlayerStatus playerStatus = playerCloudData2.getStatus();
        ClientLevel clientLevel = this.minecraft.level;
        switch (n) {
            case 1: {
                this.minecraft.setScreen((Screen)new FriendPokePromptScreen(this.field_1036, this.field_702));
                break;
            }
            case 2: {
                this.minecraft.setScreen((Screen)new ProfileMainScreen(this.field_1036, this.field_701));
                break;
            }
            case 3: {
                clientConnectionManager.sendPacket((IPacket)new PacketClanInvite(playerCloudData2.getUsername()));
                break;
            }
            case 4: {
                if (playerStatus.getPartyStatus() != PartyStatus.NONE) {
                    clientConnectionManager.sendPacket((IPacket)new PacketPartyInvite(this.field_702));
                    break;
                }
                BFNotification.show(this.minecraft, (Component)Component.translatable((String)"bf.message.party.already.in", (Object[])new Object[]{this.field_701.getUsername()}));
                break;
            }
            case 5: {
                if (playerCloudData.getParty().isEmpty()) {
                    if (playerStatus.getPartyStatus() != PartyStatus.NONE) {
                        if (clientLevel != null) {
                            this.manager.disconnect(this.minecraft, clientLevel);
                        }
                        clientConnectionManager.sendPacket((IPacket)new PacketPartyJoin(this.field_702));
                        break;
                    }
                    BFNotification.show(this.minecraft, field_685);
                    break;
                }
                BFNotification.show(this.minecraft, field_684);
                break;
            }
            case 6: {
                Optional optional = playerCloudData.getParty();
                if (optional.isEmpty() || ((MatchParty)optional.get()).getPlayers().contains(this.field_702)) {
                    if (this.field_701.getStatus().getServerOn() == null) break;
                    if (clientLevel != null) {
                        this.manager.disconnect(this.minecraft, clientLevel);
                    }
                    clientConnectionManager.sendPacket((IPacket)new PacketFriendJoinGame(this.field_702));
                    break;
                }
                BFNotification.show(this.minecraft, field_684);
                break;
            }
            case 7: {
                BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen(this.field_1036, field_686, bl -> {
                    if (bl) {
                        clientConnectionManager.sendPacket((IPacket)new PacketFriendRemove(this.field_701.getUUID()));
                    }
                });
                bFConfirmPromptScreen.method_1084(field_687);
                this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
            }
        }
    }
}

