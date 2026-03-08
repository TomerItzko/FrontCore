/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.prompt.text;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.friend.PacketFriendPoke;
import com.boehmod.blockfront.client.player.ClientFriendManager;
import com.boehmod.blockfront.client.screen.prompt.text.BFTextPromptScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class FriendPokePromptScreen
extends BFTextPromptScreen {
    private static final Component field_1574 = Component.translatable((String)"bf.message.prompt.friend.poke.title");
    private static final Component field_1575 = Component.translatable((String)"bf.message.prompt.friend.poke");
    private final UUID target;

    public FriendPokePromptScreen(@NotNull Screen screen, @NotNull UUID uUID) {
        super(screen, field_1574);
        this.target = uUID;
        this.setMaxLength(50);
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudProfile(uUID);
        this.addFilter(BFTextPromptScreen.Filter.COMMON);
        this.method_1085(new Component[]{field_1575, Component.empty(), Component.literal((String)playerCloudData.getUsername()), Component.translatable((String)"bf.message.prompt.friend.poke.par1", (Object[])new Object[]{Component.literal((String)String.valueOf(this.method_1088()))}).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC)});
    }

    @Override
    public void confirm() {
        ClientFriendManager clientFriendManager = this.manager.getFriendManager();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudProfile(this.target);
        if (clientFriendManager.hasFriend(this.target)) {
            ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketFriendPoke(playerCloudData.getUUID(), this.method_1096()));
        }
        this.method_1081();
    }
}

