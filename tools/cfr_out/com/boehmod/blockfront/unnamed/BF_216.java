/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.profile.ProfileMainScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.ClanUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

public final class BF_216
extends BF_218 {
    private static final ResourceLocation field_1347 = BFRes.loc("textures/gui/scoreboard/trophy_gold.png");
    private static final ResourceLocation field_1348 = BFRes.loc("textures/gui/scoreboard/trophy_silver.png");
    private static final ResourceLocation field_1349 = BFRes.loc("textures/gui/scoreboard/trophy_bronze.png");
    @NotNull
    private final Type field_1344;
    @NotNull
    private final UUID field_1352;
    private final int field_1350;
    private final int field_1351;
    private boolean field_1346 = false;
    private boolean field_1345 = false;

    public BF_216(@NotNull UUID uUID, int n, int n2, @NotNull Type type) {
        this.field_1352 = uUID;
        this.field_1350 = n;
        this.field_1351 = n2;
        this.field_1344 = type;
    }

    @Override
    public void method_981(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull Font font, int n, int n2, float f, float f2) {
        int n3;
        int n4;
        PlayerCloudData playerCloudData;
        super.method_981(poseStack, minecraft, bFClientManager, guiGraphics, clientPlayerDataHandler, font, n, n2, f, f2);
        if (this.method_992()) {
            if (!this.field_1345) {
                this.field_1345 = true;
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, (float)2.0f));
            }
        } else {
            this.field_1345 = false;
        }
        int n5 = this.field_1357 + 3;
        int n6 = this.field_1358 + 3;
        int n7 = this.method_989() - 4;
        int n8 = this.height() - 3;
        BFRendering.rectangleWithDarkShadow(guiGraphics, n5, n6, n7, n8, BFRendering.translucentBlack());
        if (this.field_1346) {
            BFRendering.rectangle(guiGraphics, n5, n6, n7, n8, 0x11FFFFFF);
        }
        int n9 = 13;
        int n10 = 0xFFFFFF;
        int n11 = 0x22FFFFFF;
        if (this.field_1351 == 1) {
            n10 = ColorReferences.COLOR_BLACK_SOLID;
            n11 = -13995;
        } else if (this.field_1351 == 2) {
            n10 = ColorReferences.COLOR_BLACK_SOLID;
            n11 = -3419944;
        } else if (this.field_1351 == 3) {
            n10 = ColorReferences.COLOR_BLACK_SOLID;
            n11 = -4096689;
        }
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(this.field_1351)).withColor(n10);
        BFRendering.rectangle(guiGraphics, n5, n6, 13, n8, n11);
        BFRendering.centeredString(font, guiGraphics, (Component)mutableComponent, n5 + 7, n6 + 5);
        int n12 = 0;
        if (this.field_1344 == Type.PLAYER) {
            playerCloudData = clientPlayerDataHandler.getCloudProfile(this.field_1352);
            n4 = n8;
            n3 = n5 + 13 + 1;
            int n13 = n6;
            n12 += n4 + 2;
            BFRendering.playerHead(minecraft, bFClientManager, poseStack, guiGraphics, playerCloudData.getMcProfile(), n3, n13, n4);
        }
        switch (this.field_1344.ordinal()) {
            default: {
                throw new MatchException(null, null);
            }
            case 0: {
                Object object = ClanUtils.makeClanPrefix(clientPlayerDataHandler.getClanData(this.field_1352));
                break;
            }
            case 1: {
                Object object = playerCloudData = clientPlayerDataHandler.getCloudProfile(this.field_1352).method_1715();
            }
        }
        if (this.method_992()) {
            playerCloudData = Component.literal((String)playerCloudData.getString()).withColor(0xFFFF55);
        }
        n4 = n5 + n12 + 13 + 2;
        BFRendering.drawString(font, guiGraphics, (Component)playerCloudData, n4, n6 + 5);
        n3 = n5 + 120;
        MutableComponent mutableComponent2 = Component.literal((String)StringUtils.formatLong(this.field_1350)).withStyle(ChatFormatting.GREEN);
        BFRendering.drawString(font, guiGraphics, (Component)mutableComponent2, n3, n6 + 5);
        int n14 = 4;
        int n15 = n8 - 4;
        int n16 = n5 + n7 - n15 - 2;
        int n17 = n6 + 2;
        ResourceLocation resourceLocation = null;
        switch (this.field_1351) {
            case 1: {
                resourceLocation = field_1347;
                break;
            }
            case 2: {
                resourceLocation = field_1348;
                break;
            }
            case 3: {
                resourceLocation = field_1349;
            }
        }
        if (resourceLocation != null) {
            BFRendering.texture(poseStack, guiGraphics, resourceLocation, n16, n17, n15, n15);
        }
    }

    @NotNull
    public BF_216 method_972(boolean bl) {
        this.field_1346 = bl;
        return this;
    }

    @Override
    public void method_987(@NotNull Minecraft minecraft) {
    }

    @Override
    public boolean method_990() {
        return this.field_1344 == Type.PLAYER;
    }

    @Override
    public void method_982(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, int n, int n2, int n3) {
        if (this.field_1344 == Type.PLAYER) {
            PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(this.field_1352);
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)2.0f));
            minecraft.setScreen((Screen)new ProfileMainScreen(this.field_1354.getScreen(), playerCloudData));
        } else {
            super.method_982(minecraft, bFClientManager, clientPlayerDataHandler, n, n2, n3);
        }
    }

    @Override
    public int height() {
        return 20;
    }

    @Override
    public int method_989() {
        return this.field_1354.method_558() - 15;
    }

    public static enum Type {
        CLAN,
        PLAYER;

    }
}

