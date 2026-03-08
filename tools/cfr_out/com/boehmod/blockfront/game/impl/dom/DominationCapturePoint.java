/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.dom;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.game.impl.dom.DominationPlayerManager;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class DominationCapturePoint
extends AbstractCapturePoint<DominationPlayerManager> {
    public DominationCapturePoint(@NotNull DominationPlayerManager playerHandler) {
        super(playerHandler);
    }

    public DominationCapturePoint(@NotNull DominationPlayerManager playerHandler, @NotNull ByteBuf buf) throws IOException {
        super(playerHandler, buf);
    }

    public DominationCapturePoint(@NotNull DominationPlayerManager dominationPlayerManager, @NotNull String string, @NotNull FDSTagCompound fDSTagCompound) {
        super(dominationPlayerManager, string, fDSTagCompound);
    }

    public DominationCapturePoint(@NotNull DominationPlayerManager dominationPlayerManager, @NotNull Player player, @NotNull String string) {
        super(dominationPlayerManager, player, string);
    }

    @Override
    protected boolean method_5506(@NotNull LivingEntity livingEntity) {
        return true;
    }
}

