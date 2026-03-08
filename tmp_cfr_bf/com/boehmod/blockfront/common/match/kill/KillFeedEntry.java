/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.match.kill;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.match.kill.AbstractKillSection;
import com.boehmod.blockfront.common.match.kill.KillEntryType;
import com.boehmod.blockfront.common.match.kill.KillSectionRegistry;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class KillFeedEntry {
    @NotNull
    private final ObjectList<AbstractKillSection> sections = new ObjectArrayList();
    public int field_3307 = 0;
    @NotNull
    private KillEntryType type = KillEntryType.DEFAULT;
    public static final int field_3308 = 240;
    private float field_3305;
    private float field_3306 = 0.0f;
    @Nullable
    private UUID field_3309;
    @Nullable
    private UUID field_3310;

    @NotNull
    public KillFeedEntry type(@NotNull KillEntryType type) {
        this.type = type;
        return this;
    }

    @NotNull
    public KillEntryType getType() {
        return this.type;
    }

    public boolean method_3209(@NotNull Minecraft minecraft, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        ++this.field_3307;
        float f = this.method_3210(minecraft.font);
        this.field_3306 = this.field_3305;
        if (this.field_3305 < f) {
            this.field_3305 = Mth.lerp((float)0.2f, (float)this.field_3305, (float)f);
            if (this.field_3305 > f) {
                this.field_3305 = f;
            }
        }
        for (AbstractKillSection abstractKillSection : this.sections) {
            abstractKillSection.update(minecraft, abstractGame, this);
        }
        return this.field_3307 > 240;
    }

    public void render(@NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull AbstractGame<?, ?, ?> abstractGame, boolean bl, float f) {
        PoseStack poseStack = guiGraphics.pose();
        float f2 = MathUtils.lerpf1(this.field_3305, this.field_3306, f);
        float f3 = this.method_3210(font);
        poseStack.translate(-f3 + f2, 0.0f, 0.0f);
        this.method_3214(guiGraphics, poseStack, f3);
        poseStack.pushPose();
        for (AbstractKillSection abstractKillSection : this.sections) {
            float f4 = abstractKillSection.getWidth(font);
            if (bl) {
                int n = abstractKillSection.method_3223();
                BFRendering.rectangle(poseStack, guiGraphics, 0.0f, 0.0f, f4, 11.0f, n, 0.2f);
                BFRendering.rectangle(poseStack, guiGraphics, 0.0f, 0.0f, f4, 1.0f, n);
            }
            abstractKillSection.render(poseStack, guiGraphics, font, abstractGame, this, f);
            poseStack.translate(f4, 0.0f, 0.0f);
        }
        poseStack.popPose();
    }

    private void method_3214(@NotNull GuiGraphics guiGraphics, PoseStack poseStack, float f) {
        BFRendering.rectangle(poseStack, guiGraphics, -2.0f, -2.0f, f + 3.0f, 1.0f, -14606047);
        BFRendering.rectangle(poseStack, guiGraphics, -3.0f, -1.0f, f + 5.0f, 13.0f, -14606047);
        BFRendering.rectangle(poseStack, guiGraphics, -2.0f, 12.0f, f + 3.0f, 1.0f, -14606047);
        int n = this.type.getOutlineColorTop();
        int n2 = this.type.getOutlineColorBottom();
        BFRendering.rectangle(poseStack, guiGraphics, -2.0f, -1.0f, f + 3.0f, 1.0f, n);
        BFRendering.rectangle(poseStack, guiGraphics, -2.0f, 11.0f, f + 3.0f, 1.0f, n2);
        BFRendering.rectangleGradient(guiGraphics, -2, 0, 1, 11, n, n2);
        BFRendering.rectangleGradient(poseStack, guiGraphics, f, 0.0f, 1.0f, 11.0f, n, n2);
    }

    public float method_3210(@NotNull Font font) {
        float f = 0.0f;
        for (AbstractKillSection abstractKillSection : this.sections) {
            f += (float)abstractKillSection.getWidth(font);
        }
        return f;
    }

    public void addSection(@NotNull AbstractKillSection section) {
        this.sections.add((Object)section);
    }

    public void method_3213(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this.field_3309 = registryFriendlyByteBuf.readBoolean() ? registryFriendlyByteBuf.readUUID() : null;
        this.field_3310 = registryFriendlyByteBuf.readBoolean() ? registryFriendlyByteBuf.readUUID() : null;
        int n = registryFriendlyByteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            this.method_3216(registryFriendlyByteBuf);
        }
    }

    private void method_3216(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        Class<?> clazz = KillSectionRegistry.toClass(registryFriendlyByteBuf.readByte());
        if (clazz == null) {
            return;
        }
        try {
            AbstractKillSection abstractKillSection = (AbstractKillSection)clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            abstractKillSection.read(registryFriendlyByteBuf);
            this.sections.add((Object)abstractKillSection);
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            BFLog.logThrowable("Error while reading kill-feed data.", reflectiveOperationException, new Object[0]);
        }
    }

    public void method_3215(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeBoolean(this.field_3309 != null);
        if (this.field_3309 != null) {
            registryFriendlyByteBuf.writeUUID(this.field_3309);
        }
        registryFriendlyByteBuf.writeBoolean(this.field_3310 != null);
        if (this.field_3310 != null) {
            registryFriendlyByteBuf.writeUUID(this.field_3310);
        }
        registryFriendlyByteBuf.writeInt(this.sections.size());
        for (AbstractKillSection abstractKillSection : this.sections) {
            byte by = KillSectionRegistry.toId(abstractKillSection.getClass());
            registryFriendlyByteBuf.writeByte(by);
            abstractKillSection.write(registryFriendlyByteBuf);
        }
    }

    public static KillFeedEntry method_3212(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        KillFeedEntry killFeedEntry = new KillFeedEntry();
        killFeedEntry.method_3213(registryFriendlyByteBuf);
        return killFeedEntry;
    }

    public void detectType(Minecraft minecraft) {
        UUID uUID = minecraft.getUser().getProfileId();
        if (uUID.equals(this.field_3310)) {
            this.type = KillEntryType.SELF;
        } else if (uUID.equals(this.field_3309)) {
            this.type = KillEntryType.SELF_DEATH;
        }
    }

    public void method_3217(@NotNull UUID uUID) {
        this.field_3309 = uUID;
    }

    public void method_3218(@NotNull UUID uUID) {
        this.field_3310 = uUID;
    }
}

