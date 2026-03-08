/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block.base;

import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public interface IBlockStepSoundable {
    @NotNull
    public SoundEvent getStepSound();
}

