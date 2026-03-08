/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.camera;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.render.BFRendering;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class CutsceneTextElement {
    private final Component component;
    private final Vec3 position;
    private final float scale;

    public CutsceneTextElement(@NotNull FDSTagCompound root) {
        String string = root.getString("component");
        this.component = Component.Serializer.fromJson((String)string, (HolderLookup.Provider)RegistryAccess.EMPTY);
        this.position = new Vec3(root.getDouble("positionX"), root.getDouble("positionY"), root.getDouble("positionZ"));
        this.scale = root.getFloat("scale");
    }

    public CutsceneTextElement(@NotNull FriendlyByteBuf buf) {
        this.component = Component.Serializer.fromJson((String)buf.readUtf(Short.MAX_VALUE), (HolderLookup.Provider)RegistryAccess.EMPTY);
        this.position = buf.readVec3();
        this.scale = buf.readFloat();
    }

    public CutsceneTextElement(@NotNull ByteBuf buf) throws IOException {
        this.component = Component.Serializer.fromJson((String)IPacket.readString((ByteBuf)buf), (HolderLookup.Provider)RegistryAccess.EMPTY);
        this.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.scale = buf.readFloat();
    }

    public CutsceneTextElement(@NotNull Component component, @NotNull Vec3 position, float scale) {
        this.component = component;
        this.position = position;
        this.scale = scale;
    }

    public void writeToFDS(@NotNull FDSTagCompound root) {
        root.setString("component", Component.Serializer.toJson((Component)this.component, (HolderLookup.Provider)RegistryAccess.EMPTY));
        root.setDouble("positionX", this.position.x);
        root.setDouble("positionY", this.position.y);
        root.setDouble("positionZ", this.position.z);
        root.setFloat("scale", this.scale);
    }

    public void writeWithRegistry(@NotNull RegistryFriendlyByteBuf buf) {
        buf.writeUtf(Component.Serializer.toJson((Component)this.component, (HolderLookup.Provider)RegistryAccess.EMPTY));
        buf.writeVec3(this.position);
        buf.writeFloat(this.scale);
    }

    public void write(@NotNull ByteBuf buf) throws IOException {
        IPacket.writeString((ByteBuf)buf, (String)Component.Serializer.toJson((Component)this.component, (HolderLookup.Provider)RegistryAccess.EMPTY));
        buf.writeDouble(this.position.x);
        buf.writeDouble(this.position.y);
        buf.writeDouble(this.position.z);
        buf.writeFloat(this.scale);
    }

    @OnlyIn(value=Dist.CLIENT)
    public void render(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Camera camera) {
        BFRendering.component(poseStack, font, camera, graphics, this.component, this.position.x, this.position.y, this.position.z, this.scale);
    }
}

