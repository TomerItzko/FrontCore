/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity.goal;

import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.goal.BotGoal;
import com.boehmod.blockfront.common.gun.GunMagType;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.registry.custom.BotVoice;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BotReloadGoal
extends BotGoal {
    private int field_2427 = 0;

    public BotReloadGoal(@NotNull BotEntity botEntity) {
        super(botEntity);
    }

    public void start() {
        super.start();
        this.botEntity.method_1974(true);
        this.botEntity.setAggressive(false);
        this.botEntity.method_2011().ifPresent(deferredHolder -> this.botEntity.method_1993(((BotVoice)deferredHolder.get()).reloadingSound()));
        ItemStack itemStack = this.botEntity.getMainHandItem();
        Item item = itemStack.getItem();
        if (item instanceof GunItem) {
            GunItem gunItem = (GunItem)item;
            this.field_2427 = gunItem.getReloadTime(itemStack);
            item = gunItem.getReloadSound(itemStack);
            if (item != null) {
                this.botEntity.playSound((SoundEvent)item, 1.0f, 1.0f);
            }
        }
    }

    public void stop() {
        super.stop();
        this.method_2052();
        this.botEntity.method_1974(false);
        this.botEntity.setAggressive(true);
    }

    @Override
    public boolean canUse() {
        ItemStack itemStack = this.botEntity.getMainHandItem();
        if (itemStack.getItem() instanceof GunItem) {
            int n = GunItem.getAmmoLoaded(itemStack);
            return n <= 0;
        }
        return false;
    }

    public boolean canContinueToUse() {
        return this.field_2427 > 0;
    }

    public void tick() {
        super.tick();
        --this.field_2427;
    }

    private void method_2052() {
        Object object;
        ItemStack itemStack = this.botEntity.getMainHandItem();
        if (!itemStack.isEmpty() && (object = itemStack.getItem()) instanceof GunItem) {
            GunItem gunItem = (GunItem)object;
            object = gunItem.getMagTypeOrDefault(itemStack);
            int n = ((GunMagType)object).capacity();
            GunItem.setAmmoLoaded(itemStack, n);
        }
    }
}

