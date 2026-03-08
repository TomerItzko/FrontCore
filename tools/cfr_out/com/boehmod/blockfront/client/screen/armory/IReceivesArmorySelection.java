/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.armory;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface IReceivesArmorySelection {
    public void acceptSelected(@NotNull List<CloudItemStack> var1);
}

