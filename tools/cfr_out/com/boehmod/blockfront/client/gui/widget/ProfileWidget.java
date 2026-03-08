/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.CloudItemType
 *  com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin
 *  com.boehmod.bflib.cloud.common.mm.MatchParty
 *  com.boehmod.bflib.cloud.common.mm.SearchGame
 *  com.boehmod.bflib.cloud.common.player.PlayerGroup
 *  com.boehmod.bflib.cloud.common.player.PlayerRank
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.authlib.GameProfile
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.CloudItemType;
import com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin;
import com.boehmod.bflib.cloud.common.mm.MatchParty;
import com.boehmod.bflib.cloud.common.mm.SearchGame;
import com.boehmod.bflib.cloud.common.player.PlayerGroup;
import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemRenderers;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.profile.ProfileMainScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.EllipsisUtils;
import com.boehmod.blockfront.util.PlayerGroupUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class ProfileWidget
extends BFWidget {
    private static final Component field_633 = Component.translatable((String)"bf.container.profile.popup.prestige").withColor(ColorReferences.COLOR_THEME_PRESTIGE_SOLID);
    private static final Component field_634 = Component.translatable((String)"bf.menu.lobby.searching");
    private static final int field_638 = 16;
    private boolean field_637 = false;
    private float field_635;
    private float field_636 = 0.0f;

    public ProfileWidget(int n, int n2, int n3, int n4, Screen screen) {
        super(n, n2, n3, n4, screen);
    }

    private void method_597(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull GameProfile gameProfile, @NotNull PlayerCloudData playerCloudData, @NotNull String string, float f) {
        Object object;
        Object object2;
        MutableComponent mutableComponent;
        CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
        String string2 = "default";
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        CloudItemStack cloudItemStack = playerCloudInventory.method_1673(CloudItemType.CARD);
        if (cloudItemStack != null && cloudItemStack.getCloudItem(cloudRegistry) != null) {
            string2 = cloudItemStack.getCloudItem(cloudRegistry).getSuffixForDisplay();
        }
        ResourceLocation resourceLocation = BFRes.loc("textures/gui/callingcard/" + string2 + ".png");
        BFRendering.texture(poseStack, guiGraphics, resourceLocation, this.field_564, this.field_565, this.field_566, 25, 0.0f, 0.75f);
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565 + 26, this.field_566, 14, BFRendering.translucentBlack());
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565 + 2, this.field_566, 21, 0, 0.5f);
        MutableComponent mutableComponent2 = Component.empty();
        Optional<PlayerGroup> optional = playerCloudData.getGroup();
        if (optional.isPresent()) {
            mutableComponent = PlayerGroupUtils.getComponent(optional.get());
            mutableComponent2.append((Component)mutableComponent).append(" ");
        }
        mutableComponent = Component.literal((String)string).withColor(0xFFFFFF);
        mutableComponent2.append((Component)mutableComponent);
        BFRendering.playerHead(minecraft, bFClientManager, poseStack, guiGraphics, gameProfile, this.field_564 + 1, this.field_565 + 3, 19);
        BFRendering.drawStringWithShadow(font, guiGraphics, (Component)mutableComponent2, this.field_564 + 22, this.field_565 + 4);
        CloudItemStack cloudItemStack2 = playerCloudInventory.method_1673(CloudItemType.COIN);
        if (cloudItemStack2 != null && (object2 = cloudItemStack2.getCloudItem(cloudRegistry)) instanceof AbstractCloudItemCoin) {
            object = (AbstractCloudItemCoin)object2;
            object2 = CloudItemRenderers.getRenderer(object);
            ((CloudItemRenderer)object2).method_1745(object, cloudItemStack2, poseStack, minecraft, guiGraphics, this.field_564, this.field_565, 16.0f, 16.0f, 1.0f);
        }
        this.method_599(guiGraphics, font, poseStack, playerCloudData);
        this.method_598(guiGraphics, playerCloudData);
        this.method_596(poseStack, guiGraphics, playerCloudData, f);
        object = StringUtils.formatLong(playerCloudData.getTrophies());
        object2 = Component.literal((String)object).withColor(16765813);
        BFRendering.drawStringWithShadow(font, guiGraphics, (Component)object2, this.field_564 + 32, this.field_565 + 13);
        BFRendering.tintedTexture(poseStack, guiGraphics, BFMenuScreen.TROPHY, this.field_564 + 22, this.field_565 + 13, 8, 8, 0.0f, 1.0f, ColorReferences.COLOR_STOPWATCH_SOLID);
    }

    private void method_599(@NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull PlayerCloudData playerCloudData) {
        int n = playerCloudData.getExp();
        PlayerRank playerRank = PlayerRank.getRankFromEXP((int)n);
        boolean bl = playerCloudData.canPrestige();
        int n2 = playerCloudData.getPrestigeLevel();
        ResourceLocation resourceLocation = BFRes.loc("textures/misc/ranks/" + playerRank.getTexture() + ".png");
        MutableComponent mutableComponent = Component.literal((String)playerRank.getTitle()).withColor(bl ? ColorReferences.COLOR_THEME_PRESTIGE_SOLID : ColorReferences.COLOR_WHITE_SOLID);
        MutableComponent mutableComponent2 = Component.literal((String)(n2 > 0 ? "P" + n2 + " " : "")).withColor(ColorReferences.COLOR_THEME_PRESTIGE_SOLID);
        MutableComponent mutableComponent3 = mutableComponent2.append((Component)mutableComponent);
        BFRendering.drawString(font, guiGraphics, (Component)mutableComponent3, this.field_564 + 17, this.field_565 + 29);
        BFRendering.texture(poseStack, guiGraphics, BFMenuScreen.BG_US, this.field_564, this.field_565 + 26, 14, 14);
        BFRendering.texture(poseStack, guiGraphics, resourceLocation, this.field_564, this.field_565 + 26, 14, 14);
    }

    private void method_598(@NotNull GuiGraphics guiGraphics, @NotNull PlayerCloudData playerCloudData) {
        int n = playerCloudData.getExp();
        PlayerRank playerRank = PlayerRank.getRankFromEXP((int)n);
        PlayerRank playerRank2 = PlayerRank.getNextRank((int)playerRank.getID());
        float f = (float)(n - PlayerRank.getTotalRequiredEXPForRank((PlayerRank)playerRank)) / (float)playerRank2.getEXPRequired();
        int n2 = this.field_565 + this.height - 16;
        int n3 = (int)((float)this.field_566 * f);
        int n4 = playerCloudData.canPrestige() ? ColorReferences.COLOR_THEME_PRESTIGE_SOLID : ColorReferences.COLOR_WHITE_SOLID;
        BFRendering.rectangle(guiGraphics, this.field_564, n2, this.field_566, 1, BFRendering.RED_CHAT_COLOR);
        BFRendering.enableScissor(guiGraphics, this.field_564, n2, n3, 1);
        BFRendering.rectangle(guiGraphics, this.field_564, n2, this.field_566, 1, n4);
        guiGraphics.disableScissor();
    }

    private void method_596(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull PlayerCloudData playerCloudData, float f) {
        int n = playerCloudData.getExp();
        PlayerRank playerRank = PlayerRank.getRankFromEXP((int)n);
        PlayerRank playerRank2 = PlayerRank.getNextRank((int)playerRank.getID());
        float f2 = (float)(n - PlayerRank.getTotalRequiredEXPForRank((PlayerRank)playerRank)) / (float)playerRank2.getEXPRequired();
        float f3 = Math.max(0.0f, (float)this.field_566 * f2);
        int n2 = this.field_565 + this.height - 16;
        int n3 = playerCloudData.canPrestige() ? ColorReferences.COLOR_THEME_PRESTIGE_SOLID : ColorReferences.COLOR_WHITE_SOLID;
        float f4 = 88.0f;
        float f5 = 43.0f;
        float f6 = Mth.sin((float)(f / 5.0f));
        float f7 = Mth.sin((float)(f / 7.0f)) * f6;
        float f8 = Mth.sin((float)(f / 10.0f)) * f7;
        if (n > 0) {
            BFRendering.enableScissor(guiGraphics, this.field_564, this.field_565, this.field_566, this.height);
            BFRendering.centeredTintedTexture(poseStack, guiGraphics, BFMenuScreen.FLARE_WHITE, (float)this.field_564 + f3, (float)n2 + 0.5f, 88.0f, 43.0f, 0.0f, 1.0f, n3);
            BFRendering.centeredTintedTexture(poseStack, guiGraphics, BFMenuScreen.FLARE_WHITE, (float)this.field_564 + f3, (float)n2 + 0.5f, 88.0f, 43.0f, 0.0f, 1.5f + f8, n3);
            guiGraphics.disableScissor();
        }
    }

    private void method_595(@NotNull BFClientManager bFClientManager, @NotNull Font font, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, float f) {
        int n = this.field_566 / 2;
        int n2 = this.height / 2;
        int n3 = this.field_566 + 25;
        float f2 = bFClientManager.getMatchMaking().method_1912(1.0f);
        float f3 = (Mth.sin((float)(f / 20.0f)) + 1.0f) / 2.0f;
        float f4 = (0.5f + f3 * 0.5f) * f2;
        BFRendering.centeredTintedTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW_WHITE, this.field_564 + n, this.field_565 + n2, n3, n3, 0.0f, f4, ColorReferences.COLOR_TEAM_ALLIES_SOLID);
        BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, ColorReferences.COLOR_TEAM_ALLIES_SOLID, 0.5f);
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565, this.field_566, 1, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565 + this.height - 1, this.field_566, 1, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565 + 1, 1, this.height - 2, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.rectangle(guiGraphics, this.field_564 + this.field_566 - 1, this.field_565 + 1, 1, this.height - 2, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.enableScissor(guiGraphics, this.field_564 + 1, this.field_565 + 1, this.field_566 - 2, this.height - 2);
        SearchGame searchGame = bFClientManager.getMatchMaking().getSearchGame();
        ResourceLocation resourceLocation = BFRes.loc("textures/gui/gamemode/gamemode_" + searchGame.getId() + ".png");
        int n4 = 520;
        int n5 = 520;
        BFRendering.centeredTextureScaled(poseStack, guiGraphics, resourceLocation, this.field_564 + n, this.field_565 + n2, 520.0f, 520.0f, 0.25f, 0.25f);
        float f5 = BFRendering.getRenderTime() * 3.0f;
        int n6 = 60;
        int n7 = 25;
        int n8 = this.field_564 + n;
        int n9 = this.field_565 + n2;
        poseStack.pushPose();
        poseStack.translate((float)n8, (float)n9, 0.0f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(f5));
        BFRendering.orderedRectangle(poseStack, 0.0f, 0.0f, 60.0f, 25.0f, ColorReferences.COLOR_TEAM_ALLIES_SOLID, 2);
        BFRendering.orderedRectangle(poseStack, 0.0f, -25.0f, 60.0f, 25.0f, ColorReferences.COLOR_TEAM_ALLIES_SOLID, 3);
        poseStack.popPose();
        MutableComponent mutableComponent = Component.literal((String)(" " + bFClientManager.getMatchMaking().getSearchRegion().toString().toUpperCase(Locale.ROOT)));
        MutableComponent mutableComponent2 = field_634.copy().append((Component)mutableComponent).append(EllipsisUtils.cyclingEllipsis(f)).withStyle(ChatFormatting.BOLD).withColor(11120486);
        n7 = 16;
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565 + this.height / 2 - 8 + 2, this.field_566, 16, BFRendering.translucentBlack());
        BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent2, this.field_564 + n, this.field_565 + this.height / 2 - 2);
        guiGraphics.disableScissor();
        float f6 = (Mth.sin((float)(f / 10.0f)) + 1.0f) / 2.0f;
        n9 = this.field_564 + 1;
        int n10 = this.field_565 + 1;
        BFRendering.rectangle(guiGraphics, n9, n10, this.field_566 - 2, 1, ColorReferences.COLOR_TEAM_ALLIES_SOLID, f6);
        BFRendering.rectangle(guiGraphics, n9, n10 + 1, 1, this.height - 4, ColorReferences.COLOR_TEAM_ALLIES_SOLID, f6);
        BFRendering.rectangle(guiGraphics, n9 + (this.field_566 - 3), n10 + 1, 1, this.height - 4, ColorReferences.COLOR_TEAM_ALLIES_SOLID, f6);
        BFRendering.rectangle(guiGraphics, n9, n10 + (this.height - 3), this.field_566 - 2, 1, ColorReferences.COLOR_TEAM_ALLIES_SOLID, f6);
    }

    @Override
    public void method_537(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, float f) {
        super.method_537(minecraft, bFClientManager, clientPlayerDataHandler, f);
        SoundManager soundManager = minecraft.getSoundManager();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        boolean bl = playerCloudData.canPrestige();
        Optional optional = playerCloudData.getParty();
        boolean bl2 = bFClientManager.getMatchMaking().isSearching() || optional.isPresent() && ((MatchParty)optional.get()).isSearching();
        float f2 = Mth.sin((float)(f / 10.0f));
        float f3 = bl ? 0.5f - f2 / 5.0f : (this.method_557() ? 1.0f : 0.0f);
        this.field_636 = this.field_635;
        this.field_635 = Mth.lerp((float)0.5f, (float)this.field_635, (float)f3);
        if (this.method_557() && !bl2) {
            if (!this.field_637) {
                this.field_637 = true;
                soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_HOVER.get()), (float)2.0f));
            }
        } else if (this.field_637) {
            this.field_637 = false;
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_HOVER.get()), (float)1.5f));
        }
    }

    @Override
    public boolean onPress(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, double d, double d2, int n) {
        boolean bl;
        PlayerCloudData playerCloudData = ((ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler()).getCloudData(minecraft);
        Optional optional = playerCloudData.getParty();
        boolean bl2 = bl = bFClientManager.getMatchMaking().isSearching() || optional.isPresent() && ((MatchParty)optional.get()).isSearching();
        if (this.method_557() && !bl && n == 0) {
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.0f));
            minecraft.setScreen((Screen)new ProfileMainScreen(minecraft.screen));
            return true;
        }
        return super.onPress(minecraft, bFClientManager, clientPlayerDataHandler, d, d2, n);
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        Optional optional = playerCloudData.getParty();
        boolean bl = bFClientManager.getMatchMaking().isSearching() || optional.isPresent() && ((MatchParty)optional.get()).isSearching();
        GameProfile gameProfile = minecraft.getGameProfile();
        String string = minecraft.getUser().getName();
        if (!bl) {
            boolean bl2 = playerCloudData.canPrestige();
            int n3 = bl2 ? ColorReferences.COLOR_THEME_PRESTIGE_SOLID : ColorReferences.COLOR_WHITE_SOLID;
            BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, BFRendering.translucentBlack());
            int n4 = this.field_566 / 2;
            int n5 = this.height / 2;
            float f3 = MathUtils.lerpf1(this.field_635, this.field_636, f2);
            BFRendering.rectangle(guiGraphics, this.field_564 - 1, this.field_565 - 1, this.field_566 + 2, 1, n3, f3);
            BFRendering.rectangle(guiGraphics, this.field_564 - 1, this.field_565, 1, this.height, n3, f3);
            BFRendering.rectangle(guiGraphics, this.field_564 + this.field_566, this.field_565, 1, this.height, n3, f3);
            BFRendering.rectangle(guiGraphics, this.field_564 - 1, this.field_565 + this.height, this.field_566 + 2, 1, n3, f3);
            int n6 = this.field_566;
            BFRendering.centeredTintedTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW_WHITE, this.field_564 + n4, this.field_565 + n5, n6, n6, 0.0f, f3, n3);
        }
        if (bl) {
            this.method_595(bFClientManager, font, guiGraphics, poseStack, f);
        } else {
            this.method_597(minecraft, bFClientManager, guiGraphics, font, poseStack, gameProfile, playerCloudData, string, f);
        }
    }

    @Override
    public void method_541(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, Font font, @NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        boolean bl;
        super.method_541(minecraft, bFClientManager, clientPlayerDataHandler, font, guiGraphics, n, n2, f);
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        Optional optional = playerCloudData.getParty();
        boolean bl2 = bl = bFClientManager.getMatchMaking().isSearching() || optional.isPresent() && ((MatchParty)optional.get()).isSearching();
        if (!this.method_557() || bl) {
            return;
        }
        int n3 = playerCloudData.getExp();
        PlayerRank playerRank = playerCloudData.getRank();
        PlayerRank playerRank2 = PlayerRank.getNextRank((int)playerRank.getID());
        int n4 = PlayerRank.getTotalRequiredEXPForRank((PlayerRank)playerRank2);
        boolean bl3 = playerCloudData.canPrestige();
        int n5 = PlayerRank.getTotalRequiredEXPForRank((PlayerRank)playerRank);
        int n6 = bl3 ? ColorReferences.COLOR_THEME_PRESTIGE_SOLID : ColorReferences.COLOR_THEME_YELLOW_SOLID;
        MutableComponent mutableComponent = Component.literal((String)StringUtils.formatLong(n3)).withColor(n6);
        MutableComponent mutableComponent2 = Component.literal((String)StringUtils.formatLong(n4)).withColor(n6);
        MutableComponent mutableComponent3 = Component.literal((String)" / ").withColor(0xFFFFFF);
        Object object = bl3 ? field_633 : Component.translatable((String)"bf.container.profile.popup.rank", (Object[])new Object[]{Component.literal((String)playerRank2.getTitle()).withStyle(ChatFormatting.GRAY)});
        MutableComponent mutableComponent4 = Component.literal((String)StringUtils.formatLong(n3 - n5)).withStyle(ChatFormatting.GRAY);
        MutableComponent mutableComponent5 = Component.literal((String)StringUtils.formatLong(n4 - n5)).withStyle(ChatFormatting.GRAY);
        MutableComponent mutableComponent6 = Component.literal((String)"Progress: ").append((Component)mutableComponent4).append((Component)Component.literal((String)" / ").withColor(0xFFFFFF)).append((Component)mutableComponent5);
        MutableComponent mutableComponent7 = Component.literal((String)"Total EXP: ").append((Component)mutableComponent).append((Component)mutableComponent3).append((Component)mutableComponent2);
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(mutableComponent6);
        objectArrayList.add(mutableComponent7);
        objectArrayList.add(object);
        guiGraphics.renderTooltip(font, (List)objectArrayList, Optional.empty(), n, n2);
    }
}

