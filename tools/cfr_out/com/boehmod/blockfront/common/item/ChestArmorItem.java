/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.core.Holder
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.ArmorMaterial
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animatable.GeoItem
 *  software.bernie.geckolib.animatable.client.GeoRenderProvider
 *  software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
 *  software.bernie.geckolib.animation.AnimatableManager$ControllerRegistrar
 *  software.bernie.geckolib.model.DefaultedItemGeoModel
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.renderer.GeoArmorRenderer
 *  software.bernie.geckolib.util.GeckoLibUtil
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.util.BFRes;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public final class ChestArmorItem
extends ArmorItem
implements GeoItem {
    @NotNull
    private final String field_4079;
    private final boolean field_4081;
    @NotNull
    private final AnimatableInstanceCache field_4080 = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> field_4078;
    private boolean field_4082 = false;

    public ChestArmorItem(@NotNull String string, @NotNull Holder<ArmorMaterial> holder, @NotNull ArmorItem.Type type, @NotNull Item.Properties properties, boolean bl) {
        super(holder, type, properties);
        this.field_4079 = string;
        this.field_4081 = bl;
    }

    @NotNull
    public ChestArmorItem method_4237(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder, boolean bl) {
        this.field_4078 = deferredHolder;
        this.field_4082 = bl;
        return this;
    }

    public boolean method_4238() {
        return this.field_4082;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> method_4240() {
        return this.field_4078;
    }

    @NotNull
    public String getName() {
        return this.field_4079;
    }

    public boolean method_4239() {
        return this.field_4081;
    }

    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider(this){
            private GeoArmorRenderer<?> field_4083;

            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T t, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> humanoidModel) {
                if (this.field_4083 == null) {
                    ChestArmorItem chestArmorItem = (ChestArmorItem)itemStack.getItem();
                    this.field_4083 = new GeoArmorRenderer((GeoModel)new DefaultedItemGeoModel(BFRes.loc("armor/" + chestArmorItem.getName())));
                }
                return this.field_4083;
            }
        });
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.field_4080;
    }
}

