/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.jetbrains.annotations.NotNull;

public class RandomUtils {
    @NotNull
    public static <T> T randomFromSet(@NotNull Set<T> set) {
        if (set.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }
        return (T)set.toArray()[ThreadLocalRandom.current().nextInt(set.size())];
    }

    @NotNull
    public static <T> T randomFromList(@NotNull List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    @NotNull
    public static <T> T randomFromList(@NotNull List<T> list, long seed) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        threadLocalRandom.setSeed(seed);
        return list.get(threadLocalRandom.nextInt(list.size()));
    }
}

