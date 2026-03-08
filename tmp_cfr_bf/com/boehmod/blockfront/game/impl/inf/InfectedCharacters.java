/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.blockfront.game.impl.inf.InfectedCharacter;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;

public class InfectedCharacters {
    @NotNull
    private static final Int2ObjectMap<InfectedCharacter> ENTRIES = (Int2ObjectMap)Util.make((Object)new Int2ObjectOpenHashMap(), map -> {
        map.put(0, (Object)new InfectedCharacter(0, "Herman"));
        map.put(1, (Object)new InfectedCharacter(1, "Michael"));
        map.put(2, (Object)new InfectedCharacter(2, "Monty"));
        map.put(3, (Object)new InfectedCharacter(3, "Tokarev"));
        map.put(4, (Object)new InfectedCharacter(4, "Emeryk"));
        map.put(5, (Object)new InfectedCharacter(5, "Hiroko"));
    });

    @NotNull
    public static InfectedCharacter get(int id) {
        return (InfectedCharacter)ENTRIES.get(id);
    }
}

