/*
 * Decompiled with CFR 0.152.
 */
package com.boehmod.blockfront.client.setup;

import com.boehmod.blockfront.client.render.ClipTextRenderers;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.registry.BFItems;

public final class BFClipTextRendererSetup {
    public static void register() {
        ((GunItem)BFItems.GUN_M2_FLAMETHROWER.get()).setClipTextRenderer(ClipTextRenderers.FLAMETHROWER);
        ((GunItem)BFItems.GUN_FLAMMENWERFER_34.get()).setClipTextRenderer(ClipTextRenderers.FLAMETHROWER);
    }
}

