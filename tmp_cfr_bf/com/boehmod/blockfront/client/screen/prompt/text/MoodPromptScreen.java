/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.prompt.text;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.profile.PacketEditMood;
import com.boehmod.blockfront.client.screen.prompt.text.BFTextPromptScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class MoodPromptScreen
extends BFTextPromptScreen {
    private static final Component field_1567 = Component.translatable((String)"bf.message.prompt.mood.title");
    private static final Component field_1568 = Component.translatable((String)"bf.message.prompt.mood");

    public MoodPromptScreen(@NotNull Screen screen) {
        super(screen, field_1567);
        this.setMaxLength(48);
        this.field_1536 = true;
        this.addFilter(BFTextPromptScreen.Filter.COMMON);
        this.method_1084(field_1568);
    }

    @Override
    public void confirm() {
        ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketEditMood(this.method_1096()));
        this.method_1081();
    }
}

