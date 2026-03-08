/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.mm.MatchParty
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.mm.PacketMMSearchHostCanceled
 *  com.boehmod.bflib.cloud.packet.common.party.PacketPartyCreate
 *  com.boehmod.bflib.cloud.packet.common.party.PacketPartyLeave
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.bflib.cloud.common.mm.MatchParty;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.mm.PacketMMSearchHostCanceled;
import com.boehmod.bflib.cloud.packet.common.party.PacketPartyCreate;
import com.boehmod.bflib.cloud.packet.common.party.PacketPartyLeave;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.BFVersionChecker;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.match.ClientMatchMaking;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BootcampSkipScreen;
import com.boehmod.blockfront.client.screen.GameTypeSelectScreen;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_52;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.FormatUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FindGameWidget
extends BFWidget {
    private static final Component field_610 = Component.translatable((String)"bf.message.bootcamp.description");
    private static final Component field_611 = Component.translatable((String)"bf.menu.lobby.search.start").withStyle(ChatFormatting.BOLD);
    private static final Component field_612 = Component.translatable((String)"bf.menu.lobby.search.cancel").withStyle(ChatFormatting.BOLD);
    private static final Component field_613 = Component.translatable((String)"bf.menu.lobby.cancel").withStyle(ChatFormatting.BOLD);
    private static final Component field_614 = Component.translatable((String)"bf.menu.lobby.search.bootcamp").withStyle(ChatFormatting.BOLD);
    private static final Component field_615 = Component.literal((String)"+").withColor(11120486);
    private static final Component field_616 = Component.literal((String)"-").withColor(13721420);
    private static final Component field_617 = field_615.copy().append(" ").append((Component)Component.translatable((String)"bf.menu.lobby.button.party.create").withStyle(ChatFormatting.WHITE));
    private static final Component field_618 = field_616.copy().append(" ").append((Component)Component.translatable((String)"bf.menu.lobby.button.party.leave").withStyle(ChatFormatting.WHITE));
    private static final Component field_619 = field_616.copy().append(" ").append((Component)Component.translatable((String)"bf.menu.lobby.button.party.disband").withStyle(ChatFormatting.WHITE));
    @NotNull
    private static final ResourceLocation field_620 = BFRes.loc("textures/gui/menu/icons/party_create.png");
    @NotNull
    private static final ResourceLocation field_622 = BFRes.loc("textures/gui/menu/icons/party_disband.png");
    @NotNull
    private static final ResourceLocation field_623 = BFRes.loc("textures/gui/menu/icons/party_leave.png");
    @NotNull
    private static final ResourceLocation field_624 = BFRes.loc("textures/gui/menu/button_gray.png");
    @NotNull
    private static final ResourceLocation field_625 = BFRes.loc("textures/gui/menu/button_green.png");
    @NotNull
    private static final ResourceLocation field_626 = BFRes.loc("textures/gui/menu/button_green_hover.png");
    @NotNull
    private static final ResourceLocation field_6818 = BFRes.loc("textures/gui/menu/button_yellow.png");
    @NotNull
    private static final ResourceLocation field_6819 = BFRes.loc("textures/gui/menu/button_yellow_hover.png");
    @Nullable
    public BF_52 field_621;
    @Nullable
    public BFButton field_627;

    public FindGameWidget(int n, int n2, int n3, int n4, @NotNull Screen screen) {
        super(n, n2, n3, n4, screen);
    }

    @Override
    public void method_535(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler) {
        super.method_535(minecraft, bFClientManager, clientPlayerDataHandler);
        int n = 20;
        int n2 = 100;
        int n3 = 24;
        int n4 = this.field_564 + this.field_566 - 100 - 20 + 5;
        int n5 = this.field_565 + 7;
        int n6 = n4 + 100;
        int n7 = n5 + 12 - 10;
        this.field_627 = new BFButton(n6, n7, 19, 20, (Component)Component.empty(), button -> this.method_591(minecraft, bFClientManager, clientPlayerDataHandler)).size(15, 15).method_391(0, -2).displayType(BFButton.DisplayType.SHADOW).method_392(field_624);
        this.addMcWidget((AbstractWidget)this.field_627);
        this.field_621 = new BF_52((Component)Component.empty(), n4, n5, 100, 24, button -> this.method_590(minecraft, bFClientManager, clientPlayerDataHandler));
        this.field_621.method_368(0.0f, -1.0f);
        this.field_621.displayType(BFButton.DisplayType.SHADOW);
        this.field_621.method_390(0xFFFFFF);
        this.field_621.method_386(null);
        this.field_621.textStyle(BFButton.TextStyle.SHADOW);
        this.addMcWidget((AbstractWidget)this.field_621);
        this.method_588(minecraft, bFClientManager, clientPlayerDataHandler);
    }

    @Override
    public void method_537(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, float f) {
        super.method_537(minecraft, bFClientManager, clientPlayerDataHandler, f);
        this.method_588(minecraft, bFClientManager, clientPlayerDataHandler);
    }

    private void method_588(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        Object object;
        Component component;
        if (this.field_621 == null || this.field_627 == null) {
            return;
        }
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        BFVersionChecker bFVersionChecker = bFClientManager.getVersionChecker();
        UUID uUID = minecraft.getUser().getProfileId();
        this.method_593(true);
        this.method_592(true);
        boolean bl = bFClientManager.getMatchMaking().isSearching();
        boolean bl2 = minecraft.screen instanceof GameTypeSelectScreen || bl;
        Component component2 = component = bl ? field_612 : field_613;
        if (playerCloudData.hasCompletedBootcamp()) {
            object = Component.literal((String)(bl2 ? component : field_611).getString().toUpperCase(Locale.ROOT)).withStyle(ChatFormatting.BOLD);
            this.field_621.textStyle(BFButton.TextStyle.SHADOW);
            this.field_621.setHeight(24);
            this.field_621.setMessage((Component)object);
            this.field_621.displayType(BFButton.DisplayType.NONE);
            this.field_621.method_385(field_625, field_626);
            this.field_621.method_365(ColorReferences.COLOR_TEAM_ALLIES_SOLID);
        } else {
            this.method_592(false);
            object = Component.literal((String)(bl2 ? component : field_614).getString().toUpperCase(Locale.ROOT)).withColor(0).withStyle(ChatFormatting.BOLD);
            this.field_621.textStyle(BFButton.TextStyle.DEFAULT);
            this.field_621.setHeight(16);
            this.field_621.setMessage((Component)object);
            this.field_621.displayType(BFButton.DisplayType.NONE);
            this.field_621.method_385(field_6818, field_6819);
            this.field_621.method_365(ColorReferences.COLOR_THEME_YELLOW_SOLID);
        }
        object = playerCloudData.getParty();
        ResourceLocation resourceLocation = field_620;
        Component component3 = field_617;
        if (((Optional)object).isPresent()) {
            MatchParty matchParty = (MatchParty)((Optional)object).get();
            boolean bl3 = matchParty.isHost(uUID);
            if (bl3) {
                resourceLocation = field_622;
                component3 = field_619;
            } else {
                this.method_593(false);
                resourceLocation = field_623;
                component3 = field_618;
            }
        }
        this.field_627.texture(resourceLocation);
        this.field_627.tip(component3);
        if (!((ClientConnectionManager)bFClientManager.getConnectionManager()).getStatus().isConnected()) {
            this.method_592(false);
            this.method_593(false);
        }
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        BFVersionChecker bFVersionChecker = bFClientManager.getVersionChecker();
        if (bFVersionChecker.hasUpdate() || !((ClientConnectionManager)bFClientManager.getConnectionManager()).getStatus().isConnected()) {
            return;
        }
        if (!playerCloudData.hasCompletedBootcamp()) {
            this.method_586(poseStack, guiGraphics, font);
        }
    }

    private void method_586(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font) {
        if (this.field_621 == null) {
            return;
        }
        int n = this.field_621.getX() + this.field_621.getWidth() / 2;
        ObjectList<String> objectList = FormatUtils.parseMarkup(font, field_610.getString(), (this.field_566 - 10) * 2);
        Style style = Style.EMPTY.withColor(0xFFFF55);
        int n2 = 0;
        for (String string : objectList) {
            MutableComponent mutableComponent = Component.literal((String)string).withStyle(style);
            int n3 = this.field_565 + 27 + n2 * 5;
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)mutableComponent, n, n3, 0.5f);
            ++n2;
        }
    }

    private void method_585(@NotNull BFButton bFButton, boolean bl) {
        bFButton.active = bl;
        bFButton.visible = bl;
    }

    private void method_592(boolean bl) {
        if (this.field_627 == null) {
            return;
        }
        this.method_585(this.field_627, bl);
    }

    private void method_593(boolean bl) {
        if (this.field_621 == null) {
            return;
        }
        this.method_585(this.field_621, bl);
    }

    private void method_590(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        ClientMatchMaking clientMatchMaking = bFClientManager.getMatchMaking();
        Optional optional = playerCloudData.getParty();
        SoundManager soundManager = minecraft.getSoundManager();
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)bFClientManager.getConnectionManager();
        if (clientMatchMaking.isSearching() || optional.isPresent() && ((MatchParty)optional.get()).isSearching()) {
            BFLog.log("Attempting to cancel search for game...", new Object[0]);
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_LOBBY_CANCEL.get()), (float)1.0f));
            clientConnectionManager.sendPacket((IPacket)new PacketMMSearchHostCanceled());
            clientMatchMaking.setSearching(false);
        } else {
            if (!playerCloudData.hasCompletedBootcamp()) {
                minecraft.setScreen((Screen)new BootcampSkipScreen(this.getScreen()).method_5491(true));
                soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.0f));
                return;
            }
            boolean bl = minecraft.screen instanceof GameTypeSelectScreen;
            if (bl) {
                soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_LOBBY_CANCEL.get()), (float)1.0f));
            } else {
                soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.0f));
            }
            minecraft.setScreen((Screen)(bl ? new LobbyTitleScreen() : new GameTypeSelectScreen(this.getScreen())));
        }
    }

    private void method_591(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)bFClientManager.getConnectionManager();
        if (playerCloudData.getParty().isPresent()) {
            BFLog.log("Attempting to leave party...", new Object[0]);
            clientConnectionManager.sendPacket((IPacket)new PacketPartyLeave());
        } else {
            BFLog.log("Attempting to create party...", new Object[0]);
            clientConnectionManager.sendPacket((IPacket)new PacketPartyCreate());
        }
        clientConnectionManager.sendPacket((IPacket)new PacketMMSearchHostCanceled());
        bFClientManager.getMatchMaking().setSearching(false);
    }
}

