/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemRarity
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemArmour
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemBooster
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemCase
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemGun
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemMelee
 *  com.boehmod.bflib.common.ColorReferences
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util.armory;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemRarity;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemArmour;
import com.boehmod.bflib.cloud.common.item.types.CloudItemBooster;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCase;
import com.boehmod.bflib.cloud.common.item.types.CloudItemGun;
import com.boehmod.bflib.cloud.common.item.types.CloudItemMelee;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.FormatUtils;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.lang.runtime.SwitchBootstraps;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

public class ArmoryTooltipUtils {
    private static final Component MESSAGE_TRY_LUCK = Component.translatable((String)"bf.message.try.luck");
    private static final Component MESSAGE_REQUIRED_KEY = Component.translatable((String)"bf.message.required.key").withStyle(ChatFormatting.BOLD);
    private static final Component MESSAGE_NATION = Component.translatable((String)"bf.message.nation").withStyle(ChatFormatting.BOLD);
    private static final Component MESSAGE_AUTHOR = Component.translatable((String)"bf.message.author").withStyle(ChatFormatting.BOLD);
    private static final Component MESSAGE_RARITY = Component.translatable((String)"bf.message.rarity").withStyle(ChatFormatting.BOLD);
    private static final Component MESSAGE_COLLECTION = Component.translatable((String)"bf.message.collection").withStyle(ChatFormatting.BOLD);
    private static final Component MESSAGE_EQUIPPED = Component.translatable((String)"bf.message.equipped").withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
    private static final Component MESSAGE_SHOWCASED = Component.translatable((String)"bf.message.showcased").withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
    private static final Component MESSAGE_DEFAULT_ITEM = Component.translatable((String)"bf.message.default.item").withStyle(ChatFormatting.GRAY);
    private static final Component MESSAGE_MINT = Component.translatable((String)"bf.message.mint").withStyle(ChatFormatting.BOLD);

