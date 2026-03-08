/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.chars.Char2ObjectMap
 *  it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public final class StringUtils {
    @NotNull
    private static final NumberFormat FORMAT = NumberFormat.getInstance(Locale.ROOT);
    @NotNull
    private static final Char2ObjectMap<String> FANCY_CHARACTERS = new Char2ObjectOpenHashMap();

    @NotNull
    public static String formatLong(long value) {
        return FORMAT.format(value);
    }

    @NotNull
    public static String abbreviate(@NotNull String str, int max) {
        return StringUtils.abbreviate(str, 0, max);
    }

    @NotNull
    public static String abbreviate(@NotNull String str, int min, int max) {
        int n = str.length();
        if (n <= max) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder(max);
        if (min > n) {
            stringBuilder.append("...");
        } else if (n - min < max - 3) {
            stringBuilder.append(str, n - (max - 3), n);
        } else if (min - 1 < 3) {
            stringBuilder.append(str, 0, max - 3).append("...");
        } else {
            if (max - 3 < 4) {
                throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
            }
            if (min + (max - 3) < n) {
                stringBuilder.append("...").append(StringUtils.abbreviate(str.substring(min), 0, max - 3));
            } else {
                stringBuilder.append("...").append(str.substring(n - (max - 3)));
            }
        }
        return stringBuilder.toString();
    }

    @NotNull
    public static String formatDuration(long millis) {
        if (millis < 0L) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        long l = TimeUnit.MILLISECONDS.toDays(millis);
        long l2 = TimeUnit.MILLISECONDS.toHours(millis -= TimeUnit.DAYS.toMillis(l));
        long l3 = TimeUnit.MILLISECONDS.toMinutes(millis -= TimeUnit.HOURS.toMillis(l2));
        long l4 = TimeUnit.MILLISECONDS.toSeconds(millis -= TimeUnit.MINUTES.toMillis(l3));
        return l + "d " + l2 + "h " + l3 + "m " + l4 + "s";
    }

    @NotNull
    public static String makeFancy(@NotNull String str) {
        StringBuilder stringBuilder = new StringBuilder(str.length());
        for (char c2 : str.toCharArray()) {
            stringBuilder.append((String)FANCY_CHARACTERS.getOrDefault(c2, (Object)String.valueOf(c2)));
        }
        return stringBuilder.toString();
    }

    static {
        FANCY_CHARACTERS.put('a', (Object)"\u1d00");
        FANCY_CHARACTERS.put('b', (Object)"\u0299");
        FANCY_CHARACTERS.put('c', (Object)"\u1d04");
        FANCY_CHARACTERS.put('d', (Object)"\u1d05");
        FANCY_CHARACTERS.put('e', (Object)"\u1d07");
        FANCY_CHARACTERS.put('f', (Object)"\ua730");
        FANCY_CHARACTERS.put('g', (Object)"\u0262");
        FANCY_CHARACTERS.put('h', (Object)"\u029c");
        FANCY_CHARACTERS.put('i', (Object)"\u026a");
        FANCY_CHARACTERS.put('j', (Object)"\u1d0a");
        FANCY_CHARACTERS.put('k', (Object)"\u1d0b");
        FANCY_CHARACTERS.put('l', (Object)"\u029f");
        FANCY_CHARACTERS.put('m', (Object)"\u1d0d");
        FANCY_CHARACTERS.put('n', (Object)"\u0274");
        FANCY_CHARACTERS.put('o', (Object)"\u1d0f");
        FANCY_CHARACTERS.put('p', (Object)"\u1d18");
        FANCY_CHARACTERS.put('q', (Object)"\u01eb");
        FANCY_CHARACTERS.put('r', (Object)"\u0280");
        FANCY_CHARACTERS.put('s', (Object)"s");
        FANCY_CHARACTERS.put('t', (Object)"\u1d1b");
        FANCY_CHARACTERS.put('u', (Object)"\u1d1c");
        FANCY_CHARACTERS.put('v', (Object)"\u1d20");
        FANCY_CHARACTERS.put('w', (Object)"\u1d21");
        FANCY_CHARACTERS.put('x', (Object)"x");
        FANCY_CHARACTERS.put('y', (Object)"\u028f");
        FANCY_CHARACTERS.put('z', (Object)"\u1d22");
        FANCY_CHARACTERS.put('A', (Object)"\u1d00");
        FANCY_CHARACTERS.put('B', (Object)"\u0299");
        FANCY_CHARACTERS.put('C', (Object)"\u1d04");
        FANCY_CHARACTERS.put('D', (Object)"\u1d05");
        FANCY_CHARACTERS.put('E', (Object)"\u1d07");
        FANCY_CHARACTERS.put('F', (Object)"\ua730");
        FANCY_CHARACTERS.put('G', (Object)"\u0262");
        FANCY_CHARACTERS.put('H', (Object)"\u029c");
        FANCY_CHARACTERS.put('I', (Object)"\u026a");
        FANCY_CHARACTERS.put('J', (Object)"\u1d0a");
        FANCY_CHARACTERS.put('K', (Object)"\u1d0b");
        FANCY_CHARACTERS.put('L', (Object)"\u029f");
        FANCY_CHARACTERS.put('M', (Object)"\u1d0d");
        FANCY_CHARACTERS.put('N', (Object)"\u0274");
        FANCY_CHARACTERS.put('O', (Object)"\u1d0f");
        FANCY_CHARACTERS.put('P', (Object)"\u1d18");
        FANCY_CHARACTERS.put('Q', (Object)"\u01eb");
        FANCY_CHARACTERS.put('R', (Object)"\u0280");
        FANCY_CHARACTERS.put('S', (Object)"s");
        FANCY_CHARACTERS.put('T', (Object)"\u1d1b");
        FANCY_CHARACTERS.put('U', (Object)"\u1d1c");
        FANCY_CHARACTERS.put('V', (Object)"\u1d20");
        FANCY_CHARACTERS.put('W', (Object)"\u1d21");
        FANCY_CHARACTERS.put('X', (Object)"x");
        FANCY_CHARACTERS.put('Y', (Object)"\u028f");
        FANCY_CHARACTERS.put('Z', (Object)"\u1d22");
        FANCY_CHARACTERS.put(' ', (Object)" ");
    }
}

