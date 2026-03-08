/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.IntegerProperty
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block.base;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class BFBlockProperties {
    public static final int MAX_TEXTURE_INDEX = 8;
    @NotNull
    public static final IntegerProperty BODY_ROTATION = IntegerProperty.create((String)"body_rotation", (int)0, (int)7);
    @NotNull
    public static final IntegerProperty HEAD_ROTATION = IntegerProperty.create((String)"head_rotation", (int)0, (int)7);
    @NotNull
    public static final IntegerProperty TEXTURE_INDEX = IntegerProperty.create((String)"texture_index", (int)0, (int)8);
    @NotNull
    public static final BooleanProperty SPARKING = BooleanProperty.create((String)"sparking");
}

