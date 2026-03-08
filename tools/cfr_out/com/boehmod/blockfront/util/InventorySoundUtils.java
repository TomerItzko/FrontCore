/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemTrophy
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.sounds.SoundEvent
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemTrophy;
import com.boehmod.blockfront.registry.BFSounds;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class InventorySoundUtils {
    public static void playInspectFull(@NotNull Minecraft minecraft, @NotNull CloudRegistry cloudRegistry, @NotNull CloudItemStack itemStack) {
        CloudItem cloudItem = itemStack.getCloudItem(cloudRegistry);
        SoundManager soundManager = minecraft.getSoundManager();
        InventorySoundUtils.playInspect(soundManager);
        InventorySoundUtils.playInventory(soundManager, cloudItem);
    }

    private static void playInventory(@NotNull SoundManager soundManager, @Nullable CloudItem<? extends CloudItem<?>> item) {
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = null;
        if (item instanceof CloudItemTrophy) {
            deferredHolder = BFSounds.GUI_INVENTORY_ITEM_TROPHY;
        }
        if (deferredHolder != null) {
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)1.0f, (float)1.0f));
        }
    }

    private static void playInspect(@NotNull SoundManager soundManager) {
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = BFSounds.GUI_INVENTORY_ITEM_INSPECT;
        soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)1.0f, (float)1.0f));
    }
}

