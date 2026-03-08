/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.event.tick.MenuMusicTickable;
import com.boehmod.blockfront.client.screen.intro.BFIntroScreen;
import com.boehmod.blockfront.client.screen.match.MatchPauseScreen;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFClientScreenSubscriber {
    @SubscribeEvent
    public static void onOpenScreen(@NotNull ScreenEvent.Opening event) {
        Object object;
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (event.getScreen() instanceof TitleScreen) {
            BFClientAntiCheat.enabled = true;
            MenuMusicTickable.play = true;
            event.setNewScreen(BFIntroScreen.method_655());
            return;
        }
        if (event.getScreen() instanceof PauseScreen && abstractGame != null) {
            event.setNewScreen((Screen)new MatchPauseScreen());
            return;
        }
        Screen screen = event.getScreen();
        if (screen instanceof DisconnectedScreen) {
            object = (DisconnectedScreen)screen;
            bFClientManager.getMatchMaking().reset();
        }
        if (clientLevel != null && event.getScreen() instanceof InventoryScreen && (object = bFClientManager.getGameClient()) != null && !((AbstractGameClient)object).method_2718()) {
            event.setCanceled(true);
        }
    }
}

