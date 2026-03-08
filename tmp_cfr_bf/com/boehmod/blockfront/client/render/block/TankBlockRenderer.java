/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.block;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.block.BFBlockRenderer;
import com.boehmod.blockfront.client.render.geo.gun.MuzzleFlashGunGeoPart;
import com.boehmod.blockfront.common.block.base.BFBlockProperties;
import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.vehicle.AbstractVehicleControl;
import com.boehmod.blockfront.unnamed.BF_232;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.unnamed.BF_631;
import com.boehmod.blockfront.unnamed.BF_633;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class TankBlockRenderer<T extends BFBlockEntity>
extends BFBlockRenderer<T> {
    private static final Pattern field_1766 = Pattern.compile("^part_turret(\\d*)$");
    @NotNull
    private static final IntegerProperty field_6526 = BFBlockProperties.BODY_ROTATION;
    @NotNull
    private static final IntegerProperty field_6527 = BFBlockProperties.HEAD_ROTATION;
    private static final int field_1767 = 256;

    public TankBlockRenderer(@NotNull BlockEntityRendererProvider.Context context) {
        this(new BF_232());
    }

    public TankBlockRenderer(@NotNull GeoModel<T> geoModel) {
        super(geoModel);
    }

    @Override
    public boolean method_5953() {
        return true;
    }

    @Override
    public boolean method_1284(@NotNull BFBlockEntity bFBlockEntity) {
        return !bFBlockEntity.method_1882();
    }

    @NotNull
    public static String method_1291(@NotNull String string, @NotNull String string2) {
        return string.substring(5, string.length() - string2.length());
    }

    public void method_1295(@NotNull GeoBone geoBone2, @NotNull BF_624<?> bF_624, @NotNull AbstractVehicleEntity abstractVehicleEntity, float f) {
        String string = geoBone2.getName();
        this.method_1296(geoBone2, bF_624, string, f);
        this.method_1294(geoBone2, abstractVehicleEntity, string, f);
        this.method_1298(geoBone2, bF_624, string, f);
        geoBone2.getChildBones().forEach(geoBone -> this.method_1295((GeoBone)geoBone, bF_624, abstractVehicleEntity, f));
    }

    private void method_1294(@NotNull GeoBone geoBone, @NotNull AbstractVehicleEntity abstractVehicleEntity, @NotNull String string, float f) {
        if (!string.endsWith("_barrel_main")) {
            return;
        }
        String string2 = TankBlockRenderer.method_1291(string, "_barrel_main");
        BF_624<AbstractVehicleEntity> bF_624 = abstractVehicleEntity.method_2343();
        BF_633<AbstractVehicleEntity> bF_633 = bF_624.method_2403(string2);
        if (bF_633 instanceof BF_631) {
            BF_631 bF_631 = (BF_631)bF_633;
            geoBone.setPosZ(bF_631.method_2462(f));
        }
    }

    private void method_1296(@NotNull GeoBone geoBone, @NotNull BF_624<?> bF_624, @NotNull String string, float f) {
        if (!string.startsWith("wheel_")) {
            return;
        }
        AbstractVehicleControl abstractVehicleControl = bF_624.field_2675;
        if (abstractVehicleControl == null) {
            return;
        }
        boolean bl = string.contains("front");
        if (string.contains("left")) {
            geoBone.setRotX(abstractVehicleControl.method_2299(f));
            if (bl) {
                geoBone.setRotY(-abstractVehicleControl.method_2301(f) * 0.2f);
            }
        } else if (string.contains("right")) {
            geoBone.setRotX(-abstractVehicleControl.method_2300(f));
            if (bl) {
                geoBone.setRotY(-abstractVehicleControl.method_2302(f) * 0.2f);
            }
        }
    }

    private void method_1298(@NotNull GeoBone geoBone, @NotNull BF_624<?> bF_624, String string, float f) {
        for (BF_633 bF_633 : bF_624.field_2679) {
            if (!string.equals("part_" + bF_633.field_2736)) continue;
            float f2 = bF_633.method_2471(f);
            geoBone.setRotY(-f2 * ((float)Math.PI / 180));
            for (GeoBone geoBone2 : geoBone.getChildBones()) {
                String string2 = geoBone2.getName();
                if (!string2.startsWith("barrel") || bF_633.method_2465() == null) continue;
                Entity entity = bF_633.method_2465();
                float f3 = Mth.clamp((float)entity.getViewXRot(f), (float)-15.0f, (float)10.0f);
                geoBone2.setRotX(-f3 * ((float)Math.PI / 180));
            }
        }
    }

    public void method_1293(@NotNull GeoBone geoBone2, float f, float f2) {
        String string = geoBone2.getName();
        float f3 = 0.0f;
        if (string.equals("body")) {
            f3 = f2;
        }
        if (string.equals("head") || field_1766.matcher(string).matches()) {
            f3 = f;
        }
        if (f3 != 0.0f) {
            geoBone2.setRotY(-f3 * ((float)Math.PI / 180));
        }
        geoBone2.getChildBones().forEach(geoBone -> this.method_1293((GeoBone)geoBone, f, f2));
    }

    private boolean method_1292(@NotNull T t, @Nullable AbstractVehicleEntity abstractVehicleEntity, @NotNull PoseStack poseStack, @NotNull GeoBone geoBone, @NotNull MultiBufferSource multiBufferSource, String string, int n, float f) {
        BF_624<AbstractVehicleEntity> bF_624;
        BF_633<AbstractVehicleEntity> bF_633;
        if (!string.endsWith("_muzzleflash")) {
            return false;
        }
        String string2 = TankBlockRenderer.method_1291(string, "_muzzleflash");
        float f2 = ((BFBlockEntity)((Object)t)).method_1890();
        if (abstractVehicleEntity != null && (bF_633 = (bF_624 = abstractVehicleEntity.method_2343()).method_2403(string2)) instanceof BF_631) {
            BF_631 bF_631 = (BF_631)bF_633;
            f2 = bF_631.method_2453();
        }
        if (f2 > 0.0f) {
            this.method_1290(poseStack, geoBone, t, multiBufferSource, f, n);
        }
        return true;
    }

    private void method_1290(@NotNull PoseStack poseStack, @NotNull GeoBone geoBone, @NotNull T t, @NotNull MultiBufferSource multiBufferSource, float f, int n) {
        RenderType renderType = RenderType.beaconBeam((ResourceLocation)MuzzleFlashGunGeoPart.TEXTURES.getFirst(), (boolean)true);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
        super.renderRecursively(poseStack, t, geoBone, renderType, multiBufferSource, vertexConsumer, false, f, 0xF000F0, n, ColorReferences.COLOR_WHITE_SOLID);
    }

    public void renderRecursively(PoseStack poseStack, T t, GeoBone geoBone, RenderType renderType, MultiBufferSource multiBufferSource, VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        String string;
        AbstractVehicleEntity abstractVehicleEntity = ((BFBlockEntity)((Object)t)).getVehicleEntity();
        if (this.method_1292(t, abstractVehicleEntity, poseStack, geoBone, multiBufferSource, string = geoBone.getName(), n2, f)) {
            return;
        }
        super.renderRecursively(poseStack, t, geoBone, renderType, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
    }

    public int getViewDistance() {
        return 256;
    }

    public boolean shouldRender(@NotNull BFBlockEntity bFBlockEntity, @NotNull Vec3 vec3) {
        return Vec3.atCenterOf((Vec3i)bFBlockEntity.getBlockPos()).closerThan((Position)vec3, 256.0);
    }

    @NotNull
    public AABB getRenderBoundingBox(@NotNull T t) {
        return super.getRenderBoundingBox(t).inflate(5.0);
    }

    public boolean shouldRenderOffScreen(@NotNull BFBlockEntity bFBlockEntity) {
        return false;
    }

    public long getInstanceId(@NotNull T t) {
        if (((BFBlockEntity)((Object)t)).getVehicleEntity() != null) {
            return ((BFBlockEntity)((Object)t)).getVehicleEntity().getId();
        }
        return t.getBlockState().getBlock().hashCode();
    }

    public void preRender(PoseStack poseStack, T t, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        BlockState blockState;
        super.preRender(poseStack, t, bakedGeoModel, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
        List list = bakedGeoModel.topLevelBones();
        AbstractVehicleEntity abstractVehicleEntity = ((BFBlockEntity)((Object)t)).getVehicleEntity();
        if (abstractVehicleEntity != null) {
            blockState = abstractVehicleEntity.method_2343();
            for (GeoBone geoBone : list) {
                for (GeoBone geoBone2 : geoBone.getChildBones()) {
                    this.method_1295(geoBone2, (BF_624<?>)blockState, abstractVehicleEntity, f);
                }
            }
        }
        blockState = t.getBlockState();
        float f2 = 45.0f * (float)((Integer)blockState.getValue((Property)field_6527)).intValue();
        float f3 = 45.0f * (float)((Integer)blockState.getValue((Property)field_6526)).intValue();
        for (GeoBone geoBone2 : list) {
            for (GeoBone geoBone : geoBone2.getChildBones()) {
                this.method_1293(geoBone, f2, f3);
            }
        }
    }

    public /* synthetic */ void renderRecursively(PoseStack poseStack, BlockEntity blockEntity, GeoBone geoBone, RenderType renderType, MultiBufferSource multiBufferSource, VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        this.renderRecursively(poseStack, (T)((Object)((BFBlockEntity)blockEntity)), geoBone, renderType, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
    }

    public /* synthetic */ void preRender(PoseStack poseStack, BlockEntity blockEntity, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        this.preRender(poseStack, (T)((Object)((BFBlockEntity)blockEntity)), bakedGeoModel, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
    }

    public /* synthetic */ long getInstanceId(@NotNull BlockEntity blockEntity) {
        return this.getInstanceId((T)((Object)((BFBlockEntity)blockEntity)));
    }

    public /* synthetic */ void renderRecursively(PoseStack poseStack, GeoAnimatable geoAnimatable, GeoBone geoBone, RenderType renderType, MultiBufferSource multiBufferSource, VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        this.renderRecursively(poseStack, (T)((Object)((BFBlockEntity)geoAnimatable)), geoBone, renderType, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
    }

    public /* synthetic */ void preRender(PoseStack poseStack, GeoAnimatable geoAnimatable, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        this.preRender(poseStack, (T)((Object)((BFBlockEntity)geoAnimatable)), bakedGeoModel, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
    }

    public /* synthetic */ long getInstanceId(@NotNull GeoAnimatable geoAnimatable) {
        return this.getInstanceId((T)((Object)((BFBlockEntity)geoAnimatable)));
    }

    public /* synthetic */ boolean shouldRender(@NotNull BlockEntity blockEntity, @NotNull Vec3 vec3) {
        return this.shouldRender((BFBlockEntity)blockEntity, vec3);
    }

    public /* synthetic */ boolean shouldRenderOffScreen(@NotNull BlockEntity blockEntity) {
        return this.shouldRenderOffScreen((BFBlockEntity)blockEntity);
    }

    @NotNull
    public /* synthetic */ AABB getRenderBoundingBox(@NotNull BlockEntity blockEntity) {
        return this.getRenderBoundingBox((T)((Object)((BFBlockEntity)blockEntity)));
    }
}

