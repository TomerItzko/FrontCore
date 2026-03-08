/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.assets;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import net.minecraft.commands.CommandSource;
import org.jetbrains.annotations.NotNull;

public interface IAsset {
    public void readFDS(@NotNull FDSTagCompound var1);

    public void writeFDS(@NotNull FDSTagCompound var1);

    @NotNull
    public AssetCommandBuilder getCommand();

    public void sendErrorMessages(@NotNull CommandSource var1);
}

