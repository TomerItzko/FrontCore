/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.PlayerDeployableEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public final class AmmoCrateEntity
extends PlayerDeployableEntity {
    private static final Component field_2779 = Component.translatable((String)"bf.message.gamemode.ammo.box.refill");

    public AmmoCrateEntity(EntityType<? extends AmmoCrateEntity> entityType, Level level) {
        super((EntityType<? extends PlayerDeployableEntity>)entityType, level);
    }

    @Override
    protected DeferredHolder<SoundEvent, SoundEvent> method_2279(@NotNull SoundType soundType) {
        return BFSounds.ITEM_AMMO_BOX_BOUNCE;
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    @Override
    void method_2515(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerPlayer serverPlayer, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager) {
        boolean bl = false;
        for (ItemStack itemStack : serverPlayer.getInventory().items) {
            if (itemStack.isEmpty() || !BFUtils.method_2967(itemStack)) continue;
            bl = true;
        }
        if (bl) {
            Iterator iterator = Component.literal((String)String.valueOf('\ue009')).withColor(0xFFFFFF).append(" ");
            BFUtils.sendFancyMessage(serverPlayer, (Component)iterator, field_2779);
            BFUtils.playSound(serverPlayer, (SoundEvent)BFSounds.ITEM_AMMO_BOX_HEAL.get(), SoundSource.NEUTRAL);
        }
    }

    @Override
    public int method_2283() {
        return 0;
    }
}

