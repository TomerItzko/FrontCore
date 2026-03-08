/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.booleans.BooleanArrayList
 *  it.unimi.dsi.fastutil.booleans.BooleanList
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.ContainerEventHandler
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.dropdown;

import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.SidebarScreen;
import com.boehmod.blockfront.registry.BFSounds;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public abstract class DropdownScreen
extends SidebarScreen
implements ContainerEventHandler {
    @NotNull
    protected final ObjectList<Component> field_1012 = new ObjectArrayList();
    @NotNull
    protected final ObjectList<Component> field_1013 = new ObjectArrayList();
    @NotNull
    protected final BooleanList field_1014 = new BooleanArrayList();
    protected int field_1015;
    protected int field_1016;
    protected int field_1017;
    protected int field_1018;

    public DropdownScreen(@NotNull Screen screen, int n, int n2, int n3, int n4, @NotNull Component component) {
        super(screen, component);
        this.field_1015 = n;
        this.field_1016 = n2;
        this.field_1017 = n3;
        this.field_1018 = n4;
        while (this.field_1015 + n3 > this.minecraft.getWindow().getGuiScaledWidth()) {
            this.field_1015 -= n3;
        }
        this.field_1012.clear();
    }

    public DropdownScreen method_744(@NotNull Component component) {
        this.field_1012.add((Object)component);
        this.field_1013.add(null);
        this.field_1014.add(true);
        return this;
    }

    public DropdownScreen method_745(@NotNull Component component, @NotNull Component component2) {
        this.field_1012.add((Object)component);
        this.field_1013.add((Object)component2);
        this.field_1014.add(true);
        int n = this.field_1018 * this.field_1012.size();
        while (this.field_1016 + n > this.minecraft.getWindow().getGuiScaledHeight()) {
            this.field_1016 -= this.field_1018;
        }
        return this;
    }

    public DropdownScreen method_747(@NotNull Component component, boolean bl) {
        this.field_1012.add((Object)component);
        this.field_1013.add(null);
        this.field_1014.add(bl);
        int n = this.field_1018 * this.field_1012.size();
        while (this.field_1016 + n > this.minecraft.getWindow().getGuiScaledHeight()) {
            this.field_1016 -= this.field_1018;
        }
        return this;
    }

    public DropdownScreen method_746(@NotNull Component component, @NotNull Component component2, boolean bl) {
        this.field_1012.add((Object)component);
        this.field_1013.add((Object)component2);
        this.field_1014.add(bl);
        int n = this.field_1018 * this.field_1012.size();
        while (this.field_1016 + n > this.minecraft.getWindow().getGuiScaledHeight()) {
            this.field_1016 -= this.field_1018;
        }
        return this;
    }

    @Override
    public boolean method_753(double d, double d2) {
        return false;
    }

    @Override
    public void init() {
        super.init();
        int n = this.field_1012.size();
        if (n == 0) {
            this.method_752();
        }
        for (int i = 0; i < n; ++i) {
            int n2 = i * this.field_1018;
            int n3 = i;
            BFButton bFButton = new BFButton(this.field_1015, this.field_1016 + n2, this.field_1017, this.field_1018, (Component)this.field_1012.get(i), button -> this.method_748(n3 + 1));
            Component component = (Component)this.field_1013.get(i);
            if (component != null) {
                bFButton.tip(component);
            }
            bFButton.active = this.field_1014.getBoolean(i);
            this.addRenderableWidget((GuiEventListener)bFButton);
        }
        this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.5f));
    }

    public abstract void method_748(int var1);

    @Override
    public boolean mouseClicked(double d, double d2, int n) {
        this.method_752();
        this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)2.0f));
        return super.mouseClicked(d, d2, n);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        this.field_1036.render(guiGraphics, this.field_1036.width, this.field_1036.height, f);
        float f2 = BFRendering.getRenderTime();
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_1015, this.field_1016, this.field_1017, this.renderables.size() * this.field_1018, BFRendering.translucentBlack());
        int n3 = this.renderables.size();
        for (int i = 0; i < n3; ++i) {
            if (i % 2 == 0) continue;
            BFRendering.rectangle(guiGraphics, this.field_1015, this.field_1016 + this.field_1018 * i, this.field_1017, this.field_1018, 0x22FFFFFF);
        }
        for (Renderable renderable : this.renderables) {
            renderable.render(guiGraphics, n, n2, f);
        }
        for (Renderable renderable : this.renderables) {
            if (!(renderable instanceof BFButton)) continue;
            BFButton bFButton = (BFButton)renderable;
            bFButton.method_379(this.minecraft, poseStack, this.font, guiGraphics, n, n2, f2);
        }
        poseStack.popPose();
    }
}

