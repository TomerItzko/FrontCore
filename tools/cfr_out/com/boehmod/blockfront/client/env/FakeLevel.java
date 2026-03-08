/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.multiplayer.ClientLevel$ClientLevelData
 *  net.minecraft.client.multiplayer.ClientPacketListener
 *  net.minecraft.core.Holder
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.util.profiling.InactiveProfiler
 *  net.minecraft.util.valueproviders.IntProvider
 *  net.minecraft.util.valueproviders.UniformInt
 *  net.minecraft.world.Difficulty
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.dimension.BuiltinDimensionTypes
 *  net.minecraft.world.level.dimension.DimensionType
 *  net.minecraft.world.level.dimension.DimensionType$MonsterSettings
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.env;

import com.boehmod.blockfront.client.env.FakeDimensionHolder;
import com.boehmod.blockfront.client.env.FakePacketListener;
import java.util.OptionalLong;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;

public class FakeLevel
extends ClientLevel {
    public static final DimensionType DIMENSION_TYPE = new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true, false, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, 0.0f, new DimensionType.MonsterSettings(false, true, (IntProvider)UniformInt.of((int)0, (int)7), 0));

    public FakeLevel(@NotNull Minecraft minecraft) {
        super((ClientPacketListener)new FakePacketListener(minecraft), FakeLevel.getLevelData(), Level.OVERWORLD, (Holder)new FakeDimensionHolder(), 16, 16, () -> InactiveProfiler.INSTANCE, minecraft.levelRenderer, false, 1L);
    }

    @NotNull
    public static ClientLevel.ClientLevelData getLevelData() {
        return new ClientLevel.ClientLevelData(Difficulty.PEACEFUL, false, true);
    }
}

