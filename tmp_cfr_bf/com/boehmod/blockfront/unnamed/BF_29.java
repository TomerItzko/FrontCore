/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFRenderFrameSubscriber;
import com.boehmod.blockfront.client.event.BFRenderHandSubscriber;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.entity.CameraEntity;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameCinematics;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;

public final class BF_29
extends ClientTickable {
    public static float field_119 = 0.0f;
    public static float field_120 = 0.0f;

    @Override
    public void run(@NotNull ClientTickEvent.Post event, @NotNull Random random, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull BFClientManager manager, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull PlayerCloudData cloudData, @NotNull Vec3 pos, @NotNull BlockPos blockPos, @Nullable AbstractGame<?, ?, ?> game, boolean bl, float renderTime) {
        if (player == null || level == null) {
            return;
        }
        ShakeManager.update(renderTime);
        this.method_159(player);
        this.method_160(player);
        this.method_158(manager, minecraft, playerData, player);
    }

    private void method_158(@NotNull BFClientManager bFClientManager, @NotNull Minecraft minecraft, @NotNull BFClientPlayerData bFClientPlayerData, @NotNull LocalPlayer localPlayer) {
        BF_30 bF_30 = BF_30.PLAYER;
        if (bFClientPlayerData.isOutOfGame() || localPlayer.isDeadOrDying()) {
            bF_30 = BF_30.DEATH_FADE;
        }
        GameCinematics gameCinematics = bFClientManager.getCinematics();
        if (bFClientManager.getCinematics().isSequencePlaying()) {
            bF_30 = BF_30.CINEMATIC;
        }
        switch (bF_30.ordinal()) {
            case 0: {
                if (minecraft.getCameraEntity() != localPlayer) {
                    ClientUtils.setCameraEntity(minecraft, (Entity)localPlayer);
                }
                field_120 = 0.0f;
                field_119 = 0.0f;
                return;
            }
            case 1: {
                CameraEntity cameraEntity = gameCinematics.getCamera();
                if (cameraEntity != null && minecraft.getCameraEntity() != cameraEntity) {
                    ClientUtils.setCameraEntity(minecraft, cameraEntity);
                }
                field_120 = 0.0f;
                field_119 = 0.0f;
                return;
            }
            case 2: {
                field_120 = field_119;
                field_119 = MathUtils.moveTowards(field_119, 2.0f, 0.02f);
            }
        }
    }

    private void method_159(@NotNull LocalPlayer localPlayer) {
        float f = localPlayer.walkDist - localPlayer.walkDistO;
        boolean bl = localPlayer.isSprinting();
        boolean bl2 = localPlayer.onGround() && f > 0.0f;
        BFRenderFrameSubscriber.field_339 = BFRenderFrameSubscriber.field_338;
        BFRenderFrameSubscriber.field_338 = Mth.lerp((float)0.3f, (float)BFRenderFrameSubscriber.field_338, (float)(bl ? 1.0f : 0.0f));
        BFRenderFrameSubscriber.field_336 = BFRenderFrameSubscriber.field_334;
        BFRenderFrameSubscriber.field_334 = Mth.lerp((float)0.3f, (float)BFRenderFrameSubscriber.field_334, (float)(bl2 ? 1.0f : 0.0f));
        BFRenderFrameSubscriber.field_341 = BFRenderFrameSubscriber.field_340;
        BFRenderFrameSubscriber.field_340 = Mth.lerp((float)0.4f, (float)BFRenderFrameSubscriber.field_340, (float)0.0f);
        Vec3 vec3 = localPlayer.position().subtract(localPlayer.xOld, localPlayer.yOld, localPlayer.zOld).yRot((localPlayer.getYRot() + 90.0f) * ((float)Math.PI / 180) - 1.5707964f).scale(10.0);
        Item item = localPlayer.getMainHandItem().getItem();
        if (item instanceof BFWeaponItem) {
            BFWeaponItem bFWeaponItem = (BFWeaponItem)item;
            float f2 = (float)(vec3.x + vec3.y + vec3.z);
            bFWeaponItem.field_3634 += Mth.abs((float)f2) / 100.0f;
        }
    }

    private void method_160(@NotNull LocalPlayer localPlayer) {
        boolean bl = localPlayer.isSprinting();
        BFRenderHandSubscriber.field_348 = BFRenderHandSubscriber.field_347;
        float f = bl ? 0.1f : (GunItem.field_4019 ? 0.4f : 0.2f);
        BFRenderHandSubscriber.field_347 = Mth.lerp((float)f, (float)BFRenderHandSubscriber.field_347, (float)(bl ? 1.25f : 0.0f));
    }

    private static enum BF_30 {
        PLAYER,
        CINEMATIC,
        DEATH_FADE;

    }
}

