/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.ComponentSerialization
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.util.Mth
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public final class BFPopup {
    public static final int field_3658 = 12;
    @NotNull
    private final Component field_3657;
    private int field_3656;
    private float field_3654;
    private float field_3655 = 0.0f;

    public BFPopup(@NotNull Component component, int n) {
        this.field_3657 = component;
        this.field_3656 = n;
    }

    public BFPopup(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this((Component)ComponentSerialization.STREAM_CODEC.decode((Object)registryFriendlyByteBuf), registryFriendlyByteBuf.readInt());
    }

    public void method_3811(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        ComponentSerialization.STREAM_CODEC.encode((Object)registryFriendlyByteBuf, (Object)this.field_3657);
        registryFriendlyByteBuf.writeInt(this.field_3656);
    }

    public boolean method_3812() {
        this.field_3655 = this.field_3654;
        --this.field_3656;
        if (this.field_3656 > 0) {
            this.field_3654 = Mth.lerp((float)0.6f, (float)this.field_3654, (float)1.0f);
        } else {
            if (this.field_3654 <= 0.0f && this.field_3655 <= 0.0f) {
                return true;
            }
            this.field_3654 = MathUtils.moveTowards(this.field_3654, 0.0f, 0.3f);
        }
        return false;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_3809(@NotNull GuiGraphics guiGraphics, Font font, int n, int n2, float f) {
        int n3 = ColorReferences.COLOR_WHITE_SOLID;
        TextColor textColor = this.field_3657.getStyle().getColor();
        if (textColor != null) {
            n3 = textColor.getValue();
        }
        int n4 = font.width((FormattedText)this.field_3657) + 3;
        float f2 = MathUtils.lerpf1(this.field_3654, this.field_3655, f);
        float f3 = 1.0f - f2;
        int n5 = BFRendering.translucentBlack();
        BFRendering.rectangle(guiGraphics, n + 1, n2, n4 - 2, 1, n5);
        BFRendering.rectangle(guiGraphics, n, n2 + 1, n4, 11, n5);
        BFRendering.rectangle(guiGraphics, n + 1, n2 + 12, n4 - 2, 1, n5);
        if (this.field_3656 > 0) {
            BFRendering.rectangle(guiGraphics, n + 1, n2, n4 - 2, 1, n3, f3);
            BFRendering.rectangle(guiGraphics, n, n2 + 1, n4, 11, n3, f3);
            BFRendering.rectangle(guiGraphics, n + 1, n2 + 12, n4 - 2, 1, n3, f3);
        }
        BFRendering.drawString(font, guiGraphics, this.field_3657, n + 2, n2 + 3);
    }

    @NotNull
    public Component getMessage() {
        return this.field_3657;
    }

    public int method_3810() {
        return this.field_3656;
    }

    public int getHeight() {
        return 12;
    }
}

