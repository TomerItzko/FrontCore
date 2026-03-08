/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.ActivatedCloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemCape
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemCase
 *  net.minecraft.client.gui.screens.Screen
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util.armory;

import com.boehmod.bflib.cloud.common.item.ActivatedCloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCape;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCase;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.screen.dropdown.armory.ActivatedItemDropdownScreen;
import com.boehmod.blockfront.client.screen.dropdown.armory.BoosterItemDropdownScreen;
import com.boehmod.blockfront.client.screen.dropdown.armory.CapeItemDropdownScreen;
import com.boehmod.blockfront.client.screen.dropdown.armory.CaseItemDropdownScreen;
import com.boehmod.blockfront.client.screen.dropdown.armory.ItemDropdownScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

public class ArmoryDropdownUtils {
    @NotNull
    public static Screen getScreenFromItem(@NotNull Screen parent, @NotNull BFClientManager manager, @NotNull CloudItemStack itemStack, int x, int y) {
        CloudItem cloudItem = itemStack.getCloudItem(manager.getCloudRegistry());
        if (cloudItem instanceof CloudItemCape) {
            return new CapeItemDropdownScreen(parent, x + 2, y + 2, 80, 12, itemStack);
        }
        if (cloudItem instanceof CloudItemCase) {
            return new CaseItemDropdownScreen(parent, x + 2, y + 2, 80, 12, itemStack);
        }
        if (cloudItem instanceof AbstractCloudItemCoin) {
            return new BoosterItemDropdownScreen(parent, x + 2, y + 2, 80, 12, itemStack);
        }
        if (cloudItem instanceof ActivatedCloudItem) {
            return new ActivatedItemDropdownScreen(parent, x + 2, y + 2, 80, 12, itemStack);
        }
        return new ItemDropdownScreen(parent, itemStack, x + 2, y + 2, 80, 12);
    }
}

