/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IRenderConsumeInfo {
    public int getTicksUntilAction();

    public int getTotalTicksUntilAction();

    @NotNull
    public String getTranslation();

    public int getColor();

    @NotNull
    public ResourceLocation getIcon();
}

