/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util;

import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegistryUtils {
    @NotNull
    private static final Logger LOGGER = LogManager.getLogger((String)"RegistryUtils");

    @Nullable
    public static Supplier<BlockEntityType<?>> retrieveBlockEntity(@NotNull String id) {
        BlockEntityType blockEntityType = (BlockEntityType)BuiltInRegistries.BLOCK_ENTITY_TYPE.get(ResourceLocation.tryParse((String)id));
        if (blockEntityType == null) {
            LOGGER.warn("Failed to find block entity type for given key: {}", (Object)id);
            return null;
        }
        return () -> blockEntityType;
    }

    @Nullable
    public static String getBlockEntityTypeId(@NotNull Supplier<? extends BlockEntityType<?>> supplier) {
        ResourceLocation resourceLocation = BlockEntityType.getKey(supplier.get());
        if (resourceLocation == null) {
            LOGGER.warn("Failed to find key for given block entity type: {}", supplier.get());
        }
        return resourceLocation != null ? resourceLocation.toString() : null;
    }

    @NotNull
    public static Supplier<EntityType<?>> retrieveEntityType(@NotNull String id) {
        return () -> (EntityType)BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse((String)id));
    }

    @NotNull
    public static String getEntityTypeId(@NotNull Supplier<? extends EntityType<?>> supplier) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(supplier.get()).toString();
    }

    @NotNull
    public static Supplier<Block> retrieveBlock(@NotNull String id) {
        return () -> (Block)BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse((String)id));
    }

    @NotNull
    public static String getBlockId(@NotNull Block block) {
        return BuiltInRegistries.BLOCK.getKey((Object)block).toString();
    }

    @Nullable
    public static Supplier<SoundEvent> retrieveSoundEvent(@NotNull String id) {
        SoundEvent soundEvent = (SoundEvent)BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.tryParse((String)id));
        if (soundEvent == null) {
            LOGGER.warn("Failed to find sound event for given key: '{}'", (Object)id);
            return null;
        }
        return () -> soundEvent;
    }

    @Nullable
    public static String getSoundEventId(@NotNull SoundEvent soundEvent) {
        ResourceLocation resourceLocation = BuiltInRegistries.SOUND_EVENT.getKey((Object)soundEvent);
        if (resourceLocation == null) {
            LOGGER.warn("Failed to find key for given sound event: '{}'", (Object)soundEvent);
        }
        return resourceLocation != null ? resourceLocation.toString() : null;
    }

    @Nullable
    public static Supplier<ParticleType<?>> retrieveParticleType(@NotNull String id) {
        ParticleType particleType = (ParticleType)BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.tryParse((String)id));
        if (particleType == null) {
            LOGGER.warn("Failed to find particle type for given key: '{}'", (Object)id);
            return null;
        }
        return () -> particleType;
    }

    @Nullable
    public static String getParticleTypeId(@NotNull ParticleType<?> particleType) {
        ResourceLocation resourceLocation = BuiltInRegistries.PARTICLE_TYPE.getKey(particleType);
        if (resourceLocation == null) {
            LOGGER.warn("Failed to find key for given particle type: '{}'", particleType);
        }
        return resourceLocation != null ? resourceLocation.toString() : null;
    }

    @NotNull
    public static String getItemId(@NotNull Item item) {
        return BuiltInRegistries.ITEM.getKey((Object)item).toString();
    }

    @NotNull
    public static Supplier<Item> retrieveItem(@NotNull String id) {
        return () -> (Item)BuiltInRegistries.ITEM.get(ResourceLocation.tryParse((String)id));
    }
}

