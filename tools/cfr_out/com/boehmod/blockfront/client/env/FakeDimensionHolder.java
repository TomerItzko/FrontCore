/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Either
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Holder$Kind
 *  net.minecraft.core.HolderOwner
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.level.dimension.BuiltinDimensionTypes
 *  net.minecraft.world.level.dimension.DimensionType
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.env;

import com.boehmod.blockfront.client.env.FakeLevel;
import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;

public record FakeDimensionHolder() implements Holder<DimensionType>
{
    public boolean isBound() {
        return true;
    }

    public boolean is(@NotNull ResourceLocation resourceLocation) {
        return false;
    }

    public boolean is(@NotNull ResourceKey<DimensionType> resourceKey) {
        return false;
    }

    public boolean is(@NotNull TagKey<DimensionType> tagKey) {
        return false;
    }

    public boolean is(@NotNull Holder<DimensionType> holder) {
        return false;
    }

    public boolean is(@NotNull Predicate<ResourceKey<DimensionType>> resourceKeyPredicate) {
        return false;
    }

    @NotNull
    public Either<ResourceKey<DimensionType>, DimensionType> unwrap() {
        return Either.right((Object)FakeLevel.DIMENSION_TYPE);
    }

    @NotNull
    public Optional<ResourceKey<DimensionType>> unwrapKey() {
        return Optional.of(BuiltinDimensionTypes.OVERWORLD);
    }

    @NotNull
    public Holder.Kind kind() {
        return Holder.Kind.DIRECT;
    }

    @Override
    public String toString() {
        return "Direct{" + String.valueOf(this.value()) + "}";
    }

    public boolean canSerializeIn(@NotNull HolderOwner<DimensionType> holderOwner) {
        return true;
    }

    @NotNull
    public Stream<TagKey<DimensionType>> tags() {
        return Stream.of(new TagKey[0]);
    }

    @NotNull
    public DimensionType value() {
        return FakeLevel.DIMENSION_TYPE;
    }

    @NotNull
    public /* synthetic */ Object value() {
        return this.value();
    }
}

