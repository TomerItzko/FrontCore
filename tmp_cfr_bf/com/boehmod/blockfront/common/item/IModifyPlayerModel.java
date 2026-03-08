/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.client.player.BFClientPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public interface IModifyPlayerModel {
    @OnlyIn(value=Dist.CLIENT)
    public void modifyPlayerModel(@NotNull Minecraft var1, boolean var2, @NotNull PlayerModel<?> var3, @NotNull BFClientPlayerData var4, @NotNull AbstractClientPlayer var5, float var6, float var7);
}

