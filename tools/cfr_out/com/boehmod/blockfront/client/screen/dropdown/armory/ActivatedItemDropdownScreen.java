/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryActivateItem
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 */
package com.boehmod.blockfront.client.screen.dropdown.armory;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryActivateItem;
import com.boehmod.blockfront.client.screen.armory.ArmoryInspectScreen;
import com.boehmod.blockfront.client.screen.dropdown.armory.ArmoryDropdownScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ActivatedItemDropdownScreen
extends ArmoryDropdownScreen {
    private static final Component field_724 = Component.translatable((String)"bf.screen.dropdown.item.activated");
    private static final Component field_725 = Component.translatable((String)"bf.message.inspect");
    private static final Component field_726 = Component.translatable((String)"bf.message.activate");

    public ActivatedItemDropdownScreen(Screen screen, int n, int n2, int n3, int n4, CloudItemStack cloudItemStack) {
        super(screen, n, n2, n3, n4, field_724, cloudItemStack);
        this.method_744(field_725);
        this.method_744(field_726);
    }

    @Override
    public void method_748(int n) {
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
        switch (n) {
            case 1: {
                this.minecraft.setScreen((Screen)new ArmoryInspectScreen(this.field_709));
                break;
            }
            case 2: {
                clientConnectionManager.sendPacket((IPacket)new PacketInventoryActivateItem(this.field_709.getUUID()));
            }
        }
    }
}

