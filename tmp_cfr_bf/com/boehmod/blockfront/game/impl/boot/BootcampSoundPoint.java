/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.boot;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.RegistryUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public final class BootcampSoundPoint
extends FDSPose {
    public static final int field_3277 = 4;
    public BFStat field_3275;
    public String field_3273;
    public boolean field_3276 = false;
    public String field_3274;

    public BootcampSoundPoint() {
    }

    public BootcampSoundPoint(@NotNull String string, double d, double d2, double d3, float f, float f2) {
        super(d, d2, d3, f, f2);
        this.field_3273 = string;
        this.field_3275 = new BFStat("blockLocation" + this.field_3273);
    }

    @NotNull
    public static BootcampSoundPoint method_3184(@NotNull String string, @NotNull FDSTagCompound fDSTagCompound) {
        BootcampSoundPoint bootcampSoundPoint = new BootcampSoundPoint();
        bootcampSoundPoint.readFromFDS(fDSTagCompound.getTagCompound(string));
        return bootcampSoundPoint;
    }

    public void update(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        List<ServerPlayer> list = this.playersInSurroundingArea(serverLevel, 4, set);
        for (ServerPlayer serverPlayer : list) {
            UUID uUID = serverPlayer.getUUID();
            if (serverPlayer.tickCount <= 20 || BFUtils.getPlayerStat(abstractGame, uUID, this.field_3275) != 0) continue;
            BFUtils.setPlayerStat(abstractGame, uUID, this.field_3275, 1);
            Supplier<SoundEvent> supplier = RegistryUtils.retrieveSoundEvent(this.field_3273);
            if (supplier != null) {
                BFUtils.playSound(serverPlayer, supplier.get(), SoundSource.NEUTRAL);
            }
            if (!this.field_3276) continue;
            BFUtils.sendNoticeMessage(serverPlayer, (Component)Component.literal((String)this.field_3274));
        }
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setString("sound", this.field_3273);
        fDSTagCompound.setBoolean("hasMessage", this.field_3276);
        if (this.field_3276) {
            fDSTagCompound.setString("message", this.field_3274);
        }
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.field_3273 = fDSTagCompound.getString("sound");
        this.field_3276 = fDSTagCompound.getBoolean("hasMessage");
        this.field_3275 = new BFStat("blockLocation" + this.field_3273);
        if (this.field_3276) {
            this.field_3274 = fDSTagCompound.getString("message");
        }
    }

    @Override
    public void writeNamedFDS(@NotNull String nameTag, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound(nameTag);
        this.writeToFDS(fDSTagCompound);
        root.setTagCompound(nameTag, fDSTagCompound);
    }

    @Override
    @NotNull
    public BootcampSoundPoint copy() {
        return new BootcampSoundPoint(this.field_3273, this.position.x, this.position.y, this.position.z, this.rotation.x, this.rotation.y);
    }

    @Override
    @NotNull
    public /* synthetic */ FDSPose copy() {
        return this.copy();
    }
}

