/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.player;

import com.boehmod.blockfront.common.net.packet.BFEntitySoundPacket;
import com.boehmod.blockfront.common.net.packet.BFExplosionPacket;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.world.ExplosionType;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class a<P extends BFAbstractPlayerData<?, ?, ?, ?>> {
    private static final int field_1146 = 60;
    protected boolean field_1145 = false;
    private int field_1147 = 60;
    @NotNull
    private ItemStack field_1149 = ItemStack.EMPTY;
    private int field_1148 = -1;
    @NotNull
    private final P field_1144;

    public a(@NotNull P p) {
        this.field_1144 = p;
    }

    public void method_821(@NotNull ServerPlayer serverPlayer, @NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack) {
        if (this.field_1145) {
            return;
        }
        this.method_820(true);
        this.field_1147 = 60;
        this.field_1149 = itemStack.copy();
        this.field_1148 = livingEntity.getId();
        int n = serverPlayer.getId();
        PacketUtils.sendToAllPlayers(new BFEntitySoundPacket((Holder<SoundEvent>)BFSounds.ITEM_GUN_M2FLAMETHROWER_IGNITE, SoundSource.PLAYERS, n, 1.0f, 1.5f));
        PacketUtils.sendToAllPlayers(new BFEntitySoundPacket((Holder<SoundEvent>)BFSounds.ITEM_GUN_M2FLAMETHROWER_IGNITE_START, SoundSource.PLAYERS, n, 1.0f, 1.5f));
        PacketUtils.sendToAllPlayers(new BFEntitySoundPacket((Holder<SoundEvent>)BFSounds.TEAM_SHARED_IGNITE, SoundSource.PLAYERS, n, 1.0f, 1.0f));
    }

    private void method_825() {
        this.method_820(false);
        this.field_1147 = 60;
        this.field_1149 = ItemStack.EMPTY;
        this.field_1148 = -1;
    }

    public void method_822(@NotNull Level level, @NotNull ServerPlayer serverPlayer) {
        if (BFUtils.isPlayerUnavailable((Player)serverPlayer, this.field_1144)) {
            this.method_825();
            return;
        }
        if (!this.field_1145 || this.field_1147-- > 0) {
            return;
        }
        this.method_824(level, serverPlayer);
        this.method_825();
    }

    public void method_824(@NotNull Level level, @NotNull ServerPlayer serverPlayer) {
        Entity entity = this.field_1148 == -1 ? null : level.getEntity(this.field_1148);
        Vec3 vec3 = serverPlayer.position();
        Explosion explosion = BFUtils.explosionFromPlayer(level, entity, vec3, this.field_1149, 5.5f);
        explosion.explode();
        explosion.finalizeExplosion(true);
        explosion.clearToBlow();
        PacketUtils.sendToAllPlayers(new BFExplosionPacket(ExplosionType.GENERIC, vec3));
    }

    public void method_820(boolean bl) {
        if (this.field_1145 != bl) {
            this.field_1145 = bl;
            ((BFAbstractPlayerData)this.field_1144).method_846();
        }
    }

    public void method_823(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(this.field_1145);
    }

    public void method_826(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this.field_1145 = friendlyByteBuf.readBoolean();
    }
}

