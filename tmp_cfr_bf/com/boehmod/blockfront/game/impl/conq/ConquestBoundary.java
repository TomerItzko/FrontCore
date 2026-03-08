/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.conq;

import com.boehmod.bflib.fds.IFDSObject;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import java.io.IOException;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

public class ConquestBoundary
implements IFDSObject<FDSTagCompound> {
    @NotNull
    private final ObjectList<WallLine> wallLines = new ObjectArrayList();
    @NotNull
    private final ObjectList<Vec2> wallPoints = new ObjectArrayList();

    public int numWallPoints() {
        return this.wallPoints.size();
    }

    public int numWallLines() {
        return this.wallLines.size();
    }

    @NotNull
    public ObjectList<WallLine> method_3127() {
        return ObjectLists.unmodifiable(this.wallLines);
    }

    public void addWallPoint(@NotNull Vec2 point) {
        this.wallPoints.add((Object)point);
        this.updateWallLines();
    }

    public boolean method_3128(float f, float f2) {
        int n = 0;
        for (WallLine wallLine : this.wallLines) {
            if (!wallLine.method_3136(f, f2)) continue;
            ++n;
        }
        return n % 2 != 0;
    }

    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        FDSTagCompound fDSTagCompound2 = fDSTagCompound.getTagCompound("boundaryWall");
        if (fDSTagCompound2 == null) {
            return;
        }
        this.wallPoints.clear();
        int n = fDSTagCompound2.getInteger("boundaryWallSize", 0);
        for (int i = 0; i < n; ++i) {
            Vec2 vec2 = new Vec2(fDSTagCompound2.getFloat("boundaryWallPoint" + i + "X"), fDSTagCompound2.getFloat("boundaryWallPoint" + i + "Y"));
            this.wallPoints.add((Object)vec2);
        }
        this.updateWallLines();
    }

    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        FDSTagCompound fDSTagCompound2 = new FDSTagCompound("boundaryWall");
        int n = this.wallPoints.size();
        fDSTagCompound2.setInteger("boundaryWallSize", n);
        for (int i = 0; i < n; ++i) {
            Vec2 vec2 = (Vec2)this.wallPoints.get(i);
            fDSTagCompound2.setFloat("boundaryWallPoint" + i + "X", vec2.x);
            fDSTagCompound2.setFloat("boundaryWallPoint" + i + "Y", vec2.y);
        }
        fDSTagCompound.setTagCompound("boundaryWall", fDSTagCompound2);
    }

    public void read(@NotNull ByteBuf byteBuf) throws IOException {
        this.wallPoints.clear();
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            Vec2 vec2 = new Vec2(byteBuf.readFloat(), byteBuf.readFloat());
            this.wallPoints.add((Object)vec2);
        }
        this.updateWallLines();
    }

    public void write(@NotNull ByteBuf byteBuf) throws IOException {
        int n = this.wallPoints.size();
        byteBuf.writeInt(n);
        for (Vec2 vec2 : this.wallPoints) {
            byteBuf.writeFloat(vec2.x);
            byteBuf.writeFloat(vec2.y);
        }
    }

    private void updateWallLines() {
        this.wallLines.clear();
        int n = this.wallPoints.size();
        for (int i = 0; i < n; ++i) {
            Vec2 vec2 = (Vec2)this.wallPoints.get(i);
            Vec2 vec22 = (Vec2)this.wallPoints.get((i + 1) % n);
            this.wallLines.add((Object)new WallLine(vec2, vec22));
        }
    }

    public boolean method_3132() {
        return this.wallPoints.isEmpty();
    }

    public static class WallLine {
        private final Vec2 field_3216;
        private final Vec2 field_3217;
        private final float field_3214;
        private final float field_3215;

        WallLine(@NotNull Vec2 vec2, @NotNull Vec2 vec22) {
            this.field_3216 = vec2;
            this.field_3217 = vec22;
            this.field_3214 = vec22.x - vec2.x;
            this.field_3215 = vec22.y - vec2.y;
        }

        public Vec2 method_3134() {
            return this.field_3216;
        }

        public Vec2 method_3135() {
            return this.field_3217;
        }

        boolean method_3136(float f, float f2) {
            boolean bl;
            boolean bl2 = bl = this.field_3216.y < f2 && this.field_3217.y >= f2 || this.field_3217.y < f2 && this.field_3216.y >= f2;
            if (bl) {
                float f3 = this.field_3216.x + (f2 - this.field_3216.y) * this.field_3214 / this.field_3215;
                return f3 > f;
            }
            return false;
        }
    }
}

