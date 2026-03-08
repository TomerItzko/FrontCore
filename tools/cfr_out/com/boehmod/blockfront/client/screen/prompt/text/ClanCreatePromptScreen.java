/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.clan.PacketClanCreate
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.prompt.text;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.clan.PacketClanCreate;
import com.boehmod.blockfront.client.screen.prompt.text.BFTextPromptScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class ClanCreatePromptScreen
extends BFTextPromptScreen {
    private static final Component field_1565 = Component.translatable((String)"bf.message.prompt.clan.create.title");
    private static final Component field_1566 = Component.translatable((String)"bf.message.prompt.clan.create");

    public ClanCreatePromptScreen(@NotNull Screen screen) {
        super(screen, field_1565);
        this.setMaxLength(16);
        this.addFilter(BFTextPromptScreen.Filter.COMMON);
        this.method_1084(field_1566);
    }

    @Override
    public void confirm() {
        ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketClanCreate(this.method_1096()));
        this.method_1081();
    }
}

