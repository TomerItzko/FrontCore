/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.google.common.collect.Iterators
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.OverridingMethodsMustInvokeSuper
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_215;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.unnamed.BF_219;
import com.google.common.collect.Iterators;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public abstract class BF_208<S extends BF_218>
extends BFWidget {
    protected static final int field_1307 = 40;
    protected static final int field_1308 = 6;
    protected static final int field_1309 = 2;
    protected int field_1310 = 0;
    protected int field_1311 = 0;
    protected int field_1312 = 0;
    protected int field_1313 = 0;
    @NotNull
    protected final List<S> field_1302 = new ObjectArrayList();
    protected int field_1314 = -1;
    protected boolean field_1304 = false;
    protected int field_1303 = ColorReferences.COLOR_BLACK_TRANSPARENT;
    protected int field_1315;
    protected int field_1316;
    protected int field_1317;
    protected int field_1305;
    protected int field_1306 = 0;

    public BF_208(int n, int n2, int n3, int n4, @NotNull Screen screen) {
        super(n, n2, n3, n4, screen);
    }

    @NotNull
    public BF_208 method_949(int n) {
        this.field_1303 = n;
        return this;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void method_535(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler) {
        this.field_1317 = this.field_1316 = this.method_944();
        this.field_1313 = this.field_1310 = this.field_1316;
        this.field_1311 = this.field_1312 = this.field_1316;
    }

    abstract int method_944();

    abstract void method_956(GuiGraphics var1, float var2, int var3, int var4);

    protected int method_957(int n) {
        return Mth.clamp((int)n, (int)this.field_1316, (int)this.field_1317);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void method_537(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, float f) {
        try {
            this.field_1302.forEach(bF_218 -> bF_218.method_987(minecraft));
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
        this.field_1313 = this.field_1310;
        this.field_1311 = this.field_1312;
    }

    public void method_960(@NotNull List<String> list) {
        this.method_948();
        for (String string : list) {
            Object object;
            if (string.startsWith("//")) continue;
            if (string.contains("<image>")) {
                String[] stringArray = string.replace("<image>", "").split("<>");
                object = stringArray[0];
                int n = Integer.parseInt(stringArray[1]);
                int n2 = Integer.parseInt(stringArray[2]);
                BF_215 bF_215 = new BF_215((String)object, n, n2);
                bF_215.field_1354 = this;
                this.method_950(bF_215);
                continue;
            }
            if (string.contains("<link>")) {
                string = string.replace("<link>", "");
                String[] stringArray = string.split("<>");
                object = stringArray[0];
                String string2 = stringArray[1];
                BF_219 bF_219 = new BF_219((String)object).method_994(string2);
                bF_219.field_1354 = this;
                this.method_950(bF_219);
                continue;
            }
            boolean bl = false;
            if (string.startsWith("<s>")) {
                bl = true;
                string = string.replace("<s>", "");
            }
            object = new BF_219(string);
            if (bl) {
                ((BF_219)object).method_993();
            }
            ((BF_219)object).field_1354 = this;
            this.method_950(object);
        }
    }

    public void method_950(@NotNull S s) {
        this.field_1302.add(s);
        this.method_947();
    }

    public void method_948() {
        this.field_1302.clear();
        this.method_947();
    }

    public void method_954(@NotNull Iterator<? extends S> iterator) {
        this.field_1302.clear();
        try {
            Iterators.addAll(this.field_1302, iterator);
            this.method_947();
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }

    protected void method_953(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull GuiGraphics guiGraphics, Font font, int n, int n2, float f, float f2, float f3) {
        try {
            for (BF_218 bF_218 : this.field_1302) {
                bF_218.method_983(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, n, n2, f3);
            }
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
        try {
            for (BF_218 bF_218 : this.field_1302) {
                bF_218.method_981(poseStack, minecraft, bFClientManager, guiGraphics, clientPlayerDataHandler, font, n, n2, f2, f3);
            }
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }

    protected void method_959(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull GuiGraphics guiGraphics, Font font, int n, int n2, float f, float f2, float f3) {
        try {
            for (BF_218 bF_218 : this.field_1302) {
                bF_218.method_985(poseStack, minecraft, bFClientManager, guiGraphics, clientPlayerDataHandler, font, n, n2, f2, f3);
            }
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }

    public abstract boolean method_952(@NotNull BF_218 var1, int var2, int var3);

    public abstract float method_963(float var1);

    public boolean method_951(@NotNull BF_218 bF_218) {
        return bF_218.method_978() == this.field_1314;
    }

    void method_958(@NotNull BF_218 bF_218) {
        this.field_1314 = bF_218.method_978();
    }

    abstract void method_947();

    abstract int method_945();

    public int method_946() {
        return this.field_1302.size();
    }

    @NotNull
    public List<S> method_962() {
        return this.field_1302;
    }

    public void method_961(@NotNull List<S> list) {
        this.field_1302.clear();
        this.field_1302.addAll(list);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        this.method_956(guiGraphics, f2, n, n2);
    }

    @Override
    protected void method_534(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, float f, float f2) {
        super.method_534(minecraft, clientPlayerDataHandler, poseStack, guiGraphics, n, n2, f, f2);
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, this.field_1303);
    }

    @Override
    public boolean mouseReleased(double d, double d2, int n) {
        this.field_1304 = false;
        return true;
    }

    @Override
    public boolean mouseScrolled(double d, double d2, double d3, double d4) {
        float f = (float)d4 * 150.0f;
        if (!this.field_1304 && !this.field_1302.isEmpty() && BFRendering.isPointInRectangle(this.field_564, this.field_565, this.field_566, this.height, d, d2)) {
            this.field_1306 = this.method_957((int)((float)this.method_945() - f / (float)this.field_1305 * 30.0f));
            return true;
        }
        return super.mouseScrolled(d, d2, d3, d4);
    }
}

