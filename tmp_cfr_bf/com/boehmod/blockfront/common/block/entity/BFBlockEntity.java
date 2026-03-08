/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block.entity;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BFBlockEntity
extends BlockEntity
implements GeoBlockEntity {
    @NotNull
    private static final String field_2229 = "block_controller";
    @NotNull
    private final AnimatableInstanceCache field_2227 = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);
    @NotNull
    private final List<String> queuedAnimations = new ObjectArrayList();
    @Nullable
    private AbstractVehicleEntity vehicleEntity = null;
    @Nullable
    private String field_2230 = null;
    private float field_2232 = 0.0f;
    private boolean field_2231 = false;
    @NotNull
    private final ThreadLocal<AnimatableInstanceCache> field_2226 = new ThreadLocal();

    public BFBlockEntity(@NotNull BlockEntityType<?> blockEntityType, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void method_1887() {
        this.field_2231 = true;
    }

    public boolean method_1882() {
        return this.field_2231 || this.vehicleEntity != null;
    }

    @Nullable
    public AbstractVehicleEntity getVehicleEntity() {
        return this.vehicleEntity;
    }

    public void setVehicleEntity(@NotNull AbstractVehicleEntity entity) {
        this.vehicleEntity = entity;
    }

    public void method_1888(@NotNull String string) {
        this.field_2230 = string;
    }

    public void queueAnimation(@NotNull String id) {
        this.queuedAnimations.add(id);
    }

    public float method_1890() {
        return this.field_2232;
    }

    @NotNull
    public <E extends BlockEntity> PlayState method_1885(@NotNull AnimationState<E> animationState) {
        String string;
        if (this.field_2232 > 0.0f) {
            this.field_2232 -= animationState.getPartialTick();
        }
        AnimationController animationController = animationState.getController();
        Block block = ((BlockEntity)animationState.getAnimatable()).getBlockState().getBlock();
        String string2 = BuiltInRegistries.BLOCK.getKey((Object)block).getPath();
        String string3 = string = this.field_2230 != null ? this.field_2230 : "idle";
        if (!this.queuedAnimations.isEmpty()) {
            animationController.forceAnimationReset();
            animationController.stop();
            RawAnimation rawAnimation = RawAnimation.begin();
            for (String string4 : this.queuedAnimations) {
                rawAnimation.then("animation." + string2 + "." + string4, Animation.LoopType.PLAY_ONCE);
            }
            this.queuedAnimations.clear();
            rawAnimation.then("animation." + string2 + "." + string, Animation.LoopType.LOOP);
            animationState.setAnimation(rawAnimation);
        }
        if (animationController.getAnimationState() == AnimationController.State.STOPPED) {
            animationController.setAnimation(RawAnimation.begin().thenLoop("animation." + string2 + "." + string));
        }
        return PlayState.CONTINUE;
    }

    protected void method_1886(@NotNull CustomInstructionKeyframeEvent<BFBlockEntity> customInstructionKeyframeEvent) {
        String string = customInstructionKeyframeEvent.getKeyframeData().getInstructions();
        if (string.contains("fire;")) {
            this.field_2232 = 5.0f;
        }
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController((GeoAnimatable)this, field_2229, this::method_1885).setCustomInstructionKeyframeHandler(this::method_1886));
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        if (!this.method_1882()) {
            AnimatableInstanceCache animatableInstanceCache = this.field_2226.get();
            if (animatableInstanceCache == null) {
                animatableInstanceCache = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);
                this.field_2226.set(animatableInstanceCache);
            }
            return animatableInstanceCache;
        }
        return this.field_2227;
    }
}

