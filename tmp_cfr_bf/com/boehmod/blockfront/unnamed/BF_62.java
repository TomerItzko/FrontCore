/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.event.impl.GunEvent;
import com.boehmod.blockfront.game.AbstractGame;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public class BF_62 {
    @SubscribeEvent
    public static void method_405(@NotNull GunEvent.BF_671 bF_671) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        int n = abstractGame.method_2638();
        if (bF_671.method_2573() < n) {
            bF_671.method_2574(n);
        }
        bF_671.method_2574((int)((float)bF_671.method_2573() * abstractGame.getFireRateMultiplier()));
    }
}

