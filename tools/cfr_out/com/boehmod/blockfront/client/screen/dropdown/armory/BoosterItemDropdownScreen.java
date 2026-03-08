/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.RequestType
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemShowcase
 *  com.boehmod.bflib.cloud.packet.common.inventory.PacketInventorySetItemDefault
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 */
package com.boehmod.blockfront.client.screen.dropdown.armory;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemShowcase;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventorySetItemDefault;
import com.boehmod.blockfront.client.screen.armory.ArmoryInspectScreen;
import com.boehmod.blockfront.client.screen.dropdown.armory.ArmoryDropdownScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.armory.ArmorySoundUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class BoosterItemDropdownScreen
extends ArmoryDropdownScreen {
    private static final Component field_738 = Component.translatable((String)"bf.message.inspect");
    private static final Component field_739 = Component.translatable((String)"bf.message.unequip");
    private static final Component field_740 = Component.translatable((String)"bf.message.equip");
    private static final Component field_741 = Component.translatable((String)"bf.message.showcase.add");
    private static final Component field_742 = Component.translatable((String)"bf.message.showcase.remove");
    private static final Component field_743 = Component.translatable((String)"bf.screen.dropdown.item.booster");

    public BoosterItemDropdownScreen(Screen screen, int n, int n2, int n3, int n4, CloudItemStack cloudItemStack) {
        super(screen, n, n2, n3, n4, field_743, cloudItemStack);
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        Component component = playerCloudInventory.isItemEquipped(cloudRegistry, cloudItemStack) ? field_739 : field_740;
        Component component2 = playerCloudInventory.isItemShowcased(cloudItemStack.getUUID()) ? field_742 : field_741;
        this.method_744(field_738);
        this.method_744(component);
        this.method_744(component2);
    }

    @Override
    public void method_748(int n) {
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
        CloudRequestManager cloudRequestManager = clientConnectionManager.getRequester();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        switch (n) {
            case 1: {
                ArmoryInspectScreen armoryInspectScreen = new ArmoryInspectScreen(this.field_709);
                this.minecraft.setScreen((Screen)armoryInspectScreen);
                break;
            }
            case 2: {
                PacketInventorySetItemDefault packetInventorySetItemDefault = new PacketInventorySetItemDefault(this.field_709.getUUID());
                clientConnectionManager.sendPacket((IPacket)packetInventorySetItemDefault);
                cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, playerCloudData.getUUID());
                ArmorySoundUtils.playDefault(this.minecraft, this.manager, this.field_709);
                break;
            }
            case 3: {
                PacketInventoryItemShowcase packetInventoryItemShowcase = new PacketInventoryItemShowcase(this.field_709.getUUID());
                clientConnectionManager.sendPacket((IPacket)packetInventoryItemShowcase);
                cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, playerCloudData.getUUID());
                ArmorySoundUtils.playDefault(this.minecraft, this.manager, this.field_709);
            }
        }
    }
}

