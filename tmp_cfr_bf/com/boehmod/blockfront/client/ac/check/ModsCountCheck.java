/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.ac.check;

import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.ac.check.IAntiCheatCheck;
import java.util.Collections;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;

public final class ModsCountCheck
implements IAntiCheatCheck<BFClientAntiCheat> {
    public static final int targetModCount = 4;

    @Override
    public boolean run(@NotNull Minecraft minecraft, @NotNull BFClientAntiCheat bFClientAntiCheat) {
        return Collections.unmodifiableList(ModList.get().getMods()).size() != 4;
    }

    @Override
    @NotNull
    public BFClientAntiCheat.Stage getStage() {
        return BFClientAntiCheat.Stage.STARTUP;
    }
}

