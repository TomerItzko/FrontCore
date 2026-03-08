/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.common.item.IModifyPlayerModel;
import com.boehmod.blockfront.common.item.base.IHasCrosshair;
import com.boehmod.blockfront.common.net.packet.BFRadioCommandPacket;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameEventType;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_905;
import com.boehmod.blockfront.unnamed.BF_909;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.PacketUtils;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public final class RadioItem
extends BF_909
implements IHasCrosshair,
IModifyPlayerModel {
    @NotNull
    private static final String field_3962 = "radio_controller";
    private static final double field_3960 = 128.0;
    @NotNull
    private static final Vec3 field_3959 = new Vec3(-10.0, 5.0, 0.0);
    @NotNull
    public static GameEventType field_3961 = GameEventType.AIR_STRIKE;
    public static float field_3966;
    public static float field_3967;
    private boolean field_3963 = false;
    private boolean field_3964 = false;

    public RadioItem(@NotNull String string, @NotNull Item.Properties properties) {
        super(string, field_3962, properties);
    }

    private void method_4076(@NotNull Entity entity, @NotNull BlockPos blockPos) {
        this.method_3782("call");
        float f = 0.9f + ThreadLocalRandom.current().nextFloat() * 0.2f;
        entity.playSound((SoundEvent)BFSounds.ITEM_RADIO_CALL.get(), 1.0f, f);
        PacketUtils.sendToServer(new BFRadioCommandPacket(field_3961, blockPos));
    }

    private void method_4077() {
        if (field_3961.ordinal() >= GameEventType.values().length) {
            field_3961 = GameEventType.values()[0];
            return;
        }
        field_3961 = GameEventType.values()[(field_3961.ordinal() + 1) % GameEventType.values().length];
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int n, boolean bl) {
        Object object;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        if (entity instanceof Player) {
            object = (Player)entity;
            if (bl) {
                ((Player)object).swinging = false;
            }
        }
        if ((object = bFClientManager.getGame()) == null) {
            return;
        }
        if (bl) {
            BlockHitResult blockHitResult;
            HitResult hitResult = BF_905.method_3753(entity, 128.0, 0.0f);
            if (hitResult.getType() == HitResult.Type.MISS) {
                return;
            }
            Minecraft minecraft = Minecraft.getInstance();
            BlockPos blockPos = null;
            Direction direction = null;
            if (hitResult instanceof BlockHitResult) {
                blockHitResult = (BlockHitResult)hitResult;
                blockPos = blockHitResult.getBlockPos();
                direction = blockHitResult.getDirection();
            }
            blockHitResult = hitResult.getLocation();
            if (direction != null && minecraft.level != null) {
                ClientUtils.spawnParticleDirection(minecraft, bFClientManager, minecraft.level, (Vec3)blockHitResult, direction, (ParticleOptions)BFParticleTypes.POOF_PARTICLE.get(), 0.15f, 0.25f);
            }
            if (minecraft.mouseHandler.isRightPressed()) {
                if (!this.field_3963) {
                    this.field_3963 = true;
                    this.method_4077();
                    minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value()), (float)1.5f, (float)1.0f));
                }
            } else {
                this.field_3963 = false;
            }
            if (minecraft.mouseHandler.isLeftPressed()) {
                if (!this.field_3964) {
                    this.field_3964 = true;
                    if (((AbstractGame)object).getStatus() == GameStatus.GAME) {
                        GameTeam gameTeam = ((AbstractGamePlayerManager)((AbstractGame)object).getPlayerManager()).getPlayerTeam(minecraft.player.getUUID());
                        int n2 = 0;
                        if (gameTeam != null) {
                            n2 = gameTeam.getObjectInt(BFStats.RADIO_DELAY, 0);
                        }
                        if (!(blockPos == null || field_3961.hasTimeLimit() && n2 > 0)) {
                            this.method_4076(entity, blockPos);
                        }
                    }
                }
            } else {
                this.field_3964 = false;
            }
        }
        super.inventoryTick(itemStack, level, entity, n, bl);
    }

    @Override
    @NotNull
    public Vec3 method_3778() {
        return field_3959;
    }

    public boolean shouldCauseReequipAnimation(@NotNull ItemStack itemStack, @NotNull ItemStack itemStack2, boolean bl) {
        return true;
    }

    @Override
    public void modifyPlayerModel(@NotNull Minecraft minecraft, boolean bl, @NotNull PlayerModel<?> model, @NotNull BFClientPlayerData playerData, @NotNull AbstractClientPlayer player, float f, float f2) {
        model.leftArm.xRot = model.leftSleeve.xRot = -1.5f + model.head.xRot;
        model.leftArm.yRot = model.leftSleeve.yRot = model.head.yRot;
        model.rightArm.xRot = model.rightSleeve.xRot = model.head.xRot - 1.0f;
        model.rightSleeve.yRot = -0.3f;
        model.rightArm.yRot = -0.3f;
        model.rightSleeve.zRot = 0.2f;
        model.rightArm.zRot = 0.2f;
    }

    static {
        field_3967 = 0.0f;
    }
}

