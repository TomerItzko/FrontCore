/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_1161 {
    private final int field_6599;
    private int field_6600;
    protected Vec3 field_6601;

    public BF_1161(@NotNull Vec3 vec3, int n) {
        this.field_6601 = vec3;
        this.field_6599 = n;
        this.field_6600 = 0;
    }

    @OverridingMethodsMustInvokeSuper
    public boolean method_5619(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel) {
        if (this.field_6600 >= this.field_6599) {
            return false;
        }
        ++this.field_6600;
        return true;
    }
}

