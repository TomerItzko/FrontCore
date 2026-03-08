/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.settings;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.settings.BFClientSettingCategory;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BFClientSetting {
    private static final ResourceLocation field_1945 = BFRes.loc("textures/gui/menu/icons/check_white.png");
    private static final ResourceLocation field_1946 = BFRes.loc("textures/gui/menu/icons/cross_white.png");
    private final String name;
    private final String description;
    private final String key;
    private final boolean field_1941;
    float field_1943;
    float field_1944 = 0.0f;
    private boolean isEnabled;
    Consumer<BFClientSetting> field_1940;

    BFClientSetting(@NotNull String name, @NotNull String key, @NotNull BFClientSettingCategory bFClientSettingCategory, boolean bl) {
        BFClientSettings.INSTANCES.add(this);
        this.name = name;
        this.description = name + ".desc";
        this.key = key;
        this.field_1941 = bl;
        this.isEnabled = bl;
        bFClientSettingCategory.method_1518(this);
    }

    public BFClientSetting method_1508(Consumer<BFClientSetting> consumer) {
        this.field_1940 = consumer;
        return this;
    }

    public static float method_1512(float f) {
        if (f == 1.0f) {
            return 0.75f;
        }
        if (f == 0.75f) {
            return 0.5f;
        }
        if (f == 0.5f) {
            return 0.25f;
        }
        if (f == 0.25f) {
            return 1.0f;
        }
        return 1.0f;
    }

    public int getColor() {
        return this.isEnabled() ? ColorReferences.COLOR_TEAM_ALLIES_SOLID : ColorReferences.COLOR_THEME_RED_SOLID;
    }

    public void method_1510(@NotNull Minecraft minecraft, @NotNull Screen screen, int n) {
        if (n == 0) {
            this.method_1513(!this.isEnabled);
        } else if (n == 1) {
            this.method_1513(this.field_1941);
        }
        BFClientSettings.isUnsaved = true;
        this.field_1943 = 1.0f;
    }

    public void readFDS(@NotNull FDSTagCompound root) {
        this.isEnabled = root.getBoolean(this.getKey(), this.field_1941);
    }

    public void writeFDS(@NotNull FDSTagCompound root) {
        root.setBoolean(this.key, this.isEnabled);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getKey() {
        return this.key;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void method_1513(boolean bl) {
        if (this.isEnabled != bl) {
            this.isEnabled = bl;
            if (this.field_1940 != null) {
                this.field_1940.accept(this);
            }
        }
    }

    public void method_1509(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, int n, int n2, int n3, int n4, int n5, int n6, float f, float f2) {
        int n7 = this.isEnabled() ? ColorReferences.COLOR_TEAM_ALLIES_SOLID : ColorReferences.COLOR_THEME_RED_SOLID;
        float f3 = MathUtils.lerpf1(this.field_1943, this.field_1944, f2);
        BFRendering.rectangle(guiGraphics, n, n2, n3, n4, n7, f3);
        BFRendering.rectangle(guiGraphics, n, n2 + n4 - 1, n3, 1, n7);
        int n8 = 16;
        BFRendering.tintedTexture(poseStack, guiGraphics, this.isEnabled ? field_1945 : field_1946, n5 - 8, n6 - 8, 16, 16, 0.0f, 1.0f, n7);
    }

    public void onUpdate() {
        this.field_1944 = this.field_1943;
        this.field_1943 = (float)Mth.lerp((double)0.35, (double)this.field_1943, (double)0.0);
    }
}

