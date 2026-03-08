/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.assets.impl;

import com.boehmod.blockfront.assets.AssetFactory;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.common.BFAbstractManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MapAssetFactory
extends AssetFactory<MapAsset> {
    @Override
    @Nullable
    public MapAsset create(boolean bl, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull String string, @NotNull String string2) {
        return new MapAsset(string, string2);
    }
}

