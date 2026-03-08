/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.common.item.BFUtilityItem;
import com.boehmod.blockfront.common.item.IModifyPlayerModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class FlagItem
extends BFUtilityItem
implements IModifyPlayerModel {
    public FlagItem(@NotNull String string, Item.Properties properties) {
        super(string, properties);
    }

    @Override
    public void modifyPlayerModel(@NotNull Minecraft minecraft, boolean bl, @NotNull PlayerModel<?> model, @NotNull BFClientPlayerData playerData, @NotNull AbstractClientPlayer player, float f, float f2) {
        float f3 = Mth.sin((float)(f / 15.0f));
        float f4 = Mth.sin((float)(f / 12.0f));
        float f5 = f3 * f4 / 6.0f;
        model.rightArm.xRot = model.rightSleeve.xRot = -1.6f + model.head.xRot + f5;
        model.rightArm.yRot = model.rightSleeve.yRot = -0.2f + model.head.yRot;
        model.rightArm.zRot = model.rightSleeve.zRot = model.head.zRot + f5;
        model.leftArm.xRot = model.leftSleeve.xRot = -1.6f + model.head.xRot + f5;
        model.leftArm.yRot = model.leftSleeve.yRot = 0.8f + model.head.yRot;
        model.leftArm.zRot = model.leftSleeve.zRot = model.head.zRot + f5;
    }
}

