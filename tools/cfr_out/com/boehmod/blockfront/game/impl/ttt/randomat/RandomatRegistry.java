/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ttt.randomat;

import com.boehmod.blockfront.game.impl.ttt.randomat.ExplodeRandomatEvent;
import com.boehmod.blockfront.game.impl.ttt.randomat.FartRandomatEvent;
import com.boehmod.blockfront.game.impl.ttt.randomat.MelonCannonRandomatEvent;
import com.boehmod.blockfront.game.impl.ttt.randomat.NextbotRandomatEvent;
import com.boehmod.blockfront.game.impl.ttt.randomat.RandomatEvent;
import com.boehmod.blockfront.game.impl.ttt.randomat.SpeedRandomatEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class RandomatRegistry {
    public static final List<Supplier<RandomatEvent>> ENTRIES = new ObjectArrayList();

    @NotNull
    public static RandomatEvent getRandomEvent() {
        int n = ThreadLocalRandom.current().nextInt(ENTRIES.size());
        return ENTRIES.get(n).get();
    }

    static {
        ENTRIES.add(SpeedRandomatEvent::new);
        ENTRIES.add(ExplodeRandomatEvent::new);
        ENTRIES.add(MelonCannonRandomatEvent::new);
        ENTRIES.add(FartRandomatEvent::new);
        ENTRIES.add(NextbotRandomatEvent::new);
    }
}

