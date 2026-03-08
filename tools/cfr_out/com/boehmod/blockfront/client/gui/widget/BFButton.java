/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_51;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BFButton
extends Button {
    private static final ResourceLocation field_283 = BFRes.loc("textures/gui/effects/fade_red.png");
    @Nullable
    protected ResourceLocation texture = null;
    @Nullable
    protected ResourceLocation field_282 = null;
    protected float field_310 = 1.0f;
    @Nullable
    protected ResourceLocation field_289 = null;
    protected int width = 0;
    protected int height = 0;
    protected int field_299 = 0;
    protected int field_300 = 0;
    protected float field_311 = 0.0f;
    protected float field_312 = 0.0f;
    private TextStyle textStyle = TextStyle.DEFAULT;
    private int field_301 = ColorReferences.COLOR_WHITE_SOLID;
    private int field_302 = -7829368;
    private int field_308 = -12303292;
    private int field_309 = MathUtils.withAlphaf(BFRendering.translucentBlack(), 85.0f);
    private int field_296 = BFRendering.translucentBlack();
    private boolean field_303 = true;
    private float field_313 = 1.0f;
    @Nullable
    private Component field_314 = null;
    @Nullable
    private Predicate<BFButton> enabledPredicate = null;
    private boolean field_304 = false;
    @NotNull
    private Alignment alignment = Alignment.CENTER;
    @NotNull
    private DisplayType displayType = DisplayType.DEFAULT;
    private boolean field_305 = true;
    private boolean field_306 = false;
    @Nullable
    private BF_51 field_290;
    @Nullable
    private Button.OnPress field_307;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> field_295 = BFSounds.GUI_BUTTON_PRESS;

    public BFButton(int n, int n2, int n3, int n4, @NotNull Component component, @NotNull Button.OnPress onPress) {
        super(n, n2, n3, n4, component, onPress, DEFAULT_NARRATION);
    }

    @NotNull
    public BFButton method_369(int n) {
        this.field_301 = n;
        return this;
    }

    @NotNull
    public BFButton textStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    @NotNull
    public BFButton method_390(int n) {
        this.field_302 = n;
        return this;
    }

    @NotNull
    public BFButton method_393(int n) {
        this.field_308 = n;
        return this;
    }

    @NotNull
    public BFButton enabled(@NotNull Predicate<BFButton> predicate) {
        this.enabledPredicate = predicate;
        this.active = predicate.test(this);
        return this;
    }

    @NotNull
    public BFButton method_386(@Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        this.field_295 = deferredHolder;
        return this;
    }

    @NotNull
    public BFButton method_382(@NotNull Button.OnPress onPress) {
        this.field_307 = onPress;
        return this;
    }

    @NotNull
    public BFButton texture(@NotNull ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    @NotNull
    public BFButton size(int size) {
        return this.size(size, size);
    }

    @NotNull
    public BFButton size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @NotNull
    public BFButton method_391(int n, int n2) {
        this.field_299 = n;
        this.field_300 = n2;
        return this;
    }

    @NotNull
    public BFButton method_368(float f, float f2) {
        this.field_311 = f;
        this.field_312 = f2;
        return this;
    }

    @NotNull
    public BFButton method_366() {
        this.field_305 = false;
        return this;
    }

    @NotNull
    public BFButton tip(@Nullable Component component) {
        this.field_314 = component;
        return this;
    }

    @NotNull
    public BFButton alignment(@NotNull Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @NotNull
    public BFButton displayType(@NotNull DisplayType displayType) {
        this.displayType = displayType;
        return this;
    }

    @NotNull
    public BFButton method_371(@NotNull BF_51 bF_51) {
        this.field_290 = bF_51;
        return this;
    }

    @NotNull
    public BFButton method_392(@NotNull ResourceLocation resourceLocation) {
        return this.method_385(resourceLocation, resourceLocation);
    }

    @NotNull
    public BFButton method_385(@NotNull ResourceLocation resourceLocation, @NotNull ResourceLocation resourceLocation2) {
        this.field_282 = resourceLocation;
        this.field_289 = resourceLocation2;
        return this;
    }

    @NotNull
    public BFButton method_367(float f) {
        this.field_310 = Mth.clamp((float)f, (float)0.0f, (float)1.0f);
        return this;
    }

    @NotNull
    public BFButton method_387(boolean bl) {
        this.field_304 = bl;
        return this;
    }

    @NotNull
    public BFButton method_389(float f) {
        this.field_313 = f;
        return this;
    }

    @NotNull
    public BFButton method_395(int n) {
        this.field_296 = n;
        return this;
    }

    @NotNull
    public BFButton method_388() {
        this.field_303 = false;
        return this;
    }

    public void playDownSound(@NotNull SoundManager soundManager) {
        if (this.field_295 != null) {
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)this.field_295.get()), (float)((float)((double)0.8f + (double)0.2f * Math.random()))));
        }
    }

    public void renderWidget(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        BFMenuScreen bFMenuScreen;
        float f2 = BFRendering.getRenderTime();
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        boolean bl = minecraft.screen != null && minecraft.screen.renderables.contains((Object)this);
        Screen screen = minecraft.screen;
        if (screen instanceof BFMenuScreen && (bFMenuScreen = (BFMenuScreen)screen).method_761(this)) {
            bl = true;
        }
        boolean bl2 = this.isHovered = this.active && bl && n >= this.getX() && n2 >= this.getY() && n < this.getX() + this.width && n2 < this.getY() + this.height;
        if (this.enabledPredicate != null) {
            this.active = this.enabledPredicate.test(this);
        }
        if (!this.visible) {
            return;
        }
        if (this.isHovered) {
            if (!this.field_306 && this.field_305 && this.active) {
                this.field_306 = true;
                minecraft.getNarrator().sayNow((Component)Component.literal((String)ChatFormatting.stripFormatting((String)(this.getMessage().getString() + (String)(this.field_314 != null ? ", " + this.field_314.getString() : "")))));
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_HOVER.get()), (float)((float)((double)0.9f + (double)0.1f * Math.random()))));
                if (this.field_307 != null) {
                    this.field_307.onPress((Button)this);
                }
            }
        } else {
            this.field_306 = false;
        }
        bFMenuScreen = guiGraphics.pose();
        this.method_381(guiGraphics, (PoseStack)bFMenuScreen, n, n2, f2, f);
        this.method_376((PoseStack)bFMenuScreen, font, guiGraphics, n, n2, f);
    }

    public void method_376(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        int n3 = this.getWidth() / 2;
        int n4 = this.height / 2;
        MutableComponent mutableComponent = this.getMessage().copy();
        float f2 = this.method_396();
        int n5 = 4;
        float f3 = (float)font.width((FormattedText)mutableComponent) * f2;
        Objects.requireNonNull(font);
        float f4 = 9.0f * this.field_313;
        float f5 = 0.0f;
        float f6 = 0.0f;
        switch (this.alignment.ordinal()) {
            case 1: {
                f5 = (float)(this.getX() + n3) - f3 / 2.0f;
                f6 = (float)(this.getY() + n4) - f4 / 2.0f;
                break;
            }
            case 0: {
                f5 = this.getX() + 4;
                f6 = this.getY() + 4;
                break;
            }
            case 2: {
                f5 = (float)(this.getX() + this.getWidth()) - f3 - 4.0f;
                f6 = this.getY() + 4;
                break;
            }
            case 3: {
                f5 = (float)(this.getX() + n3) - f3 / 2.0f;
                f6 = (float)(this.getY() + this.height) - f4 - 4.0f;
            }
        }
        f5 += this.field_311;
        f6 += this.field_312;
        switch (this.textStyle.ordinal()) {
            case 0: {
                BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent, f5, f6, this.method_398(), f2);
                break;
            }
            case 1: {
                BFRendering.component2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent, f5, f6, this.method_398(), f2);
            }
        }
    }

    public void method_379(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        BFMenuScreen bFMenuScreen;
        boolean bl;
        Screen screen = minecraft.screen;
        boolean bl2 = bl = screen instanceof BFMenuScreen && (bFMenuScreen = (BFMenuScreen)screen).method_761(this);
        if (minecraft.screen == null || !bl && !minecraft.screen.renderables.contains((Object)this)) {
            return;
        }
        this.method_375(poseStack, font, guiGraphics, f);
        this.method_380(font, guiGraphics, n, n2);
    }

    private void method_375(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics guiGraphics, float f) {
        if (this.field_290 == null) {
            return;
        }
        int n = this.field_290.getNotificationCount();
        if (n <= 0) {
            return;
        }
        int n2 = 6;
        int n3 = 3;
        int n4 = this.getX() + this.width;
        int n5 = this.getY();
        float f2 = Mth.sin((float)(f / 10.0f));
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(n)).withStyle(BFStyles.BOLD).withColor(0xFFFFFF);
        BFRendering.centeredTexture(poseStack, guiGraphics, field_283, n4, n5, 12, 12);
        BFRendering.centeredTexture(poseStack, guiGraphics, field_283, n4, n5, 24, 24, 0.0f, f2);
        BFRendering.rectangle(guiGraphics, n4 - 3, n5 - 3, 6, 6, ColorReferences.COLOR_BLACK_SOLID + BFRendering.RED_CHAT_COLOR);
        BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent, (float)(n4 - 1), (float)n5 - 1.5f, 0.5f);
    }

    public void method_381(@NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, int n, int n2, float f, float f2) {
        ResourceLocation resourceLocation;
        int n3 = this.method_399();
        int n4 = this.getHeight();
        int n5 = this.getWidth();
        int n6 = this.getX();
        int n7 = this.getY();
        switch (this.displayType.ordinal()) {
            case 1: {
                BFRendering.rectangleWithDarkShadow(guiGraphics, n6, n7, n5, n4, n3);
                break;
            }
            case 2: {
                BFRendering.rectangle(guiGraphics, n6 + 1, n7, n5 - 2, 1, n3);
                BFRendering.rectangle(guiGraphics, n6, n7 + 1, n5, n4 - 2, n3);
                BFRendering.rectangle(guiGraphics, n6 + 1, n7 + n4 - 1, n5 - 2, 1, n3);
                break;
            }
            case 3: {
                break;
            }
            default: {
                BFRendering.rectangle(guiGraphics, n6, n7, n5, n4, n3);
            }
        }
        ResourceLocation resourceLocation2 = this.isHovered ? (this.field_289 != null ? this.field_289 : this.field_282) : (resourceLocation = this.field_282);
        if (resourceLocation != null) {
            BFRendering.texture(poseStack, guiGraphics, resourceLocation, n6, n7, n5, n4, this.field_310);
        }
        this.method_377(poseStack, guiGraphics);
        if (this.field_304) {
            BFRendering.rectangle(guiGraphics, n6, n7, n5, n4, BFRendering.translucentBlack());
        }
    }

    public void method_377(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics) {
        int n = this.getWidth() / 2;
        int n2 = this.getHeight() / 2;
        float f = 0.0f;
        if (this.texture != null) {
            BFRendering.centeredTexture(poseStack, guiGraphics, this.texture, this.getX() + n + this.field_299, this.getY() + n2 + this.field_300, this.width, this.height, f, this.isHoveredOrFocused() || !this.active ? 0.5f : 1.0f);
        }
    }

    public int method_398() {
        if (!this.active) {
            return this.field_308;
        }
        if (this.field_303) {
            return this.isHovered ? this.field_302 : this.field_301;
        }
        return this.field_301;
    }

    public BFButton method_397(int n) {
        this.field_309 = n;
        return this;
    }

    public float method_396() {
        return this.field_313;
    }

    public int method_399() {
        return this.isHovered ? this.field_309 : this.field_296;
    }

    public void method_380(@NotNull Font font, @NotNull GuiGraphics guiGraphics, int n, int n2) {
        if (this.field_314 != null && this.active && this.visible && this.isHovered) {
            guiGraphics.renderTooltip(font, this.field_314, n, n2);
        }
    }

    public int getHeight() {
        return this.height;
    }

    public static enum TextStyle {
        DEFAULT,
        SHADOW;

    }

    public static enum Alignment {
        LEFT,
        CENTER,
        RIGHT,
        CENTER_BOTTOM;

    }

    public static enum DisplayType {
        DEFAULT,
        SHADOW,
        FANCY,
        NONE;

    }
}

