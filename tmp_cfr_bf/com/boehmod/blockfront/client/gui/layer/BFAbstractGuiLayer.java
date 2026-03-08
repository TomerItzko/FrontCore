/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui.layer;

import com.boehmod.blockfront.client.BFClientManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import org.jetbrains.annotations.NotNull;

public abstract class BFAbstractGuiLayer
implements LayeredDraw.Layer {
    protected abstract void render(@NotNull GuiGraphics var1, @NotNull DeltaTracker var2, @NotNull BFClientManager var3);

    public void render(@NotNull GuiGraphics graphics, @NotNull DeltaTracker delta) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        if (bFClientManager != null) {
            this.render(graphics, delta, bFClientManager);
        }
    }
}

