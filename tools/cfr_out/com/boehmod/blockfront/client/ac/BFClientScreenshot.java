/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderTarget
 *  com.mojang.blaze3d.platform.NativeImage
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.Screenshot
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.ac;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.ac.BFAbstractScreenshotManager;
import com.boehmod.blockfront.common.net.packet.BFScreenshotResponsePacket;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.PacketUtils;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import org.jetbrains.annotations.NotNull;

public class BFClientScreenshot
extends BFAbstractScreenshotManager<BFClientManager> {
    public static final int field_3835 = 854;
    public static final int field_3836 = 480;
    @NotNull
    public Status status = Status.IDLE;

    public void method_3932() {
        this.status = Status.RECEIVED_REQUEST;
    }

    private void method_3933() throws IOException {
        BFLog.log("[AC] Creating new screenshot for server...", new Object[0]);
        NativeImage nativeImage = Screenshot.takeScreenshot((RenderTarget)Minecraft.getInstance().getMainRenderTarget());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(nativeImage.asByteArray());
        BufferedImage bufferedImage = this.method_3931(ImageIO.read(byteArrayInputStream), 854, 480);
        byteArrayInputStream.close();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage)bufferedImage, "PNG", byteArrayOutputStream);
        byte[] byArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        BFLog.log("[AC] Sending screenshot to server...", new Object[0]);
        PacketUtils.sendToServer(new BFScreenshotResponsePacket(byArray));
    }

    @NotNull
    private BufferedImage method_3931(@NotNull BufferedImage bufferedImage, int n, int n2) {
        BufferedImage bufferedImage2 = new BufferedImage(n, n2, bufferedImage.getType());
        Graphics2D graphics2D = bufferedImage2.createGraphics();
        graphics2D.drawImage(bufferedImage, 0, 0, n, n2, null);
        graphics2D.dispose();
        return bufferedImage2;
    }

    @Override
    protected void onUpdate(@NotNull BFClientManager bFClientManager) {
        switch (this.status.ordinal()) {
            case 1: {
                this.status = Status.PROCESSING_REQUEST;
                break;
            }
            case 2: {
                this.status = Status.IDLE;
                if (!RenderSystem.isOnRenderThread()) {
                    RenderSystem.recordRenderCall(() -> {
                        try {
                            this.method_3933();
                        }
                        catch (IOException iOException) {
                            BFLog.log("[AC] Failed to create new screenshot for server.", iOException);
                        }
                    });
                    break;
                }
                try {
                    this.method_3933();
                    break;
                }
                catch (IOException iOException) {
                    BFLog.log("[AC] Failed to create new screenshot for server.", iOException);
                }
            }
        }
    }

    @Override
    protected /* synthetic */ void onUpdate(@NotNull BFAbstractManager manager) {
        this.onUpdate((BFClientManager)manager);
    }

    public static enum Status {
        IDLE,
        RECEIVED_REQUEST,
        PROCESSING_REQUEST;

    }
}

