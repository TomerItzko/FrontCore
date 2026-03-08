/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.server.PacketPlayerDataIncreaseValue
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.stat;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.server.PacketPlayerDataIncreaseValue;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class BFStat {
    @NotNull
    private final String key;
    private boolean field_3787;
    @NotNull
    private Trigger trigger = (manager, stat, game, uuid, change, newValue) -> ((AbstractConnectionManager)manager.getConnectionManager()).sendPacket((IPacket)new PacketPlayerDataIncreaseValue(uuid, this.getKey(), change));

    public BFStat(@NotNull String key) {
        this.key = key;
    }

    public BFStat setTrigger(@NotNull Trigger trigger) {
        this.trigger = trigger;
        return this;
    }

    public void trigger(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull AbstractGame<?, ?, ?> game, @NotNull UUID uuid, int change, int newValue) {
        this.trigger.onGlobalStat(manager, this, game, uuid, change, newValue);
    }

    @NotNull
    public String getKey() {
        return this.key;
    }

    public boolean method_3923() {
        return this.field_3787;
    }

    @NotNull
    public BFStat method_3922(boolean bl) {
        this.field_3787 = bl;
        return this;
    }

    public static interface Trigger {
        public void onGlobalStat(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull BFStat var2, @NotNull AbstractGame<?, ?, ?> var3, @NotNull UUID var4, int var5, int var6);
    }
}

