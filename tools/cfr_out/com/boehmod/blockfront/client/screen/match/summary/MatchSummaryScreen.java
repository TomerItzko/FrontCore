/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  com.mojang.blaze3d.platform.Window
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.OverridingMethodsMustInvokeSuper
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.MouseHandler
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.match.summary;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.match.MatchPauseScreen;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryChatScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Set;
import java.util.UUID;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public abstract class MatchSummaryScreen
extends BFMenuScreen {
    private static final ResourceLocation field_791 = BFRes.loc("textures/gui/stopwatch.png");
    private static final MutableComponent field_6648 = Component.translatable((String)"bf.message.game.match.summary.button.skip");
    private static final MutableComponent field_6649 = Component.translatable((String)"bf.message.game.match.summary.button.skip.tip");
    private static final int field_793 = 256;
    private static final int field_794 = 30;
    private final int endTime = this.method_649();
    private int timer;
    private int field_796;
    private boolean isLastSummary = false;
    private boolean field_790 = false;
    @NotNull
    private final UUID field_797;

    public MatchSummaryScreen(@NotNull UUID uUID) {
        super((Component)Component.empty());
        this.field_797 = uUID;
    }

    @NotNull
    public MatchSummaryScreen setIsLastSummary(boolean isLastSummaru) {
        this.isLastSummary = isLastSummaru;
        return this;
    }

    @NotNull
    protected abstract SoundEvent method_642();

    protected abstract int method_649();

    @NotNull
    protected abstract Component method_645();

    @NotNull
    protected abstract Component method_646();

    @NotNull
    protected abstract Component method_647();

    public boolean isFinished() {
        return this.timer >= this.endTime;
    }

    private void method_643(@NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, float f) {
        int n = 2;
        float f2 = MathUtils.lerpf1(this.timer, this.field_796, f);
        float f3 = f2 / (float)this.endTime;
        float f4 = f3 * (float)this.width;
        BFRendering.rectangle(poseStack, guiGraphics, 0.0f, 30.0f, this.width, 2.0f, ColorReferences.COLOR_BLACK_SOLID, 0.25f);
        BFRendering.rectangle(poseStack, guiGraphics, 0.0f, 30.0f, f4, 2.0f, ColorReferences.COLOR_STOPWATCH_SOLID);
    }

    @Override
    public void method_758() {
        super.method_758();
        if (!this.isLastSummary) {
            int n = 5;
            int n2 = 50;
            int n3 = 20;
            int n4 = this.width - 5 - 50;
            int n5 = this.height - 5 - 20;
            BFButton bFButton = new BFButton(n4, n5, 50, 20, (Component)field_6648, button -> this.method_5746()).tip((Component)field_6649).textStyle(BFButton.TextStyle.SHADOW).displayType(BFButton.DisplayType.NONE);
            this.addRenderableWidget((GuiEventListener)bFButton);
        }
    }

    @OverridingMethodsMustInvokeSuper
    public void method_5746() {
        this.timer = this.endTime;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        int n3;
        AbstractGameClient<?, ?> abstractGameClient;
        super.render(guiGraphics, n, n2, f);
        int n4 = this.width / 2;
        int n5 = this.height / 2;
        PoseStack poseStack = guiGraphics.pose();
        Font font = this.minecraft.font;
        if (!this.isLastSummary) {
            this.method_643(guiGraphics, poseStack, f);
        }
        if ((abstractGameClient = this.manager.getGameClient()) != null) {
            int n6 = 10;
            int n7 = 7;
            n3 = 16;
            GameStageTimer gameStageTimer = abstractGameClient.method_2678();
            BFRendering.texture(poseStack, guiGraphics, field_791, 10, 7, 16, 16);
            BFRendering.component2dWithShadow(poseStack, font, guiGraphics, (Component)gameStageTimer.getComponent(), 30.0f, 10.0f, 0xFFFFFF, 1.5f);
        }
        BFRendering.centeredComponent2d(poseStack, font, guiGraphics, this.method_645(), n4, 6, 2.0f);
        Window window = this.minecraft.getWindow();
        MouseHandler mouseHandler = this.minecraft.mouseHandler;
        n3 = Mth.floor((double)(mouseHandler.xpos() * (double)window.getGuiScaledWidth() / (double)window.getScreenWidth()));
        int n8 = Mth.floor((double)(mouseHandler.ypos() * (double)window.getGuiScaledHeight() / (double)window.getScreenHeight()));
        this.minecraft.gui.getChat().render(guiGraphics, this.minecraft.gui.getGuiTicks(), n3, n8, false);
        boolean bl = InputConstants.isKeyDown((long)this.minecraft.getWindow().getWindow(), (int)this.minecraft.options.keyPlayerList.getKey().getValue());
        if (abstractGameClient != null && this.minecraft.player != null && bl) {
            Set<UUID> set = ((AbstractGamePlayerManager)((AbstractGame)abstractGameClient.getGame()).getPlayerManager()).getPlayerUUIDs();
            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, 1200.0f);
            PlayerCloudData playerCloudData = ((ClientPlayerDataHandler)this.manager.getPlayerDataHandler()).getCloudData(this.minecraft);
            abstractGameClient.method_2695(poseStack, guiGraphics, font, this.minecraft, this.manager, this.minecraft.player, playerCloudData, this.width, this.height, f);
            poseStack.popPose();
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        BFRendering.backgroundBlur(this.minecraft, f);
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, BFRendering.translucentBlack(), 0.25f);
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, 30, BFRendering.translucentBlack(), 0.35f);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void tick() {
        super.tick();
        AbstractGame<?, ?, ?> abstractGame = this.manager.getGame();
        if (abstractGame == null || !abstractGame.getUUID().equals(this.field_797)) {
            this.minecraft.setScreen(null);
            return;
        }
        this.minecraft.gui.getChat().tick();
        if (!this.field_790) {
            this.field_790 = true;
            this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)this.method_642(), (float)((float)((double)0.8f + (double)0.4f * Math.random())), (float)1.5f));
        }
        this.field_796 = this.timer++;
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(int n, int n2, int n3) {
        KeyMapping keyMapping = this.minecraft.options.keyChat;
        InputConstants.Key key = keyMapping.getKey();
        if (n == key.getValue()) {
            this.minecraft.setScreen((Screen)new MatchSummaryChatScreen(this));
            return false;
        }
        if (n == 256) {
            this.minecraft.setScreen((Screen)new MatchPauseScreen(this));
            return true;
        }
        return super.keyPressed(n, n2, n3);
    }
}

