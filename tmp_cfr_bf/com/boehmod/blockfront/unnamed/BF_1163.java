/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.event.tick.GunSpreadTickable;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.gun.GunCameraConfig;
import com.boehmod.blockfront.common.gun.GunSpreadTarget;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BF_1163
extends ClientTickable {
    public static float field_6609 = 0.0f;
    public static float field_6610 = 0.0f;
    public static float field_6611 = 0.0f;
    public static float field_6612 = 0.0f;
    public static float field_6613 = 0.0f;
    public static float field_6614 = 0.0f;
    public static float field_6615 = 0.0f;
    public static float field_6616 = 0.0f;
    private static final float field_6617 = 0.85f;

    @Override
    public void run(@NotNull ClientTickEvent.Post event, @NotNull Random random, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull BFClientManager manager, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull PlayerCloudData cloudData, @NotNull Vec3 pos, @NotNull BlockPos blockPos, @Nullable AbstractGame<?, ?, ?> game, boolean bl, float renderTime) {
        Object object;
        field_6610 = field_6609;
        field_6612 = field_6611;
        field_6614 = field_6613;
        field_6616 = field_6615;
        if (player != null && (object = player.getMainHandItem().getItem()) instanceof GunItem) {
            GunItem gunItem = (GunItem)object;
            object = gunItem.getCameraConfig();
            float f = ((GunCameraConfig)object).recoverSpeed() * (GunItem.field_4019 ? 2.0f : 1.0f);
            field_6609 = Mth.lerp((float)f, (float)field_6609, (float)0.0f);
            float f2 = 1.0f - PlayerTickable.field_153 * 0.5f;
            float f3 = ((GunCameraConfig)object).maxCameraPitch() * f2 * field_6609;
            float f4 = ((GunCameraConfig)object).maxModelPitch() * (GunItem.field_4019 ? 0.25f : 1.0f) * field_6609;
            float f5 = ((GunCameraConfig)object).maxModelDistance() * (GunItem.field_4019 ? 0.75f : 1.0f) * field_6609;
            field_6611 = Mth.lerp((float)0.85f, (float)field_6611, (float)f3);
            field_6613 = Mth.lerp((float)0.85f, (float)field_6613, (float)f4);
            field_6615 = Mth.lerp((float)0.85f, (float)field_6615, (float)f5);
        } else {
            field_6609 = 0.0f;
            field_6611 = Mth.lerp((float)0.85f, (float)field_6611, (float)0.0f);
            field_6613 = Mth.lerp((float)0.85f, (float)field_6613, (float)0.0f);
            field_6615 = Mth.lerp((float)0.85f, (float)field_6615, (float)0.0f);
        }
    }

    public static void method_5621(@NotNull GunCameraConfig gunCameraConfig) {
        float f = switch (GunSpreadTickable.target) {
            default -> throw new MatchException(null, null);
            case GunSpreadTarget.IDLE -> gunCameraConfig.idleAmount();
            case GunSpreadTarget.WALKING -> gunCameraConfig.walkingAmount();
            case GunSpreadTarget.SPRINTING -> gunCameraConfig.sprintingAmount();
            case GunSpreadTarget.CRAWLING -> gunCameraConfig.crawlingAmount();
            case GunSpreadTarget.JUMPING -> gunCameraConfig.jumpingAmount();
        };
        f = GunItem.field_4019 ? f * 0.5f : f;
        field_6609 += (f *= 1.0f - PlayerTickable.field_153 * 0.5f);
        field_6610 = (field_6610 + f) * 0.8f;
    }
}

