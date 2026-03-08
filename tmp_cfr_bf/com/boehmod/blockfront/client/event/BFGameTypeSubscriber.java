/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.util.BFLog;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerChangeGameTypeEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public class BFGameTypeSubscriber {
    @SubscribeEvent
    public static void onChangeGameType(@NotNull ClientPlayerChangeGameTypeEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && event.getInfo().getProfile().getName().equals(minecraft.getGameProfile().getName())) {
            minecraft.levelRenderer.allChanged();
            BFLog.log("Refreshing world renderer after game-type change to '%s'", event.getNewGameType().getName());
        }
    }
}

