/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui;

import com.boehmod.blockfront.common.data.BFResource;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record BFCrosshair(String name, ResourceLocation top, ResourceLocation bottom, ResourceLocation left, ResourceLocation right, ResourceLocation middle) {
    @NotNull
    public static final Codec<BFCrosshair> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.STRING.fieldOf("name").forGetter(BFCrosshair::name), (App)ResourceLocation.CODEC.fieldOf("top").forGetter(BFCrosshair::top), (App)ResourceLocation.CODEC.fieldOf("bottom").forGetter(BFCrosshair::bottom), (App)ResourceLocation.CODEC.fieldOf("left").forGetter(BFCrosshair::left), (App)ResourceLocation.CODEC.fieldOf("right").forGetter(BFCrosshair::right), (App)ResourceLocation.CODEC.fieldOf("middle").forGetter(BFCrosshair::middle)).apply((Applicative)instance, BFCrosshair::new));
    private static final ResourceLocation DEFAULT_TOP = BFRes.loc("textures/crosshairs/default/top.png");
    private static final ResourceLocation DEFAULT_BOTTOM = BFRes.loc("textures/crosshairs/default/bottom.png");
    private static final ResourceLocation DEFAULT_LEFT = BFRes.loc("textures/crosshairs/default/left.png");
    private static final ResourceLocation DEFAULT_RIGHT = BFRes.loc("textures/crosshairs/default/right.png");
    private static final ResourceLocation DEFAULT_MIDDLE = BFRes.loc("textures/crosshairs/default/middle.png");
    @NotNull
    public static final BFCrosshair DEFAULT = new BFCrosshair("default", DEFAULT_TOP, DEFAULT_BOTTOM, DEFAULT_LEFT, DEFAULT_RIGHT, DEFAULT_MIDDLE);

    public static class Resource
    extends BFResource<BFCrosshair> {
        public Resource(@NotNull Minecraft minecraft) {
            super(minecraft.getResourceManager(), "crosshairs", "crosshairs.json", CODEC, DEFAULT, BFCrosshair::name);
        }
    }
}

