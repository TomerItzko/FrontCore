/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Matrix4fc;

public interface BF_1146 {
    public boolean method_5578(@NotNull Camera var1, @NotNull Matrix4f var2);

    public static class BF_1148
    implements BF_1146 {
        private float field_6489 = 0.0f;
        private boolean field_6488 = true;

        @Override
        public boolean method_5578(@NotNull Camera camera, @NotNull Matrix4f matrix4f) {
            float f = camera.getYRot();
            if (Math.abs(f - this.field_6489) > 0.1f) {
                this.field_6488 = true;
                this.field_6489 = f;
            }
            if (this.field_6488) {
                matrix4f.identity();
                float f2 = (float)Math.toRadians(-f);
                matrix4f.rotateY(f2);
                this.field_6488 = false;
                return true;
            }
            return false;
        }
    }

    public static class BF_1147
    implements BF_1146 {
        private final Matrix4f field_6487 = new Matrix4f();
        private boolean field_6486 = true;

        @Override
        public boolean method_5578(@NotNull Camera camera, @NotNull Matrix4f matrix4f) {
            Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
            if (!matrix4fStack.equals((Object)this.field_6487)) {
                this.field_6486 = true;
                this.field_6487.set((Matrix4fc)matrix4fStack);
            }
            if (this.field_6486) {
                matrix4fStack.invert(matrix4f);
                this.field_6486 = false;
                return true;
            }
            return false;
        }
    }
}

