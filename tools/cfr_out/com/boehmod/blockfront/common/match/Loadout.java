/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.ShieldItem
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.common.match;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.common.item.BFUtilityItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.MeleeItem;
import com.boehmod.blockfront.util.RegistryUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.lang.runtime.SwitchBootstraps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Loadout {
    public static final int field_3256 = 4;
    @NotNull
    private final List<ItemStack> extra = new ObjectArrayList();
    @Nullable
    private ItemStack primary;
    @Nullable
    private ItemStack secondary;
    private ItemStack melee;
    private ItemStack offHand;
    @Nullable
    private ItemStack head;
    @Nullable
    private ItemStack chest;
    @Nullable
    private ItemStack legs;
    @Nullable
    private ItemStack feet;
    private int field_3255 = 0;
    private int minimumXp = 0;

    public Loadout() {
    }

    public Loadout(@Nullable ItemStack primary, @Nullable ItemStack secondary, ItemStack melee, ItemStack offHand, @Nullable ItemStack head, @Nullable ItemStack chest, @Nullable ItemStack legs, @Nullable ItemStack feet) {
        this.primary = primary;
        this.secondary = secondary;
        this.melee = melee;
        this.offHand = offHand;
        this.head = head;
        this.chest = chest;
        this.legs = legs;
        this.feet = feet;
    }

    public Loadout(@Nullable ItemStack primary, @Nullable ItemStack secondary, @Nullable ItemStack melee) {
        this(primary, secondary, melee, null, null, null, null, null);
    }

    public static Loadout createFromInventory(@NotNull ServerPlayer player) {
        Loadout loadout = new Loadout();
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack == null || itemStack.isEmpty()) continue;
            ItemStack itemStack2 = itemStack.copy();
            loadout.addAutoSlot(itemStack2);
        }
        return loadout;
    }

    @Nullable
    public static Loadout readFDS(@NotNull String tagName, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = root.getTagCompound(tagName);
        if (fDSTagCompound != null) {
            Loadout loadout = new Loadout();
            int n = fDSTagCompound.getInteger("size");
            for (int i = 0; i < n; ++i) {
                String string;
                if (!fDSTagCompound.hasTag("stack" + i) || (string = fDSTagCompound.getString("stack" + i)) == null) continue;
                Supplier<Item> supplier = RegistryUtils.retrieveItem(string);
                ItemStack itemStack = new ItemStack((ItemLike)supplier.get());
                loadout.addAutoSlot(itemStack);
            }
            return loadout;
        }
        return null;
    }

    public Loadout addExtra(@NotNull ItemStack itemStack) {
        this.extra.add(itemStack);
        return this;
    }

    public Loadout addExtra(@NotNull Collection<ItemStack> itemStacks) {
        this.extra.addAll(itemStacks);
        return this;
    }

    public Loadout setChest(@NotNull ItemStack itemStack) {
        this.chest = itemStack;
        return this;
    }

    public void writeFDS(@NotNull String tagName, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound(tagName);
        List<ItemStack> list = this.getItemStackList();
        int n = list.size();
        fDSTagCompound.setInteger("size", n);
        for (int i = 0; i < n; ++i) {
            ItemStack itemStack = list.get(i);
            ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey((Object)itemStack.getItem());
            fDSTagCompound.setString("stack" + i, resourceLocation.toString());
        }
        root.setTagCompound(tagName, fDSTagCompound);
    }

    public void addAutoSlot(@NotNull ItemStack itemStack) {
        Item item = itemStack.getItem();
        Objects.requireNonNull(item);
        Item item2 = item;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{GunItem.class, MeleeItem.class, ShieldItem.class, ArmorItem.class, BFUtilityItem.class}, (Object)item2, n)) {
            case 0: {
                GunItem gunItem = (GunItem)item2;
                if (gunItem.isSecondary()) {
                    this.primary = itemStack;
                    break;
                }
                this.secondary = itemStack;
                break;
            }
            case 1: {
                MeleeItem meleeItem = (MeleeItem)item2;
                this.melee = itemStack;
                break;
            }
            case 2: {
                ShieldItem shieldItem = (ShieldItem)item2;
                this.offHand = itemStack;
                break;
            }
            case 3: {
                ArmorItem armorItem = (ArmorItem)item2;
                EquipmentSlot equipmentSlot = armorItem.getEquipmentSlot(itemStack);
                if (equipmentSlot != null) {
                    switch (equipmentSlot) {
                        case HEAD: {
                            this.head = itemStack;
                            break;
                        }
                        case CHEST: {
                            this.chest = itemStack;
                            break;
                        }
                        case LEGS: {
                            this.legs = itemStack;
                            break;
                        }
                        case FEET: {
                            this.feet = itemStack;
                        }
                    }
                }
                break;
            }
            case 4: {
                BFUtilityItem bFUtilityItem = (BFUtilityItem)item2;
                this.extra.add(itemStack);
                if (this.extra.size() <= 4) break;
                this.extra.removeFirst();
                break;
            }
        }
    }

    public int method_3160() {
        return this.field_3255;
    }

    public Loadout method_3154(int n) {
        this.field_3255 = n;
        return this;
    }

    public int getMinimumXp() {
        return this.minimumXp;
    }

    public Loadout setMinimumXp(int minimumXp) {
        this.minimumXp = minimumXp;
        return this;
    }

    public boolean isInitialized() {
        return !this.getItemStackList().isEmpty();
    }

    @NotNull
    public List<ItemStack> getExtra() {
        return Collections.unmodifiableList(this.extra);
    }

    public ItemStack getPrimary() {
        return this.primary != null ? this.primary : ItemStack.EMPTY;
    }

    public ItemStack getSecondary() {
        return this.secondary != null ? this.secondary : ItemStack.EMPTY;
    }

    public ItemStack getMelee() {
        return this.melee != null ? this.melee : ItemStack.EMPTY;
    }

    public ItemStack getOffHand() {
        return this.offHand != null ? this.offHand : ItemStack.EMPTY;
    }

    public ItemStack getHead() {
        return this.head != null ? this.head : ItemStack.EMPTY;
    }

    public ItemStack getChest() {
        return this.chest != null ? this.chest : ItemStack.EMPTY;
    }

    public ItemStack getLegs() {
        return this.legs != null ? this.legs : ItemStack.EMPTY;
    }

    public ItemStack getFeet() {
        return this.feet != null ? this.feet : ItemStack.EMPTY;
    }

    public List<ItemStack> getItemStackList() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        if (this.primary != null) {
            objectArrayList.add(this.primary);
        }
        if (this.secondary != null) {
            objectArrayList.add(this.secondary);
        }
        if (this.melee != null) {
            objectArrayList.add(this.melee);
        }
        if (this.offHand != null) {
            objectArrayList.add(this.offHand);
        }
        if (this.head != null) {
            objectArrayList.add(this.head);
        }
        if (this.chest != null) {
            objectArrayList.add(this.chest);
        }
        if (this.legs != null) {
            objectArrayList.add(this.legs);
        }
        if (this.feet != null) {
            objectArrayList.add(this.feet);
        }
        if (!this.extra.isEmpty()) {
            objectArrayList.addAll(this.extra);
        }
        return objectArrayList;
    }

    public void addFromInventory(@NotNull Player player) {
        for (ItemStack itemStack : player.getInventory().items) {
            this.addAutoSlot(itemStack.copy());
        }
    }

    public Loadout organize() {
        Loadout loadout = new Loadout();
        for (ItemStack itemStack : this.getItemStackList()) {
            loadout.addAutoSlot(itemStack.copy());
        }
        return loadout;
    }
}

