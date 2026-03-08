/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.item.BFUtilityItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.IModifyPlayerModel;
import com.boehmod.blockfront.common.item.base.IHasCrosshair;
import com.boehmod.blockfront.common.net.packet.BFGrenadeThrowPacket;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrenadeFragItem
extends BFUtilityItem
implements IHasCrosshair,
IModifyPlayerModel {
    private static final float field_3977 = 1.4f;
    private static final float field_3978 = 0.6f;
    private boolean field_3975 = false;
    private float field_3979 = 1.1f;
    private boolean field_3976 = true;
    private float field_3980 = 0.5f;
    private float field_3981 = 5.0f;

    public GrenadeFragItem(@NotNull String string, @NotNull Item.Properties properties) {
        super(string, properties);
    }

    public boolean method_4094() {
        return this.field_3976;
    }

    public GrenadeFragItem method_4091(boolean bl) {
        this.field_3976 = bl;
        return this;
    }

    public float method_4085() {
        return this.field_3980;
    }

    public GrenadeFragItem method_4088(float f) {
        this.field_3980 = f;
        return this;
    }

    public float method_4086() {
        return this.field_3981;
    }

    public GrenadeFragItem method_4092(float f) {
        this.field_3981 = f;
        return this;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int n, boolean bl) {
        super.inventoryTick(itemStack, level, entity, n, bl);
        if (!bl || !(entity instanceof Player)) {
            return;
        }
        Player player = (Player)entity;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null) {
            return;
        }
        int n2 = Math.round(player.zza);
        float f = (float)n2 * 0.13f;
        if (player.isSprinting()) {
            f += 0.13f;
        }
        boolean bl2 = minecraft.mouseHandler.isLeftPressed();
        boolean bl3 = minecraft.mouseHandler.isRightPressed();
        if (bl2) {
            this.field_3979 = 1.4f;
            this.field_3975 = true;
        } else if (bl3) {
            this.field_3979 = 0.6f;
            this.field_3975 = true;
        }
        this.field_3979 += f;
        if (!bl3 && !bl2 && this.field_3975) {
            Vec3 vec3;
            double d;
            boolean bl4;
            boolean bl5 = bl4 = GunItem.field_4059 <= 0;
            if (minecraft.hitResult != null && (d = (vec3 = minecraft.hitResult.getLocation()).distanceTo(minecraft.player.getEyePosition())) <= 0.8) {
                bl4 = false;
            }
            if (bl4) {
                GunItem.field_4059 = 10;
                PacketUtils.sendToServer(new BFGrenadeThrowPacket(this.field_3979));
            }
            this.field_3975 = false;
        }
    }

    public void method_4089(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ItemStack itemStack, @NotNull Level level, @NotNull ServerPlayer serverPlayer, float f) {
        BFProjectileEntity bFProjectileEntity;
        Object obj = playerDataHandler.getPlayerData((Player)serverPlayer);
        if (((BFAbstractPlayerData)obj).method_849() || BFUtils.isPlayerUnavailable((Player)serverPlayer, obj)) {
            return;
        }
        Vec3 vec3 = serverPlayer.position();
        float f2 = (float)((double)0.9f + (double)0.2f * Math.random());
        level.playSound(null, vec3.x, vec3.y, vec3.z, this.method_4095(), SoundSource.PLAYERS, 1.0f, f2);
        if (!serverPlayer.isCreative()) {
            itemStack.shrink(1);
        }
        if ((bFProjectileEntity = this.method_4090(level)) != null) {
            bFProjectileEntity.method_1951(new ItemStack((ItemLike)this));
            bFProjectileEntity.method_1949(serverPlayer, f, new ItemStack((ItemLike)this));
            level.addFreshEntity((Entity)bFProjectileEntity);
        }
    }

    @NotNull
    public SoundEvent method_4095() {
        return (SoundEvent)BFSounds.ITEM_GRENADE_THROW.get();
    }

    @NotNull
    public SoundEvent method_4096() {
        return (SoundEvent)BFSounds.ITEM_GRENADE_EQUIP.get();
    }

    @Nullable
    protected BFProjectileEntity method_4090(@NotNull Level level) {
        return (BFProjectileEntity)((EntityType)BFEntityTypes.GRENADE_FRAG.get()).create(level);
    }

    @NotNull
    public Type method_4087() {
        return Type.FRAG;
    }

    public boolean method_4093() {
        return true;
    }

    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        return new InteractionResultHolder(InteractionResult.FAIL, (Object)player.getItemInHand(interactionHand));
    }

    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity, @NotNull LivingEntity livingEntity2) {
        return false;
    }

    public boolean onLeftClickEntity(@NotNull ItemStack itemStack, @NotNull Player player, @NotNull Entity entity) {
        return false;
    }

    public boolean canAttackBlock(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player) {
        return false;
    }

    @Override
    public void modifyPlayerModel(@NotNull Minecraft minecraft, boolean bl, @NotNull PlayerModel<?> model, @NotNull BFClientPlayerData playerData, @NotNull AbstractClientPlayer player, float f, float f2) {
        if (!this.method_4093()) {
            return;
        }
        model.leftArm.xRot = -1.6f + model.head.xRot;
        model.leftArm.yRot = model.head.yRot;
        model.rightArm.xRot += -1.6f + model.head.xRot;
        model.rightArm.yRot = model.head.yRot;
        model.rightArm.zRot = -0.2f;
    }

    public static enum Type {
        FRAG,
        SMOKE,
        FIRE,
        DECOY,
        FLASH,
        MEDICAL_BAG,
        AMMO_BOX,
        LANDMINE;

    }
}

