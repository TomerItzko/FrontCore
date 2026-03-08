/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.pattern.SkinPattern
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemGun
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  javax.annotation.Nonnull
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.pattern.SkinPattern;
import com.boehmod.bflib.cloud.common.item.types.CloudItemGun;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.boehmod.blockfront.util.BFRes;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class ItemSkinIndex {
    @NotNull
    private static final Int2ObjectMap<ItemStack> ID_TO_ITEM_STACK = new Int2ObjectOpenHashMap();
    @NotNull
    private static final Int2ObjectMap<Item> ID_TO_ITEM = new Int2ObjectOpenHashMap();

    @NotNull
    public static ItemStack method_1722(@NotNull CloudItem<?> cloudItem, @NotNull CloudItemStack cloudItemStack) {
        ItemStack itemStack = (ItemStack)ID_TO_ITEM_STACK.computeIfAbsent(cloudItem.getId(), itemId -> {
            Item item = ItemSkinIndex.getItem(cloudItem);
            ItemStack itemStack = new ItemStack((ItemLike)item);
            ItemSkinIndex.method_5941(cloudItem, itemStack);
            return itemStack;
        });
        ItemSkinIndex.method_5942(cloudItemStack, itemStack);
        return itemStack;
    }

    public static void method_5941(@NotNull CloudItem<?> cloudItem, @Nonnull ItemStack itemStack) {
        float f = cloudItem.getSkin();
        itemStack.set(BFDataComponents.HAS_PATTERN, (Object)false);
        if (f != 0.0f) {
            CloudItemGun cloudItemGun;
            SkinPattern skinPattern;
            itemStack.set(BFDataComponents.SKIN_ID, (Object)Float.valueOf(f));
            if (cloudItem instanceof CloudItemGun && (skinPattern = (cloudItemGun = (CloudItemGun)cloudItem).getPatternSkin()) != null) {
                itemStack.set(BFDataComponents.HAS_PATTERN, (Object)true);
                itemStack.set(BFDataComponents.PATTERN_WIDTH, (Object)skinPattern.width());
                itemStack.set(BFDataComponents.PATTERN_HEIGHT, (Object)skinPattern.height());
                itemStack.set(BFDataComponents.PATTERN_NAME, (Object)skinPattern.name());
            }
        }
    }

    public static void method_5942(@NotNull CloudItemStack cloudItemStack, @Nonnull ItemStack itemStack) {
        itemStack.set(BFDataComponents.MINT, (Object)cloudItemStack.getMint());
        for (int i = 0; i < 16; ++i) {
            BFWeaponItem.method_3764(itemStack, i, cloudItemStack.getSticker(i));
        }
        Optional optional = cloudItemStack.getNameTag();
        optional.ifPresent(string -> BFWeaponItem.method_3774(itemStack, string));
    }

    @NotNull
    public static Item getItem(@NotNull CloudItem<?> cloudItem) {
        return (Item)ID_TO_ITEM.computeIfAbsent(cloudItem.getId(), itemId -> (Item)BuiltInRegistries.ITEM.get(BFRes.fromCloud(cloudItem.getMinecraftItem())));
    }
}

