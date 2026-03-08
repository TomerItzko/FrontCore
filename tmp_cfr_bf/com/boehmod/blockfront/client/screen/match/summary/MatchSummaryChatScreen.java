/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.match.summary;

import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

public class MatchSummaryChatScreen
extends ChatScreen {
    @NotNull
    private final MatchSummaryScreen parent;
    private boolean field_804 = true;

    public MatchSummaryChatScreen(@NotNull MatchSummaryScreen parent) {
        super("");
        this.parent = parent;
    }

    public void tick() {
        super.tick();
        this.parent.tick();
    }

    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        this.parent.render(guiGraphics, n, n2, f);
        if (this.field_804 && this.input.getValue().equalsIgnoreCase("t")) {
            this.field_804 = false;
            this.input.setValue("");
        }
        super.render(guiGraphics, n, n2, f);
    }

    public boolean keyPressed(int n, int n2, int n3) {
        boolean bl = super.keyPressed(n, n2, n3);
        if (this.minecraft != null && this.minecraft.screen != this) {
            this.minecraft.setScreen((Screen)this.parent);
        }
        return bl;
    }
}

