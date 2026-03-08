/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.animation.Animation$LoopType
 *  software.bernie.geckolib.animation.AnimationController
 *  software.bernie.geckolib.animation.AnimationController$State
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.animation.PlayState
 *  software.bernie.geckolib.animation.RawAnimation
 *  software.bernie.geckolib.animation.keyframe.event.CustomInstructionKeyframeEvent
 *  software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent
 *  software.bernie.geckolib.constant.DataTickets
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BF_909
extends BFWeaponItem<BF_909> {
    @NotNull
    private final AnimatableInstanceCache field_3642 = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);
    @NotNull
    private final List<String> field_3641 = new ObjectArrayList();
    @NotNull
    private final String field_3640;

    public BF_909(@NotNull String string, @NotNull String string2, @NotNull Item.Properties properties) {
        super(string, properties);
        this.field_3640 = string2;
    }

    public void method_3782(@NotNull String string) {
        this.field_3641.add(string);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected void method_3769(@NotNull SoundKeyframeEvent<BF_909> soundKeyframeEvent) {
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected void method_3776(@NotNull CustomInstructionKeyframeEvent<BF_909> customInstructionKeyframeEvent) {
        String string;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        ClientLevel clientLevel = minecraft.level;
        if (localPlayer == null || clientLevel == null) {
            return;
        }
        switch (string = customInstructionKeyframeEvent.getKeyframeData().getInstructions()) {
            default: 
        }
        if (string.startsWith("shake(") && string.endsWith(");")) {
            ShakeManager.applyInstruction(string);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void registerControllers(@NotNull AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController((GeoAnimatable)this, this.field_3640, 0, this::method_3768).setSoundKeyframeHandler(this::method_3769).setCustomInstructionKeyframeHandler(this::method_3776));
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.field_3642;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected PlayState method_3768(@NotNull AnimationState<BF_909> animationState) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return PlayState.STOP;
        }
        ItemDisplayContext itemDisplayContext = (ItemDisplayContext)animationState.getData(DataTickets.ITEM_RENDER_PERSPECTIVE);
        if (itemDisplayContext == null || !itemDisplayContext.firstPerson()) {
            return PlayState.STOP;
        }
        BF_909 bF_909 = (BF_909)animationState.getAnimatable();
        String string = bF_909.method_3757();
        ItemStack itemStack = (ItemStack)animationState.getData(DataTickets.ITEMSTACK);
        if (itemStack == null) {
            return PlayState.STOP;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        boolean bl = clientPlayerDataHandler.getPlayerData(minecraft).isOutOfGame();
        String string2 = "animation." + string + ".";
        AnimationController animationController = animationState.getController();
        if (!this.field_3641.isEmpty()) {
            animationController.forceAnimationReset();
            RawAnimation rawAnimation = RawAnimation.begin();
            for (String string3 : this.field_3641) {
                if (string3.contains("fire")) continue;
                rawAnimation.then(string2 + string3, Animation.LoopType.PLAY_ONCE);
            }
            this.field_3641.clear();
            rawAnimation.thenPlayAndHold(string2 + "idle");
            animationState.resetCurrentAnimation();
            animationState.setAnimation(rawAnimation);
        } else if (animationController.getAnimationState() == AnimationController.State.STOPPED || bl) {
            animationState.resetCurrentAnimation();
            animationState.setAnimation(RawAnimation.begin().thenPlayAndHold(string2 + "idle"));
        }
        return PlayState.CONTINUE;
    }
}

