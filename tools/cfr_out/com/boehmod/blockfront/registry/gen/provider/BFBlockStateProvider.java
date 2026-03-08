/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.data.PackOutput
 *  net.neoforged.neoforge.client.model.generators.BlockStateProvider
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.registry.gen.provider;

import com.boehmod.blockfront.registry.gen.CrateGenerator;
import com.boehmod.blockfront.registry.gen.MultiformGenerator;
import com.boehmod.blockfront.registry.gen.PlanksGenerator;
import com.boehmod.blockfront.registry.gen.ShuttersGenerator;
import com.boehmod.blockfront.registry.gen.WoodTypeGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class BFBlockStateProvider
extends BlockStateProvider {
    public BFBlockStateProvider(@NotNull PackOutput packOutput, @NotNull ExistingFileHelper existingFileHelper) {
        super(packOutput, "bf", existingFileHelper);
    }

    protected void registerStatesAndModels() {
        for (Object object : MultiformGenerator.INSTANCES) {
            ((MultiformGenerator)object).provideBlock(this);
        }
        for (Object object : CrateGenerator.INSTANCES) {
            ((CrateGenerator)object).provideBlock(this);
        }
        for (Object object : PlanksGenerator.INSTANCES) {
            ((PlanksGenerator)object).provideBlock(this);
        }
        for (Object object : ShuttersGenerator.INSTANCES) {
            ((ShuttersGenerator)object).provideBlock(this);
        }
        for (Object object : WoodTypeGenerator.INSTANCES) {
            ((WoodTypeGenerator)object).provideBlock(this);
        }
    }
}

