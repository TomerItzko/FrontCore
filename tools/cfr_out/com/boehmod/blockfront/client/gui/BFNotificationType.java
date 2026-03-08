/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.gui;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public enum BFNotificationType {
    CLIENT_NOTIFICATION(-9143992),
    MOD_UPDATE_NOTIFICATION(ColorReferences.COLOR_THEME_YELLOW_SOLID, "update"),
    CHALLENGE_NOTIFICATION(ColorReferences.COLOR_THEME_YELLOW_SOLID, "challenge");

    private final int color;
    @NotNull
    private final ResourceLocation icon;

    private BFNotificationType(int color) {
        this(color, "bf");
    }

    private BFNotificationType(int color, String icon) {
        this.color = color;
        this.icon = BFRes.loc("textures/gui/toast/" + icon + ".png");
    }

    public int getColor() {
        return this.color;
    }

    @NotNull
    public ResourceLocation getIcon() {
        return this.icon;
    }
}

