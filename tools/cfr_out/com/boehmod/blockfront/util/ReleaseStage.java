/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public enum ReleaseStage {
    ALPHA((Component)Component.translatable((String)"bf.message.alpha"), true),
    BETA((Component)Component.translatable((String)"bf.message.beta"), true),
    RELEASE((Component)Component.translatable((String)"bf.message.release"), false);

    private final boolean displayDisclaimer;
    @NotNull
    private final Component component;

    private ReleaseStage(Component component, boolean bl) {
        this.component = component;
        this.displayDisclaimer = bl;
    }

    @NotNull
    public Component getComponent() {
        return this.component;
    }

    public boolean shouldDisplayDisclaimer() {
        return this.displayDisclaimer;
    }
}

