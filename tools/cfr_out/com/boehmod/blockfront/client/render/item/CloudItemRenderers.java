/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemArmour
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemCallingCard
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemCape
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemCoin
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemGun
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemMelee
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemPrestigeCard
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemSticker
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemTrophy
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.item;

import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.types.CloudItemArmour;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCallingCard;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCape;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCoin;
import com.boehmod.bflib.cloud.common.item.types.CloudItemGun;
import com.boehmod.bflib.cloud.common.item.types.CloudItemMelee;
import com.boehmod.bflib.cloud.common.item.types.CloudItemPrestigeCard;
import com.boehmod.bflib.cloud.common.item.types.CloudItemSticker;
import com.boehmod.bflib.cloud.common.item.types.CloudItemTrophy;
import com.boehmod.blockfront.client.render.item.CloudItemArmorRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemCallingCardRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemCapeRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemCoinRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemGunRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemMeleeRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemPrestigeCardRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemStickerRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemTrophyRenderer;
import com.boehmod.blockfront.client.render.item.DefaultCloudItemRenderer;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class CloudItemRenderers {
    @NotNull
    public static final CloudItemRenderer<?> DEFAULT = new DefaultCloudItemRenderer();
    @NotNull
    private static final Map<Class<? extends CloudItem<?>>, CloudItemRenderer<?>> RENDERERS = new HashMap();

    public static void registerRenderer(@NotNull Class<? extends CloudItem<?>> cloudItemClass, @NotNull CloudItemRenderer<? extends CloudItem<?>> renderer) {
        RENDERERS.put(cloudItemClass, renderer);
    }

    @NotNull
    public static CloudItemRenderer<?> getRenderer(@NotNull CloudItem<?> cloudItem) {
        return RENDERERS.getOrDefault(cloudItem.getClass(), DEFAULT);
    }

    static {
        CloudItemRenderers.registerRenderer(CloudItemArmour.class, new CloudItemArmorRenderer());
        CloudItemRenderers.registerRenderer(CloudItemCallingCard.class, new CloudItemCallingCardRenderer());
        CloudItemRenderers.registerRenderer(CloudItemCape.class, new CloudItemCapeRenderer());
        CloudItemRenderers.registerRenderer(CloudItemTrophy.class, new CloudItemTrophyRenderer());
        CloudItemRenderers.registerRenderer(CloudItemCoin.class, new CloudItemCoinRenderer());
        CloudItemRenderers.registerRenderer(CloudItemGun.class, new CloudItemGunRenderer());
        CloudItemRenderers.registerRenderer(CloudItemMelee.class, new CloudItemMeleeRenderer());
        CloudItemRenderers.registerRenderer(CloudItemSticker.class, new CloudItemStickerRenderer());
        CloudItemRenderers.registerRenderer(CloudItemPrestigeCard.class, new CloudItemPrestigeCardRenderer());
    }
}

