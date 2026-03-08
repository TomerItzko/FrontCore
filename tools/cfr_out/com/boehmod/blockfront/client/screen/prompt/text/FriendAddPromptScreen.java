/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.friend.PacketFriendRequest
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.prompt.text;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.friend.PacketFriendRequest;
import com.boehmod.blockfront.client.screen.prompt.text.BFTextPromptScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class FriendAddPromptScreen
extends BFTextPromptScreen {
    private static final Component field_1558 = Component.translatable((String)"bf.message.prompt.friend.add.title");
    private static final Component field_1559 = Component.translatable((String)"bf.message.prompt.friend.add.par1");
    private static final Component field_1560 = Component.translatable((String)"bf.message.prompt.friend.add.par2").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC);

    public FriendAddPromptScreen(@NotNull Screen screen) {
        super(screen, field_1558);
        this.setMaxLength(16);
        this.method_1094(false);
        this.addFilter(BFTextPromptScreen.Filter.MINECRAFT_USERNAME);
        this.method_1085(new Component[]{field_1559, Component.empty(), field_1560});
    }

    @Override
    public void confirm() {
        ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketFriendRequest(this.method_1096()));
        this.method_1081();
    }
}

