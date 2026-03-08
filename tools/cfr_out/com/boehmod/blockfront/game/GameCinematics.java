/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.client.Camera
 *  net.minecraft.client.CameraType
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.ReceivingLevelScreen
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.camera.CutscenePoint;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.entity.CameraEntity;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.GameSequence;
import com.boehmod.blockfront.game.GameSequenceSettings;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.unnamed.BF_605;
import com.boehmod.blockfront.unnamed.BF_643;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GameCinematics {
    @NotNull
    private final List<BF_605> field_2507 = new ObjectArrayList();
    @NotNull
    private final List<BF_605> field_2508 = new ObjectArrayList();
    private static final int field_2526 = 20;
    @NotNull
    private static final ResourceLocation field_2515 = BFRes.loc("textures/gui/menu/icons/mouse.png");
    @NotNull
    private static final ResourceLocation field_2516 = BFRes.loc("textures/gui/menu/icons/mouse_button_right.png");
    @NotNull
    private static final ResourceLocation field_2517 = BFRes.loc("textures/gui/shadoweffect.png");
    @NotNull
    private static final Component field_2533 = Component.translatable((String)"bf.message.skip");
    private static final int field_2527 = 20;
    private static final int field_2528 = 6;
    @NotNull
    private final BFClientManager manager;
    @Nullable
    private GameSequence sequence;
    @Nullable
    private GameSequenceSettings musicData = null;
    private int field_2529;
    private float field_2514;
    private boolean isSequencePlaying;
    private int field_2530 = 20;
    private int field_2531 = 0;
    private boolean field_2511 = false;
    private float field_2503;
    private float field_2504;
    private float field_2519;
    private float field_2520;
    @Nullable
    private CameraEntity camera;
    private Vec3 field_2534;
    private Vec3 field_2535;
    private int field_2532;
    private float field_2521;
    private float field_2522;
    private float field_2523;
    private float field_2524;
    private float field_2513;
    private float field_2525;

    public GameCinematics(@NotNull BFClientManager manager) {
        this.manager = manager;
    }

    @Nullable
    public GameSequence getSequence() {
        return this.sequence;
    }

    public void method_2198(@NotNull BF_605 bF_605) {
        this.field_2507.add(bF_605);
    }

    public void method_2205(@NotNull BF_605 bF_605) {
        this.field_2508.add(bF_605);
    }

    private void method_2211(@NotNull Minecraft minecraft, @Nullable LocalPlayer localPlayer, @Nullable ClientLevel clientLevel) {
        this.field_2507.removeIf(bF_605 -> bF_605.method_2222(minecraft, this.manager, localPlayer, clientLevel));
        this.field_2508.removeIf(bF_605 -> bF_605.method_2222(minecraft, this.manager, localPlayer, clientLevel));
    }

    public void method_2201(@NotNull Minecraft minecraft, @NotNull GameSequence gameSequence, @Nullable GameSequenceSettings gameSequenceSettings) {
        this.stop(minecraft);
        this.musicData = gameSequenceSettings;
        this.field_2532 = (Integer)minecraft.options.fov().get();
        this.sequence = gameSequence;
        this.isSequencePlaying = true;
        minecraft.options.setCameraType(CameraType.FIRST_PERSON);
        for (CutscenePoint cutscenePoint : gameSequence.method_2185()) {
            cutscenePoint.method_2172(minecraft.player);
        }
        this.method_2200(minecraft, gameSequence.method_2185().getFirst());
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null";
        bFClientManager.queueChunkReload();
    }

    public void stop(@NotNull Minecraft minecraft) {
        if (!this.isSequencePlaying) {
            return;
        }
        this.isSequencePlaying = false;
        if (this.musicData != null && this.musicData.getStopMusicOnFinish()) {
            this.manager.getMusicManager().method_1532();
        }
        this.method_2215(minecraft);
        if (this.sequence != null && this.sequence.method_2180()) {
            this.field_2519 = 1.0f;
            this.field_2520 = 1.0f;
        }
        if (this.camera != null && this.camera.isAlive()) {
            this.camera.discard();
            this.camera = null;
        }
        if (minecraft.player != null) {
            ClientUtils.setCameraEntity(minecraft, (Entity)minecraft.player);
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null";
        bFClientManager.queueChunkReload();
    }

    private void method_2215(@NotNull Minecraft minecraft) {
        this.field_2530 = 20;
        this.field_2529 = 0;
        this.field_2514 = 0.0f;
        this.musicData = null;
        if (this.field_2532 != -1) {
            minecraft.options.fov().set((Object)this.field_2532);
        }
    }

    private void method_2200(@NotNull Minecraft minecraft, @NotNull CutscenePoint cutscenePoint) {
        this.field_2535 = this.field_2534 = cutscenePoint.start.position;
        this.field_2523 = this.field_2524 = cutscenePoint.start.rotation.x;
        this.field_2521 = this.field_2522 = cutscenePoint.start.rotation.y;
        this.field_2513 = this.field_2525 = cutscenePoint.getStartRoll();
        this.field_2514 = 0.0f;
        this.method_2197(cutscenePoint);
        minecraft.options.fov().set((Object)Math.max(cutscenePoint.getFOV(), 30));
    }

    private void method_2197(@NotNull CutscenePoint cutscenePoint) {
        for (String string : cutscenePoint.getInstructions()) {
            String string2;
            String[] stringArray = string.split(" ");
            if (stringArray.length == 0) continue;
            switch (string2 = stringArray[0]) {
                case "summary": {
                    AbstractGameClient<?, ?> abstractGameClient = this.manager.getGameClient();
                    if (abstractGameClient == null) break;
                    abstractGameClient.getSummaryManager().updateScreens(false);
                }
            }
        }
    }

    public void update(@NotNull Minecraft minecraft, @Nullable LocalPlayer localPlayer, @Nullable ClientLevel clientLevel) {
        this.method_2211(minecraft, localPlayer, clientLevel);
        if (!this.isSequencePlaying) {
            this.field_2520 = this.field_2519;
            this.field_2519 = MathUtils.lerpf2(this.field_2519, 0.0f, 0.1f);
            return;
        }
        if (clientLevel == null || localPlayer == null) {
            this.stop(minecraft);
            return;
        }
        this.field_2504 = this.field_2503;
        this.field_2503 = this.method_2192();
        --this.field_2530;
        if (this.field_2531-- <= 0) {
            this.field_2531 = 6;
            boolean bl = this.field_2511 = !this.field_2511;
        }
        if (minecraft.screen instanceof ReceivingLevelScreen) {
            return;
        }
        this.method_2212(clientLevel);
        this.method_2216(minecraft);
        if (this.sequence != null && this.sequence.method_2179() && this.field_2531 <= 0 && minecraft.mouseHandler.isLeftPressed()) {
            this.stop(minecraft);
        }
    }

    private void method_2216(@NotNull Minecraft minecraft) {
        if (this.sequence == null) {
            return;
        }
        List<CutscenePoint> list = this.sequence.method_2185();
        if (this.field_2529 >= list.size()) {
            if (this.sequence.method_2178()) {
                this.field_2529 = 0;
                this.method_2200(minecraft, list.getFirst());
            } else {
                this.stop(minecraft);
                return;
            }
        }
        CutscenePoint cutscenePoint = list.get(this.field_2529);
        this.field_2535 = this.field_2534;
        this.field_2522 = this.field_2521;
        this.field_2524 = this.field_2523;
        this.field_2525 = this.field_2513;
        this.field_2514 += 1.0f / (cutscenePoint.getDurationSeconds() * 20.0f);
        if (this.field_2514 >= 1.0f) {
            ++this.field_2529;
            if (this.field_2529 < list.size()) {
                this.method_2200(minecraft, list.get(this.field_2529));
            }
            return;
        }
        FDSPose fDSPose = cutscenePoint.method_2153(this.field_2514);
        this.field_2534 = new Vec3(fDSPose.getX(), fDSPose.getY(), fDSPose.getZ());
        this.field_2521 = fDSPose.rotation.y;
        this.field_2523 = fDSPose.rotation.x;
        this.field_2513 = cutscenePoint.method_2148();
    }

    public void method_2202(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2, float f) {
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        this.method_2207(minecraft, poseStack, guiGraphics, font, n, n2, f);
        if (!this.isSequencePlaying) {
            float f2 = MathUtils.lerpf1(this.field_2519, this.field_2520, f);
            if (f2 > 0.0f) {
                BFRendering.rectangle(guiGraphics, 0, 0, n, n2, 0, f2);
            }
            return;
        }
        if (minecraft.screen instanceof ReceivingLevelScreen) {
            BFRendering.rectangle(guiGraphics, 0, 0, n, n2, ColorReferences.COLOR_BLACK_SOLID);
        }
        if (this.camera != null) {
            double d = MathUtils.lerpdFt(this.field_2534.x, this.field_2535.x, f);
            double d2 = MathUtils.lerpdFt(this.field_2534.y, this.field_2535.y, f);
            double d3 = MathUtils.lerpdFt(this.field_2534.z, this.field_2535.z, f);
            float f3 = MathUtils.rotateLerpf(this.field_2521, this.field_2522, f);
            float f4 = MathUtils.rotateLerpf(this.field_2523, this.field_2524, f);
            this.camera.absMoveTo(d, d2, d3, f3, f4);
        }
        BFRendering.texture(poseStack, guiGraphics, field_2517, 0, 0, n, n2);
        float f5 = MathUtils.lerpf1(this.field_2503, this.field_2504, f);
        if (f5 > 0.0f) {
            BFRendering.rectangle(guiGraphics, 0, 0, n, n2, ColorReferences.COLOR_BLACK_SOLID, f5);
        }
        if (this.sequence != null && this.sequence.method_2179() && this.field_2530 <= 0) {
            this.method_2199(poseStack, guiGraphics, font, n, n2);
        }
        poseStack.popPose();
    }

    private void method_2207(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2, float f) {
        for (BF_605 bF_605 : this.field_2507) {
            bF_605.method_2223(minecraft, poseStack, guiGraphics, font, n, n2, f);
        }
    }

    public void method_2210(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2, float f) {
        for (BF_605 bF_605 : this.field_2508) {
            bF_605.method_2223(minecraft, poseStack, guiGraphics, font, n, n2, f);
        }
    }

    private void method_2199(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2) {
        int n3 = 10;
        Component component = field_2533;
        int n4 = font.width((FormattedText)component);
        int n5 = n - n4 - 10 + 1;
        Objects.requireNonNull(font);
        int n6 = n2 - 9 - 10 + 1;
        BFRendering.drawString(font, guiGraphics, component, n5, n6);
        int n7 = 16;
        int n8 = n5 - 16 - 3;
        Objects.requireNonNull(font);
        int n9 = n6 + (9 - 16) / 2 - 1;
        BFRendering.texture(poseStack, guiGraphics, field_2515, n8, n9, 16, 16);
        if (this.field_2511) {
            BFRendering.texture(poseStack, guiGraphics, field_2516, n8, n9, 16, 16);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void render(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics guiGraphics, @NotNull Camera camera) {
        if (this.sequence == null) {
            return;
        }
        List<CutscenePoint> list = this.sequence.method_2185();
        if (this.field_2529 >= list.size()) {
            return;
        }
        CutscenePoint cutscenePoint = list.get(this.field_2529);
        if (cutscenePoint != null) {
            cutscenePoint.renderTextElements(poseStack, font, guiGraphics, camera);
        }
    }

    private float method_2192() {
        float f;
        float f2;
        if (this.sequence == null) {
            return 0.0f;
        }
        List<CutscenePoint> list = this.sequence.method_2185();
        if (this.field_2529 >= list.size()) {
            return 0.0f;
        }
        CutscenePoint cutscenePoint = list.get(this.field_2529);
        float f3 = cutscenePoint.getDurationSeconds();
        float f4 = cutscenePoint.getFadeFromDuration();
        if (f4 > 0.0f && (f2 = this.field_2514 / (f4 / f3)) < 1.0f) {
            return 1.0f - f2;
        }
        f2 = cutscenePoint.getFadeToDuration();
        if (f2 > 0.0f && this.field_2514 > (f = 1.0f - f2 / f3)) {
            return (this.field_2514 - f) / (1.0f - f);
        }
        return 0.0f;
    }

    private void method_2212(@NotNull ClientLevel clientLevel) {
        if (this.camera == null || !this.camera.isAlive()) {
            this.camera = (CameraEntity)((EntityType)BFEntityTypes.CAMERA.get()).create((Level)clientLevel);
            if (this.camera != null) {
                this.camera.method_2490(BF_643.CINEMATIC);
                ClientUtils.spawnEntity(this.camera, clientLevel);
            }
        }
    }

    public float getCurrentRoll(float delta) {
        return MathUtils.rotateLerpf(this.field_2513, this.field_2525, delta);
    }

    public boolean isSequencePlaying() {
        return this.isSequencePlaying;
    }

    public float method_2193() {
        return this.field_2514;
    }

    public int method_2195() {
        return this.field_2529;
    }

    @Nullable
    public CameraEntity getCamera() {
        return this.camera;
    }
}

