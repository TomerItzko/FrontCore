/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.friend.PacketFriendRequestAccept
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.dropdown;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.friend.PacketFriendRequestAccept;
import com.boehmod.blockfront.client.screen.dropdown.DropdownScreen;
import com.boehmod.blockfront.client.screen.profile.ProfileMainScreen;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public final class FriendRequestDropdownScreen
extends DropdownScreen {
    private static final Component field_704 = Component.translatable((String)"bf.message.accept").withStyle(ChatFormatting.GREEN);
    private static final Component field_705 = Component.translatable((String)"bf.message.accept.tip").withStyle(ChatFormatting.GREEN);
    private static final Component field_706 = Component.translatable((String)"bf.message.ignore").withStyle(ChatFormatting.RED);
    private static final Component field_707 = Component.translatable((String)"bf.message.ignore.tip").withStyle(ChatFormatting.RED);
    private static final Component field_703 = Component.translatable((String)"bf.dropdown.text.profile");
    @NotNull
    private final PlayerCloudData field_708;

    public FriendRequestDropdownScreen(@NotNull Screen screen, int n, int n2, int n3, int n4, @NotNull PlayerCloudData playerCloudData) {
        super(screen, n, n2, n3, n4, (Component)Component.translatable((String)"bf.screen.dropdown.friend.accept", (Object[])new Object[]{playerCloudData.getUsername()}));
        this.field_708 = playerCloudData;
        this.method_745(field_704, field_705);
        this.method_745(field_706, field_707);
        MutableComponent mutableComponent = Component.literal((String)playerCloudData.getUsername());
        this.method_745(field_703, (Component)Component.translatable((String)"bf.dropdown.text.profile.tip", (Object[])new Object[]{mutableComponent}));
    }

    @Override
    public void method_748(int n) {
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
        UUID uUID = this.field_708.getUUID();
        switch (n) {
            case 1: {
                clientConnectionManager.sendPacket((IPacket)new PacketFriendRequestAccept(uUID, true));
                break;
            }
            case 2: {
                clientConnectionManager.sendPacket((IPacket)new PacketFriendRequestAccept(uUID, false));
                break;
            }
            case 3: {
                this.minecraft.setScreen((Screen)new ProfileMainScreen(this.field_1036, this.field_708));
                return;
            }
        }
        if (this.minecraft.level == null) {
            this.minecraft.setScreen((Screen)new LobbyTitleScreen());
        }
    }
}

