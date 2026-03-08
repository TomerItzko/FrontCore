/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.item.BFWeaponItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class BF_1180 {
    public static final int field_6712 = 3;

    public static void method_5651(@NotNull Level level, @NotNull Vec3 vec3, double d, @NotNull DataComponentType<Integer> dataComponentType, double d2) {
        AABB aABB = AABB.ofSize((Vec3)vec3, (double)1.0, (double)1.0, (double)1.0).inflate(d);
        for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, aABB)) {
            ItemStack itemStack = livingEntity.getMainHandItem();
            if (!(itemStack.getItem() instanceof BFWeaponItem) || !(level.random.nextDouble() < d2)) continue;
            int n = (Integer)itemStack.getOrDefault(dataComponentType, (Object)0);
            int n2 = Math.min(n + 1, 3);
            itemStack.set(dataComponentType, (Object)n2);
        }
    }

    private static void method_5650(@NotNull ItemStack itemStack, @NotNull DataComponentType<Integer> dataComponentType, int n) {
        if (n <= 0 || !(itemStack.getItem() instanceof BFWeaponItem)) {
            return;
        }
        int n2 = (Integer)itemStack.getOrDefault(dataComponentType, (Object)0);
        int n3 = Math.min(n2 + n, 3);
        itemStack.set(dataComponentType, (Object)n3);
    }
}

