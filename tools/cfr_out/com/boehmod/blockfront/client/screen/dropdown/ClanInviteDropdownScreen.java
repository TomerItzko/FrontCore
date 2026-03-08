/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.clan.PacketClanResponse
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.dropdown;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.clan.PacketClanResponse;
import com.boehmod.blockfront.client.screen.dropdown.DropdownScreen;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class ClanInviteDropdownScreen
extends DropdownScreen {
    private static final Component field_673 = Component.translatable((String)"bf.message.accept").withStyle(ChatFormatting.GREEN);
    private static final Component field_674 = Component.translatable((String)"bf.message.accept.tip").withStyle(ChatFormatting.GREEN);
    private static final Component field_675 = Component.translatable((String)"bf.message.ignore").withStyle(ChatFormatting.RED);
    private static final Component field_676 = Component.translatable((String)"bf.message.ignore.tip").withStyle(ChatFormatting.RED);
    private static final Component field_677 = Component.translatable((String)"bf.screen.dropdown.clan.invite");
    @NotNull
    private final UUID field_678;

    public ClanInviteDropdownScreen(@NotNull Screen screen, int n, int n2, int n3, int n4, @NotNull UUID uUID) {
        super(screen, n, n2, n3, n4, field_677);
        this.field_678 = uUID;
        this.method_745(field_673, field_674);
        this.method_745(field_675, field_676);
    }

    @Override
    public void method_748(int n) {
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
        switch (n) {
            case 1: {
                clientConnectionManager.sendPacket((IPacket)new PacketClanResponse(this.field_678, true));
                break;
            }
            case 2: {
                clientConnectionManager.sendPacket((IPacket)new PacketClanResponse(this.field_678, false));
            }
        }
        if (this.minecraft.level == null) {
            this.minecraft.setScreen((Screen)new LobbyTitleScreen());
        }
    }
}

