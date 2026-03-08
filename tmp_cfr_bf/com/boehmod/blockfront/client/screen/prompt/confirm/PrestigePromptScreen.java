/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.prompt.confirm;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.menu.MenuParticle;
import com.boehmod.blockfront.client.screen.prompt.confirm.BFConfirmPromptScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.ConfirmCallback;
import com.boehmod.blockfront.unnamed.BF_146;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PrestigePromptScreen
extends BFConfirmPromptScreen {
    private static final Component field_1516 = Component.translatable((String)"bf.message.prestige").withColor(0);
    private static final Component field_1517 = Component.translatable((String)"bf.message.cancel").withColor(0xFFFFFF);
    private static final ResourceLocation field_1515 = BFRes.loc("textures/gui/popup/player.png");
    private final List<MenuParticle> field_1514 = new ObjectArrayList();

    public PrestigePromptScreen(@NotNull Screen screen, @NotNull Component component, @NotNull ConfirmCallback confirmCallback) {
        super(screen, component, confirmCallback);
    }

    @Override
    public void tick() {
        super.tick();
        float f = BFRendering.getRenderTime();
        this.field_1514.removeIf(menuParticle -> menuParticle.shouldRemove(f));
        if (Math.random() < (double)0.4f) {
            float f2 = (float)((double)this.width * Math.random());
            BF_146 bF_146 = new BF_146(ColorReferences.COLOR_THEME_PRESTIGE_SOLID, f2, this.height);
            this.field_1514.add(bF_146);
        }
    }

    @Override
    public void method_758() {
        int n = 80;
        int n2 = 20;
        int n3 = this.width / 2 - 95;
        int n4 = 85 + 11 * this.field_1518.size() + 6;
        this.addRenderableWidget((GuiEventListener)new BFButton(n3 + 2, n4, 80, 20, field_1517, button -> {
            if (this.field_1522) {
                this.minecraft.setScreen(this.parentScreen);
            }
            this.method_1101(false);
        }).textStyle(BFButton.TextStyle.SHADOW).displayType(BFButton.DisplayType.SHADOW).method_388().method_395(-12891082).method_397(-10981296));
        this.addRenderableWidget((GuiEventListener)new BFButton(n3 + 190 - 80 - 2, n4, 80, 20, field_1516, button -> {
            if (this.field_1522) {
                this.minecraft.setScreen(this.parentScreen);
            }
            this.method_1101(true);
        }).displayType(BFButton.DisplayType.SHADOW).method_388().method_395(ColorReferences.COLOR_THEME_PRESTIGE_SOLID).method_397(-3670091));
    }

    @Override
    public void renderOverlay(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, int n, int n2, float f) {
        super.renderOverlay(poseStack, graphics, n, n2, f);
        float f2 = BFRendering.getRenderTime();
        for (MenuParticle menuParticle : this.field_1514) {
            menuParticle.renderAbove(this.minecraft, graphics, poseStack, f2, f);
            menuParticle.renderBelow(this.minecraft, graphics, poseStack, f2, f);
        }
        int n3 = ColorReferences.COLOR_THEME_PRESTIGE_SOLID;
        int n4 = this.height / 2;
        BFRendering.rectangleGradient(graphics, 0, this.height - n4, this.width, n4, n3 - ColorReferences.COLOR_BLACK_SOLID, n3 - -872415232);
    }

    @Override
    protected void method_1086(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics) {
        int n = this.width / 2;
        int n2 = 120;
        int n3 = 64;
        int n4 = 75;
        int n5 = n - 60;
        int n6 = n;
        int n7 = 43;
        BFRendering.rectangle(guiGraphics, n5, 75, 120, 1, ColorReferences.COLOR_THEME_PRESTIGE_SOLID);
        BFRendering.centeredTintedTexture(poseStack, guiGraphics, field_1515, n6, 43, 64, 64, 0.0f, 1.0f, ColorReferences.COLOR_THEME_PRESTIGE_SOLID);
        super.method_1086(poseStack, guiGraphics);
    }
}

