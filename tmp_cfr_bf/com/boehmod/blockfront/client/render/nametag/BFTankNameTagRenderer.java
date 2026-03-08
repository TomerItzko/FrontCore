/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.nametag;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.nametag.BFAbstractNameTagRenderer;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import org.jetbrains.annotations.NotNull;

public class BFTankNameTagRenderer
extends BFAbstractNameTagRenderer<AbstractVehicleEntity> {
    private static final ResourceLocation field_1788 = BFRes.loc("textures/misc/waypoints/waypoint_vehicle.png");

    @Override
    public boolean method_1340(@NotNull RenderNameTagEvent renderNameTagEvent, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull LocalPlayer localPlayer, @NotNull AbstractVehicleEntity abstractVehicleEntity, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGameClient<?, ?> abstractGameClient, @NotNull GameTeam gameTeam) {
        if (minecraft.player != null && (!minecraft.player.hasLineOfSight((Entity)abstractVehicleEntity) || abstractVehicleEntity.equals(minecraft.player.getVehicle()))) {
            return false;
        }
        return gameTeam.getName().equalsIgnoreCase(abstractVehicleEntity.method_2304());
    }

    @Override
    public void method_1336(@NotNull BFClientManager bFClientManager, @NotNull RenderNameTagEvent renderNameTagEvent, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull AbstractVehicleEntity abstractVehicleEntity, @NotNull GuiGraphics guiGraphics, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGameClient<?, ?> abstractGameClient, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager, @NotNull GameTeam gameTeam, int n, float f, float f2) {
        Player player;
        if (minecraft.cameraEntity == null) {
            return;
        }
        BF_624<AbstractVehicleEntity> bF_624 = abstractVehicleEntity.method_2343();
        int n2 = 42;
        int n3 = 24;
        float f3 = -21.0f;
        float f4 = -12.0f;
        float f5 = abstractVehicleEntity.getBbHeight() + 3.0f - bF_624.field_2699;
        Vec2 vec2 = minecraft.cameraEntity.getRotationVector();
        float f6 = 0.025f;
        ResourceLocation resourceLocation = bF_624.method_2421();
        float f7 = 0.5f;
        Object object = abstractVehicleEntity.method_2319();
        if (object instanceof Player && ((BFClientPlayerData)(object = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData(player = (Player)object))).getCalloutTimer() > 0) {
            resourceLocation = ((BFClientPlayerData)object).getWaypointTexture();
            n = ColorReferences.COLOR_THEME_YELLOW_SOLID;
            float f8 = Mth.sin((float)(f / 10.0f));
            f7 = Math.abs(f8);
        }
        BFTankNameTagRenderer.method_1337(guiGraphics, n, 42, 24, f5, vec2, 0.025f, -21.0f, -12.0f, resourceLocation, field_1788, f7);
    }
}

