/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.gui.layer.HealthEffectsGuiLayer;
import com.boehmod.blockfront.client.gui.layer.MatchGuiLayer;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;

public final class BF_39
extends ClientTickable {
    private static final float field_180 = 0.8f;
    private static final float field_181 = 0.005f;
    private static final float field_182 = 0.1f;
    public static float field_183;
    public static float field_184;
    public static float field_185;
    public static float field_186;
    public static float field_187;
    public static float field_188;

    @Override
    public void run(@NotNull ClientTickEvent.Post event, @NotNull Random random, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull BFClientManager manager, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull PlayerCloudData cloudData, @NotNull Vec3 pos, @NotNull BlockPos blockPos, @Nullable AbstractGame<?, ?, ?> game, boolean bl, float renderTime) {
        if (player == null) {
            HealthEffectsGuiLayer.field_534.clear();
            HealthEffectsGuiLayer.DAMAGE_INDICATORS.clear();
            return;
        }
        field_186 = field_185;
        if (field_184 > field_183) {
            field_185 += 0.1f * (field_184 - field_183);
        }
        field_184 = field_183;
        field_183 = player.getHealth();
        if (field_185 > 0.8f) {
            field_185 = 0.8f;
        } else if (field_185 > 0.0f && (field_185 -= 0.005f) < 0.0f) {
            field_185 = 0.0f;
        }
        field_188 = field_187;
        field_187 = Mth.lerp((float)0.05f, (float)field_187, (float)0.0f);
        HealthEffectsGuiLayer.field_534.removeIf(MatchGuiLayer.BF_114::method_511);
        HealthEffectsGuiLayer.DAMAGE_INDICATORS.removeIf(HealthEffectsGuiLayer.DamageIndicator::update);
    }

    static {
        field_184 = 0.0f;
        field_186 = 0.0f;
        field_188 = 0.0f;
    }
}

