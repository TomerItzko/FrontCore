/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class BFScreen
extends Screen {
    @NotNull
    protected final ClientPlayerDataHandler playerDataHandler;
    @NotNull
    protected final BFClientManager manager;
    @NotNull
    protected final Minecraft minecraft;

    protected BFScreen(@NotNull Component title) {
        super(title);
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        this.manager = bFClientManager;
        this.playerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        this.minecraft = Minecraft.getInstance();
        this.font = this.minecraft.font;
    }
}

