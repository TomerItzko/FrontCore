/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.nametag.BFAbstractNameTagRenderer;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.BFRes;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import org.jetbrains.annotations.NotNull;

public abstract class BF_379<T extends LivingEntity>
extends BFAbstractNameTagRenderer<T> {
    protected static final float field_1787 = 15.0f;
    protected static final ResourceLocation field_1786 = BFRes.loc("textures/misc/waypoints/waypoint_player.png");

    @Override
    public boolean method_1340(@NotNull RenderNameTagEvent renderNameTagEvent, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @Nullable LocalPlayer localPlayer, @NotNull T t, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGameClient<?, ?> abstractGameClient, @NotNull GameTeam gameTeam) {
        if (localPlayer != null && !localPlayer.hasLineOfSight(t)) {
            return false;
        }
        return this.method_1348(t, abstractGame).equals(gameTeam.getName()) && t.getVehicle() == null;
    }

    @Override
    public void method_1336(@NotNull BFClientManager bFClientManager, @NotNull RenderNameTagEvent renderNameTagEvent, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull T t, @NotNull GuiGraphics guiGraphics, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGameClient<?, ?> abstractGameClient, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager, @NotNull GameTeam gameTeam, int n, float f, float f2) {
        if (minecraft.cameraEntity == null) {
            return;
        }
        LocalPlayer localPlayer = minecraft.player;
        int n2 = 28;
        int n3 = 16;
        float f3 = t.getBbHeight() + 1.0f;
        Vec2 vec2 = minecraft.cameraEntity.getRotationVector();
        float f4 = 0.025f;
        float f5 = -14.0f;
        float f6 = -8.0f;
        this.method_1345(bFClientManager, clientPlayerDataHandler, guiGraphics, t, abstractGame, abstractGamePlayerManager, localPlayer, vec2, n, 28, 16, f3, f4, -14.0f, -8.0f, f);
    }

    public void method_1345(@NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull GuiGraphics guiGraphics, @NotNull T t, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager, @Nullable LocalPlayer localPlayer, @NotNull Vec2 vec2, int n, int n2, int n3, float f, float f2, float f3, float f4, float f5) {
        ResourceLocation resourceLocation = this.method_1347(t, abstractGame).getIcon();
        BF_379.method_1337(guiGraphics, n, n2, n3, f, vec2, f2, f3, f4, resourceLocation, field_1786, 0.5f);
    }

    public abstract String method_1348(@NotNull T var1, @NotNull AbstractGame<?, ?, ?> var2);

    public abstract MatchClass method_1347(@NotNull T var1, @NotNull AbstractGame<?, ?, ?> var2);
}

