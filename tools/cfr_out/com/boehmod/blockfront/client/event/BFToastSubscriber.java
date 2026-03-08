/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.components.toasts.SystemToast
 *  net.minecraft.client.gui.components.toasts.Toast
 *  net.minecraft.client.resources.language.I18n
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ToastAddEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.event;

import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.resources.language.I18n;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ToastAddEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFToastSubscriber {
    public static final String UNSECURE_SERVER_TITLE = I18n.get((String)"multiplayer.unsecureserver.toast.title", (Object[])new Object[0]);

    @SubscribeEvent
    public static void onToastAdd(@NotNull ToastAddEvent event) {
        Toast toast = event.getToast();
        if (toast instanceof SystemToast) {
            SystemToast systemToast = (SystemToast)toast;
            if (systemToast.title.getString().equalsIgnoreCase(UNSECURE_SERVER_TITLE)) {
                event.setCanceled(true);
            }
        }
    }
}

