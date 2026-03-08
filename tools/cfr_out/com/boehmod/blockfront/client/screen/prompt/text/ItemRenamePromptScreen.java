/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemRename
 *  javax.annotation.Nullable
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.prompt.text;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemRename;
import com.boehmod.blockfront.client.screen.prompt.text.BFTextPromptScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

public final class ItemRenamePromptScreen
extends BFTextPromptScreen {
    private static final Component field_1569 = Component.translatable((String)"bf.message.prompt.item.rename");
    private static final Component field_1570 = Component.translatable((String)"bf.message.prompt.item.rename.title");
    public static final Component field_1571 = Component.translatable((String)"bf.message.item.rename.error.format");
    private final CloudItemStack field_1572;

    public ItemRenamePromptScreen(@Nullable Screen screen, @NotNull CloudItemStack cloudItemStack) {
        super(screen, field_1570);
        this.field_1572 = cloudItemStack;
        this.addFilter(BFTextPromptScreen.Filter.COMMON);
        this.method_1085(new Component[]{Component.empty(), field_1569});
    }

    @Override
    public void confirm() {
        boolean bl;
        String string = this.method_1096();
        int n = 16;
        boolean bl2 = bl = string.length() <= 16 && string.matches("[A-Za-z0-9_ ]+") && !string.isEmpty();
        if (!bl) {
            this.field_1557 = field_1571;
            return;
        }
        SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, (float)1.0f);
        this.minecraft.getSoundManager().play((SoundInstance)simpleSoundInstance);
        ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketInventoryItemRename(this.field_1572.getUUID(), string));
        this.method_1081();
    }
}

