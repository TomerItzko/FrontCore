/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameEventType;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class GameRadioPoint {
    @Nonnull
    private final GameEventType field_7042;
    private static final int field_3271 = 32;
    @NotNull
    private final ResourceLocation texture;
    private final Vec3 field_3272;
    private int duration;

    public GameRadioPoint(@NotNull String string, int n, @NotNull Vec3 vec3, @Nonnull GameEventType gameEventType) {
        this.texture = BFRes.loc("textures/gui/game/radio/" + string + ".png");
        this.duration = n;
        this.field_3272 = vec3;
        this.field_7042 = gameEventType;
    }

    public boolean method_3180(@NotNull LocalPlayer localPlayer) {
        if (Mth.sqrt((float)((float)localPlayer.distanceToSqr(this.field_3272))) <= 5.0f) {
            return true;
        }
        return this.duration-- <= 0;
    }

    public void renderDebug(@NotNull Camera camera, @NotNull LocalPlayer localPlayer, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager) {
        GameTeam gameTeam = abstractGamePlayerManager.getPlayerTeam(localPlayer.getUUID());
        if (gameTeam == null) {
            return;
        }
        int n = Objects.requireNonNull(gameTeam.getStyleText().getColor()).getValue();
        BFRendering.tintedBillboardTexture(poseStack, camera, guiGraphics, this.getIcon(), this.method_3181().add(0.0, 1.5, 0.0), 32, 32, 0.5f, n, false);
    }

    public GameEventType method_5957() {
        return this.field_7042;
    }

    @NotNull
    public ResourceLocation getIcon() {
        return this.texture;
    }

    public Vec3 method_3181() {
        return this.field_3272;
    }
}

