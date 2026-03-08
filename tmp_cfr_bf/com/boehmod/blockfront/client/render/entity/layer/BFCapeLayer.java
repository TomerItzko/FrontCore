/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.entity.layer;

import com.boehmod.blockfront.client.BFClientManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import org.jetbrains.annotations.NotNull;

public class BFCapeLayer
extends CapeLayer {
    public BFCapeLayer(@NotNull RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent) {
        super(renderLayerParent);
    }

    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, @NotNull AbstractClientPlayer abstractClientPlayer, float f, float f2, float f3, float f4, float f5, float f6) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        if (bFClientManager.getGame() == null) {
            super.render(poseStack, multiBufferSource, n, abstractClientPlayer, f, f2, f3, f4, f5, f6);
        }
    }
}

