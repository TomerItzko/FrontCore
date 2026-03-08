/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.title.sidebar;

import com.boehmod.bflib.cloud.common.AbstractClanData;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.player.ClientFriendManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.SidebarScreen;
import com.boehmod.blockfront.client.screen.profile.ProfileMainScreen;
import com.boehmod.blockfront.client.screen.prompt.text.FriendAddPromptScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_209;
import com.boehmod.blockfront.unnamed.BF_211;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.unnamed.BF_219;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FriendsSidebarScreen
extends SidebarScreen {
    private static final Component field_1028 = Component.translatable((String)"bf.screen.overlay.friends");
    private static final Component field_1022 = Component.translatable((String)"bf.message.prompt.friend.add.title").withStyle(ChatFormatting.BOLD);
    private static final Component field_1023 = Component.translatable((String)"bf.message.prompt.friend.add.description");
    private static final Component field_1024 = Component.translatable((String)"bf.message.profile").withStyle(ChatFormatting.BOLD);
    private static final Component field_1025 = Component.translatable((String)"bf.message.profile.description", (Object[])new Object[]{"BlockFront"});
    private static final Component field_1026 = Component.translatable((String)"bf.dropdown.text.my.friends").withStyle(BFStyles.BOLD);
    private static final ResourceLocation field_1020 = BFRes.loc("textures/gui/menu/icons/addfriend.png");
    private static final ResourceLocation field_1021 = BFRes.loc("textures/gui/menu/icons/myprofile.png");
    private static final int field_1029 = 20;
    @Nullable
    private static BF_209<BF_218> field_1019;
    private int field_1027 = 0;

    public FriendsSidebarScreen(Screen screen, boolean bl) {
        super(screen, field_1028, bl);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.field_1027++ > 20) {
            this.method_749();
            this.field_1027 = 0;
        }
    }

    @Override
    public float method_756(float f) {
        return -super.method_756(f);
    }

    @Override
    public void method_754(@NotNull GuiGraphics guiGraphics, float f) {
        super.method_754(guiGraphics, f);
        PoseStack poseStack = guiGraphics.pose();
        int n = this.width / 4;
        int n2 = 20;
        int n3 = 18;
        poseStack.pushPose();
        BFRendering.rectangleWithDarkShadow(guiGraphics, 0, 0, n, this.height, BFRendering.translucentBlack());
        BFRendering.method_197(this.playerDataHandler, guiGraphics, this.font, this.minecraft.getUser().getProfileId(), BF_211.BF_213.FRIEND, 0, 12, 20, false);
        BFRendering.playerHead(this.minecraft, this.manager, poseStack, guiGraphics, this.minecraft.getGameProfile(), n - 18 - 2, 14.0f, 18);
        BFRendering.centeredString(this.font, guiGraphics, field_1026, n / 2, 4);
        poseStack.popPose();
    }

    @Override
    public boolean method_753(double d, double d2) {
        return d > (double)((float)this.width / 4.0f);
    }

    @Override
    public void method_758() {
        super.method_758();
        int n = this.width / 4;
        int n2 = 34;
        int n3 = n / 2;
        FriendsSidebarScreen friendsSidebarScreen = this;
        this.addRenderableWidget((GuiEventListener)new BFButton(0, 34, n3, 16, field_1022, button -> this.minecraft.setScreen((Screen)new FriendAddPromptScreen(friendsSidebarScreen))).texture(field_1020).size(12, 12).method_391(0, -3).alignment(BFButton.Alignment.CENTER_BOTTOM).method_368(0.0f, 3.0f).method_389(0.5f).tip(field_1023));
        this.addRenderableWidget((GuiEventListener)new BFButton(n3, 34, n3, 16, field_1024, button -> this.minecraft.setScreen((Screen)new ProfileMainScreen(friendsSidebarScreen, this.playerDataHandler.getCloudData(this.minecraft)))).texture(field_1021).size(12, 12).method_391(0, -3).alignment(BFButton.Alignment.CENTER_BOTTOM).method_368(0.0f, 3.0f).method_389(0.5f).tip(field_1025));
    }

    @Override
    public void method_774() {
        super.method_774();
        int n = this.width / 4;
        if (field_1019 == null) {
            field_1019 = new BF_209(0, 50, n, this.height - 50, this);
        }
        field_1019.setScreen(this);
        field_1019.method_553(n);
        field_1019.method_554(this.height - 50);
        this.method_749();
        this.method_764(field_1019);
    }

    private void method_749() {
        Object object;
        Object object22;
        ObjectIterator objectIterator2;
        PlayerCloudData playerCloudData;
        UUID uUID;
        PlayerCloudData playerCloudData2;
        UUID uUID22;
        ClientFriendManager clientFriendManager = this.manager.getFriendManager();
        PlayerCloudData playerCloudData3 = this.playerDataHandler.getCloudData(this.minecraft);
        ObjectOpenHashSet objectOpenHashSet = new ObjectOpenHashSet();
        ObjectOpenHashSet objectOpenHashSet2 = new ObjectOpenHashSet();
        Set<UUID> set = clientFriendManager.getFriends();
        for (UUID uUID22 : set) {
            playerCloudData2 = this.playerDataHandler.getCloudProfile(uUID22);
            if (playerCloudData2.getStatus().getOnlineStatus().isOnline()) {
                objectOpenHashSet.add(uUID22);
                continue;
            }
            objectOpenHashSet2.add(uUID22);
        }
        ObjectOpenHashSet objectOpenHashSet3 = new ObjectOpenHashSet();
        uUID22 = new ObjectOpenHashSet();
        playerCloudData2 = new ObjectOpenHashSet();
        uUID22.removeAll((Collection<?>)objectOpenHashSet2);
        AbstractClanData abstractClanData = playerCloudData3.getClanData();
        if (abstractClanData != null) {
            playerCloudData2.addAll(abstractClanData.getMembers());
        }
        ObjectOpenHashSet objectOpenHashSet4 = playerCloudData2.iterator();
        while (objectOpenHashSet4.hasNext()) {
            uUID = (UUID)objectOpenHashSet4.next();
            playerCloudData = this.playerDataHandler.getCloudProfile(uUID);
            if (playerCloudData.getStatus().getOnlineStatus().isOnline()) {
                objectOpenHashSet3.add(uUID);
                continue;
            }
            uUID22.add(uUID);
        }
        objectOpenHashSet4 = new ObjectOpenHashSet(clientFriendManager.getFriendRequests());
        uUID = new ObjectOpenHashSet(clientFriendManager.getClanRequests());
        playerCloudData = new ObjectArrayList();
        String string = String.format("%s/%s (Max: %s)", objectOpenHashSet.size(), set.size(), playerCloudData3.getMaxFriends());
        String string2 = String.format("%s/%s (Max: %s)", uUID22.size(), playerCloudData2.size(), 6);
        playerCloudData.add(new BF_219(""));
        playerCloudData.add(new BF_219(String.valueOf(ChatFormatting.BOLD) + I18n.get((String)"bf.dropdown.text.my.friends", (Object[])new Object[0])));
        playerCloudData.add(new BF_219(string));
        for (ObjectIterator objectIterator2 : objectOpenHashSet) {
            object22 = new BF_211(this, (UUID)objectIterator2, BF_211.BF_213.FRIEND);
            playerCloudData.add(object22);
        }
        if (!objectOpenHashSet3.isEmpty()) {
            object = abstractClanData != null ? abstractClanData.getName() : "Clan";
            playerCloudData.add(new BF_219(""));
            playerCloudData.add(new BF_219(String.valueOf(ChatFormatting.BOLD) + I18n.get((String)"bf.dropdown.text.clan.members", (Object[])new Object[]{object}) + String.valueOf(ChatFormatting.RESET) + " " + string2));
            for (Object object22 : objectOpenHashSet3) {
                BF_211 bF_211 = new BF_211(this, (UUID)object22, BF_211.BF_213.CLAN_MEMBER);
                playerCloudData.add(bF_211);
            }
        }
        if (!objectOpenHashSet2.isEmpty() || !uUID22.isEmpty()) {
            playerCloudData.add(new BF_219(""));
            object = Component.translatable((String)"bf.dropdown.text.offline");
            playerCloudData.add(new BF_219(String.valueOf(ChatFormatting.BOLD) + object.getString()));
        }
        for (ObjectIterator objectIterator2 : objectOpenHashSet2) {
            object22 = new BF_211(this, (UUID)objectIterator2, BF_211.BF_213.FRIEND);
            playerCloudData.add(object22);
        }
        object = uUID22.iterator();
        while (object.hasNext()) {
            objectIterator2 = (UUID)object.next();
            object22 = new BF_211(this, (UUID)objectIterator2, BF_211.BF_213.CLAN_MEMBER);
            playerCloudData.add(object22);
        }
        if (!clientFriendManager.getFriendRequests().isEmpty()) {
            playerCloudData.add(new BF_219(""));
            playerCloudData.add(new BF_219(String.valueOf(ChatFormatting.BOLD) + I18n.get((String)"bf.dropdown.text.requests.friends", (Object[])new Object[0])));
        }
        object = new ObjectArrayList((ObjectCollection)objectOpenHashSet4);
        object.sort(null);
        objectIterator2 = object.iterator();
        while (objectIterator2.hasNext()) {
            object22 = (UUID)objectIterator2.next();
            playerCloudData.add(new BF_211(this, (UUID)object22, BF_211.BF_213.FRIEND_REQUEST));
        }
        if (!uUID.isEmpty()) {
            playerCloudData.add(new BF_219(""));
            playerCloudData.add(new BF_219(String.valueOf(ChatFormatting.BOLD) + I18n.get((String)"bf.dropdown.text.requests.clans", (Object[])new Object[0])));
            objectIterator2 = uUID.iterator();
            while (objectIterator2.hasNext()) {
                object22 = (UUID)objectIterator2.next();
                playerCloudData.add(new BF_211(this, (UUID)object22, BF_211.BF_213.CLAN_INVITE));
            }
        }
        if (field_1019 != null) {
            try {
                field_1019.method_961((List<BF_218>)((Object)playerCloudData));
            }
            catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
            field_1019.method_947();
        }
    }
}

