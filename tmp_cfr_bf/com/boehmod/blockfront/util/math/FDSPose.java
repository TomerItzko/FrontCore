/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util.math;

import com.boehmod.bflib.fds.IFDSObject;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.game.AbstractGame;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FDSPose
implements IFDSObject<FDSTagCompound> {
    @NotNull
    public Vec2 rotation;
    @NotNull
    public Vec3 position;

    public FDSPose() {
        this.position = new Vec3(0.0, 0.0, 0.0);
        this.rotation = new Vec2(0.0f, 0.0f);
    }

    public FDSPose(double x, double y, double z, float pitch, float yaw) {
        this.position = new Vec3(x, y, z);
        this.rotation = new Vec2(pitch, yaw);
    }

    public FDSPose(double x, double y, double z) {
        this(x, y, z, 0.0f, 0.0f);
    }

    public FDSPose(@NotNull BlockPos blockPos, float pitch, float yaw) {
        this(blockPos.getX(), blockPos.getY(), blockPos.getZ(), pitch, yaw);
    }

    public FDSPose(@NotNull Vec3 vec, float pitch, float yaw) {
        this(vec.x, vec.y, vec.z, pitch, yaw);
    }

    public FDSPose(@NotNull Player player, float pitch, float yaw) {
        this(player.getX(), player.getY(), player.getZ(), pitch, yaw);
    }

    public FDSPose(@NotNull Player player) {
        this(player, player.getYHeadRot(), player.getXRot());
    }

    public FDSPose(@NotNull ByteBuf buf) throws IOException {
        this.read(buf);
    }

    public void setFrom(@NotNull FDSPose other) {
        this.position = other.position;
        this.rotation = other.rotation;
    }

    @Nullable
    public static FDSPose readNamedFDS(@NotNull String tagName, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = root.getTagCompound(tagName);
        if (fDSTagCompound != null) {
            double d = fDSTagCompound.getDouble("x");
            double d2 = fDSTagCompound.getDouble("y");
            double d3 = fDSTagCompound.getDouble("z");
            float f = fDSTagCompound.getFloat("pitch");
            float f2 = fDSTagCompound.getFloat("yaw");
            return new FDSPose(d, d2, d3, f, f2);
        }
        return null;
    }

    public static void method_3009(@NotNull FDSTagCompound fDSTagCompound, @NotNull String string, @NotNull List<FDSPose> list) {
        int n = list.size();
        fDSTagCompound.setInteger(string + "Size", n);
        for (int i = 0; i < n; ++i) {
            list.get(i).writeNamedFDS(string + i, fDSTagCompound);
        }
    }

    public static List<FDSPose> method_3008(@NotNull FDSTagCompound fDSTagCompound, @NotNull String string) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        int n = fDSTagCompound.getInteger(string + "Size");
        for (int i = 0; i < n; ++i) {
            objectArrayList.add(FDSPose.readNamedFDS(string + i, fDSTagCompound));
        }
        return objectArrayList;
    }

    public void writeNamedFDS(@NotNull String nameTag, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound(nameTag);
        fDSTagCompound.setDouble("x", this.position.x);
        fDSTagCompound.setDouble("y", this.position.y);
        fDSTagCompound.setDouble("z", this.position.z);
        fDSTagCompound.setFloat("pitch", this.rotation.x);
        fDSTagCompound.setFloat("yaw", this.rotation.y);
        root.setTagCompound(nameTag, fDSTagCompound);
    }

    public float getPitch() {
        return this.rotation.x;
    }

    public float getYaw() {
        return this.rotation.y;
    }

    @NotNull
    public List<Player> method_3024(@NotNull Level level, int n, @NotNull Set<UUID> set) {
        return level.getEntitiesOfClass(Player.class, this.getSurroundingArea(n), player -> player.isAlive() && player.tickCount >= 20 && set.contains(player.getUUID()));
    }

    @NotNull
    public List<ServerPlayer> playersInSurroundingArea(@NotNull ServerLevel level, int size, @NotNull Set<UUID> players) {
        return level.getEntitiesOfClass(ServerPlayer.class, this.getSurroundingArea(size), player -> player.isAlive() && player.tickCount >= 20 && players.contains(player.getUUID()));
    }

    @NotNull
    public List<AbstractVehicleEntity> getVehiclesInArea(@NotNull Level level, int radius) {
        return level.getEntitiesOfClass(AbstractVehicleEntity.class, this.getSurroundingArea(radius));
    }

    @NotNull
    public List<BotEntity> getGameBotsInArea(@NotNull AbstractGame<?, ?, ?> game, @NotNull ServerLevel level, int radius) {
        return level.getEntitiesOfClass(BotEntity.class, this.getSurroundingArea(radius), bot -> bot.getGame() == game);
    }

    public boolean isInRadius(@NotNull Entity entity, int radius) {
        return this.distanceTo(entity) <= (float)radius;
    }

    public float distanceTo(@NotNull Entity entity) {
        return Mth.sqrt((float)((float)entity.distanceToSqr(this.position)));
    }

    public double distance(@NotNull FDSPose other) {
        return Mth.sqrt((float)((float)other.distanceSqr(this)));
    }

    public double distanceTo(@NotNull Vec3 vec) {
        return Mth.sqrt((float)((float)vec.distanceToSqr(this.position)));
    }

    public double distanceSqr(@NotNull FDSPose other) {
        double d = other.position.x - this.position.x;
        double d2 = other.position.y - this.position.y;
        double d3 = other.position.z - this.position.z;
        return d * d + d2 * d2 + d3 * d3;
    }

    public double distance(@NotNull BlockPos blockPos) {
        return Mth.sqrt((float)((float)blockPos.distToLowCornerSqr(this.position.x, this.position.y, this.position.z)));
    }

    @Nullable
    public Player getNearestPlayer(@NotNull Level level, int maxRadius) {
        return level.getNearestPlayer(this.position.x, this.position.y, this.position.z, (double)maxRadius, false);
    }

    @NotNull
    public FDSPose copy() {
        return new FDSPose(new Vec3(this.position.x, this.position.y, this.position.z), this.rotation.x, this.rotation.y);
    }

    public boolean equals(Object object) {
        if (object instanceof FDSPose) {
            FDSPose fDSPose = (FDSPose)object;
            return fDSPose.position.equals((Object)this.position) && fDSPose.rotation.equals(this.rotation);
        }
        return false;
    }

    @NotNull
    public AABB getSurroundingArea(int size) {
        return new AABB(this.position, this.position).inflate((double)size, 0.0, (double)size).expandTowards(0.0, 2.0, 0.0);
    }

    public final double getX() {
        return this.position.x;
    }

    public final double getY() {
        return this.position.y;
    }

    public final double getZ() {
        return this.position.z;
    }

    @NotNull
    public BlockPos asBlockPos() {
        return BlockPos.containing((Position)this.position);
    }

    @OverridingMethodsMustInvokeSuper
    public void readFromFDS(@NotNull FDSTagCompound root) {
        this.position = new Vec3(root.getDouble("x"), root.getDouble("y"), root.getDouble("z"));
        this.rotation = new Vec2(root.getFloat("pitch"), root.getFloat("yaw"));
    }

    @OverridingMethodsMustInvokeSuper
    public void writeToFDS(@NotNull FDSTagCompound root) {
        root.setDouble("x", this.position.x);
        root.setDouble("y", this.position.y);
        root.setDouble("z", this.position.z);
        root.setFloat("pitch", this.rotation.x);
        root.setFloat("yaw", this.rotation.y);
    }

    @OverridingMethodsMustInvokeSuper
    public void read(@NotNull ByteBuf buf) throws IOException {
        this.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.rotation = new Vec2(buf.readFloat(), buf.readFloat());
    }

    @OverridingMethodsMustInvokeSuper
    public void write(@NotNull ByteBuf buf) throws IOException {
        buf.writeDouble(this.position.x);
        buf.writeDouble(this.position.y);
        buf.writeDouble(this.position.z);
        buf.writeFloat(this.rotation.x);
        buf.writeFloat(this.rotation.y);
    }

    public static FDSPose readBuf(@NotNull FriendlyByteBuf buf) {
        double d = buf.readDouble();
        double d2 = buf.readDouble();
        double d3 = buf.readDouble();
        float f = buf.readFloat();
        float f2 = buf.readFloat();
        return new FDSPose(d, d2, d3, f, f2);
    }

    public void writeBuf(@NotNull FriendlyByteBuf buf) {
        buf.writeDouble(this.position.x);
        buf.writeDouble(this.position.y);
        buf.writeDouble(this.position.z);
        buf.writeFloat(this.rotation.x);
        buf.writeFloat(this.rotation.y);
    }
}

