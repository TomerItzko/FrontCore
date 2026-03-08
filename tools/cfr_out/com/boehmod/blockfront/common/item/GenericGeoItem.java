/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item$Properties
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoItem
 *  software.bernie.geckolib.animatable.client.GeoRenderProvider
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.Animation$LoopType
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationController$State
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.client.render.geo.GenericGeoRenderer;
import com.boehmod.blockfront.common.item.BFCommonItem;
import com.boehmod.blockfront.util.BFRes;
import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GenericGeoItem
extends BFCommonItem
implements GeoItem {
    @NotNull
    private static final String field_3584 = "gui_controller";
    @NotNull
    private final AnimatableInstanceCache field_3583 = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);
    @NotNull
    private final String id;
    @NotNull
    private final ResourceLocation texture;
    @NotNull
    private final ResourceLocation animation;
    @NotNull
    private final ResourceLocation model;

    public GenericGeoItem(@NotNull String string, @NotNull Item.Properties properties) {
        super(string, properties);
        this.id = string;
        this.texture = BFRes.loc("textures/item/" + this.id + ".png");
        this.animation = BFRes.loc("animations/item/" + this.id + ".animation.json");
        this.model = BFRes.loc("geo/item/" + this.id + ".geo.json");
    }

    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        final GenericGeoItem genericGeoItem = this;
        consumer.accept(new GeoRenderProvider(){
            private GenericGeoRenderer field_3589;

            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.field_3589 == null) {
                    this.field_3589 = new GenericGeoRenderer(genericGeoItem);
                }
                return this.field_3589;
            }
        });
    }

    @NotNull
    public ResourceLocation getTexture() {
        return this.texture;
    }

    @NotNull
    public ResourceLocation getAnimation() {
        return this.animation;
    }

    @NotNull
    public ResourceLocation getModel() {
        return this.model;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void registerControllers(@NotNull AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController((GeoAnimatable)this, field_3584, 0, this::method_3723));
    }

    @OnlyIn(value=Dist.CLIENT)
    protected PlayState method_3723(@NotNull AnimationState<GenericGeoItem> animationState) {
        AnimationController animationController = animationState.getController();
        if (animationController.getAnimationState() == AnimationController.State.STOPPED) {
            animationController.forceAnimationReset();
            animationController.stop();
            String string = "animation." + this.id + ".";
            animationState.setAnimation(RawAnimation.begin().then(string + "idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.field_3583;
    }

    public boolean shouldPlayAnimsWhileGamePaused() {
        return true;
    }
}

