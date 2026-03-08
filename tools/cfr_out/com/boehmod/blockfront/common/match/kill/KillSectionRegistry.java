/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.bytes.Byte2ObjectMap
 *  it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.objects.Object2ByteMap
 *  it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap
 *  javax.annotation.Nullable
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.match.kill;

import com.boehmod.blockfront.common.match.kill.KillSectionAttribute;
import com.boehmod.blockfront.common.match.kill.KillSectionDistance;
import com.boehmod.blockfront.common.match.kill.KillSectionPlayer;
import com.boehmod.blockfront.common.match.kill.KillSectionWeapon;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public class KillSectionRegistry {
    @NotNull
    private static final Byte2ObjectMap<Class<?>> ID_TO_CLASS = new Byte2ObjectOpenHashMap();
    @NotNull
    private static final Object2ByteMap<Class<?>> CLASS_TO_ID = new Object2ByteOpenHashMap();

    public static void registerSection(byte id, @NotNull Class<?> clazz) {
        ID_TO_CLASS.put(id, clazz);
        CLASS_TO_ID.put(clazz, id);
    }

    @Nullable
    public static Class<?> toClass(byte id) {
        if (!ID_TO_CLASS.containsKey(id)) {
            throw new IllegalArgumentException("Invalid kill-feed section type byte: " + id);
        }
        return (Class)ID_TO_CLASS.get(id);
    }

    public static byte toId(@NotNull Class<?> clazz) {
        if (!CLASS_TO_ID.containsKey(clazz)) {
            throw new IllegalArgumentException("Invalid kill-feed section type class: " + String.valueOf(clazz));
        }
        return CLASS_TO_ID.getByte(clazz);
    }

    static {
        KillSectionRegistry.registerSection((byte)1, KillSectionPlayer.class);
        KillSectionRegistry.registerSection((byte)2, KillSectionWeapon.class);
        KillSectionRegistry.registerSection((byte)3, KillSectionAttribute.class);
        KillSectionRegistry.registerSection((byte)4, KillSectionDistance.class);
    }
}

