/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class AmmoPoint
extends FDSPose {
    public static final int field_6930 = 5;
    private int field_2959 = 0;

    public AmmoPoint() {
    }

    public AmmoPoint(double d, double d2, double d3, float f, float f2) {
        super(d, d2, d3, f, f2);
    }

    public AmmoPoint(@NotNull Player player) {
        super(player);
    }

    @Nullable
    public static AmmoPoint newFromFDS(@NotNull String tagName, @NotNull FDSTagCompound root) {
        AmmoPoint ammoPoint = new AmmoPoint();
        FDSTagCompound fDSTagCompound = root.getTagCompound(tagName);
        if (fDSTagCompound != null) {
            ammoPoint.readFromFDS(fDSTagCompound);
            return ammoPoint;
        }
        return null;
    }

    public void update(@NotNull ServerLevel level, @NotNull Set<UUID> players) {
        if (this.field_2959++ <= 5) {
            return;
        }
        this.field_2959 = 0;
        for (ServerPlayer serverPlayer : this.playersInSurroundingArea(level, 6, players)) {
            boolean bl = false;
            for (ItemStack itemStack : serverPlayer.getInventory().items) {
                Item item;
                if (itemStack.isEmpty() || !((item = itemStack.getItem()) instanceof GunItem)) continue;
                GunItem gunItem = (GunItem)item;
                if (!BFUtils.method_2967(itemStack)) continue;
                bl = true;
                item = gunItem.getName(itemStack).copy().withColor(0xFFFFFF);
                MutableComponent mutableComponent = Component.literal((String)String.valueOf('\ue009')).withColor(0xFFFFFF).append(" ");
                MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.ammopoint.refill", (Object[])new Object[]{item});
                BFUtils.sendFancyMessage(serverPlayer, (Component)mutableComponent, (Component)mutableComponent2);
            }
            if (!bl || !(serverPlayer instanceof ServerPlayer)) continue;
            Iterator iterator = serverPlayer;
            BFUtils.playSound((ServerPlayer)iterator, (SoundEvent)BFSounds.MATCH_AMMOPOINT_REFILL.get(), SoundSource.NEUTRAL);
        }
    }

    @Override
    @NotNull
    public AmmoPoint copy() {
        return new AmmoPoint(this.position.x, this.position.y, this.position.z, this.rotation.x, this.rotation.y);
    }

    @Override
    @NotNull
    public /* synthetic */ FDSPose copy() {
        return this.copy();
    }
}

