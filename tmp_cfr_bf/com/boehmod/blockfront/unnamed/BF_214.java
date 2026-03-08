/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.match.ClientMatchMaking;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class BF_214
extends BF_218 {
    @NotNull
    public static final ResourceLocation field_1336 = BFRes.loc("textures/gui/gamemode/ribbon.png");
    @NotNull
    public static final ResourceLocation field_1337 = BFRes.loc("textures/gui/menu/icons/check_white.png");
    @NotNull
    public static final ResourceLocation field_1338 = BFRes.loc("textures/gui/menu/icons/friends.png");
    @NotNull
    public static final ResourceLocation field_1339 = BFRes.loc("textures/gui/cornershadow.png");
    @NotNull
    public static final ResourceLocation field_1340 = BFRes.loc("textures/gui/gamemode/gamemode_background.png");
    @NotNull
    private final BFGameType field_1334;
    private float field_1332;
    private float field_1333 = 0.0f;
    private boolean field_1335 = false;

    public BF_214(@NotNull BFGameType bFGameType) {
        this.field_1334 = bFGameType;
    }

    @Override
    public void method_987(@NotNull Minecraft minecraft) {
        this.field_1333 = this.field_1332;
        this.field_1332 = Mth.lerp((float)0.5f, (float)this.field_1332, (float)(this.method_992() ? 1.0f : 0.0f));
    }

    public static void method_970(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull BFGameType bFGameType) {
        SoundManager soundManager = minecraft.getSoundManager();
        Supplier<SoundEvent> supplier = bFGameType.getSelectSound();
        if (supplier != null) {
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)supplier.get(), (float)1.0f));
        }
        ClientMatchMaking clientMatchMaking = bFClientManager.getMatchMaking();
        clientMatchMaking.setSearchGame(bFGameType.getSearchGame());
        clientMatchMaking.search(minecraft, bFClientManager);
        minecraft.setScreen((Screen)new LobbyTitleScreen());
    }

    @Override
    public void method_981(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull Font font, int n, int n2, float f, float f2) {
        String string;
        super.method_981(poseStack, minecraft, bFClientManager, guiGraphics, clientPlayerDataHandler, font, n, n2, f, f2);
        float f3 = this.method_971(f2);
        boolean bl = this.method_992();
        int n3 = this.method_989();
        int n4 = this.height();
        int n5 = n3 / 2;
        if (bl) {
            if (!this.field_1335) {
                this.field_1335 = true;
                String string2 = this.method_969().getDescription().getString();
                string = I18n.get((String)"bf.container.gamemode.click", (Object[])new Object[0]);
                String string3 = this.method_969().getDisplayName().getString() + ", " + string2 + ". " + string;
                String string4 = ChatFormatting.stripFormatting((String)string3);
                minecraft.getNarrator().sayNow(string4);
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_HOVER.get()), (float)1.0f));
            }
        } else {
            this.field_1335 = false;
        }
        BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_1357, this.field_1358 + 1, n3, n4, BFRendering.translucentBlack());
        BFRendering.rectangle(guiGraphics, this.field_1357, this.field_1358 + 1, n3, n4, -13750738);
        float f4 = 0.2f * f3;
        string = this.field_1334.getTexture();
        BFRendering.texture(poseStack, guiGraphics, (ResourceLocation)string, this.field_1357, this.field_1358 + 1, n3, n4, 0.4f + f4);
        BFRendering.rectangle(guiGraphics, this.field_1357, this.field_1358 + 1, n3, n4, ColorReferences.COLOR_TEAM_ALLIES_SOLID, 0.25f * f3);
        int n6 = 5;
        BFRendering.promptBackground(poseStack, guiGraphics, this.field_1357 + 5, this.field_1358 + 5, n3 - 10, n4 - 10);
        BFRendering.texture(poseStack, guiGraphics, field_1339, this.field_1357, this.field_1358 + 1, n3, n4);
        n6 = 32;
        int n7 = this.field_1357 + n5;
        int n8 = this.field_1358 + 35;
        int n9 = 16;
        ResourceLocation resourceLocation = this.field_1334.getIconTexture();
        BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, n7, n8, 32, 32, 0.0f, 0.4f);
        BFRendering.centeredTexture(poseStack, guiGraphics, field_1340, n7, n8, 32, 32);
        BFRendering.centeredTexture(poseStack, guiGraphics, resourceLocation, n7, n8, 16, 16);
        int n10 = this.field_1357 + n5;
        int n11 = n8 + 8 + 13;
        float f5 = 1.5f;
        MutableComponent mutableComponent = this.field_1334.getDisplayName().copy().withStyle(BFStyles.BOLD);
        List list = font.split((FormattedText)mutableComponent, (int)((float)(this.method_989() - 14) / 1.5f));
        BFRendering.renderTextLines(poseStack, font, (MultiBufferSource)guiGraphics.bufferSource(), list, n10, n11, 14, 1.5f, false);
        int n12 = n11 + 4 + 14 * list.size();
        float f6 = 0.5f;
        List list2 = font.split((FormattedText)this.field_1334.getDescription(), (int)((float)(this.method_989() - 14) / 0.5f));
        BFRendering.renderTextLines(poseStack, font, (MultiBufferSource)guiGraphics.bufferSource(), list2, n10, n12, 5, 0.5f, true);
        n6 = this.field_1357 + n5;
        n7 = this.field_1358 + this.height() - 30;
        n8 = 16;
        n9 = BFClientSettings.GAME_PLAYER_COUNT.getOrDefault((Object)this.field_1334.getName(), 0);
        resourceLocation = Component.literal((String)StringUtils.formatLong(n9)).withColor(n9 > 0 ? 11120486 : ChatFormatting.GRAY.getColor());
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.playercount", (Object[])new Object[]{resourceLocation});
        BFRendering.centeredTexture(poseStack, guiGraphics, field_1338, n6, n7, 16, 16);
        BFRendering.centeredString(font, guiGraphics, (Component)mutableComponent2, n6, n7 + 10);
        if (!this.field_1334.isExperimental()) {
            n6 = this.field_1357 + 18;
            n7 = this.field_1358;
            n8 = 16;
            n9 = 8;
            BFRendering.tintedTexture(poseStack, guiGraphics, field_1336, n6, n7, 16, 16, 0.0f, 1.0f, 8159560);
            BFRendering.centeredTexture(poseStack, guiGraphics, field_1337, n6 + 8, n7 + 8 - 2, 16, 16, 0.0f, 1.0f);
        }
    }

    @Override
    public void method_982(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, int n, int n2, int n3) {
        if (!this.method_992()) {
            super.method_982(minecraft, bFClientManager, clientPlayerDataHandler, n, n2, n3);
            return;
        }
        minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.0f));
        BF_214.method_970(minecraft, bFClientManager, this.field_1334);
    }

    @Override
    public boolean method_990() {
        return false;
    }

    @Override
    public int height() {
        return this.field_1354.height() - 17;
    }

    @Override
    public int method_989() {
        return 130;
    }

    @Override
    public int method_976() {
        return 5;
    }

    @Override
    public int method_977() {
        return 5;
    }

    public float method_971(float f) {
        return MathUtils.lerpf1(this.field_1332, this.field_1333, f);
    }

    public BFGameType method_969() {
        return this.field_1334;
    }
}

