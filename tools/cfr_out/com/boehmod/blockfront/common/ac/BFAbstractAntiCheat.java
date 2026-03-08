/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.OverridingMethodsMustInvokeSuper
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.ac;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.ac.BFAbstractScreenshotManager;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.jetbrains.annotations.NotNull;

public abstract class BFAbstractAntiCheat<M extends BFAbstractManager<?, ?, ?>, S extends BFAbstractScreenshotManager<M>> {
    @NotNull
    protected final S screenshotManager;

    public BFAbstractAntiCheat(@NotNull S screenshotManager) {
        this.screenshotManager = screenshotManager;
    }

    @OverridingMethodsMustInvokeSuper
    public void onUpdate(@NotNull M manager) {
        ((BFAbstractScreenshotManager)this.screenshotManager).onUpdate(manager);
    }

    @NotNull
    public S getScreenshotManager() {
        return this.screenshotManager;
    }
}

