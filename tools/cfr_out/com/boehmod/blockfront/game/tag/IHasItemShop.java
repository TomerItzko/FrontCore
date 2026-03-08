/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.tag;

import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.game.GameShopItem;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public interface IHasItemShop {
    public List<GameShopItem> getShopItems(@NotNull UUID var1);

    public BFStat getPointsStat();

    public MutableComponent method_3414();
}

