/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.tag;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface IHasCapturePoints<P extends AbstractGamePlayerManager<?>, C extends AbstractCapturePoint<P>> {
    public int method_3398(@NotNull C var1);

    public void method_3397(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull Level var2, @NotNull C var3, @NotNull String var4, @NotNull String var5, @NotNull Set<UUID> var6);

    public List<C> getCapturePoints();
}

