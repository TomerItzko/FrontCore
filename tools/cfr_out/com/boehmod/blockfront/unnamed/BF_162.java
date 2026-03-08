/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.gui.widget.BFButton;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BF_162 {
    @NotNull
    private final Component field_859;
    private final // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull Button.OnPress field_858;
    @Nullable
    private Component field_860 = null;
    private int field_855 = -1;
    private int field_857 = -1;
    private int field_856 = -1;
    @Nullable
    private Consumer<BFButton> field_854 = null;

    public BF_162(@NotNull Component component, @NotNull Button.OnPress onPress) {
        this.field_859 = component;
        this.field_858 = onPress;
    }

    public BF_162 method_667(@NotNull Consumer<BFButton> consumer) {
        this.field_854 = consumer;
        return this;
    }

    @NotNull
    public BF_162 method_665(int n) {
        this.field_856 = n;
        return this;
    }

    @NotNull
    public BF_162 method_668(@Nullable Component component) {
        this.field_860 = component;
        return this;
    }

    @NotNull
    public BF_162 method_666(int n, int n2) {
        this.field_855 = n;
        this.field_857 = n2;
        return this;
    }

    @Nullable
    public Consumer<BFButton> method_663() {
        return this.field_854;
    }

    @NotNull
    public Component method_669() {
        return this.field_859;
    }

    @Nullable
    public Component method_670() {
        return this.field_860;
    }

    public int method_671() {
        return this.field_855;
    }

    public int method_672() {
        return this.field_857;
    }

    public int method_673() {
        return this.field_856;
    }

    public // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull Button.OnPress method_664() {
        return this.field_858;
    }
}

