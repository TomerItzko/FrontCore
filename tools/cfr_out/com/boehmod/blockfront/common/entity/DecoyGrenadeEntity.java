/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.net.packet.BFGunSoundPacket;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.PacketUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class DecoyGrenadeEntity
extends GrenadeEntity {
    public int field_2578 = 400;
    public boolean field_2577 = false;
    private GunItem field_2576 = (GunItem)BFItems.GUN_M1928A1_THOMPSON.get();

    public DecoyGrenadeEntity(EntityType<? extends DecoyGrenadeEntity> entityType, Level level) {
        super((EntityType<? extends GrenadeEntity>)entityType, level);
    }

    public GunItem method_2272() {
        return this.field_2576;
    }

    public DecoyGrenadeEntity method_2273(GunItem gunItem) {
        this.field_2576 = gunItem;
        return this;
    }

    @Override
    public void method_1949(@NotNull ServerPlayer serverPlayer, float f, @NotNull ItemStack itemStack) {
        super.method_1949(serverPlayer, f, itemStack);
        for (ItemStack itemStack2 : serverPlayer.getInventory().items) {
            Item item;
            if (itemStack2 == null || itemStack2.isEmpty() || !((item = itemStack2.getItem()) instanceof GunItem)) continue;
            GunItem gunItem = (GunItem)item;
            this.method_2273(gunItem);
            break;
        }
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        if (this.field_2576 == null) {
            this.discard();
            return;
        }
        Vec3 vec3 = this.position();
        if (this.field_2340 && Math.random() < 0.05 && !level.isClientSide() && this.owner != null) {
            level.addParticle((ParticleOptions)ParticleTypes.NOTE, vec3.x, vec3.y + 0.25, vec3.z, 0.0, 0.0, 0.0);
            BFGunSoundPacket bFGunSoundPacket = new BFGunSoundPacket(new ItemStack((ItemLike)this.field_2576), vec3, this.owner.getId(), false, true, true);
            for (Player player : level.players()) {
                if (!(player instanceof ServerPlayer)) continue;
                ServerPlayer serverPlayer = (ServerPlayer)player;
                PacketUtils.sendToPlayer(bFGunSoundPacket, serverPlayer);
            }
        }
        if (this.field_2578-- <= 0) {
            if (level.isClientSide()) {
                level.addParticle((ParticleOptions)ParticleTypes.EXPLOSION, true, vec3.x, vec3.y + 0.25, vec3.z, 0.0, 0.0, 0.0);
                level.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.ITEM_GRENADE_FLASH_EXPLODE.get(), SoundSource.BLOCKS, 3.0f, 2.0f, false);
            }
            this.discard();
        }
    }

    @Override
    public int method_2284() {
        return 60;
    }

    @Override
    public void method_1957() {
        if (!this.field_2340) {
            this.field_2340 = true;
        }
    }

    @Override
    protected void method_2282() {
        this.field_2577 = true;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("hasStopped", this.field_2577);
        compoundTag.putBoolean("hasItem", this.field_2576 != null);
        new ItemStack((ItemLike)this.field_2576).save((HolderLookup.Provider)this.registryAccess(), (Tag)compoundTag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.field_2577 = compoundTag.getBoolean("hasStopped");
        boolean bl = compoundTag.getBoolean("hasItem");
        if (bl) {
            this.field_2576 = (GunItem)ItemStack.parseOptional((HolderLookup.Provider)this.registryAccess(), (CompoundTag)compoundTag).getItem();
        }
    }
}

