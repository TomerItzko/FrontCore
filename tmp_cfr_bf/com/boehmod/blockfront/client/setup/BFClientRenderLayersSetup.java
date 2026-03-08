/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.setup;

import com.boehmod.blockfront.client.render.model.BFArmorModel;
import com.boehmod.blockfront.client.render.model.BackpackModel;
import com.boehmod.blockfront.client.render.model.BotRenderLayer;
import com.boehmod.blockfront.client.render.model.InfectedEyesRenderLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;

public class BFClientRenderLayersSetup {
    public static void register(@NotNull EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BotRenderLayer.EYES, () -> LayerDefinition.create((MeshDefinition)HumanoidModel.createMesh((CubeDeformation)CubeDeformation.NONE, (float)0.01f), (int)64, (int)64));
        event.registerLayerDefinition(BotRenderLayer.GAME_ARMOR, () -> LayerDefinition.create((MeshDefinition)BFArmorModel.createMesh(CubeDeformation.NONE, false, 0.015f), (int)64, (int)64));
        event.registerLayerDefinition(BotRenderLayer.GAME_ARMOR_SLIM, () -> LayerDefinition.create((MeshDefinition)BFArmorModel.createMesh(CubeDeformation.NONE, true, 0.015f), (int)64, (int)64));
        event.registerLayerDefinition(BotRenderLayer.GAME_BACKPACK, () -> LayerDefinition.create((MeshDefinition)BackpackModel.createMesh(CubeDeformation.NONE), (int)64, (int)64));
        event.registerLayerDefinition(InfectedEyesRenderLayer.EYES, () -> LayerDefinition.create((MeshDefinition)HumanoidModel.createMesh((CubeDeformation)CubeDeformation.NONE, (float)0.01f), (int)64, (int)64));
    }
}

