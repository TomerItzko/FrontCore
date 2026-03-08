/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.AbstractClanData
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.RequestType
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.CloudItemType
 *  com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.clan.PacketClanDelete
 *  com.boehmod.bflib.cloud.packet.common.clan.PacketClanLeave
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.authlib.GameProfile
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.profile;

import com.boehmod.bflib.cloud.common.AbstractClanData;
import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.CloudItemType;
import com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.clan.PacketClanDelete;
import com.boehmod.bflib.cloud.packet.common.clan.PacketClanLeave;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemRenderers;
import com.boehmod.blockfront.client.screen.profile.ProfileScreen;
import com.boehmod.blockfront.client.screen.prompt.BFPromptScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.BFConfirmPromptScreen;
import com.boehmod.blockfront.client.screen.prompt.text.ClanCreatePromptScreen;
import com.boehmod.blockfront.client.screen.prompt.text.ClanInvitePromptScreen;
import com.boehmod.blockfront.client.screen.prompt.text.ClanKickPromptScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.ClanUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class ClanScreen
extends ProfileScreen {
    private static final int field_1426 = 30;
    private static final Component field_1428 = Component.translatable((String)"bf.profile.clan.none.title").withColor(ColorReferences.COLOR_THEME_CLANS_SOLID);
    private static final Component field_1429 = Component.translatable((String)"bf.message.prestige").append(" 1").withColor(ColorReferences.COLOR_THEME_PRESTIGE_SOLID);
    private static final Component field_1430 = Component.translatable((String)"bf.profile.clan.none.description.self");
    private static final Component field_1431 = Component.translatable((String)"bf.profile.clan.none.description.requirement", (Object[])new Object[]{field_1429}).withStyle(ChatFormatting.DARK_GRAY);
    private static final Component field_1432 = Component.translatable((String)"bf.profile.clan.none.description");
    private static final Component field_1433 = Component.translatable((String)"bf.message.clan.invite");
    private static final Component field_1434 = Component.translatable((String)"bf.message.prompt.clan.invite.tip");
    private static final Component field_1435 = Component.translatable((String)"bf.message.clan.kick").withStyle(ChatFormatting.RED);
    private static final Component field_1436 = Component.translatable((String)"bf.message.prompt.clan.kick.tip");
    private static final Component field_1437 = Component.translatable((String)"bf.message.clan.delete").withStyle(ChatFormatting.DARK_RED);
    private static final Component field_1438 = Component.translatable((String)"bf.message.prompt.clan.delete.tip");
    private static final Component field_1439 = Component.translatable((String)"bf.message.prompt.clan.delete.title");
    private static final Component field_1440 = Component.translatable((String)"bf.message.prompt.clan.delete");
    private static final Component field_1441 = Component.translatable((String)"bf.message.clan.leave").withStyle(ChatFormatting.RED);
    private static final Component field_1442 = Component.translatable((String)"bf.message.prompt.clan.leave.tip");
    private static final Component field_1443 = Component.translatable((String)"bf.message.prompt.clan.leave.title");
    private static final Component field_1444 = Component.translatable((String)"bf.message.prompt.clan.leave");
    private static final Component field_1445 = Component.translatable((String)"bf.message.clan.create");
    private static final Component field_1446 = Component.translatable((String)"bf.message.prompt.clan.create.tip");
    private static final Component field_1447 = Component.translatable((String)"bf.message.clan.information").withColor(0xFFFFFF);
    private int field_1427 = 0;
    private BFButton field_1448;
    private BFButton field_1449;
    @NotNull
    private final List<BFButton> field_1425 = new ObjectArrayList();

    public ClanScreen(@NotNull Screen screen, @NotNull PlayerCloudData playerCloudData) {
        super(screen, playerCloudData, field_1447);
    }

    private void method_1041(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull AbstractClanData abstractClanData) {
        int n = this.width / 2;
        int n2 = 75;
        this.method_1044(guiGraphics, abstractClanData, 75, n);
        this.method_1042(poseStack, guiGraphics, abstractClanData, 75, n);
    }

    private void method_1044(@NotNull GuiGraphics guiGraphics, @NotNull AbstractClanData abstractClanData, int n, int n2) {
        float f = ClanUtils.getSkillLevel(this.playerDataHandler, abstractClanData);
        String string = String.format("%.2f", Float.valueOf(f));
        MutableComponent mutableComponent = Component.translatable((String)"bf.profile.clan.skill.level", (Object[])new Object[]{string}).withColor(ColorReferences.COLOR_THEME_CLANS_SOLID);
        MutableComponent mutableComponent2 = Component.literal((String)abstractClanData.getName()).withColor(ColorReferences.COLOR_THEME_CLANS_SOLID);
        MutableComponent mutableComponent3 = Component.literal((String)" - ").withColor(0xFFFFFF);
        MutableComponent mutableComponent4 = mutableComponent2.append((Component)mutableComponent3).append(field_1447).append(" ").append((Component)mutableComponent);
        BFRendering.centeredString(this.font, guiGraphics, (Component)mutableComponent4, n2, n);
    }

    private void method_1042(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull AbstractClanData abstractClanData, int n, int n2) {
        Set set = abstractClanData.getMembers();
        int n3 = set.size();
        int n4 = 55;
        int n5 = 30;
        int n6 = 12;
        int n7 = n + 15;
        int n8 = 5;
        int n9 = 60;
        int n10 = 60 * n3;
        int n11 = 0;
        for (UUID uUID : set) {
            PlayerCloudData playerCloudData = this.playerDataHandler.getCloudProfile(uUID);
            GameProfile gameProfile = playerCloudData.getMcProfile();
            int n12 = n2 - n10 / 2 + 60 * n11;
            this.method_1043(poseStack, guiGraphics, gameProfile, abstractClanData.getOwner().equals(uUID), n12, n7, 55, 30, 12);
            this.method_1045(guiGraphics, playerCloudData, n12, n7, 55);
            ++n11;
        }
    }

    private void method_1043(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull GameProfile gameProfile, boolean bl, int n, int n2, int n3, int n4, int n5) {
        Object object;
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudProfile(gameProfile.getId());
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        BFRendering.rectangle(guiGraphics, n, n2, n3, n3, BFRendering.translucentBlack());
        int n6 = n + n5;
        int n7 = n2 + n5 - 5;
        int n8 = bl ? ColorReferences.COLOR_THEME_CLANS_SOLID : ColorReferences.COLOR_WHITE_SOLID;
        BFRendering.rectangle(guiGraphics, n6 - 1, n7 - 1, n4 + 2, n4 + 2, n8);
        BFRendering.playerHead(this.minecraft, this.manager, poseStack, guiGraphics, gameProfile, n6, n7, n4);
        CloudItemStack cloudItemStack = playerCloudInventory.method_1673(CloudItemType.COIN);
        if (cloudItemStack != null && (object = cloudItemStack.getCloudItem(cloudRegistry)) instanceof AbstractCloudItemCoin) {
            AbstractCloudItemCoin abstractCloudItemCoin = (AbstractCloudItemCoin)object;
            object = CloudItemRenderers.getRenderer(abstractCloudItemCoin);
            ((CloudItemRenderer)object).method_1745(abstractCloudItemCoin, cloudItemStack, poseStack, this.minecraft, guiGraphics, n6, n7, 16.0f, 16.0f, 1.0f);
        }
    }

    private void method_1045(@NotNull GuiGraphics guiGraphics, @NotNull PlayerCloudData playerCloudData, int n, int n2, int n3) {
        int n4 = n + n3 / 2;
        int n5 = n2 + n3 - 10;
        MutableComponent mutableComponent = playerCloudData.method_1715();
        BFRendering.centeredString(this.font, guiGraphics, (Component)mutableComponent, n4, n5);
    }

    @Override
    public void tick() {
        super.tick();
        AbstractClanData abstractClanData = this.getClanData();
        UUID uUID = this.playerCloudData.getClanId();
        boolean bl = abstractClanData.getOwner().equals(this.playerCloudData.getMcProfile().getId());
        boolean bl2 = this.method_1063();
        boolean bl3 = abstractClanData.isEmpty();
        boolean bl4 = this.method_1064();
        this.field_1448.visible = bl3 && bl2;
        this.field_1449.visible = !bl && bl4;
        boolean bl5 = bl && bl2;
        for (BFButton bFButton : this.field_1425) {
            bFButton.visible = bl5;
        }
        if (uUID != null && this.field_1427++ > 30) {
            this.field_1427 = 0;
            ((ClientConnectionManager)this.manager.getConnectionManager()).getRequester().push(RequestType.CLAN_DATA, uUID);
        }
    }

    @Override
    public void init() {
        super.init();
        int n = this.width / 2;
        int n2 = 200;
        int n3 = 20;
        int n4 = n - 100;
        int n5 = this.height - 90;
        this.field_1448 = new BFButton(n4, n5, 200, 20, field_1445, button -> this.minecraft.setScreen((Screen)new ClanCreatePromptScreen(this))).tip(field_1446).textStyle(BFButton.TextStyle.SHADOW).displayType(BFButton.DisplayType.SHADOW);
        this.addRenderableWidget((GuiEventListener)this.field_1448);
        this.field_1449 = new BFButton(n4, n5, 200, 20, field_1441, button -> {
            Object t = new BFConfirmPromptScreen((Screen)this, field_1443, bl -> {
                if (bl) {
                    BFLog.log("Attempting to send clan leave request to cloud...", new Object[0]);
                    ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
                    clientConnectionManager.sendPacket((IPacket)new PacketClanLeave());
                }
            }).method_1084(field_1444);
            ((BFPromptScreen)((Object)((Object)t))).method_1084(field_1443);
            this.minecraft.setScreen(t);
        }).tip(field_1442);
        this.addRenderableWidget((GuiEventListener)this.field_1449);
        int n6 = 80;
        int n7 = 20;
        int n8 = 5;
        int n9 = 85;
        ObjectArrayList objectArrayList = new ObjectArrayList();
        ObjectArrayList objectArrayList2 = new ObjectArrayList();
        ObjectArrayList objectArrayList3 = new ObjectArrayList();
        objectArrayList.add(field_1433);
        objectArrayList2.add(field_1434);
        objectArrayList3.add(button -> this.minecraft.setScreen((Screen)new ClanInvitePromptScreen(this)));
        objectArrayList.add(field_1435);
        objectArrayList2.add(field_1436);
        objectArrayList3.add(button -> this.minecraft.setScreen((Screen)new ClanKickPromptScreen(this)));
        objectArrayList.add(field_1437);
        objectArrayList2.add(field_1438);
        objectArrayList3.add(button -> {
            BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen((Screen)this, field_1439, bl -> {
                if (bl) {
                    BFLog.log("Attempting to send clan delete request to cloud...", new Object[0]);
                    ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
                    clientConnectionManager.sendPacket((IPacket)new PacketClanDelete());
                }
            });
            bFConfirmPromptScreen.method_1084(field_1440);
            this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
        });
        int n10 = objectArrayList.size();
        int n11 = 85 * n10;
        for (int i = 0; i < n10; ++i) {
            int n12 = n - n11 / 2 + 85 * i;
            BFButton bFButton = new BFButton(n12, n5, 80, 20, (Component)objectArrayList.get(i), (Button.OnPress)objectArrayList3.get(i)).tip((Component)objectArrayList2.get(i));
            this.field_1425.add(bFButton);
            this.addRenderableWidget((GuiEventListener)bFButton);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        AbstractClanData abstractClanData = this.getClanData();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        int n5 = n4 - 35;
        if (!abstractClanData.isEmpty()) {
            this.method_1041(poseStack, guiGraphics, abstractClanData);
        } else {
            int n6 = 10;
            int n7 = 301;
            int n8 = 70;
            int n9 = n3 - 150;
            int n10 = n5 - 6 - 10;
            BFRendering.rectangleWithDarkShadow(guiGraphics, n9, n10, 301, 70, BFRendering.translucentBlack());
            int n11 = 3;
            BFRendering.promptBackground(poseStack, guiGraphics, n9 + 3, n10 + 3, 295, 64);
            BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, field_1428, n3, n5);
            if (this.method_1063()) {
                BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, field_1430, n3, n5 + 15);
                MutableComponent mutableComponent = field_1431.copy();
                BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, (Component)mutableComponent, n3, n5 + 30);
            } else {
                BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, field_1432, n3, n5 + 15);
            }
        }
    }

    @NotNull
    private AbstractClanData getClanData() {
        UUID uUID = this.playerCloudData.getClanId();
        return uUID != null ? this.playerDataHandler.getClanData(uUID) : AbstractClanData.EMPTY_CLAN_DATA;
    }
}

