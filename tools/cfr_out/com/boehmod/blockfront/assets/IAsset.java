/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  net.minecraft.commands.CommandSource
 *  org.jetbrains.annotations.NotNull
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

