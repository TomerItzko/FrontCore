/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.RequestType
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.CloudItemType
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemClearName
 *  com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemShowcase
 *  com.boehmod.bflib.cloud.packet.common.inventory.PacketInventorySetItemDefault
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.dropdown.armory;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.CloudItemType;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemClearName;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemShowcase;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventorySetItemDefault;
import com.boehmod.blockfront.client.screen.armory.ArmoryInspectScreen;
import com.boehmod.blockfront.client.screen.armory.ArmoryStickerPlaceScreen;
import com.boehmod.blockfront.client.screen.dropdown.armory.ArmoryDropdownScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.BFConfirmPromptScreen;
import com.boehmod.blockfront.client.screen.prompt.text.ItemRenamePromptScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.armory.ArmorySoundUtils;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class ItemDropdownScreen
extends ArmoryDropdownScreen {
    private static final Component field_710 = Component.translatable((String)"bf.message.inspect");
    private static final Component field_711 = Component.translatable((String)"bf.message.unequip");
    private static final Component field_712 = Component.translatable((String)"bf.message.equip");
    private static final Component field_713 = Component.translatable((String)"bf.message.showcase.add");
    private static final Component field_714 = Component.translatable((String)"bf.message.showcase.remove");
    private static final Component field_715 = Component.translatable((String)"bf.message.rename");
    private static final Component field_716 = Component.translatable((String)"bf.message.stickers");
    private static final Component field_717 = Component.translatable((String)"bf.message.rename.clear");
    private static final Component field_718 = Component.translatable((String)"bf.message.delete");
    private static final Component field_719 = Component.translatable((String)"bf.screen.dropdown.item");
    private static final Component field_720 = Component.translatable((String)"bf.message.prompt.item.rename.clear.title");
    private static final Component field_721 = Component.translatable((String)"bf.message.prompt.item.delete.title");
    private static final Component field_722 = Component.translatable((String)"bf.message.prompt.item.rename.clear");
    private static final Component field_723 = Component.translatable((String)"bf.message.prompt.item.delete");

    public ItemDropdownScreen(@NotNull Screen screen, @NotNull CloudItemStack cloudItemStack, int n, int n2, int n3, int n4) {
        super(screen, n, n2, n3, n4, field_719, cloudItemStack);
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        Component component = playerCloudInventory.isItemEquipped(cloudRegistry, cloudItemStack) ? field_711 : field_712;
        Component component2 = playerCloudInventory.isItemShowcased(cloudItemStack.getUUID()) ? field_714 : field_713;
        CloudItem cloudItem = cloudItemStack.getCloudItem(cloudRegistry);
        if (cloudItem == null) {
            return;
        }
        Optional optional = cloudItemStack.getNameTag();
        this.method_744(field_710);
        this.method_747(component, cloudItem.canEquip());
        this.method_747(component2, cloudItem.canShowcase());
        this.method_747(field_715, cloudItem.canRename() && !cloudItem.isDefault());
        this.method_747(field_716, cloudItem.getItemType() == CloudItemType.GUN);
        this.method_747(field_717, optional.isPresent());
        this.method_747(field_718, !cloudItem.isDefault());
    }

    @Override
    public void method_748(int n) {
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        UUID uUID = playerCloudData.getUUID();
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
        CloudRequestManager cloudRequestManager = clientConnectionManager.getRequester();
        switch (n) {
            case 1: {
                ArmoryInspectScreen armoryInspectScreen = new ArmoryInspectScreen(this.field_709);
                this.minecraft.setScreen((Screen)armoryInspectScreen);
                break;
            }
            case 2: {
                PacketInventorySetItemDefault packetInventorySetItemDefault = new PacketInventorySetItemDefault(this.field_709.getUUID());
                clientConnectionManager.sendPacket((IPacket)packetInventorySetItemDefault);
                cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, uUID);
                ArmorySoundUtils.playDefault(this.minecraft, this.manager, this.field_709);
                break;
            }
            case 3: {
                PacketInventoryItemShowcase packetInventoryItemShowcase = new PacketInventoryItemShowcase(this.field_709.getUUID());
                clientConnectionManager.sendPacket((IPacket)packetInventoryItemShowcase);
                cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, uUID);
                ArmorySoundUtils.playDefault(this.minecraft, this.manager, this.field_709);
                break;
            }
            case 4: {
                this.minecraft.setScreen((Screen)new ItemRenamePromptScreen(this.field_1036, this.field_709));
                break;
            }
            case 5: {
                this.minecraft.setScreen((Screen)new ArmoryStickerPlaceScreen(this.field_709));
                break;
            }
            case 6: {
                BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen((Screen)this, field_720, bl -> {
                    if (bl) {
                        ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketInventoryItemClearName(this.field_709.getUUID()));
                    }
                });
                bFConfirmPromptScreen.method_1084(field_722);
                this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
                break;
            }
            case 7: {
                BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen(this.field_1036, field_721, bl -> {
                    if (bl) {
                        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
                        ((PlayerCloudInventory)playerCloudData.getInventory()).deleteItem(this.manager, this.field_709);
                    }
                });
                bFConfirmPromptScreen.method_1084(field_723);
                this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
            }
        }
    }
}

