/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.common.item.BFCommonItem;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

public class CloudNetworkItem
extends BFCommonItem {
    @NotNull
    public static final Component DISCLAIMER = Component.translatable((String)"item.bf.cloud.disclaimer").withStyle(ChatFormatting.GRAY);

    public CloudNetworkItem(@NotNull String string, @NotNull Item.Properties properties) {
        super(string, properties);
    }

    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull Item.TooltipContext tooltipContext, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, components, tooltipFlag);
        components.add(DISCLAIMER);
    }
}

