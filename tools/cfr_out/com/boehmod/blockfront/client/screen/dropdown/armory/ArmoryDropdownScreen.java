/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.dropdown.armory;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.blockfront.client.screen.dropdown.DropdownScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class ArmoryDropdownScreen
extends DropdownScreen {
    @NotNull
    protected final CloudItemStack field_709;

    public ArmoryDropdownScreen(@NotNull Screen screen, int n, int n2, int n3, int n4, @NotNull Component component, @NotNull CloudItemStack cloudItemStack) {
        super(screen, n, n2, n3, n4, component);
        this.field_709 = cloudItemStack;
    }
}

