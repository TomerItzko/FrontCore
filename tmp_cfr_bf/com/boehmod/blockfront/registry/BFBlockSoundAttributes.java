/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry;

import com.boehmod.blockfront.client.gui.layer.MatchGuiLayer;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.registry.custom.BlockSoundAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class BFBlockSoundAttributes {
    @NotNull
    public static final DeferredRegister<BlockSoundAttribute> DR = DeferredRegister.create(BlockSoundAttribute.REGISTRY, (String)"bf");
    public static final DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> GLASS = DR.register("glass", () -> new BlockSoundAttribute(BFSounds.ITEM_GUN_BULLET_IMPACT_GLASS, BFSounds.ITEM_GUN_BULLET_IMPACT_GLASS_DEBRIS));
    public static final DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> HARD_METAL = DR.register("hard_metal", () -> new BlockSoundAttribute(BFSounds.ITEM_GUN_BULLET_IMPACT_METAL, BFSounds.ITEM_GUN_BULLET_IMPACT_METAL_DEBRIS).method_4255().method_4251().method_4249().method_4250(12, BFParticleTypes.BULLET_SPARK_PARTICLE));
    public static final DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> METAL_TRANSLUCENT = DR.register("metal_translucent", () -> new BlockSoundAttribute(BFSounds.ITEM_GUN_BULLET_IMPACT_METAL, BFSounds.ITEM_GUN_BULLET_IMPACT_METAL_DEBRIS).method_4251().method_4249().method_4250(12, BFParticleTypes.BULLET_SPARK_PARTICLE));
    public static final DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> STONE = DR.register("stone", () -> new BlockSoundAttribute(BFSounds.ITEM_GUN_BULLET_IMPACT_STONE, BFSounds.ITEM_GUN_BULLET_IMPACT_STONE_DEBRIS).method_4255().method_4251().method_4249().method_5935(1, BFParticleTypes.BULLET_IMPACT_SMOKE));
    public static final DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> SNOW = DR.register("snow", () -> new BlockSoundAttribute(BFSounds.ITEM_GUN_BULLET_IMPACT_DIRT, BFSounds.ITEM_GUN_BULLET_IMPACT_DIRT_DEBRIS, MatchGuiLayer.BF_114.BF_115.SNOW).method_4251().method_5935(1, BFParticleTypes.BULLET_IMPACT_SMOKE_SNOW).method_5935(15, BFParticleTypes.BULLET_IMPACT_SNOW));
    public static final DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> WOOL = DR.register("wool", () -> new BlockSoundAttribute(BFSounds.ITEM_GUN_BULLET_IMPACT_DIRT, BFSounds.ITEM_GUN_BULLET_IMPACT_DIRT_DEBRIS).method_4255().method_4251().method_5935(1, BFParticleTypes.BULLET_IMPACT_SMOKE));
    public static final DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> DIRT = DR.register("dirt", () -> new BlockSoundAttribute(BFSounds.ITEM_GUN_BULLET_IMPACT_DIRT, BFSounds.ITEM_GUN_BULLET_IMPACT_DIRT_DEBRIS, MatchGuiLayer.BF_114.BF_115.DIRT).method_4255().method_4251().method_5935(1, BFParticleTypes.BULLET_IMPACT_SMOKE));
    public static final DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> SAND = DR.register("sand", () -> new BlockSoundAttribute(BFSounds.ITEM_GUN_BULLET_IMPACT_DIRT, BFSounds.ITEM_GUN_BULLET_IMPACT_DIRT_DEBRIS).method_4251().method_5935(1, BFParticleTypes.BULLET_IMPACT_SMOKE));
    public static final DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> WOOD = DR.register("wood", () -> new BlockSoundAttribute(BFSounds.ITEM_GUN_BULLET_IMPACT_WOOD, BFSounds.ITEM_GUN_BULLET_IMPACT_WOOD_DEBRIS).method_4255().method_4251().method_4249().method_5935(1, BFParticleTypes.BULLET_IMPACT_SMOKE));
    public static final DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> FLESH = DR.register("flesh", () -> new BlockSoundAttribute(BFSounds.ITEM_GUN_BULLET_IMPACT_FLESH, BFSounds.ITEM_GUN_BULLET_IMPACT_FLESH_HEAD, MatchGuiLayer.BF_114.BF_115.BLOOD).method_4255().method_4251().method_4249().method_4250(6, BFParticleTypes.BLOOD_SPLAT_PARTICLE).method_5935(1, BFParticleTypes.BLOOD_IMPACT_SMOKE));
}

