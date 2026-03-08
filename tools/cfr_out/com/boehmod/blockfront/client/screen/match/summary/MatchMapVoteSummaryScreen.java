/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.sounds.SoundEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.match.summary;

import com.boehmod.blockfront.client.gui.widget.MapVoteWidget;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.common.match.MapVoteEntry;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_551;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class MatchMapVoteSummaryScreen
extends MatchSummaryScreen {
    private static final Component field_781 = Component.translatable((String)"bf.message.game.match.summary.vote.subtitle").withStyle(ChatFormatting.BOLD);
    private static final Component field_782 = Component.translatable((String)"bf.message.game.match.summary.vote.subtitle.tip");
    private static final Component field_783 = Component.translatable((String)"bf.message.map.vote.no.maps.title").withStyle(ChatFormatting.BOLD);
    private static final Component field_784 = Component.translatable((String)"bf.message.map.vote.no.maps.message").withStyle(ChatFormatting.GRAY);
    private static final int field_779 = 600;
    private static final Component field_785 = Component.translatable((String)"bf.message.game.match.summary.vote.title").withStyle(BFStyles.BOLD);
    public static final MutableComponent field_6580 = Component.translatable((String)"bf.message.game.match.summary.vote.title.gamemode.same");
    public static final MutableComponent field_6581 = Component.translatable((String)"bf.message.game.match.summary.vote.title.gamemode.other");
    public static final int field_6582 = 640;
    public static final int field_6583 = 360;
    public static final int field_6584 = 5;
    public static final int field_6585 = 6;
    public static final int field_6586 = 55;
    private boolean field_777 = false;
    private boolean field_778 = false;
    private float field_775;
    private float field_776 = 0.0f;
    private boolean field_6579 = false;

    public MatchMapVoteSummaryScreen(@NotNull AbstractGame<?, ?, ?> abstractGame) {
        this(abstractGame, BFStats.SCORE);
    }

    public MatchMapVoteSummaryScreen(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull BFStat bFStat) {
        super(abstractGame.getUUID());
    }

    @Override
    public void tick() {
        super.tick();
        AbstractGameClient<?, ?> abstractGameClient = this.manager.getGameClient();
        if (abstractGameClient != null) {
            boolean bl;
            boolean bl2;
            boolean bl3 = bl2 = abstractGameClient.method_2678().getSecondsRemaining() <= 8;
            if (bl2 && !this.field_777) {
                this.field_777 = true;
                this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_GAME_SUMMARY_INTRO_MAP_VOTE_CONCLUDE.get()), (float)1.0f, (float)1.0f));
            }
            boolean bl4 = bl = abstractGameClient.method_2678().getSecondsRemaining() <= 2;
            if (bl && !this.field_778) {
                this.field_778 = true;
                this.manager.getCinematics().method_2198(new BF_551(1.0f, 2.0f, 1.0f));
            }
            this.field_776 = this.field_775;
            this.field_775 = MathUtils.moveTowards(this.field_775, bl ? 1.0f : 0.0f, 0.025f);
        }
    }

    @Override
    public void init() {
        super.init();
        this.method_637();
    }

    private void method_637() {
        AbstractGame<?, ?, ?> abstractGame = this.manager.getGame();
        if (abstractGame == null) {
            return;
        }
        this.field_1047.clear();
        int n = this.width / 2;
        int n2 = 106;
        int n3 = 60;
        int n4 = 5;
        int n5 = 85;
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (MapVoteEntry mapVoteEntry : abstractGame.getMapVoteManager().getEntries()) {
            if (!mapVoteEntry.getGameType().equals(abstractGame.getType())) continue;
            MapVoteWidget mapVoteWidget2 = new MapVoteWidget(n, 55, 106, 60, this, mapVoteEntry);
            objectArrayList.add((Object)mapVoteWidget2);
        }
        int n6 = objectArrayList.stream().mapToInt(mapVoteWidget -> mapVoteWidget.method_558() + 5).sum();
        int n7 = n - n6 / 2;
        int n8 = 2;
        for (MapVoteWidget mapVoteWidget3 : objectArrayList) {
            mapVoteWidget3.method_551(n7 + n8);
            this.method_764(mapVoteWidget3);
            n8 += mapVoteWidget3.method_558() + 5;
        }
        objectArrayList = new ObjectArrayList();
        for (MapVoteEntry mapVoteEntry : abstractGame.getMapVoteManager().getEntries()) {
            if (mapVoteEntry.getGameType().equals(abstractGame.getType())) continue;
            MapVoteWidget mapVoteWidget4 = new MapVoteWidget(n, 145, 106, 60, this, mapVoteEntry);
            objectArrayList.add((Object)mapVoteWidget4);
        }
        this.field_6579 = !objectArrayList.isEmpty();
        int n9 = objectArrayList.stream().mapToInt(mapVoteWidget -> mapVoteWidget.method_558() + 5).sum();
        int n10 = n - n9 / 2;
        int n11 = 2;
        for (MapVoteWidget mapVoteWidget3 : objectArrayList) {
            mapVoteWidget3.method_551(n10 + n11);
            this.method_764(mapVoteWidget3);
            n11 += mapVoteWidget3.method_558() + 5;
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        AbstractGame<?, ?, ?> abstractGame = this.manager.getGame();
        if (abstractGame == null) {
            return;
        }
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        if (abstractGame.getMapVoteManager().getEntries().isEmpty()) {
            BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, field_783, n3, n4 - 35, 1.0f);
            BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, field_784, n3, n4 - 20, 0.5f);
        }
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        float f2 = MathUtils.lerpf1(this.field_775, this.field_776, f);
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, 0, f2);
        poseStack.popPose();
        MutableComponent mutableComponent = Component.literal((String)StringUtils.makeFancy(field_6580.getString()));
        BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, (Component)mutableComponent, n3, 40);
        if (this.field_6579) {
            MutableComponent mutableComponent2 = Component.literal((String)StringUtils.makeFancy(field_6581.getString()));
            BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, (Component)mutableComponent2, n3, 130);
        }
    }

    @Override
    @NotNull
    protected SoundEvent method_642() {
        return (SoundEvent)BFSounds.GUI_GAME_SUMMARY_INTRO_MAP_VOTE.get();
    }

    @Override
    protected int method_649() {
        return 600;
    }

    @Override
    @NotNull
    protected Component method_645() {
        return field_785;
    }

    @Override
    @NotNull
    protected Component method_646() {
        return field_781;
    }

    @Override
    @NotNull
    protected Component method_647() {
        return field_782;
    }
}

