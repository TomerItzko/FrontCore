/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.geom.EntityModelSet
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.constant.DataTickets
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoRenderer
 */
package com.boehmod.blockfront.client.render.geo;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.geo.model.GenericGeoModel;
import com.boehmod.blockfront.common.item.GenericGeoItem;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class GenericGeoRenderer
extends BlockEntityWithoutLevelRenderer
implements GeoRenderer<GenericGeoItem> {
    @NotNull
    private final GenericGeoModel model;
    @NotNull
    private GenericGeoItem animatable;

    public GenericGeoRenderer(@NotNull GenericGeoItem genericGeoItem) {
        this(genericGeoItem, Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), new GenericGeoModel());
    }

    public GenericGeoRenderer(@NotNull GenericGeoItem genericGeoItem, @NotNull BlockEntityRenderDispatcher blockEntityRenderDispatcher, @NotNull EntityModelSet entityModelSet, @NotNull GenericGeoModel genericGeoModel) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.model = genericGeoModel;
        this.animatable = genericGeoItem;
    }

    public GenericGeoModel getGeoModel() {
        return this.model;
    }

    public GenericGeoItem getAnimatable() {
        return this.animatable;
    }

    public void renderByItem(@NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2) {
        this.animatable = (GenericGeoItem)itemStack.getItem();
        Minecraft minecraft = Minecraft.getInstance();
        float f = MathUtils.getTickDelta(minecraft);
        GenericGeoModel genericGeoModel = this.getGeoModel();
        AnimationState animationState = new AnimationState((GeoAnimatable)this.animatable, 0.0f, 0.0f, f, false);
        animationState.setData(DataTickets.ITEMSTACK, (Object)itemStack);
        animationState.animationTick += (double)f;
        this.model.handleAnimations((GeoAnimatable)this.animatable, this.getInstanceId((GeoAnimatable)this.animatable), animationState, f);
        BakedGeoModel bakedGeoModel = genericGeoModel.getBakedModel(genericGeoModel.getModelResource(this.getAnimatable()));
        RenderType renderType = RenderType.entityTranslucentCull((ResourceLocation)this.getTextureLocation(this.animatable));
        this.actuallyRender(poseStack, (GeoAnimatable)this.animatable, bakedGeoModel, renderType, multiBufferSource, multiBufferSource.getBuffer(renderType), false, f, 0xF000F0, OverlayTexture.NO_OVERLAY, ColorReferences.COLOR_WHITE_SOLID);
    }

    public void fireCompileRenderLayersEvent() {
    }

    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float f, int n) {
        return true;
    }

    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float f, int n) {
    }

    public void updateAnimatedTextureFrame(GenericGeoItem genericGeoItem) {
    }

    public ResourceLocation getTextureLocation(GenericGeoItem genericGeoItem) {
        return this.model.getTextureResource(genericGeoItem);
    }

    public /* synthetic */ void updateAnimatedTextureFrame(GeoAnimatable geoAnimatable) {
        this.updateAnimatedTextureFrame((GenericGeoItem)geoAnimatable);
    }

    public /* synthetic */ ResourceLocation getTextureLocation(GeoAnimatable geoAnimatable) {
        return this.getTextureLocation((GenericGeoItem)geoAnimatable);
    }

    public /* synthetic */ GeoAnimatable getAnimatable() {
        return this.getAnimatable();
    }

    public /* synthetic */ GeoModel getGeoModel() {
        return this.getGeoModel();
    }
}

