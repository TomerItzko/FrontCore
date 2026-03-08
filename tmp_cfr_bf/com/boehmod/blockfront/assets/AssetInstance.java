/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.assets;

import com.boehmod.blockfront.assets.AssetRegistry;
import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.IAsset;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public class AssetInstance<T extends IAsset> {
    @NotNull
    private final Class<T> assetClass;
    @Nullable
    private final String name;
    private final int initialTimesLoaded = AssetStore.getInstance().getTimesLoaded();
    @Nullable
    private T instance = null;
    @Nullable
    private T fallback;
    private boolean noFallback;

    public AssetInstance(@NotNull Class<T> assetClass, @NotNull String name, @NotNull T fallback) {
        this.assetClass = assetClass;
        this.name = name;
        this.fallback = fallback;
        this.retrieveInstance();
    }

    public AssetInstance(@NotNull T instance) {
        this.assetClass = instance.getClass();
        this.name = null;
        this.instance = instance;
        this.noFallback = true;
    }

    @Nullable
    public T get() {
        boolean bl;
        boolean bl2 = bl = !this.noFallback && this.initialTimesLoaded != AssetStore.getInstance().getTimesLoaded();
        if (bl || this.instance == null) {
            this.retrieveInstance();
        }
        return this.instance != null ? this.instance : this.fallback;
    }

    private void retrieveInstance() {
        if (this.name == null) {
            return;
        }
        AssetRegistry<T> assetRegistry = AssetStore.getInstance().getRegistry(this.assetClass);
        this.instance = assetRegistry.getByName(this.name);
    }

    public boolean isPresent() {
        return this.get() != null;
    }

    public boolean isEmpty() {
        return !this.isPresent();
    }
}

