/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.item.GrenadeFragItem;
import com.boehmod.blockfront.common.item.IModifyPlayerModel;
import com.boehmod.blockfront.registry.BFEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class AmmoCrateItem
extends GrenadeFragItem
implements IModifyPlayerModel {
    public AmmoCrateItem(@NotNull String string, Item.Properties properties) {
        super(string, properties);
    }

    @Override
    protected BFProjectileEntity method_4090(@NotNull Level level) {
        return (BFProjectileEntity)((EntityType)BFEntityTypes.AMMO_BOX.get()).create(level);
    }

    @Override
    @NotNull
    public GrenadeFragItem.Type method_4087() {
        return GrenadeFragItem.Type.AMMO_BOX;
    }

    @Override
    public boolean method_4093() {
        return false;
    }

    @Override
    public void modifyPlayerModel(@NotNull Minecraft minecraft, boolean bl, @NotNull PlayerModel<?> model, @NotNull BFClientPlayerData playerData, @NotNull AbstractClientPlayer player, float f, float f2) {
        model.leftSleeve.xRot = -0.75f;
        model.leftArm.xRot = -0.75f;
        model.rightSleeve.xRot = -0.75f;
        model.rightArm.xRot = -0.75f;
        model.leftSleeve.yRot = 0.0f;
        model.leftArm.yRot = 0.0f;
        model.rightSleeve.yRot = 0.0f;
        model.rightArm.yRot = 0.0f;
        model.leftSleeve.zRot = 0.0f;
        model.leftArm.zRot = 0.0f;
        model.rightSleeve.zRot = 0.0f;
        model.rightArm.zRot = 0.0f;
    }
}

