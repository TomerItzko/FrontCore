/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.phys.Vec2
 *  net.neoforged.neoforge.client.event.RenderNameTagEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.nametag;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import org.jetbrains.annotations.NotNull;

public abstract class BFAbstractNameTagRenderer<T extends Entity> {
    private static final Map<EntityType<?>, BFAbstractNameTagRenderer<?>> field_1784 = new Object2ObjectOpenHashMap();

    public static BFAbstractNameTagRenderer<?> method_1338(@NotNull EntityType<?> entityType) {
        return field_1784.get(entityType);
    }

    public static void register(@NotNull EntityType<?> entityType, @NotNull BFAbstractNameTagRenderer<?> bFAbstractNameTagRenderer) {
        field_1784.put(entityType, bFAbstractNameTagRenderer);
    }

    static void method_1337(@NotNull GuiGraphics guiGraphics, int n, int n2, int n3, float f, @NotNull Vec2 vec2, float f2, float f3, float f4, @Nullable ResourceLocation resourceLocation, @Nullable ResourceLocation resourceLocation2, float f5) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0.0f, f, 0.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-vec2.y));
        poseStack.scale(-f2, -f2, f2);
        if (resourceLocation2 != null) {
            BFRendering.tintedTexture(poseStack, guiGraphics, resourceLocation2, f3, f3, (float)n2, (float)n2, 0.0f, f5, n);
        }
        if (resourceLocation != null) {
            BFRendering.texture(poseStack, guiGraphics, resourceLocation, f4, f4 - 3.0f, (float)n3, (float)n3, 0.0f, f5);
        }
        poseStack.popPose();
    }

    public abstract boolean method_1340(@NotNull RenderNameTagEvent var1, @NotNull Minecraft var2, @NotNull ClientPlayerDataHandler var3, @NotNull LocalPlayer var4, @NotNull T var5, @NotNull AbstractGame<?, ?, ?> var6, @NotNull AbstractGameClient<?, ?> var7, @NotNull GameTeam var8);

    public abstract void method_1336(@NotNull BFClientManager var1, @NotNull RenderNameTagEvent var2, @NotNull Minecraft var3, @NotNull ClientPlayerDataHandler var4, @NotNull T var5, @NotNull GuiGraphics var6, @NotNull AbstractGame<?, ?, ?> var7, @NotNull AbstractGameClient<?, ?> var8, @NotNull AbstractGamePlayerManager<?> var9, @NotNull GameTeam var10, int var11, float var12, float var13);
}

