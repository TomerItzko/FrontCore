/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.Font
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import org.jetbrains.annotations.NotNull;

public final class FormatUtils {
    @NotNull
    public static String joinWithSpaces(@NotNull List<String> strings) {
        return strings.stream().map(string -> " " + string.trim()).collect(Collectors.joining()).trim();
    }

    @NotNull
    public static ObjectList<String> parseMarkup(Font font, @NotNull String text, int maxWidth) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        String string = FormatUtils.convertFormatting(text);
        String[] stringArray = string.split(" ");
        Object object = "";
        for (String string2 : stringArray) {
            if (string2.equals("[br]")) {
                if (font.width((String)object) > 0) {
                    objectArrayList.add(object);
                }
                object = "";
                continue;
            }
            if (string2.equals("[nl]") || string2.equals("++")) {
                if (font.width((String)object) > 0) {
                    objectArrayList.add(object);
                }
                objectArrayList.add((Object)"");
                object = "";
                continue;
            }
            if (font.width((String)object) + font.width(string2) <= maxWidth) {
                object = (String)object + string2 + " ";
                continue;
            }
            objectArrayList.add(object);
            object = string2 + " ";
        }
        if (font.width((String)object) > 0) {
            objectArrayList.add(object);
        }
        return objectArrayList;
    }

    @NotNull
    public static String convertFormatting(@NotNull String text) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = false;
        int n = text.length();
        for (int i = 0; i < n; ++i) {
            char c2 = text.charAt(i);
            if (bl) {
                bl = false;
                continue;
            }
            if (c2 == '&' && i != text.length() - 1) {
                char c3 = text.charAt(i + 1);
                boolean bl2 = false;
                for (ChatFormatting chatFormatting : ChatFormatting.values()) {
                    if (chatFormatting.getChar() != c3) continue;
                    stringBuilder.append(chatFormatting);
                    bl = true;
                    bl2 = true;
                    break;
                }
                if (bl2) continue;
            }
            stringBuilder.append(c2);
        }
        return stringBuilder.toString();
    }

    @NotNull
    public static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortEntriesDescending(Map<K, V> map) {
        ObjectArrayList objectArrayList = new ObjectArrayList(map.entrySet());
        objectArrayList.sort((entry, entry2) -> ((Comparable)entry2.getValue()).compareTo(entry.getValue()));
        return objectArrayList;
    }

    @NotNull
    public static String proseStrings(List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return "";
        }
        if (strings.size() == 1) {
            return strings.getFirst();
        }
        if (strings.size() == 2) {
            return strings.get(0) + " and " + strings.get(1);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.size() - 2; ++i) {
            stringBuilder.append(strings.get(i)).append(", ");
        }
        stringBuilder.append(strings.get(strings.size() - 2)).append(" and ").append(strings.getLast());
        return stringBuilder.toString();
    }
}

