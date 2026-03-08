/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.MapVoteEntry;
import com.boehmod.blockfront.common.net.packet.BFGameVoteMapPacket;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public final class MapVoteWidget
extends BFWidget {
    private static final int field_787 = 16;
    private static final int field_788 = 12;
    private static final int field_6400 = 12;
    @NotNull
    private final MapVoteEntry field_786;
    @NotNull
    private final ResourceLocation field_6399;
    @NotNull
    private final ObjectList<ResourceLocation> field_6401 = new ObjectArrayList();

    public MapVoteWidget(int n, int n2, int n3, int n4, @NotNull Screen screen, @NotNull MapVoteEntry mapVoteEntry) {
        super(n, n2, n3, n4, screen);
        this.field_786 = mapVoteEntry;
        String string = mapVoteEntry.getName().toLowerCase(Locale.ROOT) + "_" + mapVoteEntry.method_5714().toLowerCase(Locale.ROOT);
        this.field_6399 = BFRes.loc("textures/gui/maps/" + string + ".png");
        for (BFCountry bFCountry : mapVoteEntry.getCountries()) {
            ResourceLocation resourceLocation = BFRes.loc("textures/misc/flag_" + bFCountry.getTag() + "_12x.png");
            this.field_6401.add((Object)resourceLocation);
        }
        this.method_531();
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        MutableComponent mutableComponent;
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        Set<UUID> set = this.field_786.getPlayerVotes();
        int n8 = this.field_566 / 2;
        int n9 = this.height / 2;
        BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, ColorReferences.COLOR_BLACK_SOLID, 0.5f);
        if (this.method_557()) {
            BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_564 - 1, this.field_565 - 1, this.field_566 + 2, this.height + 2, ColorReferences.COLOR_THEME_YELLOW_SOLID);
        }
        float f3 = 1.0f + (this.method_557() ? 0.2f : 0.0f);
        BFRendering.enableScissor(guiGraphics, this.field_564, this.field_565, this.field_566, this.height);
        BFRendering.centeredTextureScaled(poseStack, guiGraphics, this.field_6399, (float)(this.field_564 + n8), (float)(this.field_565 + n9), (float)this.field_566, (float)this.height, f3);
        guiGraphics.disableScissor();
        int n10 = 28;
        int n11 = this.field_565 + (this.height - 28);
        BFRendering.rectangleGradient(guiGraphics, this.field_564, n11, this.field_566, 28, ColorReferences.COLOR_BLACK_TRANSPARENT, -872415232);
        BFGameType bFGameType = BFGameType.getByName(this.method_641());
        String string = bFGameType != null ? bFGameType.getDisplayName().getString() : "Unknown";
        Style style = this.method_557() ? Style.EMPTY.withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID) : Style.EMPTY.withColor(ChatFormatting.WHITE);
        MutableComponent mutableComponent2 = Component.literal((String)this.method_640()).withStyle(style);
        String string2 = this.field_786.method_5714();
        if (!string2.equals("default")) {
            mutableComponent = Component.translatable((String)("bf.message.environment." + string2));
            mutableComponent2.append(" - ").append((Component)mutableComponent);
        }
        mutableComponent = Component.literal((String)StringUtils.makeFancy(string)).withStyle(ChatFormatting.GRAY);
        int n12 = this.field_565 + (this.height - 18);
        BFRendering.component2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent2, this.field_564 + 3, n12, this.method_557() ? ColorReferences.COLOR_THEME_YELLOW_SOLID : ColorReferences.COLOR_WHITE_SOLID);
        int n13 = n12 + 8;
        BFRendering.component2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent, this.field_564 + 3, n13, this.method_557() ? ColorReferences.COLOR_THEME_YELLOW_SOLID : ColorReferences.COLOR_WHITE_SOLID);
        int n14 = set.size();
        if (n14 > 0) {
            MutableComponent mutableComponent3 = Component.literal((String)StringUtils.formatLong(n14));
            n7 = this.field_564 + this.field_566 - font.width((FormattedText)mutableComponent3) - 2;
            int n15 = this.field_565 + this.height - 10;
            BFRendering.component2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent3, n7, n15, this.method_557() ? ColorReferences.COLOR_THEME_YELLOW_SOLID : ColorReferences.COLOR_WHITE_SOLID);
        }
        int n16 = 12;
        n7 = 0;
        for (UUID uUID : set) {
            n6 = n7 / 16;
            n5 = n7 % 16;
            n4 = this.field_564 + 3 + 4 * n5;
            n3 = this.field_565 + 3 + 2 * n6;
            PlayerCloudData playerCloudData2 = clientPlayerDataHandler.getCloudProfile(uUID);
            BFRendering.rectangle(guiGraphics, n4 - 1, n3 - 1, 14, 14, BFRendering.translucentBlack());
            BFRendering.playerHead(minecraft, bFClientManager, poseStack, guiGraphics, playerCloudData2.getMcProfile(), n4, n3, 12);
            ++n7;
        }
        int n17 = this.field_6401.size();
        int n18 = 2;
        n6 = 14 * n17 + 2;
        n5 = 16;
        n4 = 2;
        n3 = this.field_564 + this.field_566 - n6 - 2;
        int n19 = this.field_565 + 2;
        int n20 = BFRendering.translucentBlack();
        BFRendering.rectangle(guiGraphics, n3 + 1, n19, n6 - 2, 1, n20);
        BFRendering.rectangle(guiGraphics, n3, n19 + 1, n6, 14, n20);
        BFRendering.rectangle(guiGraphics, n3 + 1, n19 + 16 - 1, n6 - 2, 1, n20);
        for (int i = 0; i < this.field_6401.size(); ++i) {
            ResourceLocation resourceLocation = (ResourceLocation)this.field_6401.get(i);
            int n21 = n3 + i * 14 + 2;
            BFRendering.textureSquare(poseStack, guiGraphics, resourceLocation, n21, n19 + 2, 12);
        }
    }

    @Override
    public boolean onPress(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, double d, double d2, int n) {
        if (this.method_557()) {
            SoundManager soundManager = minecraft.getSoundManager();
            if (this.field_786.getPlayerVotes().contains(minecraft.getUser().getProfileId())) {
                SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_GAME_VOTE_ERROR.get()), (float)1.0f);
                soundManager.play((SoundInstance)simpleSoundInstance);
                return true;
            }
            SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.0f);
            soundManager.play((SoundInstance)simpleSoundInstance);
            PacketUtils.sendToServer(new BFGameVoteMapPacket(this.method_640(), this.method_641()));
            return true;
        }
        return false;
    }

    @NotNull
    private String method_640() {
        return this.field_786.getName();
    }

    @NotNull
    private String method_641() {
        return this.field_786.getGameType();
    }
}

