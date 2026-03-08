/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class PositionedMapEffect
extends AbstractMapEffect {
    @NotNull
    public Vec3 position;

    public PositionedMapEffect() {
        this(new Vec3(0.0, 0.0, 0.0));
    }

    public PositionedMapEffect(@NotNull Vec3 position) {
        this.position = position;
    }

    @NotNull
    public Vec3 getPosition() {
        return this.position;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        fDSTagCompound.setDouble("x", this.position.x);
        fDSTagCompound.setDouble("y", this.position.y);
        fDSTagCompound.setDouble("z", this.position.z);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        double d = fDSTagCompound.getDouble("x");
        double d2 = fDSTagCompound.getDouble("y");
        double d3 = fDSTagCompound.getDouble("z");
        this.position = new Vec3(d, d2, d3);
    }
}

