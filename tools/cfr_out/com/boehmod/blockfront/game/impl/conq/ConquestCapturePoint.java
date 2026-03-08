/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  it.unimi.dsi.fastutil.objects.ObjectLists
 *  javax.annotation.Nullable
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.conq;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.impl.conq.ConquestPlayerManager;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ConquestCapturePoint
extends AbstractCapturePoint<ConquestPlayerManager> {
    public static final String field_3366 = "HQ";
    @NotNull
    private final ObjectList<Vec3> field_3367 = new ObjectArrayList();
    private boolean field_3368 = false;

    public ConquestCapturePoint(@NotNull ConquestPlayerManager conquestPlayerManager) {
        super(conquestPlayerManager);
    }

    public ConquestCapturePoint(@NotNull ConquestPlayerManager conquestPlayerManager, @NotNull ByteBuf byteBuf) throws IOException {
        super(conquestPlayerManager, byteBuf);
    }

    public ConquestCapturePoint(@NotNull ConquestPlayerManager conquestPlayerManager, @NotNull String string, @NotNull FDSTagCompound fDSTagCompound) {
        super(conquestPlayerManager, string, fDSTagCompound);
    }

    public ConquestCapturePoint(@NotNull ConquestPlayerManager conquestPlayerManager, @NotNull Player player, @NotNull String string) {
        super(conquestPlayerManager, player, string);
    }

    public boolean method_3290() {
        return this.field_3368;
    }

    public void method_3291() {
        this.field_3367.clear();
    }

    public void method_3292(@NotNull Vec3 vec3) {
        this.field_3367.add((Object)vec3);
    }

    @Nullable
    public Vec3 method_3293() {
        if (this.field_3367.isEmpty()) {
            return null;
        }
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        int n = threadLocalRandom.nextInt(this.field_3367.size());
        return (Vec3)this.field_3367.get(n);
    }

    @NotNull
    public ObjectList<Vec3> method_3288() {
        return ObjectLists.unmodifiable(this.field_3367);
    }

    @Override
    public void onUpdate(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull AbstractGame<?, ?, ?> game, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        super.onUpdate(manager, dataHandler, game, level, players);
        Map<String, Integer> map = this.method_5508();
        this.field_3368 = false;
        if (this.cbTeam != null) {
            int n = map.getOrDefault(this.cbTeam.getName(), 0);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getKey().equals(this.cbTeam.getName()) || entry.getValue() <= n) continue;
                this.field_3368 = true;
                break;
            }
        }
    }

    @Override
    protected boolean method_5506(@NotNull LivingEntity livingEntity) {
        return true;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setBoolean("overrun", this.field_3368);
        int n = this.field_3367.size();
        fDSTagCompound.setInteger("sPointSize", n);
        for (int i = 0; i < n; ++i) {
            Vec3 vec3 = (Vec3)this.field_3367.get(i);
            fDSTagCompound.setDouble("sPointX" + i, vec3.x);
            fDSTagCompound.setDouble("sPointY" + i, vec3.y);
            fDSTagCompound.setDouble("sPointZ" + i, vec3.z);
        }
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.field_3368 = fDSTagCompound.getBoolean("overrun", false);
        this.field_3367.clear();
        int n = fDSTagCompound.getInteger("sPointSize", 0);
        for (int i = 0; i < n; ++i) {
            Vec3 vec3 = new Vec3(fDSTagCompound.getDouble("sPointX" + i), fDSTagCompound.getDouble("sPointY" + i), fDSTagCompound.getDouble("sPointZ" + i));
            this.field_3367.add((Object)vec3);
        }
    }

    @Override
    public void write(@NotNull ByteBuf byteBuf) throws IOException {
        super.write(byteBuf);
        byteBuf.writeBoolean(this.field_3368);
    }

    @Override
    public void read(@NotNull ByteBuf byteBuf) throws IOException {
        super.read(byteBuf);
        this.field_3368 = byteBuf.readBoolean();
    }

    @Override
    public void writeNamedFDS(@NotNull String nameTag, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound(nameTag);
        this.writeToFDS(fDSTagCompound);
        root.setTagCompound(nameTag, fDSTagCompound);
    }
}

