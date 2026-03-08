/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.boot;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class BootcampTexturePoint
extends FDSPose {
    @NotNull
    public String field_3278;
    @NotNull
    private ResourceLocation field_3279;

    public BootcampTexturePoint() {
    }

    public BootcampTexturePoint(double d, double d2, double d3, float f, float f2, @NotNull String string) {
        super(d, d2, d3, f, f2);
        this.field_3278 = string;
        this.field_3279 = BFRes.loc("textures/misc/waypoints/" + string + ".png");
    }

    @NotNull
    public static BootcampTexturePoint method_3186(@NotNull String string, @NotNull FDSTagCompound fDSTagCompound) {
        BootcampTexturePoint bootcampTexturePoint = new BootcampTexturePoint();
        bootcampTexturePoint.readFromFDS(fDSTagCompound.getTagCompound(string));
        return bootcampTexturePoint;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setString("texture", this.field_3278);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.field_3278 = fDSTagCompound.getString("texture");
        this.field_3279 = BFRes.loc("textures/misc/waypoints/" + this.field_3278 + ".png");
    }

    @Override
    public void writeNamedFDS(@NotNull String nameTag, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound(nameTag);
        this.writeToFDS(fDSTagCompound);
        root.setTagCompound(nameTag, fDSTagCompound);
    }

    @Override
    public void read(@NotNull ByteBuf byteBuf) throws IOException {
        super.read(byteBuf);
        this.field_3278 = IPacket.readString((ByteBuf)byteBuf);
        this.field_3279 = BFRes.loc("textures/misc/waypoints/" + this.field_3278 + ".png");
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void write(@NotNull ByteBuf byteBuf) throws IOException {
        super.write(byteBuf);
        IPacket.writeString((ByteBuf)byteBuf, (String)this.field_3278);
    }

    @Override
    @NotNull
    public BootcampTexturePoint copy() {
        return new BootcampTexturePoint(this.position.x, this.position.y, this.position.z, this.rotation.x, this.rotation.y, this.field_3278);
    }

    @NotNull
    public ResourceLocation method_3187() {
        return this.field_3279;
    }

    @Override
    @NotNull
    public /* synthetic */ FDSPose copy() {
        return this.copy();
    }
}

