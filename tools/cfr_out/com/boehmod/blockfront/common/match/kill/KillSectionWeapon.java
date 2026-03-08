/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.match.kill;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.common.item.GrenadeFragItem;
import com.boehmod.blockfront.common.match.kill.AbstractKillSection;
import com.boehmod.blockfront.common.match.kill.KillFeedEntry;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class KillSectionWeapon
extends AbstractKillSection {
    public static final float field_3316 = 1.0f;
    public static final float field_3315 = 0.6f;
    @NotNull
    private ItemStack field_3314;

    public KillSectionWeapon() {
        this(new ItemStack((ItemLike)Items.STICK));
    }

    public KillSectionWeapon(@NotNull ItemStack itemStack) {
        this.field_3314 = itemStack.copy();
        if (this.field_3314.getItem() instanceof BFWeaponItem) {
            this.field_3314.set(BFDataComponents.DISPLAY_GUN.get(), (Object)true);
        }
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull AbstractGame<?, ?, ?> game, @NotNull KillFeedEntry entry) {
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull AbstractGame<?, ?, ?> game, @NotNull KillFeedEntry entry, float delta) {
        if (this.field_3314.isEmpty()) {
            return;
        }
        int n = this.getWidth(font) / 2;
        float f = this.field_3314.getItem() instanceof GrenadeFragItem ? 0.6f : 1.0f;
        BFRendering.item(poseStack, graphics, this.field_3314, n, (float)n / 2.0f, f);
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return 24;
    }

    @Override
    public int method_3223() {
        return -16711936;
    }

    @Override
    public void read(@NotNull RegistryFriendlyByteBuf buf) {
        this.field_3314 = (ItemStack)ItemStack.STREAM_CODEC.decode((Object)buf);
    }

    @Override
    public void write(@NotNull RegistryFriendlyByteBuf buf) {
        ItemStack.STREAM_CODEC.encode((Object)buf, (Object)this.field_3314);
    }
}

