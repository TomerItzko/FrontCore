/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.boot;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.util.math.FDSPose;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class BootcampNpcSpawn
extends FDSPose {
    public final List<BF_758> field_3246 = new ObjectArrayList();
    public float field_3248;
    public String skin;
    public Loadout loadout;

    public BootcampNpcSpawn() {
    }

    public BootcampNpcSpawn(double d, double d2, double d3, float f, float f2, float f3, String string, Loadout loadout) {
        super(d, d2, d3, f, f2);
        this.field_3248 = f3;
        this.skin = string;
        this.loadout = loadout;
    }

    public BootcampNpcSpawn(@NotNull Player player, @NotNull String string, @NotNull Loadout loadout) {
        super(player, player.getXRot(), player.getYRot());
        this.field_3248 = player.getYHeadRot();
        this.skin = string;
        this.loadout = loadout;
    }

    @NotNull
    public static BootcampNpcSpawn method_3151(@NotNull String string, @NotNull FDSTagCompound fDSTagCompound) {
        BootcampNpcSpawn bootcampNpcSpawn = new BootcampNpcSpawn();
        bootcampNpcSpawn.readFromFDS(fDSTagCompound.getTagCompound(string));
        return bootcampNpcSpawn;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setDouble("yawHead", (double)this.field_3248);
        this.loadout.writeFDS("loadout", fDSTagCompound);
        fDSTagCompound.setString("skin", this.skin);
        fDSTagCompound.setInteger("waypointCount", this.field_3246.size());
        int n = this.field_3246.size();
        for (int i = 0; i < n; ++i) {
            this.field_3246.get(i).method_3153("waypointCount" + i, fDSTagCompound);
        }
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.field_3248 = fDSTagCompound.getFloat("yawHead");
        this.loadout = Loadout.readFDS("loadout", fDSTagCompound);
        this.skin = fDSTagCompound.getString("skin");
        int n = fDSTagCompound.getInteger("waypointCount");
        for (int i = 0; i < n; ++i) {
            BF_758 bF_758 = BF_758.method_3152("waypoint" + i, fDSTagCompound);
            this.field_3246.add(bF_758);
        }
    }

    @Override
    public void writeNamedFDS(@NotNull String nameTag, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound(nameTag);
        fDSTagCompound.setDouble("x", this.position.x);
        fDSTagCompound.setDouble("y", this.position.y);
        fDSTagCompound.setDouble("z", this.position.z);
        fDSTagCompound.setFloat("pitch", this.rotation.x);
        fDSTagCompound.setFloat("yaw", this.rotation.y);
        fDSTagCompound.setFloat("yawHead", this.field_3248);
        this.loadout.writeFDS("loadout", fDSTagCompound);
        fDSTagCompound.setString("skin", this.skin);
        fDSTagCompound.setInteger("waypointCount", this.field_3246.size());
        int n = this.field_3246.size();
        for (int i = 0; i < n; ++i) {
            this.field_3246.get(i).method_3153("waypoint" + i, fDSTagCompound);
        }
        root.setTagCompound(nameTag, fDSTagCompound);
    }

    public static class BF_758 {
        public Vec3 field_3249;

        public BF_758(Vec3 vec3) {
            this.field_3249 = vec3;
        }

        @NotNull
        public static BF_758 method_3152(@NotNull String string, @NotNull FDSTagCompound fDSTagCompound) {
            FDSTagCompound fDSTagCompound2 = fDSTagCompound.getTagCompound(string);
            return new BF_758(new Vec3(fDSTagCompound2.getDouble("x"), fDSTagCompound2.getDouble("y"), fDSTagCompound2.getDouble("z")));
        }

        public void method_3153(@NotNull String string, @NotNull FDSTagCompound fDSTagCompound) {
            FDSTagCompound fDSTagCompound2 = new FDSTagCompound(string);
            fDSTagCompound2.setDouble("x", this.field_3249.x);
            fDSTagCompound2.setDouble("y", this.field_3249.y);
            fDSTagCompound2.setDouble("z", this.field_3249.z);
            fDSTagCompound.setTagCompound(string, fDSTagCompound2);
        }
    }
}

