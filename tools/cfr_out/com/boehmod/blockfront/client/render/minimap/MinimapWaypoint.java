/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.Nullable
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.minimap;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MinimapWaypoint {
    public static final ResourceLocation TEXTURE_PLAYER = BFRes.loc("textures/gui/compass/waypoint_pp.png");
    public static final ResourceLocation TEXTURE_ENEMY = BFRes.loc("textures/gui/compass/waypoint_pp_enemy.png");
    @NotNull
    private final Vec3 field_268;
    @NotNull
    private final ResourceLocation field_259;
    @Nullable
    private ResourceLocation icon;
    @Nullable
    private Component component = null;
    private int iconTint = ColorReferences.COLOR_WHITE_SOLID;
    private boolean field_265 = false;
    private float field_261 = 0.0f;
    private int field_264 = 8;
    private float field_267 = 0.0f;
    private boolean field_266 = false;
    private AABB field_262;

    public MinimapWaypoint(@NotNull ResourceLocation resourceLocation, @NotNull Vec2 vec2) {
        this.field_268 = new Vec3((double)vec2.x, 0.0, (double)vec2.y);
        this.field_259 = resourceLocation;
        this.field_262 = this.method_358();
    }

    public MinimapWaypoint(@NotNull ResourceLocation resourceLocation, @NotNull Vec3 vec3) {
        this.field_268 = vec3;
        this.field_259 = resourceLocation;
        this.field_262 = this.method_358();
    }

    @NotNull
    public AABB method_358() {
        return new AABB(this.field_268.x, this.field_268.y, this.field_268.z, this.field_268.x, this.field_268.y, this.field_268.z).inflate((double)0.2f);
    }

    public AABB method_359() {
        return this.field_262;
    }

    public MinimapWaypoint icon(@NotNull ResourceLocation icon, int tint) {
        this.icon = icon;
        this.iconTint = tint;
        return this;
    }

    public MinimapWaypoint method_351() {
        this.field_266 = true;
        return this;
    }

    public MinimapWaypoint method_352(float f) {
        this.field_267 = f;
        return this;
    }

    public MinimapWaypoint component(@NotNull Component component) {
        this.component = component;
        return this;
    }

    public MinimapWaypoint method_353(int n) {
        this.field_264 = n;
        this.field_262 = this.method_358();
        return this;
    }

    public float method_362() {
        return this.field_264;
    }

    public void onUpdate() {
        if (this.field_265) {
            if (this.field_261 < 1.0f) {
                this.field_261 += 0.01f;
            }
        } else if (this.field_261 > 0.0f) {
            this.field_261 -= 0.01f;
        }
    }

    public void renderIcon(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, float f, float f2, float f3) {
        if (this.icon != null) {
            BFRendering.centeredTintedTexture(poseStack, guiGraphics, this.icon, f, f2, (float)this.field_264, (float)this.field_264, this.field_267, f3, this.iconTint);
        }
        BFRendering.centeredTexture(poseStack, guiGraphics, this.field_259, f, f2, (float)this.field_264, (float)this.field_264, this.field_267, f3);
    }

    public void renderComponent(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, float f, float f2) {
        if (this.component != null) {
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, this.component, f, f2 + 5.0f, 0.5f);
        }
    }

    public void method_361(boolean bl) {
        this.field_265 = bl;
    }

    @NotNull
    public ResourceLocation getTexture() {
        return this.field_259;
    }

    public boolean method_364() {
        return this.field_266;
    }

    @NotNull
    public Vec3 method_360() {
        return this.field_268;
    }

    public float method_363() {
        return this.field_267;
    }
}

