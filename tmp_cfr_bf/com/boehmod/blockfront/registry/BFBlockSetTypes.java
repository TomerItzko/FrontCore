/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry;

import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;

public class BFBlockSetTypes {
    @NotNull
    public static final BlockSetType BUNKER_DOOR = new BlockSetType("bunker_door", true, false, false, BlockSetType.PressurePlateSensitivity.EVERYTHING, SoundType.METAL, (SoundEvent)BFSounds.BLOCK_DOOR_BUNKER_CLOSE.get(), (SoundEvent)BFSounds.BLOCK_DOOR_BUNKER_OPEN.get(), SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
    @NotNull
    public static final BlockSetType BLAST_DOOR = new BlockSetType("blast_door", false, false, false, BlockSetType.PressurePlateSensitivity.EVERYTHING, SoundType.METAL, (SoundEvent)BFSounds.BLOCK_DOOR_BUNKER_CLOSE.get(), (SoundEvent)BFSounds.BLOCK_DOOR_BUNKER_OPEN.get(), SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
}

