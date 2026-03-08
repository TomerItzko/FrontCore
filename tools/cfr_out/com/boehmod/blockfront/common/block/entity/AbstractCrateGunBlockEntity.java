/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block.entity;

import com.boehmod.blockfront.common.net.packet.BFCrateEntityPacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.PacketUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AbstractCrateGunBlockEntity
extends BlockEntity {
    @NotNull
    public ItemStack itemStack = ItemStack.EMPTY;

    public AbstractCrateGunBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @NotNull
    public ItemStack getHeldItem() {
        return this.itemStack;
    }

    public void putItem(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack.copy();
    }

    public void sync(@NotNull AbstractGame<?, ?, ?> game) {
        PacketUtils.sendToGamePlayers(new BFCrateEntityPacket(this.getBlockPos(), this.getHeldItem().copy()), game);
    }

    public void saveAdditional(@NotNull CompoundTag compoundTag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putBoolean("hasItemStack", !this.itemStack.isEmpty());
        if (this.itemStack.isEmpty()) {
            return;
        }
        CompoundTag compoundTag2 = new CompoundTag();
        this.itemStack.save(provider, (Tag)compoundTag2);
        compoundTag.put("itemStack", (Tag)compoundTag2);
    }

    public void loadAdditional(@NotNull CompoundTag compoundTag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        if (!compoundTag.getBoolean("hasItemStack")) {
            return;
        }
        CompoundTag compoundTag2 = compoundTag.getCompound("itemStack");
        this.itemStack = ItemStack.parseOptional((HolderLookup.Provider)provider, (CompoundTag)compoundTag2);
    }

    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create((BlockEntity)this);
    }
}

