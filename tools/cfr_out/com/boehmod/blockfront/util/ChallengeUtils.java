/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.challenge.Challenge
 *  com.boehmod.bflib.cloud.common.player.challenge.ItemKillChallenge
 *  com.boehmod.bflib.cloud.common.player.challenge.KillCountChallenge
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.item.Item
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import com.boehmod.bflib.cloud.common.player.challenge.Challenge;
import com.boehmod.bflib.cloud.common.player.challenge.ItemKillChallenge;
import com.boehmod.bflib.cloud.common.player.challenge.KillCountChallenge;
import com.boehmod.blockfront.registry.BFItems;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class ChallengeUtils {
    private static final MutableComponent MESSAGE_ERROR = Component.translatable((String)"bf.message.error");

    @NotNull
    public static Component getComponent(@NotNull Challenge challenge) {
        if (challenge instanceof ItemKillChallenge) {
            ItemKillChallenge itemKillChallenge = (ItemKillChallenge)challenge;
            Optional<Item> optional = BFItems.retrieve(itemKillChallenge.getItem());
            if (optional.isPresent()) {
                Item item = optional.get();
                MutableComponent mutableComponent = Component.literal((String)String.valueOf(itemKillChallenge.getAmountRequired()));
                MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(itemKillChallenge.getAmount()));
                MutableComponent mutableComponent3 = Component.translatable((String)item.getDescriptionId());
                return Component.translatable((String)"bf.challenge.kills.item", (Object[])new Object[]{mutableComponent, mutableComponent3, mutableComponent2, mutableComponent});
            }
        } else if (challenge instanceof KillCountChallenge) {
            KillCountChallenge killCountChallenge = (KillCountChallenge)challenge;
            MutableComponent mutableComponent = Component.literal((String)String.valueOf(killCountChallenge.getAmountRequired()));
            MutableComponent mutableComponent4 = Component.literal((String)String.valueOf(killCountChallenge.getAmount()));
            return Component.translatable((String)"bf.challenge.kills", (Object[])new Object[]{mutableComponent, mutableComponent4, mutableComponent});
        }
        return MESSAGE_ERROR;
    }
}

