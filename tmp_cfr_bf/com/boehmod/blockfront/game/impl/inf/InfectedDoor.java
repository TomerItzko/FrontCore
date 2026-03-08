/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.IFDSObject;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.io.IOException;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class InfectedDoor
implements IFDSObject<FDSTagCompound> {
    private String id;
    private ObjectList<String> zones;
    private BlockPos position;
    private int cost;
    private boolean open = false;

    public InfectedDoor() {
        this(new BlockPos(0, 0, 0), "unknown", (ObjectList<String>)ObjectList.of(), 0);
    }

    public InfectedDoor(@NotNull BlockPos blockPos, @NotNull String string, @NotNull ObjectList<String> objectList, int n) {
        this.position = blockPos;
        this.id = string;
        this.zones = objectList;
        this.cost = n;
    }

    public void close() {
        this.open = false;
    }

    public void method_3697(@NotNull Level level, boolean bl) {
        BlockState blockState = level.getBlockState(this.position);
        if (blockState.getBlock() == Blocks.IRON_DOOR) {
            DoorBlock doorBlock = (DoorBlock)blockState.getBlock();
            doorBlock.setOpen(null, level, blockState, this.position, bl);
        }
        if (bl) {
            level.playSound(null, this.position, SoundEvents.ENDER_CHEST_OPEN, SoundSource.AMBIENT, 1.0f, 0.5f);
        }
        this.open = bl;
    }

    @NotNull
    public List<String> getZones() {
        return this.zones;
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @NotNull
    public BlockPos getPosition() {
        return this.position;
    }

    public boolean isOpen() {
        return this.open;
    }

    public int getCost() {
        return this.cost;
    }

    public boolean canOpenDoor(@NotNull BlockPos playerPos) {
        return this.position.equals((Object)playerPos) || this.position.offset(0, 1, 0).equals((Object)playerPos) || this.position.subtract(new Vec3i(0, 1, 0)).equals((Object)playerPos);
    }

    public void readFromFDS(@NotNull FDSTagCompound root) {
        this.position = new BlockPos(root.getInteger("x"), root.getInteger("y"), root.getInteger("z"));
        this.id = root.getString("id", "Unknown");
        this.zones = root.getStringArrayList("zones");
        this.open = root.getBoolean("open", false);
        this.cost = root.getInteger("cost");
    }

    public void writeToFDS(@NotNull FDSTagCompound root) {
        root.setInteger("x", this.position.getX());
        root.setInteger("y", this.position.getY());
        root.setInteger("z", this.position.getZ());
        root.setString("id", this.id);
        root.setStringArrayList("zones", this.zones);
        root.setBoolean("open", this.open);
        root.setInteger("cost", this.cost);
    }

    public void read(@NotNull ByteBuf buf) throws IOException {
        this.position = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.id = IPacket.readString((ByteBuf)buf);
        this.zones = IPacket.readStringList((ByteBuf)buf);
        this.open = IPacket.readBoolean((ByteBuf)buf);
        this.cost = buf.readInt();
    }

    public void write(@NotNull ByteBuf buf) throws IOException {
        buf.writeInt(this.position.getX());
        buf.writeInt(this.position.getY());
        buf.writeInt(this.position.getZ());
        IPacket.writeString((ByteBuf)buf, (String)this.id);
        IPacket.writeStringList((ByteBuf)buf, this.zones);
        IPacket.writeBoolean((ByteBuf)buf, (boolean)this.open);
        buf.writeInt(this.cost);
    }
}

