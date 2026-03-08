/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.ac.check;

import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.common.ac.BFAbstractAntiCheat;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public interface IAntiCheatCheck<M extends BFAbstractAntiCheat<?, ?>> {
    public boolean run(@NotNull Minecraft var1, @NotNull M var2);

    @NotNull
    public BFClientAntiCheat.Stage getStage();
}

