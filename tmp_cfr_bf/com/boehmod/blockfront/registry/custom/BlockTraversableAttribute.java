/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry.custom;

import com.boehmod.blockfront.util.BFRes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.NotNull;

public record BlockTraversableAttribute(float reduction) {
    @NotNull
    private static final ResourceKey<Registry<BlockTraversableAttribute>> KEY = ResourceKey.createRegistryKey((ResourceLocation)BFRes.loc("block_traversable_attribute"));
    @NotNull
    public static final Registry<BlockTraversableAttribute> REGISTRY = new RegistryBuilder(KEY).sync(true).create();
}

