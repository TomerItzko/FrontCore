package dev.tomerdev.mercfrontcore.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class AddonRendering {
    private AddonRendering() {
    }

    public static void cameraAsOrigin(MatrixStack matrices, Camera camera) {
        Vec3d pos = camera.getPos();
        matrices.translate(-pos.x, -pos.y, -pos.z);
    }

    public static void translate(MatrixStack matrices, Vec3d vec) {
        matrices.translate(vec.x, vec.y, vec.z);
    }

    public static void depthTest(boolean enabled) {
        if (enabled) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
    }

    public static void billboard(MatrixStack matrices, Camera camera) {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
    }

    public static void billboardString(
        MatrixStack matrices,
        Camera camera,
        TextRenderer textRenderer,
        VertexConsumerProvider.Immediate vertexConsumers,
        String text,
        Vec3d position,
        float scale,
        boolean depthTest
    ) {
        matrices.push();
        translate(matrices, position);
        billboard(matrices, camera);
        float s = -0.025f * scale;
        matrices.scale(s, s, s);
        depthTest(depthTest);
        textRenderer.draw(
            text,
            -textRenderer.getWidth(text) / 2f,
            0,
            0xFFFFFFFF,
            true,
            matrices.peek().getPositionMatrix(),
            vertexConsumers,
            TextRenderer.TextLayerType.NORMAL,
            0,
            LightmapTextureManager.MAX_LIGHT_COORDINATE
        );
        matrices.pop();
    }

    public static void texture(
        MatrixStack matrices,
        Identifier texture,
        float x,
        float y,
        float z,
        float width,
        float height,
        float alpha,
        boolean depthTest
    ) {
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1, 1, 1, alpha);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        float x2 = x + width;
        float y2 = y + height;
        builder.vertex(matrix, x, y, z).texture(0, 0);
        builder.vertex(matrix, x, y2, z).texture(0, 1);
        builder.vertex(matrix, x2, y2, z).texture(1, 1);
        builder.vertex(matrix, x2, y, z).texture(1, 0);

        depthTest(depthTest);
        BufferRenderer.drawWithGlobalProgram(builder.end());
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    public static void billboardTexture(
        MatrixStack matrices,
        Camera camera,
        Identifier texture,
        Vec3d position,
        float width,
        float height,
        float alpha,
        boolean depthTest
    ) {
        matrices.push();
        translate(matrices, position);
        billboard(matrices, camera);
        matrices.scale(-1, -1, 1);
        texture(matrices, texture, -width / 2f, -height / 2f, 0, width, height, alpha, depthTest);
        matrices.pop();
    }

    public static void line(MatrixStack matrices, Vec3d start, Vec3d end, int color, boolean depthTest) {
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);

        MatrixStack.Entry matrixEntry = matrices.peek();
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        Vec3d dir = end.subtract(start).normalize();
        builder.vertex(matrixEntry, start.toVector3f()).color(color).normal(matrixEntry, (float) dir.x, (float) dir.y, (float) dir.z);
        builder.vertex(matrixEntry, end.toVector3f()).color(color).normal(matrixEntry, (float) dir.x, (float) dir.y, (float) dir.z);

        depthTest(depthTest);
        BufferRenderer.drawWithGlobalProgram(builder.end());
    }

    public static void boxOutline(MatrixStack matrices, Box box, int color, boolean depthTest) {
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);

        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float a = ((color >> 24) & 0xFF) / 255f;
        WorldRenderer.drawBox(matrices, builder, box, r, g, b, a);

        depthTest(depthTest);
        BufferRenderer.drawWithGlobalProgram(builder.end());
    }
}
