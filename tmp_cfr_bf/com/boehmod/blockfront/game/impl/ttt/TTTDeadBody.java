/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.ttt;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerRole;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownPlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class TTTDeadBody
extends FDSPose {
    private String username;
    private UUID uuid;
    private String item;
    private boolean identified = false;

    public TTTDeadBody() {
    }

    public TTTDeadBody(@NotNull String string, @NotNull UUID uUID, @NotNull String string2, double d, double d2, double d3, float f, float f2) {
        super(d, d2, d3, f, f2);
        this.username = string;
        this.uuid = uUID;
        this.item = string2;
    }

    @NotNull
    public static TTTDeadBody method_3511(@NotNull String string, @NotNull FDSTagCompound fDSTagCompound) {
        TTTDeadBody tTTDeadBody = new TTTDeadBody();
        tTTDeadBody.readFromFDS(fDSTagCompound.getTagCompound(string));
        return tTTDeadBody;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setString("username", this.username);
        fDSTagCompound.setUUID("uuid", this.uuid);
        fDSTagCompound.setString("item", this.item);
        fDSTagCompound.setBoolean("identified", this.identified);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.username = fDSTagCompound.getString("username");
        this.uuid = fDSTagCompound.getUUID("uuid");
        this.item = fDSTagCompound.getString("item");
        this.identified = fDSTagCompound.getBoolean("identified");
    }

    @Override
    public void read(@NotNull ByteBuf byteBuf) throws IOException {
        super.read(byteBuf);
        this.username = IPacket.readString((ByteBuf)byteBuf);
        this.uuid = IPacket.readUUID((ByteBuf)byteBuf);
        this.item = IPacket.readString((ByteBuf)byteBuf);
        this.identified = IPacket.readBoolean((ByteBuf)byteBuf);
    }

    @Override
    public void write(@NotNull ByteBuf byteBuf) throws IOException {
        super.write(byteBuf);
        IPacket.writeString((ByteBuf)byteBuf, (String)this.username);
        IPacket.writeUUID((ByteBuf)byteBuf, (UUID)this.uuid);
        IPacket.writeString((ByteBuf)byteBuf, (String)this.item);
        IPacket.writeBoolean((ByteBuf)byteBuf, (boolean)this.identified);
    }

    @Override
    public void writeNamedFDS(@NotNull String nameTag, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound(nameTag);
        this.writeToFDS(fDSTagCompound);
        root.setTagCompound(nameTag, fDSTagCompound);
    }

    public String getUsername() {
        return this.username;
    }

    public String getItem() {
        return this.item;
    }

    public boolean isIdentified() {
        return this.identified;
    }

    public void method_3510(@NotNull TroubleTownGame troubleTownGame, @NotNull Player player) {
        Set<UUID> set = ((TroubleTownPlayerManager)troubleTownGame.getPlayerManager()).getPlayerUUIDs();
        TTTPlayerRole tTTPlayerRole = troubleTownGame.getPlayerRole(this.uuid);
        this.identified = true;
        MutableComponent mutableComponent = Component.literal((String)player.getScoreboardName()).withColor(0xFFFFFF);
        MutableComponent mutableComponent2 = Component.literal((String)this.username).withColor(0xFFFFFF);
        MutableComponent mutableComponent3 = Component.literal((String)tTTPlayerRole.getKey()).withColor(TextColor.fromRgb((int)tTTPlayerRole.getColor()).getValue());
        MutableComponent mutableComponent4 = Component.translatable((String)"bf.message.gamemode.ttt.murderinfo.discovered", (Object[])new Object[]{mutableComponent, mutableComponent2, mutableComponent3}).withStyle(ChatFormatting.GRAY);
        BFUtils.sendNoticeMessage(set, (Component)mutableComponent4);
    }

    public UUID getUUID() {
        return this.uuid;
    }
}

