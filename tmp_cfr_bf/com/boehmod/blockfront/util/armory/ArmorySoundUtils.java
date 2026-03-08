/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util.armory;

import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemArmour;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCallingCard;
import com.boehmod.bflib.cloud.common.item.types.CloudItemGun;
import com.boehmod.bflib.cloud.common.item.types.CloudItemMelee;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class ArmorySoundUtils {
    public static void playDefault(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull CloudItemStack itemStack) {
        CloudItem cloudItem = itemStack.getCloudItem(manager.getCloudRegistry());
        SoundManager soundManager = minecraft.getSoundManager();
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = BFSounds.GUI_ARMORY_DEFAULT_SHARED;
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder2 = null;
        if (cloudItem instanceof CloudItemGun) {
            deferredHolder2 = BFSounds.GUI_ARMORY_DEFAULT_GUN;
        } else if (cloudItem instanceof CloudItemMelee) {
            deferredHolder2 = BFSounds.GUI_ARMORY_DEFAULT_MELEE;
        } else if (cloudItem instanceof CloudItemCallingCard) {
            deferredHolder2 = BFSounds.GUI_ARMORY_DEFAULT_CARD;
        } else if (cloudItem instanceof CloudItemArmour) {
            deferredHolder2 = BFSounds.GUI_ARMORY_DEFAULT_ARMOR;
        }
        soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)1.0f, (float)1.0f));
        if (deferredHolder2 != null) {
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder2.get()), (float)1.0f, (float)1.0f));
        }
    }
}

