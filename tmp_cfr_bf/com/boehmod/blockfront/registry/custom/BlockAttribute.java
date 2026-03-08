/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry.custom;

import com.boehmod.blockfront.registry.custom.BlockSoundAttribute;
import com.boehmod.blockfront.registry.custom.BlockTraversableAttribute;
import com.boehmod.blockfront.util.BFRes;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.NotNull;

public class BlockAttribute {
    @NotNull
    private static final ResourceKey<Registry<BlockAttribute>> KEY = ResourceKey.createRegistryKey((ResourceLocation)BFRes.loc("block_attribute"));
    @NotNull
    public static final Registry<BlockAttribute> REGISTRY = new RegistryBuilder(KEY).sync(true).create();
    @NotNull
    public static final Map<Block, BlockAttribute> field_4088 = new HashMap<Block, BlockAttribute>();
    @NotNull
    public static final Map<SoundType, BlockAttribute> field_4089 = new HashMap<SoundType, BlockAttribute>();
    @Nullable
    private DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> field_4084 = null;
    @Nullable
    private DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> field_4085 = null;

    @NotNull
    public BlockAttribute method_4243(@NotNull DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> deferredHolder) {
        this.field_4084 = deferredHolder;
        return this;
    }

    @Nullable
    public DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> method_4245() {
        return this.field_4084;
    }

    @NotNull
    public BlockAttribute method_4244(@NotNull DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> deferredHolder) {
        this.field_4085 = deferredHolder;
        return this;
    }

    @Nullable
    public DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> method_4246() {
        return this.field_4085;
    }

    @NotNull
    public BlockAttribute method_4241(@NotNull Block block) {
        field_4088.put(block, this);
        return this;
    }

    @NotNull
    public BlockAttribute method_4242(@NotNull SoundType soundType) {
        field_4089.put(soundType, this);
        return this;
    }
}

