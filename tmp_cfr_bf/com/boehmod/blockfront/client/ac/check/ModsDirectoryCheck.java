/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.ac.check;

import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.ac.check.IAntiCheatCheck;
import java.nio.file.Path;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

public final class ModsDirectoryCheck
implements IAntiCheatCheck<BFClientAntiCheat> {
    public static final int field_4322 = 1;

    @Override
    public boolean run(@NotNull Minecraft minecraft, @NotNull BFClientAntiCheat bFClientAntiCheat) {
        Path path = minecraft.gameDirectory.toPath().resolve(FMLPaths.MODSDIR.relative()).toAbsolutePath().normalize();
        int n = (int)ModList.get().getMods().stream().map(iModInfo -> iModInfo.getOwningFile().getFile().getFilePath()).filter(path2 -> path2.startsWith(path)).count();
        return n > 1;
    }

    @Override
    @NotNull
    public BFClientAntiCheat.Stage getStage() {
        return BFClientAntiCheat.Stage.STARTUP;
    }
}

