/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.packs.resources.PreparableReloadListener
 *  net.minecraft.server.packs.resources.ResourceManagerReloadListener
 *  net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.setup;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.render.geo.BFWeaponItemRenderer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import org.jetbrains.annotations.NotNull;

public class BFClientReloadListenersSetup {
    public static void register(@NotNull RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((PreparableReloadListener)((ResourceManagerReloadListener)resourceManager -> {
            BFClientManager bFClientManager = BFClientManager.getInstance();
            if (bFClientManager != null) {
                bFClientManager.getPlayerRendererChecker().queueCheck();
                BFClientAntiCheat bFClientAntiCheat = (BFClientAntiCheat)bFClientManager.getAntiCheat();
                bFClientAntiCheat.startupChecksShouldRun();
            }
            ++BFWeaponItemRenderer.field_6517;
        }));
    }
}

