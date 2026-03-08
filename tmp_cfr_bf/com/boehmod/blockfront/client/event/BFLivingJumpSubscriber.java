/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFLivingJumpSubscriber {
    @SubscribeEvent
    public static void onLivingJump(@NotNull LivingEvent.LivingJumpEvent event) {
        AbstractGameClient<?, ?> abstractGameClient;
        Player player;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        Object object = event.getEntity();
        if (!(object instanceof Player) || !(player = (Player)object).equals((Object)localPlayer)) {
            return;
        }
        if (player.isCreative()) {
            return;
        }
        object = BFClientManager.getInstance();
        assert (object != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = ((BFClientManager)object).getGame();
        if (abstractGame == null) {
            return;
        }
        if (abstractGame.shouldUseStamina(player) && (abstractGameClient = ((BFClientManager)object).getGameClient()) != null) {
            float f = abstractGameClient.method_2675();
            float f2 = 3.0f;
            if (f < 3.0f) {
                player.setDeltaMovement(player.getDeltaMovement().multiply((double)0.4f, 1.0, (double)0.4f));
            } else {
                abstractGameClient.method_2731(3.0f);
            }
        }
    }
}

