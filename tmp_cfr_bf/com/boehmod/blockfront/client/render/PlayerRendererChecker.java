/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render;

import com.boehmod.blockfront.client.render.entity.BFPlayerRenderer;
import com.boehmod.blockfront.util.BFLog;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.PlayerSkin;
import org.jetbrains.annotations.NotNull;

public class PlayerRendererChecker {
    private boolean checked = false;

    public void queueCheck() {
        this.checked = false;
    }

    public void update(@NotNull Minecraft minecraft, @NotNull Font font, @NotNull ItemInHandRenderer itemRenderer) {
        if (this.checked) {
            return;
        }
        this.checked = true;
        BFLog.log("Checking player renderer...", new Object[0]);
        EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        EntityRendererProvider.Context context = new EntityRendererProvider.Context(entityRenderDispatcher, minecraft.getItemRenderer(), minecraft.getBlockRenderer(), itemRenderer, minecraft.getResourceManager(), minecraft.getEntityModels(), font);
        entityRenderDispatcher.playerRenderers = Map.of(PlayerSkin.Model.WIDE, new BFPlayerRenderer(context, false), PlayerSkin.Model.SLIM, new BFPlayerRenderer(context, true));
        BFLog.log("Finished checking and replacing player renderer!", new Object[0]);
    }
}

