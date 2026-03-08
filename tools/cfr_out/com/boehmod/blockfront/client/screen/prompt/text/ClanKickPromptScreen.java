/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.clan.PacketClanKick
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.prompt.text;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.clan.PacketClanKick;
import com.boehmod.blockfront.client.screen.prompt.text.BFTextPromptScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class ClanKickPromptScreen
extends BFTextPromptScreen {
    private static final Component field_1563 = Component.translatable((String)"bf.message.prompt.clan.kick.title");
    private static final Component field_1564 = Component.translatable((String)"bf.message.prompt.clan.kick");

    public ClanKickPromptScreen(@NotNull Screen screen) {
        super(screen, field_1563);
        this.setMaxLength(16);
        this.addFilter(BFTextPromptScreen.Filter.MINECRAFT_USERNAME);
        this.method_1084(field_1564);
    }

    @Override
    public void confirm() {
        ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketClanKick(this.method_1096()));
        this.method_1081();
    }
}

