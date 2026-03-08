/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.prompt.text;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.clan.PacketClanInvite;
import com.boehmod.blockfront.client.screen.prompt.text.BFTextPromptScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class ClanInvitePromptScreen
extends BFTextPromptScreen {
    private static final Component field_1561 = Component.translatable((String)"bf.message.prompt.clan.invite.title");
    private static final Component field_1562 = Component.translatable((String)"bf.message.prompt.clan.invite");

    public ClanInvitePromptScreen(@NotNull Screen screen) {
        super(screen, field_1561);
        this.setMaxLength(16);
        this.addFilter(BFTextPromptScreen.Filter.MINECRAFT_USERNAME);
        this.method_1084(field_1562);
    }

    @Override
    public void confirm() {
        ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketClanInvite(this.method_1096()));
        this.method_1081();
    }
}

