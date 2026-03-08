/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.game.AbstractGameClient;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public class BFInputSubscriber {
    @SubscribeEvent
    public static void onInputKey(@NotNull InputEvent.Key event) {
        AbstractGameClient<?, ?> abstractGameClient;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        if (event.getKey() == minecraft.options.keyTogglePerspective.getKey().getValue() && (abstractGameClient = bFClientManager.getGameClient()) != null && !abstractGameClient.method_2730((Player)localPlayer) && !localPlayer.isCreative()) {
            minecraft.options.setCameraType(CameraType.FIRST_PERSON);
        }
    }
}

