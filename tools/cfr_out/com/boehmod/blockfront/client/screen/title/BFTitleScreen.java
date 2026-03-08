/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.RequestType
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen
 *  net.minecraft.client.gui.screens.worldselection.SelectWorldScreen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.title;

import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.FindGameWidget;
import com.boehmod.blockfront.client.gui.widget.ProfileWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.title.ArmoryTitleScreen;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.client.screen.title.PlayerScoreboardTitleScreen;
import com.boehmod.blockfront.client.screen.title.sidebar.FriendsSidebarScreen;
import com.boehmod.blockfront.client.screen.title.sidebar.TitleSidebarScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_130;
import com.boehmod.blockfront.unnamed.BF_52;
import com.boehmod.blockfront.unnamed.BF_57;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BFTitleScreen
extends BFMenuScreen {
    public static final int field_6919 = 173;
    public static final int field_6920 = 110;
    private static final Component field_907 = Component.translatable((String)"bf.dropdown.button.settings");
    private static final Component field_908 = Component.translatable((String)"bf.dropdown.button.friends");
    private static final Component field_909 = Component.translatable((String)"menu.singleplayer").withStyle(ChatFormatting.BOLD);
    private static final Component field_910 = Component.translatable((String)"bf.message.community").withStyle(ChatFormatting.BOLD);
    private static final Component field_911 = Component.translatable((String)"bf.menu.button.nav.text.lobby").withStyle(ChatFormatting.BOLD);
    private static final Component field_916 = Component.translatable((String)"bf.menu.button.nav.text.armory").withStyle(ChatFormatting.BOLD);
    private static final Component field_917 = Component.translatable((String)"bf.menu.button.nav.text.scoreboard").withStyle(ChatFormatting.BOLD);
    private static final ResourceLocation field_902 = BFRes.loc("textures/gui/menu/icons/home.png");
    private static final ResourceLocation field_903 = BFRes.loc("textures/gui/menu/icons/menu.png");
    public static final int field_912 = 56;
    private static final int field_913 = 130;
    private static final int field_914 = 40;
    private static final float field_7031 = 410.0f;
    private static final int field_7032 = 40;
    private static final int field_7033 = 20;
    private static final int field_7034 = 200;
    private static float field_904 = 1.0f;
    private static float field_905 = 1.0f;
    private int field_915 = 0;
    private int field_906 = 0;
    public BF_130 field_900;

    public BFTitleScreen(@NotNull Component component, @NotNull Component component2) {
        super(component, component2);
    }

    @Override
    public void tick() {
        super.tick();
        field_905 = field_904;
        field_904 = Mth.lerp((float)0.05f, (float)field_904, (float)0.0f);
        BF_52.onUpdate();
        if (this.field_915-- < 0) {
            ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
            CloudRequestManager cloudRequestManager = clientConnectionManager.getRequester();
            cloudRequestManager.push(RequestType.EVENTS);
            cloudRequestManager.push(RequestType.CLOUD_STATS);
            this.field_915 = 200;
        }
        if (this.field_906++ > 20) {
            this.field_906 = 0;
            if (this.field_900 != null) {
                this.field_900.method_602(this.minecraft, this.playerDataHandler);
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        float f2 = MathUtils.lerpf1(field_904, field_905, f);
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 410.0f);
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, 0, f2);
        poseStack.popPose();
    }

    @Override
    public void method_758() {
        super.method_758();
        this.method_692(this.playerDataHandler);
        BFTitleScreen bFTitleScreen = this;
        int n = 20;
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        this.addRenderableWidget((GuiEventListener)new BFButton(this.width - 25, 18, 20, 20, (Component)Component.empty(), button -> {
            this.minecraft.setScreen((Screen)new TitleSidebarScreen(bFTitleScreen, false));
            this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_SIDEMENU_OPEN.get()), (float)(0.9f + 0.1f * threadLocalRandom.nextFloat())));
        }).texture(field_903).size(20, 20).displayType(BFButton.DisplayType.NONE).tip(field_907));
        ((BFButton)this.addRenderableWidget((GuiEventListener)new BFButton(5, 18, 20, 20, (Component)Component.empty(), button -> {
            this.minecraft.setScreen((Screen)new FriendsSidebarScreen(bFTitleScreen, false));
            this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_SIDEMENU_OPEN.get()), (float)(0.9f + 0.1f * threadLocalRandom.nextFloat())));
        }).texture(field_902).size(20, 20).displayType(BFButton.DisplayType.NONE).tip(field_908))).method_371(() -> this.manager.getFriendManager().getFriendRequests().size());
    }

    @Override
    public void method_774() {
        super.method_774();
        int n = this.width / 2 - 173;
        this.method_764(new ProfileWidget(n, 56, 110, 40, this));
        this.field_900 = new BF_130(n, 100, 110, 90, this);
        this.method_764(this.field_900);
        int n2 = 130;
        int n3 = 40;
        int n4 = this.width - 130 - 15;
        int n5 = this.height - 40;
        this.method_764(new FindGameWidget(n4, n5, 130, 40, this));
    }

    protected void method_692(@NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(this.minecraft);
        Optional optional = playerCloudData.getParty();
        int n = this.width / 2;
        int n2 = 41;
        boolean bl = true;
        int n3 = 8;
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(new BF_57(this.minecraft, n, 41, 0, 8, field_909, button -> this.minecraft.setScreen((Screen)new SelectWorldScreen((Screen)new LobbyTitleScreen()))).method_400());
        objectArrayList.add(new BF_57(this.minecraft, n, 41, 0, 8, field_910, button -> this.minecraft.setScreen((Screen)new JoinMultiplayerScreen((Screen)new LobbyTitleScreen()))).method_400());
        objectArrayList.add(new BF_57(this.minecraft, n, 41, 0, 8, field_911, new LobbyTitleScreen()).method_400().method_371(() -> optional.map(matchParty -> matchParty.getPlayers().size()).orElse(0)));
        objectArrayList.add(new BF_57(this.minecraft, n, 41, 0, 8, field_916, new ArmoryTitleScreen()).method_400().method_371(() -> ((PlayerCloudInventory)clientPlayerDataHandler.getCloudData(this.minecraft).getInventory()).method_1681().size()));
        objectArrayList.add(new BF_57(this.minecraft, n, 41, 0, 8, field_917, new PlayerScoreboardTitleScreen()).method_400());
        int n4 = objectArrayList.stream().mapToInt(bFButton -> bFButton.getWidth() + 1).sum();
        int n5 = n - n4 / 2;
        int n6 = 0;
        for (BFButton bFButton2 : objectArrayList) {
            bFButton2.setX(n5 + n6);
            this.addRenderableWidget((GuiEventListener)bFButton2);
            n6 += bFButton2.getWidth() + 1;
        }
    }
}

