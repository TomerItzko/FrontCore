/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public abstract class BFPopoutWidget
extends BFWidget {
    private float field_642;
    private float field_643 = 0.0f;
    private boolean field_644 = false;
    private boolean field_645 = false;

    public BFPopoutWidget(int n, int n2, int n3, int n4, Screen screen) {
        super(n, n2, n3, n4, screen);
    }

    @Override
    public void method_537(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, float f) {
        super.method_537(minecraft, bFClientManager, clientPlayerDataHandler, f);
        MouseHandler mouseHandler = minecraft.mouseHandler;
        Window window = minecraft.getWindow();
        double d = mouseHandler.xpos() * (double)window.getGuiScaledWidth() / (double)window.getScreenWidth();
        double d2 = mouseHandler.ypos() * (double)window.getGuiScaledHeight() / (double)window.getScreenHeight();
        this.method_555(BFRendering.isPointInRectangle(this.field_564, this.field_565, this.field_566, this.height, d, d2) && minecraft.screen == this.getScreen() && this.method_603(minecraft, bFClientManager, clientPlayerDataHandler));
        this.field_643 = this.field_642;
        this.field_642 = MathUtils.moveTowards(this.field_642, this.method_557() ? 1.0f : 0.0f, 0.15f);
        if (this.method_557()) {
            this.field_644 = true;
            if (!this.field_645) {
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_FORWARD.get()), (float)1.0f));
                this.field_645 = true;
            }
        } else if (this.field_644 && this.field_645) {
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_BACK.get()), (float)1.0f));
            this.field_645 = false;
        }
    }

    protected float method_604(float f) {
        return MathUtils.lerpf1(this.field_642, this.field_643, f);
    }

    public abstract boolean method_603(@NotNull Minecraft var1, @NotNull BFClientManager var2, @NotNull ClientPlayerDataHandler var3);
}

