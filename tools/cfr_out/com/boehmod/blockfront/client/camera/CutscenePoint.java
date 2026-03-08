/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.fds.IFDSObject
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.client.Camera
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.client.camera;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.IFDSObject;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.camera.CutscenePointMode;
import com.boehmod.blockfront.client.camera.CutsceneTextElement;
import com.boehmod.blockfront.util.math.FDSPose;
import com.boehmod.blockfront.util.math.InterpolationType;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CutscenePoint
implements IFDSObject<FDSTagCompound> {
    protected float fadeFromDuration;
    protected float fadeToDuration;
    @NotNull
    protected final List<String> instructions = new ObjectArrayList();
    @NotNull
    protected final List<CutsceneTextElement> textElements = new ObjectArrayList();
    @NotNull
    protected CutscenePointMode startRelativeMode = CutscenePointMode.NONE;
    @NotNull
    protected CutscenePointMode endRelativeMode = CutscenePointMode.NONE;
    protected FDSPose start;
    protected FDSPose end;
    private FDSPose field_2488;
    private FDSPose field_2489;
    protected float startRoll;
    protected float endRoll;
    private float field_2494;
    protected int fov = 70;
    protected float durationSeconds;
    @NotNull
    protected InterpolationType interpolationType = InterpolationType.LINEAR;

    @NotNull
    public CutscenePoint fov(int fov) {
        this.fov = fov;
        return this;
    }

    @NotNull
    public CutscenePoint startRoll(float startRoll) {
        this.startRoll = startRoll;
        return this;
    }

    @NotNull
    public CutscenePoint endRoll(float endRoll) {
        this.endRoll = endRoll;
        return this;
    }

    public float getStartRoll() {
        return this.startRoll;
    }

    public float getEndRoll() {
        return this.endRoll;
    }

    @NotNull
    public CutscenePoint start(@NotNull FDSPose start) {
        this.start = start;
        return this;
    }

    @NotNull
    public CutscenePoint end(@NotNull FDSPose end) {
        this.end = end;
        return this;
    }

    @NotNull
    public CutscenePoint durationSeconds(float durationSeconds) {
        this.durationSeconds = durationSeconds;
        return this;
    }

    @NotNull
    public CutscenePoint interpolationType(@NotNull InterpolationType interpolationType) {
        this.interpolationType = interpolationType;
        return this;
    }

    @NotNull
    public CutscenePoint fadeFromDuration(float fadeFromDuration) {
        this.fadeFromDuration = fadeFromDuration;
        return this;
    }

    @NotNull
    public CutscenePoint fadeToDuration(float fadeToDuration) {
        this.fadeToDuration = fadeToDuration;
        return this;
    }

    @NotNull
    public CutscenePoint startRelativeMode(@NotNull CutscenePointMode startRelativeMode) {
        this.startRelativeMode = startRelativeMode;
        return this;
    }

    @NotNull
    public CutscenePoint endRelativeMode(@NotNull CutscenePointMode endRelativeMode) {
        this.endRelativeMode = endRelativeMode;
        return this;
    }

    @NotNull
    public CutscenePoint instruction(@NotNull String instruction) {
        this.instructions.add(instruction);
        return this;
    }

    @NotNull
    public CutscenePoint textElement(@NotNull CutsceneTextElement textElement) {
        this.textElements.add(textElement);
        return this;
    }

    public int getFOV() {
        return this.fov;
    }

    public float getFadeFromDuration() {
        return this.fadeFromDuration;
    }

    public float getFadeToDuration() {
        return this.fadeToDuration;
    }

    public List<String> getInstructions() {
        return Collections.unmodifiableList(this.instructions);
    }

    public void method_2172(@Nullable LocalPlayer localPlayer) {
        this.field_2488 = this.method_2160(this.start, this.startRelativeMode, localPlayer);
        this.field_2489 = this.method_2160(this.end, this.endRelativeMode, localPlayer);
    }

    public FDSPose method_2153(float f) {
        float f2 = this.interpolationType.apply(f);
        double d = Mth.lerp((double)f2, (double)this.field_2488.position.x, (double)this.field_2489.position.x);
        double d2 = Mth.lerp((double)f2, (double)this.field_2488.position.y, (double)this.field_2489.position.y);
        double d3 = Mth.lerp((double)f2, (double)this.field_2488.position.z, (double)this.field_2489.position.z);
        float f3 = this.method_2154(f2, this.field_2488.rotation.y, this.field_2489.rotation.y);
        float f4 = this.method_2154(f2, this.field_2488.rotation.x, this.field_2489.rotation.x);
        float f5 = this.method_2154(f2, this.startRoll, this.endRoll);
        FDSPose fDSPose = new FDSPose(d, d2, d3, f4, f3);
        this.field_2494 = f5;
        return fDSPose;
    }

    private FDSPose method_2160(FDSPose fDSPose, CutscenePointMode cutscenePointMode, @Nullable LocalPlayer localPlayer) {
        if (localPlayer == null || cutscenePointMode == CutscenePointMode.NONE) {
            return fDSPose;
        }
        double d = fDSPose.position.x;
        double d2 = fDSPose.position.y;
        double d3 = fDSPose.position.z;
        float f = fDSPose.rotation.x;
        float f2 = fDSPose.rotation.y;
        if (cutscenePointMode == CutscenePointMode.POSITION || cutscenePointMode == CutscenePointMode.POSITION_ROTATION) {
            d += localPlayer.getX();
            d2 += localPlayer.getY();
            d3 += localPlayer.getZ();
        }
        if (cutscenePointMode == CutscenePointMode.POSITION_ROTATION) {
            f += localPlayer.getXRot();
            f2 += localPlayer.getYRot();
        }
        return new FDSPose(d, d2, d3, f, f2);
    }

    private float method_2154(float f, float f2, float f3) {
        return MathUtils.rotateLerpf(f3, f2, f);
    }

    public FDSPose getStart() {
        return this.start;
    }

    public FDSPose getEnd() {
        return this.end;
    }

    public float getDurationSeconds() {
        return this.durationSeconds;
    }

    @NotNull
    public InterpolationType getInterpolationType() {
        return this.interpolationType;
    }

    public void readWithRegistry(@NotNull RegistryFriendlyByteBuf buf) throws IOException {
        int n;
        this.fadeFromDuration = buf.readFloat();
        this.fadeToDuration = buf.readFloat();
        this.startRoll = buf.readFloat();
        this.endRoll = buf.readFloat();
        this.startRelativeMode = CutscenePointMode.values()[buf.readInt()];
        this.endRelativeMode = CutscenePointMode.values()[buf.readInt()];
        this.start = FDSPose.readBuf((FriendlyByteBuf)buf);
        this.end = FDSPose.readBuf((FriendlyByteBuf)buf);
        this.fov = buf.readInt();
        this.durationSeconds = buf.readFloat();
        this.interpolationType = InterpolationType.values()[buf.readInt()];
        this.instructions.clear();
        int n2 = buf.readInt();
        for (n = 0; n < n2; ++n) {
            this.instructions.add(buf.readUtf());
        }
        this.textElements.clear();
        n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            this.textElements.add(new CutsceneTextElement((FriendlyByteBuf)buf));
        }
    }

    public void writeWithRegistry(@NotNull RegistryFriendlyByteBuf buf) throws IOException {
        int n;
        buf.writeFloat(this.fadeFromDuration);
        buf.writeFloat(this.fadeToDuration);
        buf.writeFloat(this.startRoll);
        buf.writeFloat(this.endRoll);
        buf.writeInt(this.startRelativeMode.ordinal());
        buf.writeInt(this.endRelativeMode.ordinal());
        this.start.writeBuf((FriendlyByteBuf)buf);
        this.end.writeBuf((FriendlyByteBuf)buf);
        buf.writeInt(this.fov);
        buf.writeFloat(this.durationSeconds);
        buf.writeInt(this.interpolationType.ordinal());
        int n2 = this.instructions.size();
        buf.writeInt(n2);
        for (n = 0; n < n2; ++n) {
            buf.writeUtf(this.instructions.get(n));
        }
        n = this.textElements.size();
        buf.writeInt(n);
        for (int i = 0; i < n; ++i) {
            this.textElements.get(i).writeWithRegistry(buf);
        }
    }

    public void renderTextElements(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull Camera camera) {
        for (CutsceneTextElement cutsceneTextElement : this.textElements) {
            cutsceneTextElement.render(poseStack, font, graphics, camera);
        }
    }

    public float method_2148() {
        return this.field_2494;
    }

    public void readFromFDS(@NotNull FDSTagCompound root) {
        int n;
        this.fadeFromDuration = root.getFloat("fadeFromDuration");
        this.fadeToDuration = root.getFloat("fadeToDuration");
        this.startRoll = root.getFloat("startRoll", 0.0f);
        this.endRoll = root.getFloat("endRoll", 0.0f);
        this.start = FDSPose.readNamedFDS("start", root);
        this.end = FDSPose.readNamedFDS("end", root);
        this.fov = root.getInteger("fov");
        this.startRelativeMode = CutscenePointMode.values()[root.getInteger("startRelativeMode", 0)];
        this.endRelativeMode = CutscenePointMode.values()[root.getInteger("endRelativeMode", 0)];
        this.durationSeconds = root.getFloat("durationSeconds");
        this.interpolationType = InterpolationType.values()[root.getInteger("interpolationType")];
        this.instructions.clear();
        int n2 = root.getInteger("instructionsSize");
        for (n = 0; n < n2; ++n) {
            this.instructions.add(root.getString("instruction" + n));
        }
        this.textElements.clear();
        n = root.getInteger("textElementsSize");
        for (int i = 0; i < n; ++i) {
            this.textElements.add(new CutsceneTextElement(root.getTagCompound("textElement" + i)));
        }
    }

    public void writeToFDS(@NotNull FDSTagCompound root) {
        int n;
        root.setFloat("fadeFromDuration", this.fadeFromDuration);
        root.setFloat("fadeToDuration", this.fadeToDuration);
        root.setFloat("startRoll", this.startRoll);
        root.setFloat("endRoll", this.endRoll);
        this.start.writeNamedFDS("start", root);
        this.end.writeNamedFDS("end", root);
        root.setInteger("fov", this.fov);
        root.setInteger("startRelativeMode", this.startRelativeMode.ordinal());
        root.setInteger("endRelativeMode", this.endRelativeMode.ordinal());
        root.setFloat("durationSeconds", this.durationSeconds);
        root.setInteger("interpolationType", this.interpolationType.ordinal());
        int n2 = this.instructions.size();
        root.setInteger("instructionsSize", n2);
        for (n = 0; n < n2; ++n) {
            root.setString("instruction" + n, this.instructions.get(n));
        }
        n = this.textElements.size();
        root.setInteger("textElementsSize", n);
        for (int i = 0; i < n; ++i) {
            FDSTagCompound fDSTagCompound = new FDSTagCompound("textElement" + i);
            this.textElements.get(i).writeToFDS(fDSTagCompound);
            root.setTagCompound("textElement" + i, fDSTagCompound);
        }
    }

    public void read(@NotNull ByteBuf buf) throws IOException {
        int n;
        this.fadeFromDuration = buf.readFloat();
        this.fadeToDuration = buf.readFloat();
        this.startRoll = buf.readFloat();
        this.endRoll = buf.readFloat();
        this.startRelativeMode = CutscenePointMode.values()[buf.readInt()];
        this.endRelativeMode = CutscenePointMode.values()[buf.readInt()];
        this.start = new FDSPose();
        this.start.read(buf);
        this.end = new FDSPose();
        this.end.read(buf);
        this.fov = buf.readInt();
        this.durationSeconds = buf.readFloat();
        this.interpolationType = InterpolationType.values()[buf.readInt()];
        this.instructions.clear();
        int n2 = buf.readInt();
        for (n = 0; n < n2; ++n) {
            this.instructions.add(IPacket.readString((ByteBuf)buf));
        }
        this.textElements.clear();
        n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            this.textElements.add(new CutsceneTextElement(buf));
        }
    }

    public void write(@NotNull ByteBuf buf) throws IOException {
        int n;
        buf.writeFloat(this.fadeFromDuration);
        buf.writeFloat(this.fadeToDuration);
        buf.writeFloat(this.startRoll);
        buf.writeFloat(this.endRoll);
        buf.writeInt(this.startRelativeMode.ordinal());
        buf.writeInt(this.endRelativeMode.ordinal());
        this.start.write(buf);
        this.end.write(buf);
        buf.writeInt(this.fov);
        buf.writeFloat(this.durationSeconds);
        buf.writeInt(this.interpolationType.ordinal());
        int n2 = this.instructions.size();
        buf.writeInt(n2);
        for (n = 0; n < n2; ++n) {
            IPacket.writeString((ByteBuf)buf, (String)this.instructions.get(n));
        }
        n = this.textElements.size();
        buf.writeInt(n);
        for (int i = 0; i < n; ++i) {
            this.textElements.get(i).write(buf);
        }
    }
}

