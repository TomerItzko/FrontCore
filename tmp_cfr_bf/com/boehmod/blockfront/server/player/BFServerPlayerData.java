/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.player;

import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.ac.bullet.SuspiciousShotManager;
import com.boehmod.blockfront.server.match.ServerSequenceManager;
import com.boehmod.blockfront.server.player.PlayerAfkTracker;
import com.boehmod.blockfront.server.player.ServerFlamethrowerIgniteManager;
import com.boehmod.blockfront.unnamed.BF_1199;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BFServerPlayerData
extends BFAbstractPlayerData<BFServerManager, ServerLevel, ServerPlayer, ServerFlamethrowerIgniteManager> {
    @NotNull
    private final ServerSequenceManager sequenceManager = new ServerSequenceManager();
    @NotNull
    private final PlayerAfkTracker afkTracker = new PlayerAfkTracker(this);
    @NotNull
    private final SuspiciousShotManager suspiciousShotManager = new SuspiciousShotManager();
    @NotNull
    private final BF_1199 field_6947 = new BF_1199();

    public BFServerPlayerData(@NotNull UUID uUID) {
        super(uUID);
    }

    @NotNull
    public ServerSequenceManager getSequenceManager() {
        return this.sequenceManager;
    }

    @NotNull
    public PlayerAfkTracker getAfkTracker() {
        return this.afkTracker;
    }

    @NotNull
    public SuspiciousShotManager getSuspiciousShotManager() {
        return this.suspiciousShotManager;
    }

    @NotNull
    public BF_1199 method_5892() {
        return this.field_6947;
    }

    @Override
    @NotNull
    public ServerFlamethrowerIgniteManager method_836() {
        return new ServerFlamethrowerIgniteManager(this);
    }

    @Override
    public void update(@NotNull BFServerManager bFServerManager, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @Nullable AbstractGame<?, ?, ?> abstractGame) {
        super.update(bFServerManager, serverLevel, serverPlayer, abstractGame);
        this.afkTracker.update(serverPlayer, abstractGame);
        this.suspiciousShotManager.update(bFServerManager, serverPlayer);
        this.sequenceManager.onUpdate();
        ItemStack itemStack = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        this.field_6947.update(itemStack);
    }
}

