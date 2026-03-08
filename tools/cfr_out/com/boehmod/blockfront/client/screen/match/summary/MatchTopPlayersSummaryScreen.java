/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.CloudItemType
 *  com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin
 *  com.boehmod.bflib.cloud.common.player.PlayerRank
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.LivingEntity
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.match.summary;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.CloudItemType;
import com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin;
import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemRenderers;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class MatchTopPlayersSummaryScreen
extends MatchSummaryScreen {
    private static final int field_810 = 200;
    private static final int field_811 = 30;
    private static final int field_6311 = 120;
    private static final int field_6312 = 165;
    private static final int field_6314 = 10;
    private static final int field_6315 = 170;
    private static final float field_6304 = -5.0f;
    private static final float field_6305 = -15.0f;
    private static final Component field_813 = Component.translatable((String)"bf.message.game.match.summary.top.subtitle").withStyle(ChatFormatting.BOLD);
    private static final Component field_814 = Component.translatable((String)"bf.message.game.match.summary.top.subtitle.tip");
    private static final Component field_815 = Component.translatable((String)"bf.message.game.match.summary.top.title").withStyle(BFStyles.BOLD);
    @NotNull
    private static final ResourceLocation field_6332 = BFRes.loc("textures/gui/backshadow.png");
    @NotNull
    private static final ResourceLocation field_6333 = BFRes.loc("textures/gui/cornershadow.png");
    @NotNull
    private static final ResourceLocation field_6334 = BFRes.loc("textures/gui/game/summary/top_player_background.png");
    private static final int field_6656 = 20;
    @NotNull
    private final Set<UUID> field_816 = new LinkedHashSet<UUID>();
    @NotNull
    private final ObjectList<BF_1129> field_6331 = new ObjectArrayList();
    private int field_812 = 0;
    private boolean field_6653 = false;
    private int field_6657 = 20;
    private float field_6654 = 0.0f;
    private float field_6655 = 0.0f;
    private UUID field_6658 = null;
    @NotNull
    private final BFStat field_805;

    public MatchTopPlayersSummaryScreen(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull BFStat bFStat) {
        super(abstractGame.getUUID());
        Set<UUID> set = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs();
        ObjectArrayList objectArrayList = new ObjectArrayList(BFUtils.topPlayers(abstractGame, 3, bFStat, set));
        Collections.reverse(objectArrayList);
        this.field_816.addAll((Collection<UUID>)objectArrayList);
        this.field_805 = bFStat;
    }

    private void method_5629(@NotNull GuiGraphics guiGraphics, float f, PoseStack poseStack) {
        ClientPlayerDataHandler clientPlayerDataHandler;
        PlayerCloudData playerCloudData;
        Optional optional;
        float f2 = MathUtils.lerpf1(this.field_6654, this.field_6655, f);
        if (f2 >= 0.04f && (optional = (playerCloudData = (clientPlayerDataHandler = (ClientPlayerDataHandler)this.manager.getPlayerDataHandler()).getCloudProfile(this.field_6658)).getMood()).isPresent()) {
            String string = String.format("\"%s\" - %s", optional.get(), playerCloudData.getUsername());
            MutableComponent mutableComponent = Component.literal((String)string).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC);
            int n = this.font.width((FormattedText)mutableComponent);
            int n2 = this.height / 2 + 82 + 22;
            int n3 = this.width / 2;
            int n4 = n + 4;
            int n5 = 13;
            int n6 = n3 - n4 / 2;
            int n7 = n2 - 3;
            int n8 = MathUtils.withAlphaf(0, f2 * 0.5f);
            BFRendering.rectangle(guiGraphics, n6 + 1, n7, n4 - 2, 1, n8);
            BFRendering.rectangle(guiGraphics, n6, n7 + 1, n4, 11, n8);
            BFRendering.rectangle(guiGraphics, n6 + 1, n7 + 13 - 1, n4 - 2, 1, n8);
            int n9 = MathUtils.withAlphaf(0xFFFFFF, f2);
            BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, (Component)mutableComponent, n3, n2, n9, 1.0f);
        }
    }

    @Override
    public void tick() {
        super.tick();
        for (Iterator<UUID> iterator : this.field_6331) {
            ((BF_1129)((Object)iterator)).tick();
        }
        this.field_6655 = this.field_6654;
        if (this.field_6653 && this.field_6657-- <= 0) {
            this.field_6654 = MathUtils.moveTowards(this.field_6654, 1.0f, 0.05f);
        }
        if (this.field_812++ >= 30 && !this.field_816.isEmpty()) {
            Iterator<UUID> iterator;
            this.field_812 = 0;
            Object object = this.manager.getGame();
            iterator = this.field_816.iterator();
            if (iterator.hasNext()) {
                UUID uUID = (UUID)iterator.next();
                iterator.remove();
                int n = 0;
                if (object != null) {
                    n = BFUtils.getPlayerStat(object, uUID, this.field_805);
                }
                boolean bl = this.field_816.isEmpty();
                this.field_6331.add((Object)new BF_1129(uUID, n));
                SoundEvent soundEvent = bl ? (SoundEvent)BFSounds.GUI_GAME_SUMMARY_TOP_PLAYERS_ENTRY_FINAL.get() : (SoundEvent)BFSounds.GUI_GAME_SUMMARY_TOP_PLAYERS_ENTRY.get();
                this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)soundEvent, (float)1.0f));
                if (bl) {
                    this.field_6653 = true;
                    this.field_6658 = uUID;
                }
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        int n3 = 120 * this.field_6331.size() + 10 * (this.field_6331.size() - 1);
        int n4 = this.width / 2 - n3 / 2;
        int n5 = this.height / 2;
        int n6 = this.field_6331.size();
        int n7 = 0;
        for (BF_1129 bF_1129 : this.field_6331) {
            boolean bl = n7 == n6 - 1;
            int n8 = n4 + n7 * 130;
            bF_1129.method_5488(this.minecraft, this.manager, guiGraphics, this.font, n8, n5 - 82, bl, f);
            ++n7;
        }
        this.method_5629(guiGraphics, f, poseStack);
    }

    @Override
    @NotNull
    protected SoundEvent method_642() {
        return (SoundEvent)BFSounds.GUI_GAME_SUMMARY_INTRO_TOP_PLAYERS.get();
    }

    @Override
    protected int method_649() {
        return 200;
    }

    @Override
    @NotNull
    protected Component method_645() {
        return field_815;
    }

    @Override
    @NotNull
    protected Component method_646() {
        return field_813;
    }

    @Override
    @NotNull
    protected Component method_647() {
        return field_814;
    }

    public static final class BF_1129 {
        @NotNull
        private static final ResourceLocation field_6578 = BFRes.loc("textures/gui/game/summary/top_player_stat_background.png");
        private final UUID field_6339;
        private final int field_6338;
        private float field_6336 = 1.0f;
        private float field_6337 = 1.0f;

        public BF_1129(@NotNull UUID uUID, int n) {
            this.field_6339 = uUID;
            this.field_6338 = n;
        }

        private void tick() {
            this.field_6337 = this.field_6336;
            this.field_6336 = Mth.clamp((float)(this.field_6336 + 0.1f), (float)0.0f, (float)0.5f);
        }

        private void method_5488(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2, boolean bl, float f) {
            CloudItem cloudItem;
            ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
            PoseStack poseStack = guiGraphics.pose();
            BFRendering.rectangleWithDarkShadow(guiGraphics, n, n2, 120, 165, BFRendering.translucentBlack());
            BFRendering.enableScissor(guiGraphics, n, n2, 120, 165);
            BFRendering.texture(poseStack, guiGraphics, field_6334, n, n2, 120, 165, 0.4f);
            BFRendering.rectangle(guiGraphics, n, n2, 120, 165, ColorReferences.COLOR_TEAM_ALLIES_SOLID, 0.25f);
            PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(this.field_6339);
            PlayerRank playerRank = playerCloudData.getRank();
            PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
            if (bl) {
                float f2 = MathUtils.lerpf1(this.field_6336, this.field_6337, f);
                BFRendering.rectangle(guiGraphics, n, n2, 120, 165, ColorReferences.COLOR_WHITE_SOLID, f2);
            }
            BFRendering.centeredTextureSquare(poseStack, guiGraphics, field_6332, n + 60, n2 + 82, 150);
            int n3 = 3;
            int n4 = n + 3;
            int n5 = n2 + 3;
            int n6 = 114;
            int n7 = 159;
            BFRendering.promptBackground(poseStack, guiGraphics, n4, n5, 114, 159);
            BFRendering.texture(poseStack, guiGraphics, field_6333, n, n2, 120, 165);
            FakePlayer fakePlayer = BFRendering.ENVIRONMENT.getPlayerCached(minecraft, playerCloudData.getMcProfile());
            if (fakePlayer != null) {
                n4 = n + 60;
                n5 = n2 + 82;
                BFRendering.entity(clientPlayerDataHandler, poseStack, guiGraphics, minecraft, (LivingEntity)fakePlayer, n4, n5 + 170, 120.0f, -5.0f, -15.0f, 0.0f, f);
                poseStack.pushPose();
                poseStack.translate(0.0f, 0.0f, 1000.0f);
                n6 = 50;
                BFRendering.rectangleGradient(guiGraphics, n, n2 + 165 - 50, 120, 50, ColorReferences.COLOR_BLACK_TRANSPARENT, ColorReferences.COLOR_BLACK_SOLID);
                MutableComponent mutableComponent = Component.literal((String)playerCloudData.getUsername()).withStyle(BFStyles.BOLD);
                BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)mutableComponent, n4, n2 + 165 - 45, 1.5f);
                MutableComponent mutableComponent2 = Component.literal((String)StringUtils.makeFancy(playerRank.getTitle())).withStyle(ChatFormatting.GRAY);
                BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent2, n4, n2 + 165 - 29, 1.0f);
                poseStack.popPose();
            }
            guiGraphics.disableScissor();
            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, 1000.0f);
            int n8 = 32;
            n4 = 16;
            n5 = 2;
            n6 = playerCloudData.getPrestigeLevel();
            int n9 = n7 = n6 > 0 ? 1 : 0;
            if (n7 != 0) {
                ++n5;
            }
            int n10 = 32 * n5 + 16 * (n5 - 1);
            int n11 = n + 60 - n10 / 2 + 16;
            int n12 = n2 + 165 - 1;
            int n13 = n11;
            BFRendering.centeredTextureSquare(poseStack, guiGraphics, field_6578, n13, n12, 32);
            ResourceLocation resourceLocation = BFRes.loc("textures/misc/ranks/" + playerRank.getTexture() + ".png");
            BFRendering.centeredTextureSquare(poseStack, guiGraphics, resourceLocation, n13, n12, 32);
            int n14 = n11 + 32 + 16;
            BFRendering.centeredTextureSquare(poseStack, guiGraphics, field_6578, n14, n12, 32);
            MutableComponent mutableComponent = Component.literal((String)StringUtils.formatLong(this.field_6338)).withColor(16765813);
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)mutableComponent, n14, n12 - 3);
            if (n7 != 0) {
                int n15 = n11 + 96;
                BFRendering.centeredTextureSquare(poseStack, guiGraphics, field_6578, n15, n12, 32);
                MutableComponent mutableComponent3 = Component.literal((String)("P" + n6)).withColor(9633658);
                BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)mutableComponent3, n15, n12 - 3);
            }
            CloudItemStack cloudItemStack = playerCloudInventory.method_1673(CloudItemType.COIN);
            CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
            if (cloudItemStack != null && (cloudItem = cloudItemStack.getCloudItem(cloudRegistry)) instanceof AbstractCloudItemCoin) {
                AbstractCloudItemCoin abstractCloudItemCoin = (AbstractCloudItemCoin)cloudItem;
                int n16 = 16;
                CloudItemRenderer<?> cloudItemRenderer = CloudItemRenderers.getRenderer(abstractCloudItemCoin);
                cloudItemRenderer.method_1745(abstractCloudItemCoin, cloudItemStack, poseStack, minecraft, guiGraphics, n, n2, 16.0f, 16.0f, 1.0f);
            }
            poseStack.popPose();
        }
    }
}

