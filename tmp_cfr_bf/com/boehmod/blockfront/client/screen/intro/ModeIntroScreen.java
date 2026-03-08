/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.intro;

import com.boehmod.bflib.cloud.common.PopupType;
import com.boehmod.bflib.cloud.connection.ConnectionStatus;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.BFVersionChecker;
import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.PopupButton;
import com.boehmod.blockfront.client.net.ClientConnectingStage;
import com.boehmod.blockfront.client.net.ConnectionMode;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.intro.BFIntroScreen;
import com.boehmod.blockfront.client.screen.title.BFTitleScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.cloud.connection.BFConnection;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_162;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModeIntroScreen
extends BFIntroScreen {
    private static final int field_880 = 936;
    private static final int field_881 = 156;
    @NotNull
    private static final Component field_870 = Component.translatable((String)"bf.screen.introduction.online");
    @NotNull
    private static final Component field_871 = Component.translatable((String)"bf.screen.introduction.offline");
    @NotNull
    private static final Component field_872 = Component.translatable((String)"bf.screen.introduction.offline.tip");
    @NotNull
    private static final Component field_873 = Component.translatable((String)"bf.screen.introduction.online.tip");
    @NotNull
    private static final Component field_874 = Component.translatable((String)"bf.screen.introduction.online.cancel.tip", (Object[])new Object[]{"BlockFront"});
    @NotNull
    private static final Component field_875 = Component.translatable((String)"bf.message.okay");
    @NotNull
    private static final Component field_876 = Component.translatable((String)"bf.message.cancel");
    @NotNull
    private static final Component field_6918 = Component.translatable((String)"bf.menu.text.anticheat.detected.par1").withColor(13721420);
    @NotNull
    private static final Component field_877 = Component.translatable((String)"bf.cloud.popup.update.title");
    @NotNull
    private static final Component field_878 = Component.translatable((String)"bf.cloud.popup.update.button");
    @NotNull
    private static final Component field_879 = Component.translatable((String)"bf.cloud.popup.update.button.tip");
    @NotNull
    private static final ResourceLocation field_863 = BFRes.loc("textures/gui/logo/blockfront.png");
    @NotNull
    private static final ResourceLocation field_864 = BFRes.loc("textures/gui/intro/stage_background.png");
    @NotNull
    private static final ResourceLocation field_865 = BFRes.loc("textures/gui/intro/stage_background_complete.png");
    @NotNull
    private static final ResourceLocation field_866 = BFRes.loc("textures/gui/intro/stage_background_incomplete.png");
    public static final int field_882 = 16;
    public static final int field_883 = 32;
    public static final int field_884 = 32;
    public static final int field_885 = ClientConnectingStage.values().length;
    public static final int field_886 = 150;
    @Nullable
    private BFButton field_887;
    @Nullable
    private BFButton field_888;
    @Nullable
    private BFButton field_889;
    private boolean field_861 = false;
    private boolean field_862 = true;

    public boolean method_685() {
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)this.manager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(this.minecraft);
        return playerCloudData.method_1720();
    }

    @Override
    protected void init() {
        super.init();
        int n = this.width / 2;
        int n2 = this.height / 2;
        int n3 = 100;
        int n4 = 20;
        this.field_889 = new BFButton(n - 50, n2 + 54, 100, 20, field_876, button -> {
            ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
            clientConnectionManager.setConnectionMode(ConnectionMode.UNDECIDED);
            clientConnectionManager.getConnection().disconnect("User cancelled connection", false);
        }).tip(field_874).textStyle(BFButton.TextStyle.DEFAULT).displayType(BFButton.DisplayType.SHADOW);
        this.field_889.active = false;
        this.field_889.visible = false;
        this.addRenderableWidget((GuiEventListener)this.field_889);
    }

    @Override
    @NotNull
    protected List<BF_162> method_657() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        BFClientAntiCheat bFClientAntiCheat = (BFClientAntiCheat)this.manager.getAntiCheat();
        objectArrayList.add(new BF_162(field_870, button -> {
            BFVersionChecker bFVersionChecker = this.manager.getVersionChecker();
            if (bFVersionChecker.hasUpdate()) {
                BFTitleScreen.openUrl(this.minecraft, "https://download.blockfrontmc.com");
                return;
            }
            ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
            clientConnectionManager.setConnectionMode(ConnectionMode.ONLINE);
            if (this.field_862) {
                this.field_862 = false;
                clientConnectionManager.getConnection().bumpReconnectTick();
            }
        }).method_667(bFButton2 -> {
            this.field_887 = bFButton2;
            bFButton2.enabled(bFButton -> !bFClientAntiCheat.isActive() && ((ClientConnectionManager)this.manager.getConnectionManager()).getConnectionMode() != ConnectionMode.ONLINE);
        }).method_666(ColorReferences.COLOR_TEAM_ALLIES_SOLID, ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID).method_665(0xFFFFFF).method_668(field_873));
        objectArrayList.add(new BF_162(field_871, button -> {
            ((ClientConnectionManager)this.manager.getConnectionManager()).setConnectionMode(ConnectionMode.OFFLINE);
            this.minecraft.setScreen(ModeIntroScreen.method_655());
        }).method_667(bFButton2 -> {
            this.field_888 = bFButton2;
            bFButton2.enabled(bFButton -> ((ClientConnectionManager)this.manager.getConnectionManager()).getConnectionMode() != ConnectionMode.ONLINE);
        }).method_668(field_872));
        return objectArrayList;
    }

    @Override
    @Nullable
    protected SoundEvent method_656() {
        return null;
    }

    public void tick() {
        super.tick();
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
        boolean bl = clientConnectionManager.getConnectionMode() == ConnectionMode.ONLINE;
        ConnectionStatus connectionStatus = clientConnectionManager.getStatus();
        BFClientAntiCheat bFClientAntiCheat = (BFClientAntiCheat)this.manager.getAntiCheat();
        BFVersionChecker bFVersionChecker = this.manager.getVersionChecker();
        this.method_675(bFVersionChecker);
        if (bl && connectionStatus.isConnected() && connectionStatus.isVerified() && this.method_685()) {
            this.minecraft.setScreen(ModeIntroScreen.method_655());
        }
        if (this.field_889 != null) {
            this.field_889.active = bl;
            this.field_889.visible = bl;
        }
        if (this.field_887 != null) {
            boolean bl2 = this.field_887.visible = !bl;
        }
        if (this.field_888 != null) {
            boolean bl3 = this.field_888.visible = !bl;
        }
        if (bFVersionChecker.hasUpdate()) {
            if (this.field_887 != null) {
                this.field_887.textStyle(BFButton.TextStyle.DEFAULT);
                this.field_887.method_369(0);
                this.field_887.method_390(0);
                this.field_887.setMessage(field_878);
                this.field_887.method_395(ColorReferences.COLOR_THEME_YELLOW_SOLID);
                this.field_887.method_397(ColorReferences.COLOR_WHITE_SOLID);
                this.field_887.tip(field_879);
            }
        } else if (bFClientAntiCheat.isActive() && this.field_887 != null) {
            this.field_887.method_395(ColorReferences.COLOR_THEME_RED_SOLID);
            this.field_887.method_397(ColorReferences.COLOR_THEME_RED_HOVERED_SOLID);
        }
    }

    private void method_675(@NotNull BFVersionChecker bFVersionChecker) {
        if (!bFVersionChecker.hasUpdate() || this.field_861) {
            return;
        }
        this.field_861 = true;
        MutableComponent mutableComponent = Component.literal((String)bFVersionChecker.getCurrentVersion()).withStyle(ChatFormatting.YELLOW);
        BFClientManager.showPopup(field_877, (Component)Component.translatable((String)"bf.cloud.popup.update.message", (Object[])new Object[]{mutableComponent, "BlockFront"}), PopupType.UPDATE, new PopupButton(field_875, null, button -> this.minecraft.setScreen((Screen)this)), new PopupButton(field_878, field_879, button -> BFTitleScreen.openUrl(this.minecraft, "https://download.blockfrontmc.com")));
    }

    @Override
    @Nullable
    protected ResourceLocation method_658() {
        return null;
    }

    @Override
    public void renderBackground(@Nonnull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.renderBackground(guiGraphics, n, n2, f);
        BFVersionChecker bFVersionChecker = this.manager.getVersionChecker();
        BFClientAntiCheat bFClientAntiCheat = (BFClientAntiCheat)this.manager.getAntiCheat();
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
        boolean bl = clientConnectionManager.getConnectionMode() == ConnectionMode.ONLINE;
        boolean bl2 = bFClientAntiCheat.isActive();
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        BFRendering.centeredTextureScaled(poseStack, guiGraphics, field_863, (float)n3, (float)(n4 - 20), 936.0f, 156.0f, 0.25f);
        if (bFVersionChecker.hasUpdate()) {
            var13_13 = Component.literal((String)bFVersionChecker.getCurrentVersion()).withStyle(ChatFormatting.YELLOW);
            MutableComponent mutableComponent = Component.translatable((String)"bf.cloud.popup.update.message", (Object[])new Object[]{var13_13, "BlockFront"});
            List list = this.font.split((FormattedText)mutableComponent, 260);
            BFRendering.renderTextLines(poseStack, this.font, (MultiBufferSource)guiGraphics.bufferSource(), (List<FormattedCharSequence>)list, n3, n4 + 60, 10, true);
        } else if (bl2) {
            var13_13 = this.font.split((FormattedText)field_6918, 280);
            BFRendering.renderTextLines(poseStack, this.font, (MultiBufferSource)guiGraphics.bufferSource(), (List<FormattedCharSequence>)var13_13, n3, n4 + 60, 10, true);
        }
        if (bl) {
            this.method_678(poseStack, guiGraphics, n3, n4, n, n2);
        }
        int n5 = bl2 ? ColorReferences.COLOR_THEME_RED_SOLID : (this.field_861 ? ColorReferences.COLOR_THEME_YELLOW_SOLID : ColorReferences.COLOR_TEAM_ALLIES_SOLID);
        BFRendering.rectangleGradient(guiGraphics, 0, this.height - 150, this.width, 150, n5, n5, 0.0f, 0.25f);
    }

    private void method_678(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, int n3, int n4) {
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
        BFConnection bFConnection = clientConnectionManager.getConnection();
        ConnectionStatus connectionStatus = clientConnectionManager.getStatus();
        MutableComponent mutableComponent = Component.literal((String)"BlockFront");
        float f = BFRendering.getRenderTime();
        int n5 = Math.max(bFConnection.getReconnectTickCount() / 20, 0);
        MutableComponent mutableComponent2 = Component.literal((String)StringUtils.formatLong(n5)).withColor(11120486);
        int n6 = Math.max(bFConnection.getLoginSendTimer() / 20, 0);
        MutableComponent mutableComponent3 = Component.literal((String)StringUtils.formatLong(n6)).withColor(11120486);
        ClientConnectingStage clientConnectingStage = ClientConnectingStage.getCurrentStage(connectionStatus);
        int n7 = clientConnectingStage.ordinal();
        Pair<Component, Component> pair = clientConnectingStage.getDisplayComponents((Component)mutableComponent, (Component)(connectionStatus == ConnectionStatus.CLOSED || connectionStatus == ConnectionStatus.CONNECTED_NOT_ENCRYPTED ? mutableComponent2 : mutableComponent3), f);
        int n8 = n2 + 38;
        Component component = (Component)pair.first();
        Component component2 = (Component)pair.second();
        int n9 = this.font.width((FormattedText)component);
        int n10 = n9 + 4;
        int n11 = 13;
        int n12 = n - n10 / 2;
        int n13 = n8 - 3;
        BFRendering.rectangle(guiGraphics, n12 + 1, n13, n10 - 2, 1, BFRendering.translucentBlack());
        BFRendering.rectangle(guiGraphics, n12, n13 + 1, n10, 11, BFRendering.translucentBlack());
        BFRendering.rectangle(guiGraphics, n12 + 1, n13 + 13 - 1, n10 - 2, 1, BFRendering.translucentBlack());
        BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, component, n, n8);
        boolean bl = BFRendering.isPointInRectangle(n12, n13, n10, 13.0, n3, n4);
        if (bl) {
            guiGraphics.renderTooltip(this.font, component2, n3, n4);
        }
        int n14 = 32;
        int n15 = 32;
        int n16 = field_885;
        int n17 = n - (32 * n16 + 32 * (n16 - 1)) / 2;
        int n18 = n8 - 16 - 6;
        for (int i = 0; i < n16; ++i) {
            int n19;
            int n20 = n17 + 64 * i + 16;
            boolean bl2 = n7 + 1 > i + 1;
            boolean bl3 = n7 + 1 > i + 2;
            boolean bl4 = i == n16 - 1;
            int n21 = n20 + 16 - 2;
            int n22 = n18 - 1;
            int n23 = 36;
            int n24 = n19 = bl3 ? ColorReferences.COLOR_TEAM_ALLIES_SOLID : ColorReferences.COLOR_WHITE_SOLID;
            if (!bl4) {
                BFRendering.rectangle(guiGraphics, n21, n22, 36, 2, n19);
            }
            if (!bl4 && !bl3 && bl2) {
                BFRendering.rectangleGradientWithVertices(poseStack, guiGraphics, n21, n22, 36.0f, 2.0f, ColorReferences.COLOR_TEAM_ALLIES_SOLID, n19);
            }
            BFRendering.centeredTextureSquare(poseStack, guiGraphics, field_864, n20, n18, 32);
            ResourceLocation resourceLocation = bl2 ? field_865 : field_866;
            BFRendering.centeredTextureSquare(poseStack, guiGraphics, resourceLocation, n20, n18, 32);
            ClientConnectingStage clientConnectingStage2 = ClientConnectingStage.values()[i];
            ResourceLocation resourceLocation2 = clientConnectingStage2.getIconTexture();
            BFRendering.centeredTextureSquare(poseStack, guiGraphics, resourceLocation2, n20, n18, 16);
        }
    }
}

