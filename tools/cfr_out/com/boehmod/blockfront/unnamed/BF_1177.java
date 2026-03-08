/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.unnamed.BF_1161;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BF_1177 {
    private final BFClientManager field_6693;
    private final ObjectList<BF_1161> field_6694 = new ObjectArrayList();

    public BF_1177(@NotNull BFClientManager bFClientManager) {
        this.field_6693 = bFClientManager;
    }

    public void method_5639(@NotNull Minecraft minecraft, @Nullable ClientLevel clientLevel) {
        if (clientLevel == null) {
            return;
        }
        this.field_6694.removeIf(bF_1161 -> !bF_1161.method_5619(minecraft, this.field_6693, clientLevel));
    }

    public void method_5638(@NotNull Minecraft minecraft) {
        this.field_6694.clear();
    }

    public void method_5637(BF_1161 bF_1161) {
        this.field_6694.add((Object)bF_1161);
    }
}

