/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.PopupType
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  javax.annotation.Nullable
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.FormattedCharSequence
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen;

import com.boehmod.bflib.cloud.common.PopupType;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.PopupButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFOverlayScreen;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

public class PopupScreen
extends BFOverlayScreen {
    public static final int field_247 = 128;
    public static final int field_248 = 10;
    public static final int field_249 = 12;
    public static final float field_6301 = 0.4f;
    @NotNull
    private final Component field_256;
    private final ResourceLocation field_246;
    private final int field_251;
    private final int field_252;
    private final boolean field_253;
    @NotNull
    private final ObjectList<FormattedCharSequence> field_254;
    @NotNull
    private final PopupType field_250;
    @NotNull
    private final ObjectList<PopupButton> field_255 = new ObjectArrayList();

    public PopupScreen(@Nullable Screen screen, @NotNull Component component, @NotNull Component component2, @NotNull PopupType popupType, PopupButton ... popupButtonArray) {
        super(screen, (Component)Component.literal((String)(component.getString() + ", " + component2.getString())));
        this.field_256 = component;
        this.field_250 = popupType;
        this.field_246 = BFRes.loc("textures/gui/popup/" + popupType.getImage() + ".png");
        this.field_255.addAll(ObjectList.of((Object[])popupButtonArray));
        int n = 10;
        this.field_252 = 300;
        this.field_254 = new ObjectArrayList((Collection)this.font.split((FormattedText)component2, this.field_252 - 90));
        this.field_251 = this.field_254.size() * 10 + 80;
        this.field_253 = popupType == PopupType.FAIL;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void renderOverlay(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, int n, int n2, float f) {
        BFRendering.rectangle(graphics, 0, 0, this.width, this.height, BFRendering.translucentBlack(), 0.4f);
        float f2 = BFRendering.getRenderTime();
        MultiBufferSource.BufferSource bufferSource = graphics.bufferSource();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        int n5 = this.field_252 / 2;
        int n6 = this.field_251 / 2;
        int n7 = n3 - n5;
        int n8 = n4 - n6;
        int n9 = 2;
        int n10 = 64;
        int n11 = n7 + this.field_252 - 64 - 2;
        int n12 = n8 + 64;
        if (this.field_251 > 128) {
            n12 = n8 + this.field_251 - 64;
        }
        BFRendering.rectangleWithDarkShadow(graphics, n7, n8, this.field_252, this.field_251, BFRendering.translucentBlack());
        int n13 = n7 + 2;
        int n14 = n8 + 2;
        int n15 = this.field_252 - 4;
        int n16 = this.field_251 - 4;
        BFRendering.promptBackground(poseStack, graphics, n13, n14, n15, n16);
        BFRendering.enableScissor(graphics, n13 + 1, n14 + 1, n15 - 2, n16 - 2);
        BFRendering.centeredTintedTexture(poseStack, graphics, this.field_246, n11, n12, 128, 128, 0.0f, 1.0f, this.field_250.getColor() + ColorReferences.COLOR_BLACK_SOLID);
        graphics.disableScissor();
        int n17 = n7 + 12;
        int n18 = n8 + 12 - 1;
        int n19 = this.field_250.getColor();
        MutableComponent mutableComponent = this.field_256.copy().withStyle(BFStyles.BOLD).withColor(n19);
        BFRendering.component2d(poseStack, this.font, graphics, (Component)mutableComponent, n17, n18, 1.5f);
        BFRendering.renderTextLines(poseStack, this.font, (MultiBufferSource)bufferSource, this.field_254, n17, n18 + 20, 10);
        for (Renderable renderable : this.renderables) {
            renderable.render(graphics, n, n2, f);
        }
        for (Renderable renderable : this.renderables) {
            if (!(renderable instanceof BFButton)) continue;
            BFButton bFButton = (BFButton)renderable;
            bFButton.method_379(this.minecraft, poseStack, this.font, graphics, n, n2, f2);
        }
        poseStack.popPose();
    }

    @Override
    public void init() {
        super.init();
        this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)(this.field_253 ? (SoundEvent)SoundEvents.NOTE_BLOCK_BASS.value() : (SoundEvent)BFSounds.GUI_NEWMESSAGE.get()), (float)(this.field_253 ? 0.5f : 1.0f), (float)1.0f));
        int n = this.width / 2;
        int n2 = this.height / 2;
        int n3 = n - this.field_252 / 2;
        int n4 = n2 - this.field_251 / 2;
        int n5 = 5;
        int n6 = 5;
        int n7 = n3 + 8;
        int n8 = n4 + this.field_251 - 20;
        for (PopupButton popupButton : this.field_255) {
            int n9 = this.font.width((FormattedText)popupButton.text()) + 10;
            BFButton bFButton = new BFButton(n7, n8, n9, 13, popupButton.text(), popupButton.onPress()).displayType(BFButton.DisplayType.NONE);
            Component component = popupButton.toolTip();
            if (component != null) {
                bFButton = bFButton.tip(component);
            }
            this.addRenderableWidget((GuiEventListener)bFButton);
            n7 += n9 + 5;
        }
    }
}

