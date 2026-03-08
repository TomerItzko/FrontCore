/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RenderTooltipEvent$Color
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public class BFRenderTooltipSubscriber {
    @SubscribeEvent
    public static void onRenderTooltip(@NotNull RenderTooltipEvent.Color event) {
        event.setBorderStart(0x33FFFFFF);
        event.setBorderEnd(0x22FFFFFF);
        event.setBackground(-587202560);
    }
}

