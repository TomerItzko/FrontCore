/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.dropdown.armory;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventorySetItemDefault;
import com.boehmod.blockfront.client.screen.dropdown.armory.ArmoryDropdownScreen;
import com.boehmod.blockfront.client.screen.prompt.text.ItemRenamePromptScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.armory.ArmorySoundUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CapeItemDropdownScreen
extends ArmoryDropdownScreen {
    private static final Component field_727 = Component.translatable((String)"bf.message.unequip");
    private static final Component field_728 = Component.translatable((String)"bf.message.equip");
    private static final Component field_6302 = Component.translatable((String)"bf.screen.dropdown.item.cape");

    public CapeItemDropdownScreen(Screen screen, int n, int n2, int n3, int n4, CloudItemStack cloudItemStack) {
        super(screen, n, n2, n3, n4, field_6302, cloudItemStack);
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        Component component = playerCloudInventory.isItemEquipped(cloudRegistry, cloudItemStack) ? field_727 : field_728;
        this.method_744(component);
    }

    @Override
    public void method_748(int n) {
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
        CloudRequestManager cloudRequestManager = clientConnectionManager.getRequester();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        switch (n) {
            case 1: {
                PacketInventorySetItemDefault packetInventorySetItemDefault = new PacketInventorySetItemDefault(this.field_709.getUUID());
                ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)packetInventorySetItemDefault);
                cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, playerCloudData.getUUID());
                ArmorySoundUtils.playDefault(this.minecraft, this.manager, this.field_709);
                break;
            }
            case 2: {
                this.minecraft.setScreen((Screen)new ItemRenamePromptScreen(this.field_1036, this.field_709));
            }
        }
    }
}

