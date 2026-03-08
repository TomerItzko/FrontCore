/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.floats.FloatFloatPair
 *  net.minecraft.client.CameraType
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ComputeFovModifierEvent
 *  net.neoforged.neoforge.client.event.ViewportEvent$ComputeCameraAngles
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Quaterniond
 *  org.joml.Quaterniondc
 *  org.joml.Vector3d
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFRenderFrameSubscriber;
import com.boehmod.blockfront.client.event.BFRenderHandSubscriber;
import com.boehmod.blockfront.client.event.tick.GunAimingTickable;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.client.world.BobbingPresets;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.common.entity.CameraEntity;
import com.boehmod.blockfront.common.item.BinocularsItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.game.GameCinematics;
import com.boehmod.blockfront.unnamed.BF_1163;
import com.boehmod.blockfront.unnamed.BF_622;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_643;
import com.boehmod.blockfront.util.math.MathUtils;
import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFCameraSubscriber {
    private static final float field_7047 = -0.15f;
    private static final float field_7048 = 20.0f;
    private static final float field_7049 = 80.0f;
    private static final float field_7050 = 7.0f;
    private static final float field_7051 = 11.0f;
    private static final float field_7052 = 5.5f;

    @SubscribeEvent
    public static void onComputeCameraAngles(@NotNull ViewportEvent.ComputeCameraAngles event) {
        Object object;
        CameraEntity cameraEntity;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null || localPlayer.isSpectator()) {
            return;
        }
        float f = (float)event.getPartialTick();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        GameCinematics gameCinematics = bFClientManager.getCinematics();
        if (gameCinematics.isSequencePlaying()) {
            event.setRoll(event.getRoll() + gameCinematics.getCurrentRoll(f));
            return;
        }
        Entity entity = minecraft.getCameraEntity();
        if (entity instanceof CameraEntity && (cameraEntity = (CameraEntity)entity).method_2489() == BF_643.RAGDOLL) {
            entity = new Quaterniond().rotateX(Math.PI).premul((Quaterniondc)cameraEntity.field_2753);
            Vector3d vector3d = entity.getEulerAnglesYXZ(new Vector3d());
            float f2 = (float)Math.toDegrees(vector3d.y);
            float f3 = (float)Math.toDegrees(vector3d.x);
            float f4 = (float)Math.toDegrees(vector3d.z);
            event.setYaw(-f2);
            event.setPitch(f3);
            event.setRoll(event.getRoll() + f4);
            return;
        }
        cameraEntity = new Vector3f(0.0f, 0.0f, 0.0f);
        float f5 = BFRendering.getRenderTime();
        BFCameraSubscriber.method_456(event, (Vector3f)cameraEntity, f, f5);
        BFCameraSubscriber.method_457(event, (Vector3f)cameraEntity, f5, f);
        float f6 = GunItem.field_4019 ? 0.5f : 1.0f;
        Vector3f vector3f = ShakeManager.getDelta(f).mul(f6);
        cameraEntity.add((Vector3fc)vector3f);
        float f7 = MathUtils.lerpf1(BFRenderHandSubscriber.field_362, BFRenderHandSubscriber.field_363, f);
        float f8 = MathUtils.lerpf1(BFRenderHandSubscriber.field_343, BFRenderHandSubscriber.field_344, f);
        float f9 = MathUtils.lerpf1(BFRenderHandSubscriber.field_345, BFRenderHandSubscriber.field_346, f);
        cameraEntity.add(f7, f8, f9);
        float f10 = f7 = GunItem.field_4019 ? 80.0f : 20.0f;
        if (BFClientSettings.EXPERIMENTAL_TOGGLE_IMMERSIVE_BOBBING.isEnabled()) {
            f8 = BFClientSettings.EXPERIMENTAL_IMMERSIVE_BOBBING_SCALE.getValue();
            Vector3f vector3f2 = BobbingPresets.IMMERSIVE.getCameraVec(f5, f7).mul(f8);
            cameraEntity.add((Vector3fc)vector3f2);
        }
        f8 = MathUtils.lerpf1(GunAimingTickable.field_172, GunAimingTickable.field_173, f);
        Vector3f vector3f3 = BobbingPresets.field_1629.getCameraVec(f5, f7).mul(f8);
        cameraEntity.add((Vector3fc)vector3f3);
        f8 = MathUtils.lerpf1(GunAimingTickable.field_175, GunAimingTickable.field_176, f);
        vector3f3 = BobbingPresets.field_1628.getCameraVec(f5, 1.0f).mul(f8);
        cameraEntity.add((Vector3fc)vector3f3);
        f8 = MathUtils.lerpf1(PlayerTickable.inspectionBlurPrev, PlayerTickable.inspectionBlur, f);
        float f11 = Mth.sin((float)(f5 / 50.0f));
        float f12 = Mth.sin((float)(f5 / 25.0f));
        cameraEntity.add((8.0f + 1.5f * f12) * f8, -4.0f * f11 * f8, -2.0f * f11 * f8);
        float f13 = 7.0f;
        if (localPlayer.isShiftKeyDown()) {
            f13 = 11.0f;
        } else if (localPlayer.isSprinting()) {
            f13 = 5.5f;
        }
        if (BFClientSettings.EXPERIMENTAL_TOGGLE_BOBBING.isEnabled()) {
            float f14 = MathUtils.lerpf1(BFRenderFrameSubscriber.field_338, BFRenderFrameSubscriber.field_339, f);
            float f15 = Mth.sin((float)(f5 / f13)) * f14;
            float f16 = Mth.sin((float)(f5 / (f13 / 2.0f))) * f14;
            cameraEntity.add(f16 * 0.2f, 0.0f, f15 * 0.2f);
            float f17 = MathUtils.lerpf1(BFRenderFrameSubscriber.field_334, BFRenderFrameSubscriber.field_336, f);
            float f18 = Mth.sin((float)(f5 / f13)) * f17;
            float f19 = Mth.sin((float)(f5 / (f13 / 2.0f))) * f17;
            cameraEntity.add(f19, 0.0f, f18);
        }
        if ((object = localPlayer.getVehicle()) instanceof AbstractVehicleEntity) {
            AbstractVehicleEntity abstractVehicleEntity = (AbstractVehicleEntity)object;
            if (minecraft.options.getCameraType() == CameraType.FIRST_PERSON) {
                object = abstractVehicleEntity.method_2316();
                FloatFloatPair floatFloatPair = ((BF_622)object).method_2373(f);
                Vec3 vec3 = new Vec3((double)(8.0f * floatFloatPair.firstFloat()), 0.0, (double)(-8.0f * floatFloatPair.secondFloat())).yRot((float)Math.toRadians(localPlayer.getYHeadRot() - abstractVehicleEntity.getYRot()));
                cameraEntity.add((Vector3fc)vec3.toVector3f());
            }
        }
        event.setPitch(event.getPitch() + ((Vector3f)cameraEntity).x);
        event.setYaw(event.getYaw() + ((Vector3f)cameraEntity).y);
        event.setRoll(event.getRoll() + ((Vector3f)cameraEntity).z);
    }

    @SubscribeEvent
    public static void onComputeFovModifier(@NotNull ComputeFovModifierEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        float f = MathUtils.getTickDelta(minecraft);
        float f2 = event.getFovModifier();
        if (localPlayer != null) {
            Object object;
            float f3;
            Object object2;
            Object object3;
            ItemStack itemStack = localPlayer.getMainHandItem();
            if (!itemStack.isEmpty() && (object3 = itemStack.getItem()) instanceof GunItem) {
                object2 = (GunItem)object3;
                object3 = ((GunItem)object2).getScopeConfig(itemStack);
                f3 = object3.field_3852;
                float f4 = object3.field_3851;
                float f5 = MathUtils.lerpf1(GunAimingTickable.field_167, GunAimingTickable.field_168, f);
                f2 -= f4 * f5;
                float f6 = MathUtils.lerpf1(GunItem.field_4041, GunItem.field_4042, f);
                f2 -= 0.1f * f6;
            }
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof BinocularsItem) {
                boolean bl;
                boolean bl2 = bl = localPlayer.isUsingItem() && localPlayer.getUseItem().getItem() instanceof BinocularsItem;
                if (minecraft.options.getCameraType().isFirstPerson() && bl) {
                    float f7 = 0.8f;
                    f2 -= 0.8f;
                }
            }
            if ((object = localPlayer.getVehicle()) instanceof AbstractVehicleEntity) {
                BF_623 bF_623;
                object2 = (AbstractVehicleEntity)object;
                object = ((AbstractVehicleEntity)object2).method_2343();
                f3 = ((AbstractVehicleEntity)object2).method_2313() * (((AbstractVehicleEntity)object2).method_2333() ? object.field_2686 : object.field_2687);
                f2 += object.field_2690 * f3;
                if (minecraft.options.getCameraType() == CameraType.FIRST_PERSON && (bF_623 = ((AbstractVehicleEntity)object2).method_2324((Entity)localPlayer)) != null && ArtilleryVehicleEntity.field_2637) {
                    f2 -= bF_623.method_2379();
                }
            }
        }
        float f8 = MathUtils.lerpf1(PlayerTickable.field_153, PlayerTickable.field_154, f);
        event.setNewFovModifier(f2 += -0.15f * f8);
    }

    public static void method_456(@NotNull ViewportEvent.ComputeCameraAngles computeCameraAngles, Vector3f vector3f, float f, float f2) {
        float f3 = MathUtils.lerpf1(BF_1163.field_6611, BF_1163.field_6612, f);
        computeCameraAngles.setPitch(computeCameraAngles.getPitch() - f3);
    }

    public static void method_457(@NotNull ViewportEvent.ComputeCameraAngles computeCameraAngles, Vector3f vector3f, float f, float f2) {
        float f3 = MathUtils.lerpf1(PlayerTickable.field_147, PlayerTickable.field_148, f2);
        float f4 = Mth.sin((float)(f / 2.8f)) + 1.0f;
        float f5 = Mth.sin((float)(f / 3.2f)) * f4;
        float f6 = Mth.sin((float)(f / 2.4f));
        float f7 = Mth.sin((float)(f / 3.6f)) * f6;
        float f8 = Mth.sin((float)(f / 2.0f));
        float f9 = Mth.sin((float)(f / 3.0f)) * f8;
        Vector3f vector3f2 = new Vector3f(0.1f * f5, 0.2f * f7, 0.3f * f9);
        vector3f.add(vector3f2.x * f3, vector3f2.y * f3, vector3f2.z * f3);
    }
}

