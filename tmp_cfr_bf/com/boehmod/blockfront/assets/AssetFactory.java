/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.assets;

import com.boehmod.blockfront.assets.IAsset;
import com.boehmod.blockfront.common.BFAbstractManager;
import org.jetbrains.annotations.NotNull;

public abstract class AssetFactory<A extends IAsset> {
    public abstract A create(boolean var1, @NotNull BFAbstractManager<?, ?, ?> var2, @NotNull String var3, @NotNull String var4);
}

