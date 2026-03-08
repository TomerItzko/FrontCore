/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.authlib.GameProfile
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.WinScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFScreen;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.unnamed.BF_190;
import com.boehmod.blockfront.util.BFLog;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.jetbrains.annotations.NotNull;

public final class BFCreditsScreen
extends BFScreen {
    private static final Style field_1142 = Style.EMPTY.withColor(TextColor.fromRgb((int)8159560));
    public static final int field_1143 = 30;
    @NotNull
    public List<BF_190> field_1141 = new ObjectArrayList();

    public BFCreditsScreen() {
        super((Component)Component.translatable((String)"bf.menu.button.credits"));
        this.manager.getCreditsEntryResource().method_1652().forEach(bFCreditsEntry -> {
            String string = bFCreditsEntry.name();
            String string2 = bFCreditsEntry.translation();
            try {
                UUID uUID = UUID.fromString(bFCreditsEntry.uuid());
                BF_190 bF_190 = new BF_190(uUID, string, string2);
                BFRendering.getSkinTexture(this.minecraft, this.manager, uUID);
                this.field_1141.add(bF_190);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                BFLog.logThrowable("Failed to parse UUID for mod credit: " + string, illegalArgumentException, new Object[0]);
            }
        });
    }

    public void tick() {
        super.tick();
        if (!this.field_1141.isEmpty()) {
            if (this.field_1141.getFirst().method_814(this.minecraft)) {
                this.field_1141.removeFirst();
            }
        } else {
            this.minecraft.setScreen((Screen)new WinScreen(false, () -> this.minecraft.setScreen((Screen)new LobbyTitleScreen())));
        }
    }

    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        if (this.field_1141.isEmpty()) {
            return;
        }
        BF_190 bF_190 = this.field_1141.getFirst();
        float f2 = bF_190.method_815(f);
        PoseStack poseStack = guiGraphics.pose();
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, ColorReferences.COLOR_BLACK_SOLID);
        this.method_819(poseStack, guiGraphics, bF_190, f);
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, 0, f2);
    }

    private void method_819(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull BF_190 bF_190, float f) {
        int n = this.width / 2;
        int n2 = this.height / 2;
        float f2 = bF_190.method_815(f);
        MutableComponent mutableComponent = Component.translatable((String)bF_190.method_816()).setStyle(field_1142);
        String string = bF_190.getName();
        UUID uUID = bF_190.getUUID();
        GameProfile gameProfile = new GameProfile(uUID, string);
        poseStack.pushPose();
        poseStack.translate(0.0f, 20.0f * f2, 0.0f);
        BFRendering.playerHead(this.minecraft, this.manager, poseStack, guiGraphics, gameProfile, (float)n - 15.0f, n2 - 20, 30);
        int n3 = n2 - 30 - 10;
        int n4 = 70;
        BFRendering.rectangleGradient(guiGraphics, 0, n3 - 70, this.width, 70, ColorReferences.COLOR_BLACK_TRANSPARENT, ColorReferences.COLOR_BLACK_SOLID);
        MutableComponent mutableComponent2 = Component.literal((String)bF_190.getName()).withColor(0xFFFFFF);
        BFRendering.centeredString(this.font, guiGraphics, (Component)mutableComponent2, n, n2 + 20);
        BFRendering.centeredString(this.font, guiGraphics, (Component)mutableComponent, n, n2 + 33);
        int n5 = this.font.width((FormattedText)mutableComponent) * 2;
        int n6 = n5 / 2;
        boolean bl = true;
        int n7 = n - n6;
        int n8 = n2 + 30;
        BFRendering.rectangleGradientWithVertices(poseStack, guiGraphics, n7, n8, n6, 1.0f, ColorReferences.COLOR_BLACK_TRANSPARENT, ColorReferences.COLOR_WHITE_SOLID);
        BFRendering.rectangleGradientWithVertices(poseStack, guiGraphics, n7 + n6, n8, n6, 1.0f, ColorReferences.COLOR_WHITE_SOLID, ColorReferences.COLOR_BLACK_TRANSPARENT);
        poseStack.popPose();
    }
}

