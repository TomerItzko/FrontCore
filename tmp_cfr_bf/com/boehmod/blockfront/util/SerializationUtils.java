/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.gun.bullet.BlockBulletCollision;
import com.boehmod.blockfront.common.gun.bullet.LivingBulletCollision;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SerializationUtils {
    private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Item>> ITEM_CODEC = ByteBufCodecs.holderRegistry((ResourceKey)Registries.ITEM);

    @NotNull
    public static ItemStack readItemStack(@NotNull RegistryFriendlyByteBuf buf) {
        boolean bl = buf.readBoolean();
        return bl ? (ItemStack)ItemStack.STREAM_CODEC.decode((Object)buf) : ItemStack.EMPTY;
    }

    public static void writeItemStack(@NotNull RegistryFriendlyByteBuf buf, @NotNull ItemStack itemStack) {
        buf.writeBoolean(!itemStack.isEmpty());
        if (!itemStack.isEmpty()) {
            ItemStack.STREAM_CODEC.encode((Object)buf, (Object)itemStack);
        }
    }

    @NotNull
    public static Holder<Item> readItem(@NotNull RegistryFriendlyByteBuf buf) {
        return (Holder)ITEM_CODEC.decode((Object)buf);
    }

    public static void writeItem(@NotNull RegistryFriendlyByteBuf buf, @NotNull Holder<Item> holder) {
        ITEM_CODEC.encode((Object)buf, holder);
    }

    @NotNull
    public static BFAbstractPlayerData<?, ?, ?, ?> readPlayerGameData(@NotNull FriendlyByteBuf buf) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        Object d = ((PlayerDataHandler)obj).getPlayerData(buf.readUUID());
        ((BFAbstractPlayerData)d).readBuf(buf);
        return d;
    }

    public static void writePlayerData(@NotNull FriendlyByteBuf buf, @NotNull BFAbstractPlayerData<?, ?, ?, ?> data) {
        buf.writeUUID(data.getUUID());
        data.writeBuf(buf);
    }

    @NotNull
    public static Holder<SoundEvent> readSoundEvent(@NotNull RegistryFriendlyByteBuf buf) {
        return (Holder)SoundEvent.STREAM_CODEC.decode((Object)buf);
    }

    public static void writeSoundEvent(@NotNull RegistryFriendlyByteBuf buf, @NotNull Holder<SoundEvent> holder) {
        SoundEvent.STREAM_CODEC.encode((Object)buf, holder);
    }

    @NotNull
    public static ObjectList<BlockBulletCollision> readBlockBulletCollisions(@NotNull FriendlyByteBuf buf) {
        int n = buf.readInt();
        ObjectArrayList objectArrayList = new ObjectArrayList(n);
        for (int i = 0; i < n; ++i) {
            objectArrayList.add((Object)BlockBulletCollision.readBuf(buf));
        }
        return objectArrayList;
    }

    public static void writeBlockBulletCollisions(@NotNull FriendlyByteBuf buf, @NotNull List<BlockBulletCollision> collisions) {
        int n = collisions.size();
        buf.writeInt(n);
        for (BlockBulletCollision blockBulletCollision : collisions) {
            blockBulletCollision.writeBuf(buf);
        }
    }

    @NotNull
    public static ObjectList<LivingBulletCollision> readLivingBulletCollisions(@NotNull FriendlyByteBuf buf) {
        int n = buf.readInt();
        ObjectArrayList objectArrayList = new ObjectArrayList(n);
        for (int i = 0; i < n; ++i) {
            objectArrayList.add((Object)LivingBulletCollision.readBuf(buf));
        }
        return objectArrayList;
    }

    public static void writeLivingBulletCollisions(@NotNull FriendlyByteBuf buf, @NotNull List<LivingBulletCollision> collisions) {
        int n = collisions.size();
        buf.writeInt(n);
        for (LivingBulletCollision livingBulletCollision : collisions) {
            livingBulletCollision.writeBuf(buf);
        }
    }
}

