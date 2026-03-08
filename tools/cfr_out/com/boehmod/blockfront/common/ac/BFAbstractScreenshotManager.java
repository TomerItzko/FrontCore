/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.ac;

import com.boehmod.blockfront.common.BFAbstractManager;
import org.jetbrains.annotations.NotNull;

public abstract class BFAbstractScreenshotManager<M extends BFAbstractManager<?, ?, ?>> {
    protected abstract void onUpdate(@NotNull M var1);
}

