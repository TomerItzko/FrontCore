/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.item.base.IHasCrosshair;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_909;
import com.boehmod.blockfront.util.math.ShakeNodeData;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

public final class MeleeItem
extends BF_909
implements IHasCrosshair,
SingletonGeoAnimatable {
    @NotNull
    private static final String field_3620 = "melee_controller";
    @NotNull
    private static final List<DeferredHolder<SoundEvent, SoundEvent>> field_3621 = new ObjectArrayList();
    private static final float field_3622 = 9.0f;
    private static final float field_3623 = -1.4f;
    public static final Vec3 field_3617 = new Vec3(-10.0, 5.0, 0.0);
    private final ItemAttributeModifiers field_3618;
    private ShakeNodeData field_3616 = ShakeNodePresets.field_1931;
    private DeferredHolder<SoundEvent, SoundEvent> field_3619 = BFSounds.ITEM_KNIFE_SWING;
    private int field_3624 = 0;
    private int field_3625 = 0;
    private int field_3626 = 0;

    public MeleeItem(@NotNull String string, @NotNull Item.Properties properties) {
        super(string, field_3620, properties);
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 9.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)-1.4f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        this.field_3618 = builder.build();
    }

    public int method_3749() {
        if (this.field_3625 <= 0) {
            throw new IllegalArgumentException("Tricks count must be greater than 0");
        }
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        return threadLocalRandom.nextInt(this.field_3625);
    }

    @NotNull
    public MeleeItem method_3745(int n) {
        this.field_3625 = n;
        return this;
    }

    @NotNull
    public MeleeItem method_3748(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        this.field_3619 = deferredHolder;
        return this;
    }

    @NotNull
    public MeleeItem method_3746(ShakeNodeData shakeNodeData) {
        this.field_3616 = shakeNodeData;
        return this;
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_3750(@NotNull Minecraft minecraft) {
        if (this.field_3624 <= 0) {
            this.field_3624 = 5;
            this.field_3626 = 0;
            this.method_3782("attack");
            ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)this.field_3619.get()), (float)1.0f, (float)(0.8f + 0.2f * threadLocalRandom.nextFloat())));
            ShakeManager.applyShake(this.field_3616);
            this.field_3634 += 0.3f;
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_3747(@NotNull Minecraft minecraft, @NotNull Random random) {
        if (random.nextFloat() > 0.25f) {
            return;
        }
        int n = this.method_3749();
        this.method_3782("trick" + n);
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = field_3621.get(n);
        minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)1.0f, (float)1.0f));
    }

    @Override
    @NotNull
    public Vec3 method_3778() {
        return field_3617;
    }

    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        return new InteractionResultHolder(InteractionResult.FAIL, (Object)player.getItemInHand(interactionHand));
    }

    public boolean canAttackBlock(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player) {
        return false;
    }

    @NotNull
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return this.field_3618;
    }

    public boolean shouldCauseReequipAnimation(@NotNull ItemStack itemStack, @NotNull ItemStack itemStack2, boolean bl) {
        return true;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void inventoryTick(ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int n, boolean bl) {
        super.inventoryTick(itemStack, level, entity, n, bl);
        Minecraft minecraft = Minecraft.getInstance();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        if (entity != minecraft.player) {
            return;
        }
        if (level.isClientSide()) {
            boolean bl2 = minecraft.player.getVehicle() instanceof AbstractVehicleEntity;
            if (this.field_3624 > 0) {
                --this.field_3624;
            }
            if (bl && !bl2 && minecraft.player.swinging) {
                minecraft.player.swinging = false;
                this.method_3750(minecraft);
            }
            if (this.field_3626++ >= 1200 && bl) {
                this.field_3626 = 0;
                if (this.field_3625 > 0) {
                    this.method_3747(minecraft, threadLocalRandom);
                }
            }
        }
    }

    static {
        field_3621.add(BFSounds.ITEM_KNIFE_TRICK_0);
    }
}

