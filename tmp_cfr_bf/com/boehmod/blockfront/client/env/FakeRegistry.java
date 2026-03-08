/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.env;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeRegistry<T>
extends MappedRegistry<T> {
    public FakeRegistry(@NotNull ResourceKey<? extends Registry<T>> resourceKey) {
        super(resourceKey, Lifecycle.stable());
    }

    public // Could not load outer class - annotation placement on inner may be incorrect
     @Nullable Holder.Reference<T> getHolderOrThrow(@NotNull ResourceKey<T> resourceKey) {
        return null;
    }
}

