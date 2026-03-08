/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.phys.Vec2
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game;

import javax.annotation.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

public class GameShopItem {
    private final Item item;
    private final int price;
    private final Vec2 buttonPos;
    private int count = 1;

    public GameShopItem(@NotNull Item item, int price) {
        this(item, price, null);
    }

    public GameShopItem(@NotNull Item item, int price, @Nullable Vec2 buttonPos) {
        this.item = item;
        this.price = price;
        this.buttonPos = buttonPos;
    }

    public GameShopItem setCount(int count) {
        this.count = count;
        return this;
    }

    public ItemStack getItemStack() {
        return new ItemStack((ItemLike)this.item, this.count);
    }

    public int getPrice() {
        return this.price;
    }

    @Nullable
    public Vec2 getButtonPos() {
        return this.buttonPos;
    }
}

