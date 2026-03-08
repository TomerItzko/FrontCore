/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.unnamed.BF_208;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BF_218 {
    @NotNull
    protected final List<BFButton> field_1353 = new ObjectArrayList();
    @Nullable
    private Component field_1359 = null;
    public BF_208<?> field_1354;
    public int field_1357;
    public int field_1358;
    private boolean field_1355 = false;
    private int field_1356;

    public void method_986(@Nullable Component component) {
        this.field_1359 = component;
    }

    public final void method_980(@NotNull BF_208<?> bF_208, int n, int n2, int n3) {
        this.field_1354 = bF_208;
        this.field_1356 = n;
        this.field_1357 = n2;
        this.field_1358 = n3;
        this.method_979();
    }

    public abstract void method_987(@NotNull Minecraft var1);

    protected void method_979() {
        this.field_1353.clear();
        this.method_973();
    }

    public void method_973() {
    }

    public abstract boolean method_990();

    public final boolean method_991() {
        return this.field_1354.method_951(this);
    }

    public void method_982(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, int n, int n2, int n3) {
        for (BFButton bFButton : this.field_1353) {
            if (!bFButton.mouseClicked(n, n2, n3)) continue;
            bFButton.onClick(n, n2);
            break;
        }
    }

    @OverridingMethodsMustInvokeSuper
    public void method_981(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull Font font, int n, int n2, float f, float f2) {
        this.field_1353.forEach(bFButton -> bFButton.render(guiGraphics, n, n2, f2));
        this.field_1355 = this.field_1354.method_952(this, n, n2);
    }

    public void method_985(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull Font font, int n, int n2, float f, float f2) {
        if (this.field_1359 != null && this.field_1355) {
            guiGraphics.renderTooltip(font, this.field_1359, n, n2);
        }
    }

    public void method_983(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2, float f) {
    }

    public int method_977() {
        return 0;
    }

    public int method_976() {
        return 0;
    }

    public int method_974() {
        return 0;
    }

    public int method_975() {
        return 0;
    }

    public abstract int height();

    public abstract int method_989();

    public boolean method_992() {
        return this.field_1355;
    }

    public void method_988(boolean bl) {
        this.field_1355 = bl;
    }

    public int method_978() {
        return this.field_1356;
    }
}

