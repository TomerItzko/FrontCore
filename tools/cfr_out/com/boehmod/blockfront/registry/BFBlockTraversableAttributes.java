/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.registry;

import com.boehmod.blockfront.registry.custom.BlockTraversableAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class BFBlockTraversableAttributes {
    @NotNull
    public static final DeferredRegister<BlockTraversableAttribute> DR = DeferredRegister.create(BlockTraversableAttribute.REGISTRY, (String)"bf");
    public static final DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> AIR = DR.register("air", () -> new BlockTraversableAttribute(0.0f));
    public static final DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> SOLID = DR.register("solid", () -> new BlockTraversableAttribute(-1.0f));
    public static final DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> SOFT_METAL = DR.register("soft_metal", () -> new BlockTraversableAttribute(3.0f));
    public static final DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> SOFT_STONE = DR.register("soft_stone", () -> new BlockTraversableAttribute(3.0f));
    public static final DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> DENSE_PLANT = DR.register("dense_plant", () -> new BlockTraversableAttribute(0.4f));
    public static final DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> SOFT_WOOD = DR.register("soft_wood", () -> new BlockTraversableAttribute(5.0f));
    public static final DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> SNOW = DR.register("snow", () -> new BlockTraversableAttribute(3.0f));
    public static final DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> WOOL = DR.register("wool", () -> new BlockTraversableAttribute(0.4f));
}

