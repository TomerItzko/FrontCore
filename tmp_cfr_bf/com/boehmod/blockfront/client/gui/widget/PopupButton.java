/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui.widget;

import javax.annotation.Nullable;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public record PopupButton(@NotNull Component text, @Nullable Component toolTip, @NotNull Button.OnPress onPress) {
}

