/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.boot;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public final class BootcampWeaponPoint
extends FDSPose {
    public static final int field_3286 = 3;
    public Loadout field_3285;

    public BootcampWeaponPoint() {
    }

    public BootcampWeaponPoint(double d, double d2, double d3, float f, float f2, @NotNull Loadout loadout) {
        super(d, d2, d3, f, f2);
        this.field_3285 = loadout;
    }

    @NotNull
    public static BootcampWeaponPoint method_3194(@NotNull String string, @NotNull FDSTagCompound fDSTagCompound) {
        BootcampWeaponPoint bootcampWeaponPoint = new BootcampWeaponPoint();
        bootcampWeaponPoint.readFromFDS(fDSTagCompound.getTagCompound(string));
        return bootcampWeaponPoint;
    }

    public void update(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
        for (ServerPlayer serverPlayer : this.playersInSurroundingArea(serverLevel, 3, set)) {
            BFStat bFStat;
            UUID uUID = serverPlayer.getUUID();
            if (serverPlayer.tickCount <= 20 || BFUtils.getPlayerStat(abstractGame, uUID, bFStat = new BFStat("loadout" + this.field_3285.hashCode())) != 0) continue;
            BFUtils.setPlayerStat(abstractGame, uUID, bFStat, 1);
            BFUtils.giveLoadout(serverLevel, serverPlayer, this.field_3285);
        }
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
        this.field_3285.writeFDS("loadout", fDSTagCompound);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.field_3285 = Loadout.readFDS("loadout", fDSTagCompound);
    }

    @Override
    public void writeNamedFDS(@NotNull String nameTag, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound(nameTag);
        this.writeToFDS(fDSTagCompound);
        root.setTagCompound(nameTag, fDSTagCompound);
    }

    @Override
    @NotNull
    public BootcampWeaponPoint copy() {
        return new BootcampWeaponPoint(this.position.x, this.position.y, this.position.z, this.rotation.x, this.rotation.y, this.field_3285);
    }

    @Override
    @NotNull
    public /* synthetic */ FDSPose copy() {
        return this.copy();
    }
}

