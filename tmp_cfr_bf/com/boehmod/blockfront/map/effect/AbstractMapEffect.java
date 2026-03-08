/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMapEffect {
    public abstract void updateGame(@NotNull ServerLevel var1, @NotNull BFAbstractManager<?, ?, ?> var2, AbstractGame<?, ?, ?> var3, @NotNull Random var4, @NotNull Set<UUID> var5);

    @OnlyIn(value=Dist.CLIENT)
    public abstract void updateGameClient(@NotNull Minecraft var1, @NotNull BFClientManager var2, @NotNull Random var3, @NotNull AbstractGame<?, ?, ?> var4, @NotNull LocalPlayer var5, @NotNull ClientLevel var6, float var7);

    @OnlyIn(value=Dist.CLIENT)
    public abstract void render(@NotNull Minecraft var1, @NotNull BFClientManager var2, @NotNull LocalPlayer var3, @NotNull ClientLevel var4, @NotNull BlockRenderDispatcher var5, @NotNull MultiBufferSource.BufferSource var6, @NotNull Random var7, @NotNull RenderLevelStageEvent var8, @NotNull GuiGraphics var9, @NotNull PoseStack var10, @NotNull Camera var11, float var12, float var13);

    @OnlyIn(value=Dist.CLIENT)
    public abstract void renderDebug(@NotNull Minecraft var1, @NotNull RenderLevelStageEvent var2, @NotNull MultiBufferSource.BufferSource var3, @NotNull PoseStack var4, @NotNull GuiGraphics var5, @NotNull Font var6, @NotNull Camera var7);

    @OnlyIn(value=Dist.CLIENT)
    public abstract boolean requiresFancyGraphics();

    public abstract void reset();

    @NotNull
    public abstract RenderLevelStageEvent.Stage getRenderStage();

    @OverridingMethodsMustInvokeSuper
    public abstract void writeToFDS(@NotNull FDSTagCompound var1);

    @OverridingMethodsMustInvokeSuper
    public abstract void readFromFDS(@NotNull FDSTagCompound var1);
}

