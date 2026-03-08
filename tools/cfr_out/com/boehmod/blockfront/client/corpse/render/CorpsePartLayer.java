/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.ModelPart$Cube
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.corpse.render;

import com.boehmod.blockfront.client.corpse.render.CorpsePartLayerType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record CorpsePartLayer(@NotNull ResourceLocation texture, @NotNull CorpsePartLayerType layerType, @NotNull ModelPart.Cube[] cubes) {
    @NotNull
    public static CorpsePartLayer of(@NotNull ResourceLocation texture, @NotNull ModelPart modelPart, @NotNull CorpsePartLayerType layerType) {
        return new CorpsePartLayer(texture, layerType, modelPart.cubes.toArray(new ModelPart.Cube[0]));
    }
}

