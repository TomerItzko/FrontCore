/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.minimap.MinimapWaypoint;
import com.boehmod.blockfront.common.net.packet.BFCapturePointSpawnPacket;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.conq.ConquestCapturePoint;
import com.boehmod.blockfront.game.impl.conq.ConquestGame;
import com.boehmod.blockfront.game.impl.conq.ConquestPlayerManager;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class ConquestMapWidget
extends BFWidget {
    private static final int field_596 = 20;
    private static final int field_597 = 10;
    private static final ResourceLocation field_590 = BFRes.loc("textures/gui/compass/waypoint_pp_player.png");
    private static final ResourceLocation field_591 = BFRes.loc("textures/text/cp_neutral.png");
    private static final ResourceLocation field_592 = BFRes.loc("textures/text/cp_axis.png");
    private static final ResourceLocation field_593 = BFRes.loc("textures/text/cp_allies.png");
    @NotNull
    private final ConquestGame field_594;
    @NotNull
    private final List<BF_125> field_599 = new ObjectArrayList();
    private int field_598 = 0;

    public ConquestMapWidget(int n, int n2, int n3, int n4, @NotNull Screen screen, @NotNull ConquestGame conquestGame) {
        super(n, n2, n3, n4, screen);
        this.field_594 = conquestGame;
        this.method_577();
    }

    private void method_576() {
        int n;
        int n2;
        int n3;
        int n4;
        this.field_599.removeIf(BF_125::method_584);
        ObjectArrayList objectArrayList = new ObjectArrayList(this.field_599);
        this.field_599.clear();
        double d = Double.MAX_VALUE;
        double d2 = Double.MAX_VALUE;
        double d3 = Double.MIN_VALUE;
        double d4 = Double.MIN_VALUE;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        if (abstractGameClient != null) {
            for (MinimapWaypoint minimapWaypoint : abstractGameClient.method_2727()) {
                d = Math.min(d, minimapWaypoint.method_360().x);
                d2 = Math.min(d2, minimapWaypoint.method_360().z);
                d3 = Math.max(d3, minimapWaypoint.method_360().x);
                d4 = Math.max(d4, minimapWaypoint.method_360().z);
            }
        }
        double d5 = d3 - d;
        double d6 = d4 - d2;
        double d7 = Math.min((double)(this.field_566 - 20) / d5, (double)(this.height - 20) / d6);
        if (abstractGameClient != null) {
            for (MinimapWaypoint object2 : abstractGameClient.method_2727()) {
                n4 = this.method_578(object2.method_360().x, d, d7);
                n3 = this.method_579(object2.method_360().z, d2, d7);
                n2 = (int)object2.method_362();
                n = (int)object2.method_362();
                ResourceLocation f = object2.getTexture();
                float f2 = MathUtils.snapToMultiple(object2.method_363(), 22.5f);
                this.field_599.add(new BF_126(this.field_594, n4, n3, n2, n, f2, f));
            }
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer != null) {
            n4 = this.method_578(localPlayer.getX(), d, d7);
            n3 = this.method_579(localPlayer.getZ(), d2, d7);
            n2 = 5;
            n = 5;
            float f = MathUtils.snapToMultiple(localPlayer.getYHeadRot() + 180.0f, 22.5f);
            this.field_599.add(new BF_126(this.field_594, n4, n3, 5, 5, f, field_590));
        }
        this.field_599.addAll((Collection<BF_125>)objectArrayList);
    }

    private void method_577() {
        int n;
        int n2;
        int n3;
        int n4;
        double d = Double.MAX_VALUE;
        double d2 = Double.MAX_VALUE;
        double d3 = Double.MIN_VALUE;
        double d4 = Double.MIN_VALUE;
        List<ConquestCapturePoint> list = this.field_594.getCapturePoints();
        for (ConquestCapturePoint abstractGameClient2 : list) {
            d = Math.min(d, abstractGameClient2.getX());
            d2 = Math.min(d2, abstractGameClient2.getZ());
            d3 = Math.max(d3, abstractGameClient2.getX());
            d4 = Math.max(d4, abstractGameClient2.getZ());
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        if (abstractGameClient != null) {
            for (MinimapWaypoint minimapWaypoint : abstractGameClient.method_2727()) {
                d = Math.min(d, minimapWaypoint.method_360().x);
                d2 = Math.min(d2, minimapWaypoint.method_360().z);
                d3 = Math.max(d3, minimapWaypoint.method_360().x);
                d4 = Math.max(d4, minimapWaypoint.method_360().z);
            }
        }
        double d5 = d3 - d;
        double d6 = d4 - d2;
        double d7 = Math.min((double)(this.field_566 - 20) / d5, (double)(this.height - 20) / d6);
        if (abstractGameClient != null) {
            for (MinimapWaypoint minimapWaypoint : abstractGameClient.method_2727()) {
                n4 = this.method_578(minimapWaypoint.method_360().x, d, d7);
                n3 = this.method_579(minimapWaypoint.method_360().z, d2, d7);
                n2 = (int)minimapWaypoint.method_362();
                n = (int)minimapWaypoint.method_362();
                ResourceLocation n5 = minimapWaypoint.getTexture();
                float f = MathUtils.snapToMultiple(minimapWaypoint.method_363(), 22.5f);
                this.field_599.add(new BF_126(this.field_594, n4, n3, n2, n, f, n5));
            }
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer != null) {
            n4 = this.method_578(localPlayer.getX(), d, d7);
            n3 = this.method_579(localPlayer.getZ(), d2, d7);
            n2 = 5;
            n = 5;
            float f = MathUtils.snapToMultiple(localPlayer.getYHeadRot() + 180.0f, 22.5f);
            this.field_599.add(new BF_126(this.field_594, n4, n3, 5, 5, f, field_590));
        }
        n4 = list.size();
        for (n3 = 0; n3 < n4; ++n3) {
            ConquestCapturePoint conquestCapturePoint = list.get(n3);
            n = this.method_578(conquestCapturePoint.getX(), d, d7);
            int n5 = this.method_579(conquestCapturePoint.getZ(), d2, d7);
            int n6 = 10;
            int n7 = 10;
            this.field_599.add(new BF_124(this.field_594, n, n5, 10, 10, n3));
        }
    }

    private int method_578(double d, double d2, double d3) {
        return this.field_564 + 10 + (int)((d - d2) * d3);
    }

    private int method_579(double d, double d2, double d3) {
        return this.field_565 + 10 + (int)((d - d2) * d3);
    }

    @Override
    public void method_537(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, float f) {
        super.method_537(minecraft, bFClientManager, clientPlayerDataHandler, f);
        if (this.field_598++ >= 20) {
            this.field_598 = 0;
            this.method_576();
        }
        for (BF_125 bF_125 : this.field_599) {
            bF_125.method_581(minecraft);
        }
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, BFRendering.translucentBlack());
        guiGraphics.enableScissor(this.field_564, this.field_565, this.field_564 + this.field_566, this.field_565 + this.height);
        for (BF_125 bF_125 : this.field_599) {
            bF_125.method_580(poseStack, font, guiGraphics, n, n2);
        }
        guiGraphics.disableScissor();
    }

    @Override
    public boolean onPress(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, double d, double d2, int n) {
        for (BF_125 bF_125 : this.field_599) {
            if (!bF_125.field_601) continue;
            bF_125.method_582(minecraft);
            return true;
        }
        return super.onPress(minecraft, bFClientManager, clientPlayerDataHandler, d, d2, n);
    }

    private static class BF_126
    extends BF_125 {
        @NotNull
        private final ResourceLocation field_608;
        private final float field_609;

        public BF_126(@NotNull ConquestGame conquestGame, int n, int n2, int n3, int n4, float f, @NotNull ResourceLocation resourceLocation) {
            super(conquestGame, n, n2, n3, n4);
            this.field_608 = resourceLocation;
            this.field_609 = f;
        }

        @Override
        public void method_580(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics guiGraphics, int n, int n2) {
            super.method_580(poseStack, font, guiGraphics, n, n2);
            BFRendering.centeredTexture(poseStack, guiGraphics, this.field_608, this.field_604 + this.field_606 / 2, this.field_605 + this.field_607 / 2, this.field_606, this.field_607, this.field_609);
        }

        @Override
        public void method_582(@NotNull Minecraft minecraft) {
        }

        @Override
        public boolean method_583() {
            return false;
        }

        @Override
        public boolean method_584() {
            return true;
        }
    }

    private static class BF_124
    extends BF_125 {
        private final int field_600;

        public BF_124(@NotNull ConquestGame conquestGame, int n, int n2, int n3, int n4, int n5) {
            super(conquestGame, n, n2, n3, n4);
            this.field_600 = n5;
        }

        @Override
        public void method_580(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics guiGraphics, int n, int n2) {
            super.method_580(poseStack, font, guiGraphics, n, n2);
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localPlayer = minecraft.player;
            if (localPlayer == null) {
                return;
            }
            List<ConquestCapturePoint> list = this.field_603.getCapturePoints();
            ConquestPlayerManager conquestPlayerManager = (ConquestPlayerManager)this.field_603.getPlayerManager();
            GameTeam gameTeam = conquestPlayerManager.getTeamByName("Axis");
            GameTeam gameTeam2 = conquestPlayerManager.getTeamByName("Allies");
            assert (gameTeam != null);
            assert (gameTeam2 != null);
            if (this.field_600 != -1) {
                ConquestCapturePoint conquestCapturePoint = list.get(this.field_600);
                if (!(conquestCapturePoint instanceof ConquestCapturePoint)) {
                    return;
                }
                ConquestCapturePoint conquestCapturePoint2 = conquestCapturePoint;
                GameTeam gameTeam3 = conquestCapturePoint2.getCbTeam();
                ResourceLocation resourceLocation = field_591;
                if (gameTeam.equals(gameTeam3)) {
                    resourceLocation = field_592;
                } else if (gameTeam2.equals(gameTeam3)) {
                    resourceLocation = field_593;
                }
                int n3 = this.field_606 / 2;
                BFRendering.texture(poseStack, guiGraphics, resourceLocation, this.field_604, this.field_605, this.field_606, this.field_607);
                MutableComponent mutableComponent = Component.literal((String)conquestCapturePoint2.name).withStyle(ChatFormatting.BOLD);
                BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)mutableComponent, (float)(this.field_604 + n3) + 0.5f, (float)(this.field_605 + 2), 0.75f);
                int n4 = BFUtils.getPlayerStat(this.field_603, localPlayer.getUUID(), BFStats.CAPTURE_POINT_SPAWN, -1);
                if (n4 == this.field_600) {
                    int n5 = ChatFormatting.GREEN.getColor() + -16777216;
                    BFRendering.rectangle(guiGraphics, this.field_604, this.field_605, this.field_606, 1, n5);
                    BFRendering.rectangle(guiGraphics, this.field_604, this.field_605 + this.field_607 - 1, this.field_606, 1, n5);
                    BFRendering.rectangle(guiGraphics, this.field_604, this.field_605, 1, this.field_607, n5);
                    BFRendering.rectangle(guiGraphics, this.field_604 + this.field_606 - 1, this.field_605, 1, this.field_607, n5);
                }
                if (this.field_601) {
                    BFRendering.rectangle(guiGraphics, this.field_604, this.field_605, this.field_606, 1, ColorReferences.COLOR_THEME_YELLOW_SOLID);
                    BFRendering.rectangle(guiGraphics, this.field_604, this.field_605 + this.field_607 - 1, this.field_606, 1, ColorReferences.COLOR_THEME_YELLOW_SOLID);
                    BFRendering.rectangle(guiGraphics, this.field_604, this.field_605, 1, this.field_607, ColorReferences.COLOR_THEME_YELLOW_SOLID);
                    BFRendering.rectangle(guiGraphics, this.field_604 + this.field_606 - 1, this.field_605, 1, this.field_607, ColorReferences.COLOR_THEME_YELLOW_SOLID);
                }
            }
        }

        @Override
        public void method_582(@NotNull Minecraft minecraft) {
            PacketUtils.sendToServer(new BFCapturePointSpawnPacket(this.field_600));
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.0f));
        }

        @Override
        public boolean method_583() {
            return true;
        }

        @Override
        public boolean method_584() {
            return false;
        }
    }

    public static abstract class BF_125 {
        @NotNull
        protected final ConquestGame field_603;
        protected final int field_604;
        protected final int field_605;
        protected final int field_606;
        protected final int field_607;
        protected boolean field_601 = false;
        private boolean field_602 = false;

        public BF_125(@NotNull ConquestGame conquestGame, int n, int n2, int n3, int n4) {
            this.field_603 = conquestGame;
            this.field_604 = n;
            this.field_605 = n2;
            this.field_606 = n3;
            this.field_607 = n4;
        }

        public void method_581(@NotNull Minecraft minecraft) {
            if (this.field_601) {
                if (!this.field_602) {
                    this.field_602 = true;
                    minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_HOVER.get()), (float)1.0f));
                }
            } else {
                this.field_602 = false;
            }
        }

        public void method_580(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics guiGraphics, int n, int n2) {
            this.field_601 = this.method_583() && new Rectangle(this.field_604, this.field_605, this.field_606, this.field_607).contains(n, n2);
        }

        public abstract void method_582(@NotNull Minecraft var1);

        public abstract boolean method_583();

        public abstract boolean method_584();
    }
}

