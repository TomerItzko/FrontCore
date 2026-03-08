/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.common.item.base.IHasCrosshair;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class BinocularsItem
extends SpyglassItem
implements IHasCrosshair {
    public BinocularsItem(@NotNull Item.Properties properties) {
        super(properties);
    }

    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        player.playSound((SoundEvent)BFSounds.ITEM_BINOCULARS_USE.get(), 1.0f, 1.0f);
        return ItemUtils.startUsingInstantly((Level)level, (Player)player, (InteractionHand)interactionHand);
    }

    private void method_3722(@NotNull LivingEntity livingEntity) {
        livingEntity.playSound((SoundEvent)BFSounds.ITEM_BINOCULARS_STOP_USING.get(), 1.0f, 1.0f);
    }

    @NotNull
    public ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        this.method_3722(livingEntity);
        return itemStack;
    }

    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int n) {
        this.method_3722(livingEntity);
    }

    public static final class ClientExtension
    implements IClientItemExtensions {
        @NotNull
        private static final ResourceLocation SCOPE_TEXTURE = BFRes.loc("textures/misc/binoculars_scope.png");

        @NotNull
        public ResourceLocation getScopeOverlayTexture(@NotNull ItemStack itemStack) {
            return SCOPE_TEXTURE;
        }
    }
}

