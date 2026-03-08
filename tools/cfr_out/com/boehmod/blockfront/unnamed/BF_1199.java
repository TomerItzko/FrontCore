/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.gun.GunFireConfig;
import com.boehmod.blockfront.common.item.GunItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BF_1199 {
    @Nullable
    private Item field_6926 = null;
    private long field_6927 = -1L;
    private long field_6949 = -1L;
    private int field_6948 = 0;

    public void update(@NotNull ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (this.field_6926 != item) {
            this.field_6926 = item;
            this.field_6927 = -1L;
            this.field_6949 = -1L;
            this.field_6948 = 0;
        }
    }

    public boolean method_6010(@NotNull ItemStack itemStack, long l) {
        return this.method_5861(itemStack, l, true);
    }

    public boolean method_5861(@NotNull ItemStack itemStack, long l, boolean bl) {
        this.update(itemStack);
        Object object = itemStack.getItem();
        if (!(object instanceof GunItem)) {
            return true;
        }
        GunItem gunItem = (GunItem)object;
        object = gunItem.getDefaultFireConfig();
        long l2 = ((GunFireConfig)object).getFireRate();
        int n = ((GunFireConfig)object).method_4026();
        if (this.field_6949 != l) {
            this.field_6949 = l;
            this.field_6948 = 0;
        }
        if (this.field_6948 >= n) {
            return false;
        }
        if (this.field_6948 == 0) {
            long l3;
            if (this.field_6927 >= 0L && (l3 = l - this.field_6927) < l2) {
                return false;
            }
            if (bl) {
                this.field_6927 = l;
            }
        }
        ++this.field_6948;
        return true;
    }

    public long method_5863() {
        return this.field_6927;
    }

    public void method_5862() {
        this.field_6926 = null;
        this.field_6927 = -1L;
        this.field_6949 = -1L;
        this.field_6948 = 0;
    }
}

