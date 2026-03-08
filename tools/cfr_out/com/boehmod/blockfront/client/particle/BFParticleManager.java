/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GameRenderer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.unnamed.BF_1143;
import com.boehmod.blockfront.unnamed.BF_1149;
import com.boehmod.blockfront.unnamed.BF_1150;
import com.boehmod.blockfront.unnamed.BF_1152;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.jetbrains.annotations.NotNull;

public class BFParticleManager {
    private static BFParticleManager instance;
    @NotNull
    private final BF_1149 field_6498 = new BF_1149();
    @NotNull
    private final BF_1152 field_6499 = new BF_1152();

    private BFParticleManager() {
    }

    public static BFParticleManager getInstance() {
        if (instance == null) {
            instance = new BFParticleManager();
        }
        return instance;
    }

    public void method_5587(@NotNull BF_1150 bF_1150) {
        this.field_6498.method_5569(bF_1150);
    }

    public void method_5582(@NotNull BF_1143 bF_1143) {
        this.field_6499.method_5569(bF_1143);
    }

    public void onUpdate() {
        this.field_6498.tick();
        this.field_6499.tick();
    }

    public void render(@NotNull Minecraft minecraft, @NotNull GameRenderer gameRenderer, @NotNull Camera camera, float f) {
        this.field_6498.method_5574(minecraft, gameRenderer, camera, f);
        this.field_6499.method_5574(minecraft, gameRenderer, camera, f);
    }

    public void clearAll() {
        this.field_6498.clearAll();
        this.field_6499.clearAll();
    }

    public int count() {
        return this.field_6498.count() + this.field_6499.count();
    }

    public void method_5585() {
        this.field_6498.method_5577();
        this.field_6499.method_5577();
    }
}