    public static void append(@NotNull CloudRegistry registry, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull CloudItemStack itemStack, @NotNull ObjectList<Component> target, Font font, int fontSize) {
        CloudItem cloudItem = itemStack.getCloudItem(registry);
        if (cloudItem == null) {
            return;
        }
        target.add((Object)Component.literal((String)cloudItem.getName()));
        ArmoryTooltipUtils.appendSuffix(cloudItem, target);
        ArmoryTooltipUtils.appendAuthor(cloudItem, target);
        ArmoryTooltipUtils.appendRarity(cloudItem, target);
        ArmoryTooltipUtils.appendCollection(cloudItem, target);
        CloudItem cloudItem2 = cloudItem;
        Objects.requireNonNull(cloudItem2);
        CloudItem cloudItem3 = cloudItem2;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{CloudItemArmour.class, CloudItemBooster.class, CloudItemCase.class, CloudItemMelee.class, CloudItemGun.class}, (Object)cloudItem3, n)) {
            case 0: {
                CloudItemArmour cloudItemArmour = (CloudItemArmour)cloudItem3;
                ArmoryTooltipUtils.appendArmour(cloudItemArmour, target);
                break;
            }
            case 1: {
                CloudItemBooster cloudItemBooster = (CloudItemBooster)cloudItem3;
                ArmoryTooltipUtils.appendBooster(cloudItemBooster, target, font, fontSize);
                break;
            }
            case 2: {
                CloudItemCase cloudItemCase = (CloudItemCase)cloudItem3;
                ArmoryTooltipUtils.appendCase(cloudItemCase, target);
                break;
            }
            case 3: {
                CloudItemMelee cloudItemMelee = (CloudItemMelee)cloudItem3;
                if (cloudItemMelee.isDefault()) break;
                ArmoryTooltipUtils.appendMint(itemStack, target);
                break;
            }
            case 4: {
                CloudItemGun cloudItemGun = (CloudItemGun)cloudItem3;
                if (cloudItemGun.isDefault()) break;
                ArmoryTooltipUtils.appendMint(itemStack, target);
                break;
            }
        }
        ArmoryTooltipUtils.appendStatus(registry, minecraft, dataHandler, cloudItem, itemStack, target);
    }

    private static void appendMint(@NotNull CloudItemStack itemStack, @NotNull ObjectList<Component> target) {
        double d = itemStack.getMint();
        if (d > 0.0) {
            int n = BFRendering.lerpColor(16769148, 0xB5B5B5, d);
            target.add((Object)Component.empty());
            target.add((Object)MESSAGE_MINT);
            target.add((Object)Component.literal((String)String.format(Locale.ROOT, "%.6f", d)).withColor(n));
        }
    }

    private static void appendCase(@NotNull CloudItemCase item, @NotNull List<Component> target) {
        target.add((Component)Component.empty());
        target.add(MESSAGE_TRY_LUCK);
        target.add((Component)Component.empty());
        target.add(MESSAGE_REQUIRED_KEY);
        CloudItem cloudItem = item.getKey();
        String string = cloudItem.getDisplayName();
        Style style = Style.EMPTY.withColor(cloudItem.getRarity().getColor());
        target.add((Component)Component.literal((String)string).withStyle(style));
    }

    private static void appendBooster(@NotNull CloudItemBooster item, @NotNull List<Component> target, Font font, int fontSize) {
        target.add((Component)Component.empty());
        String string = I18n.get((String)"bf.screen.armory.booster.info", (Object[])new Object[]{String.valueOf(ChatFormatting.GREEN) + item.getSuffix() + String.valueOf(ChatFormatting.RESET)});
        ObjectList<String> objectList = FormatUtils.parseMarkup(font, string, fontSize);
        for (String string2 : objectList) {
            target.add((Component)Component.literal((String)string2));
        }
    }

    private static void appendArmour(@NotNull CloudItemArmour item, @NotNull List<Component> target) {
        target.add((Component)Component.empty());
        target.add(MESSAGE_NATION);
        target.add((Component)Component.literal((String)item.getNation().getName()).withStyle(ChatFormatting.DARK_GRAY));
    }

    private static void appendSuffix(@NotNull CloudItem<?> item, @NotNull List<Component> target) {
        String string = item.getSuffix();
        int n = item.getRarity().getColor();
        if (!string.isEmpty()) {
            target.add((Component)Component.literal((String)string).withColor(n));
        }
        target.add((Component)Component.empty());
    }

    private static void appendAuthor(@NotNull CloudItem<?> item, @NotNull List<Component> target) {
        Optional optional = item.getAuthor();
        if (optional.isPresent()) {
            target.add((Component)Component.empty());
            target.add(MESSAGE_AUTHOR);
            target.add((Component)Component.literal((String)((String)optional.get())).withStyle(ChatFormatting.GRAY));
        }
    }

    private static void appendRarity(@NotNull CloudItem<?> item, @NotNull List<Component> target) {
        CloudItemRarity cloudItemRarity = item.getRarity();
        target.add(MESSAGE_RARITY);
        target.add((Component)Component.literal((String)cloudItemRarity.getName()).withColor(cloudItemRarity.getColor()));
    }

    private static void appendCollection(@NotNull CloudItem<?> item, @NotNull List<Component> target) {
        String string = item.getCollection();
        if (string != null) {
            target.add((Component)Component.empty());
            target.add(MESSAGE_COLLECTION);
            target.add((Component)Component.literal((String)string).withStyle(ChatFormatting.GRAY));
        }
    }

    private static void appendStatus(@NotNull CloudRegistry registry, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull CloudItem<?> item, @NotNull CloudItemStack itemStack, @NotNull List<Component> target) {
        PlayerCloudData playerCloudData = dataHandler.getCloudData(minecraft);
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        if (playerCloudInventory.isItemEquipped(registry, itemStack)) {
            target.add((Component)Component.empty());
            target.add(MESSAGE_EQUIPPED);
        }
        if (playerCloudInventory.isItemShowcased(itemStack)) {
            target.add((Component)Component.empty());
            target.add(MESSAGE_SHOWCASED);
        }
        if (item.isDefault()) {
            target.add((Component)Component.empty());
            target.add(MESSAGE_DEFAULT_ITEM);
        }
    }
}

