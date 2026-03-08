/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.cloud;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.CloudItemType;
import com.boehmod.bflib.cloud.common.item.CloudResourceLocation;
import com.boehmod.bflib.cloud.common.item.equipped.BaseEquippedItem;
import com.boehmod.bflib.cloud.common.item.equipped.MultiEquippedItem;
import com.boehmod.bflib.cloud.common.item.types.CloudItemArmour;
import com.boehmod.bflib.cloud.common.player.AbstractCloudInventory;
import com.boehmod.bflib.cloud.common.player.AbstractPlayerCloudData;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemDeleteRequest;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public final class PlayerCloudInventory
extends AbstractCloudInventory<PlayerCloudData> {
    @NotNull
    private final Queue<UUID> field_2103 = new LinkedList<UUID>();

    public PlayerCloudInventory(@NotNull PlayerCloudData playerCloudData) {
        super((AbstractPlayerCloudData)playerCloudData);
    }

    @NotNull
    public List<UUID> method_1681() {
        return this.field_2103.stream().toList();
    }

    public void method_1680(@NotNull List<UUID> list) {
        this.field_2103.clear();
        this.field_2103.addAll(list);
    }

    @NotNull
    public Optional<UUID> method_1671() {
        return Optional.ofNullable(this.field_2103.poll());
    }

    public void method_1678() {
        this.field_2103.clear();
    }

    public void addItemStackLocal(@NotNull CloudItemStack cloudItemStack) {
        super.addItemStackLocal(cloudItemStack);
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Manager is null!";
        CloudRegistry cloudRegistry = bFAbstractManager.getCloudRegistry();
        CloudItem cloudItem = cloudItemStack.getCloudItem(cloudRegistry);
        if (cloudItem != null && !cloudItem.isDefault()) {
            this.field_2103.add(cloudItemStack.getUUID());
        }
    }

    public void deleteItem(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull CloudItemStack itemStack) {
        CloudRegistry cloudRegistry = manager.getCloudRegistry();
        UUID uUID = itemStack.getUUID();
        CloudItem cloudItem = itemStack.getCloudItem(cloudRegistry);
        if (cloudItem != null && !cloudItem.isDefault()) {
            this.removeItemStackLocal(uUID);
            ((AbstractConnectionManager)manager.getConnectionManager()).sendPacket((IPacket)new PacketInventoryItemDeleteRequest(uUID));
        }
    }

    @Nullable
    public CloudItemStack method_1677(@NotNull BFCountry bFCountry) {
        Object object = this.getEquippedItems(CloudItemType.ARMOR);
        if (object instanceof MultiEquippedItem) {
            MultiEquippedItem multiEquippedItem = (MultiEquippedItem)object;
            return (object = multiEquippedItem.getStack(bFCountry.getTag())) == null ? null : (CloudItemStack)this.getStackFromUUID((UUID)object).orElse(null);
        }
        return null;
    }

    @Nullable
    public CloudItemStack method_1679() {
        Object object = this.getEquippedItems(CloudItemType.ARMOR);
        if (object instanceof MultiEquippedItem) {
            MultiEquippedItem multiEquippedItem = (MultiEquippedItem)object;
            return (object = (UUID)multiEquippedItem.getCloudStackUUIDs().stream().findFirst().orElse(null)) == null ? null : (CloudItemStack)this.getStackFromUUID((UUID)object).orElse(null);
        }
        return null;
    }

    public boolean isItemEquipped(@NotNull CloudRegistry registry, @NotNull CloudItemStack itemStack) {
        CloudItem cloudItem = itemStack.getCloudItem(registry);
        if (cloudItem == null) {
            return false;
        }
        CloudItemStack cloudItemStack = this.method_5486(registry, cloudItem.getMinecraftItem());
        if (cloudItemStack != null) {
            return cloudItemStack.equals((Object)itemStack);
        }
        return false;
    }

    @Nullable
    public CloudItemStack method_1673(@NotNull CloudItemType cloudItemType) {
        return this.method_1674(cloudItemType, null);
    }

    @Nullable
    public CloudItemStack method_1674(@NotNull CloudItemType cloudItemType, @Nullable String string) {
        BaseEquippedItem baseEquippedItem = this.getEquippedItems(cloudItemType);
        UUID uUID = baseEquippedItem.getStack(string);
        return uUID == null ? null : (CloudItemStack)this.getStackFromUUID(uUID).orElse(null);
    }

    @Nullable
    public CloudItemStack method_5486(@NotNull CloudRegistry cloudRegistry, @NotNull CloudResourceLocation cloudResourceLocation) {
        CloudItem cloudItem = CloudItem.getCloudItemFromMcItem((CloudRegistry)cloudRegistry, (CloudResourceLocation)cloudResourceLocation);
        if (cloudItem != null) {
            CloudItemType cloudItemType = cloudItem.getItemType();
            BaseEquippedItem baseEquippedItem = this.getEquippedItems(cloudItemType);
            UUID uUID = baseEquippedItem.getStack(cloudResourceLocation);
            if (cloudItem instanceof CloudItemArmour) {
                CloudItemArmour cloudItemArmour = (CloudItemArmour)cloudItem;
                uUID = baseEquippedItem.getStack(cloudItemArmour.getNation().getTag());
            }
            if (uUID != null) {
                return this.getStackFromUUID(uUID).orElse(null);
            }
        }
        return null;
    }
}

