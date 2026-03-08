/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.fds.IFDSObject
 *  com.boehmod.bflib.fds.tag.FDSPosition
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.map.effect.data;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.IFDSObject;
import com.boehmod.bflib.fds.tag.FDSPosition;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.map.effect.data.VehiclePathInterpolation;
import com.boehmod.blockfront.util.math.MathUtils;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.io.IOException;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AmbientVehiclePath
implements IFDSObject<FDSTagCompound> {
    @NotNull
    public List<Vec3> field_3419 = new ObjectArrayList();
    @NotNull
    public Vec3 field_3421 = Vec3.ZERO;
    @NotNull
    public Vec3 field_3417 = Vec3.ZERO;
    public int field_3424 = 0;
    public float speed;
    public int field_3425 = 0;
    public int field_3426 = 0;
    public int startTime = 0;
    public int finishTime = 0;
    public boolean reverseOnEnd = false;
    private boolean field_3423 = false;
    @NotNull
    private VehiclePathInterpolation field_3418 = VehiclePathInterpolation.LINEAR;

    public void method_3366(@NotNull VehiclePathInterpolation vehiclePathInterpolation) {
        this.field_3418 = vehiclePathInterpolation;
    }

    public boolean getReverseOnEnd() {
        return this.reverseOnEnd;
    }

    public void setReverseOnEnd(boolean bl) {
        this.reverseOnEnd = bl;
    }

    public void method_3374(@NotNull Vec3 vec3) {
        if (this.field_3421.equals((Object)Vec3.ZERO)) {
            this.field_3421 = this.field_3417 = vec3;
        }
        this.field_3419.add(vec3);
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float f) {
        this.speed = f;
    }

    @Nullable
    public Vec3 method_3375() {
        if (this.field_3419.isEmpty()) {
            return null;
        }
        if (this.field_3423) {
            return this.field_3419.get(this.field_3424 + 1);
        }
        if (this.field_3424 <= 0) {
            return this.field_3419.getLast();
        }
        return this.field_3419.get(this.field_3424 - 1);
    }

    public Vec3 method_3376() {
        if (this.field_3419.isEmpty()) {
            return Vec3.ZERO;
        }
        return this.field_3419.get(this.field_3424);
    }

    public boolean method_3371() {
        return this.field_3425 > 0 || this.field_3426 > 0;
    }

    public void method_3372() {
        if (this.reverseOnEnd) {
            if (this.field_3423) {
                --this.field_3424;
                if (this.field_3424 < 0) {
                    this.field_3424 = 0;
                    this.field_3417 = this.field_3421;
                    this.field_3425 = this.startTime;
                    this.field_3423 = false;
                }
            } else {
                ++this.field_3424;
                if (this.field_3424 >= this.field_3419.size()) {
                    this.field_3424 = this.field_3419.size() - 2;
                    this.field_3423 = true;
                    this.field_3417 = this.field_3421;
                    this.field_3426 = this.finishTime;
                }
            }
        } else {
            ++this.field_3424;
            if (this.field_3424 >= this.field_3419.size()) {
                this.field_3424 = 0;
                this.field_3417 = this.field_3421 = this.field_3419.get(this.field_3424);
                this.field_3426 = this.finishTime;
                this.field_3425 = this.startTime;
            }
        }
    }

    public void method_3373() {
        if (this.field_3425-- > 0 || this.field_3426-- > 0) {
            return;
        }
        this.field_3417 = this.field_3421;
        this.field_3421 = this.field_3418.interpolate(this, this.speed);
        if (this.field_3421.distanceTo(this.method_3376()) <= 0.0) {
            this.method_3372();
        }
    }

    public boolean method_3378() {
        return this.field_3423 ? this.field_3424 <= 0 : this.field_3424 >= this.field_3419.size() - 1;
    }

    public Vec3 method_3365(float f) {
        return MathUtils.lerp(this.field_3421, this.field_3417, f);
    }

    public int getStartTime() {
        return this.startTime;
    }

    public void setStartTime(int n) {
        this.startTime = n;
    }

    public int getFinishTime() {
        return this.finishTime;
    }

    public void setFinishTime(int n) {
        this.finishTime = n;
    }

    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        this.field_3419.clear();
        ObjectList objectList = fDSTagCompound.getPositionArrayList("positions");
        for (FDSPosition fDSPosition : objectList) {
            this.method_3374(new Vec3(fDSPosition.x, fDSPosition.y, fDSPosition.z));
        }
        this.setSpeed(fDSTagCompound.getFloat("speed"));
        this.setStartTime(fDSTagCompound.getInteger("startTime"));
        this.setFinishTime(fDSTagCompound.getInteger("finishTime"));
        this.setReverseOnEnd(fDSTagCompound.getBoolean("reverseOnEnd"));
    }

    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (Vec3 vec3 : this.field_3419) {
            objectArrayList.add(new FDSPosition(vec3.x, vec3.y, vec3.z));
        }
        fDSTagCompound.setPositionArrayList("positions", (List)objectArrayList);
        fDSTagCompound.setFloat("speed", this.getSpeed());
        fDSTagCompound.setInteger("startTime", this.getStartTime());
        fDSTagCompound.setInteger("finishTime", this.getFinishTime());
        fDSTagCompound.setBoolean("reverseOnEnd", this.getReverseOnEnd());
    }

    public void read(@NotNull ByteBuf byteBuf) throws IOException {
        this.field_3419.clear();
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            Vec3 vec3 = new Vec3(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
            this.method_3374(vec3);
        }
        this.setSpeed(byteBuf.readFloat());
        this.setStartTime(byteBuf.readInt());
        this.setFinishTime(byteBuf.readInt());
        this.setReverseOnEnd(IPacket.readBoolean((ByteBuf)byteBuf));
    }

    public void write(@NotNull ByteBuf byteBuf) throws IOException {
        byteBuf.writeInt(this.field_3419.size());
        for (Vec3 vec3 : this.field_3419) {
            byteBuf.writeDouble(vec3.x);
            byteBuf.writeDouble(vec3.y);
            byteBuf.writeDouble(vec3.z);
        }
        byteBuf.writeFloat(this.getSpeed());
        byteBuf.writeInt(this.getStartTime());
        byteBuf.writeInt(this.getFinishTime());
        IPacket.writeBoolean((ByteBuf)byteBuf, (boolean)this.getReverseOnEnd());
    }
}

