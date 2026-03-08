/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance$Attenuation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.sound;

import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GunHeatSound
extends AbstractTickableSoundInstance {
    public GunHeatSound(@NotNull SoundEvent event, @NotNull SoundSource source) {
        super(event, source, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.volume = 0.0f;
    }

    public boolean canPlaySound() {
        return Minecraft.getInstance().player != null;
    }

    public boolean canStartSilent() {
        return true;
    }

    public void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            return;
        }
        this.x = localPlayer.getX();
        this.y = localPlayer.getY();
        this.z = localPlayer.getZ();
        float f = 0.0f;
        ItemStack itemStack = localPlayer.getMainHandItem();
        if (itemStack.getItem() instanceof GunItem) {
            f = GunItem.getHeat(itemStack);
        }
        this.volume = MathUtils.moveTowards(this.volume, f, 0.1f);
    }
}

