/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.bus.api.Event
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event.impl;

import com.boehmod.blockfront.common.item.GunItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.NotNull;

public class GunEvent {

    public static class BF_671
    extends Base {
        private int field_2804;

        public BF_671(@NotNull GunItem gunItem, @NotNull ItemStack itemStack, int n) {
            super(gunItem, itemStack);
            this.field_2804 = n;
        }

        public int method_2573() {
            return this.field_2804;
        }

        public void method_2574(int n) {
            this.field_2804 = n;
        }
    }

    public static class BF_670
    extends Base {
        public BF_670(GunItem gunItem, ItemStack itemStack) {
            super(gunItem, itemStack);
        }
    }

    public static class EntityTrigger
    extends Base {
        @NotNull
        private final LivingEntity entity;

        public EntityTrigger(@NotNull GunItem item, @NotNull ItemStack itemStack, @NotNull LivingEntity entity) {
            super(item, itemStack);
            this.entity = entity;
        }

        @NotNull
        public LivingEntity getEntity() {
            return this.entity;
        }
    }

    private static class Base
    extends Event {
        @NotNull
        private final GunItem item;
        @NotNull
        private final ItemStack itemStack;

        public Base(@NotNull GunItem item, @NotNull ItemStack itemStack) {
            this.item = item;
            this.itemStack = itemStack;
        }

        @NotNull
        public GunItem getItem() {
            return this.item;
        }

        @NotNull
        public ItemStack getItemStack() {
            return this.itemStack;
        }
    }
}

