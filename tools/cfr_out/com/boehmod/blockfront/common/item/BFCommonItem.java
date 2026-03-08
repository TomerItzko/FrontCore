/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.component.Tool
 *  net.minecraft.world.item.component.Tool$Rule
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.registry.BFItems;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Tool;
import org.jetbrains.annotations.NotNull;

public class BFCommonItem
extends Item {
    public BFCommonItem(@NotNull String id, @NotNull Item.Properties properties) {
        super(properties);
        BFItems.ID_MAP.put((Object)id, (Object)this);
    }

    @NotNull
    private static Item.Properties method_3752(int n, boolean bl) {
        Item.Properties properties = new Item.Properties().stacksTo(n);
        if (!bl) {
            properties = properties.component(DataComponents.TOOL, (Object)BFCommonItem.getTool());
        }
        return properties;
    }

    @NotNull
    public static Tool getTool() {
        return new Tool(List.of(Tool.Rule.minesAndDrops(List.of(), (float)15.0f)), 1.0f, 2);
    }
}

