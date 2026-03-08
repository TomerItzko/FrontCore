/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.util.FastColor
 *  net.minecraft.util.FastColor$ARGB32
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3d
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.util.math;

import com.boehmod.blockfront.util.math.FDSPose;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class MathUtils {
    public static final float PI = (float)Math.PI;
    public static final float DEG_TO_RAD_RATIO = (float)Math.PI / 180;
    public static final float HALF_PI = 1.5707964f;
    private static final float field_4327 = 100.0f;

    public static int withAlphaf(int color, float alpha) {
        return FastColor.ARGB32.color((int)FastColor.as8BitChannel((float)alpha), (int)color);
    }

    public static int method_5745(int n, int n2, int n3) {
        if (n == n2) {
            return n;
        }
        int n4 = Integer.signum(n2 - n);
        int n5 = n + n4 * n3;
        return n4 > 0 ? Math.min(n5, n2) : Math.max(n5, n2);
    }

    public static float moveTowards(float from, float to, float amount) {
        if (from == to) {
            return from;
        }
        float f = Math.signum(to - from);
        float f2 = from + f * amount;
        return f > 0.0f ? Math.min(f2, to) : Math.max(f2, to);
    }

    @NotNull
    public static Vec3 moveTowards(@NotNull Vec3 from, @NotNull Vec3 to, float amount) {
        Vec3 vec3 = to.subtract(from).normalize();
        double d = from.distanceTo(to);
        if (d <= (double)amount) {
            return to;
        }
        return from.add(vec3.multiply((double)amount, (double)amount, (double)amount));
    }

    @NotNull
    public static Vec3 averageMc(@NotNull List<Vec3> vecs) {
        double d = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        for (Vec3 vec3 : vecs) {
            d += vec3.x;
            d2 += vec3.y;
            d3 += vec3.z;
        }
        int n = vecs.size();
        return new Vec3(d / (double)n, d2 / (double)n, d3 / (double)n);
    }

    @NotNull
    public static Vector3f averageJoml(Vector3f ... vecs) {
        int n = vecs.length;
        if (n == 0) {
            throw new IllegalArgumentException("No positions provided");
        }
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        for (Vector3f vector3f : vecs) {
            f += vector3f.x;
            f2 += vector3f.y;
            f3 += vector3f.z;
        }
        return new Vector3f(f /= (float)n, f2 /= (float)n, f3 /= (float)n);
    }

    public static Vector3f clampPartsf(@NotNull Vector3f vec, float min, float max) {
        return new Vector3f(Math.min(max, Math.max(min, vec.x)), Math.min(max, Math.max(min, vec.y)), Math.min(max, Math.max(min, vec.z)));
    }

    public static Vector3d clampPartsd(@NotNull Vector3d vec, double min, double max) {
        return new Vector3d(Math.min(max, Math.max(min, vec.x)), Math.min(max, Math.max(min, vec.y)), Math.min(max, Math.max(min, vec.z)));
    }

    public static double lerpdFt(double max, double min, float t) {
        return min + (max - min) * (double)t;
    }

    public static float lerpf1(float max, float min, float t) {
        return min + (max - min) * t;
    }

    public static float rotateLerpf(float max, float min, float t) {
        float f = max - min;
        if (f > 180.0f) {
            f -= 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return min + t * f;
    }

    @NotNull
    public static Vec3 lerp(@NotNull Vec3 max, @NotNull Vec3 min, float t) {
        return new Vec3(MathUtils.lerpdFt(max.x, min.x, t), MathUtils.lerpdFt(max.y, min.y, t), MathUtils.lerpdFt(max.z, min.z, t));
    }

    @NotNull
    public static Vec2 lerp(@NotNull Vec2 max, @NotNull Vec2 min, float t) {
        return new Vec2(MathUtils.lerpf1(max.x, min.x, t), MathUtils.lerpf1(max.y, min.y, t));
    }

    @NotNull
    public static Vec3 lerpIntoVec3(@NotNull FDSPose max, @NotNull FDSPose min, float t) {
        return MathUtils.lerp(max.position, min.position, t);
    }

    public static float randomLerp(float min, float max) {
        return ThreadLocalRandom.current().nextFloat() * (max - min) + min;
    }

    @NotNull
    public static Vec3 stepUpAndDrift(@NotNull RandomSource random, @NotNull Vec3 origin, int magnitude) {
        return new Vec3(origin.x + (double)magnitude * random.nextGaussian(), origin.y + 1.0, origin.z + (double)magnitude * random.nextGaussian());
    }

    public static int lerpColor(int min, int max, float t) {
        int n = min >> 24 & 0xFF;
        int n2 = min >> 16 & 0xFF;
        int n3 = min >> 8 & 0xFF;
        int n4 = min & 0xFF;
        int n5 = max >> 24 & 0xFF;
        int n6 = max >> 16 & 0xFF;
        int n7 = max >> 8 & 0xFF;
        int n8 = max & 0xFF;
        int n9 = (int)((float)n + (float)(n5 - n) * t);
        int n10 = (int)((float)n2 + (float)(n6 - n2) * t);
        int n11 = (int)((float)n3 + (float)(n7 - n3) * t);
        int n12 = (int)((float)n4 + (float)(n8 - n4) * t);
        return n9 << 24 | n10 << 16 | n11 << 8 | n12;
    }

    @NotNull
    public static Vec3 lookingVec(float rotX, float rotY) {
        float f = rotX * ((float)Math.PI / 180);
        float f2 = -rotY * ((float)Math.PI / 180);
        float f3 = Mth.cos((float)f2);
        float f4 = Mth.sin((float)f2);
        float f5 = Mth.cos((float)f);
        float f6 = Mth.sin((float)f);
        return new Vec3((double)(f4 * f5), (double)(-f6), (double)(f3 * f5));
    }

    @NotNull
    public static Vec3 method_5814(@NotNull Vec2 vec2, float f, float f2) {
        return MathUtils.lookingVec(vec2.x + f, vec2.y + f2);
    }

    @NotNull
    public static Vec3 lookingVec(@NotNull Entity entity, float xRotOffset, float yRotOffset) {
        return MathUtils.lookingVec(entity.getXRot() + xRotOffset, entity.getYRot() + yRotOffset);
    }

    public static float yaw(@NotNull Vec3 vec1, @NotNull Vec3 vec2) {
        Vec3 vec3 = vec2.subtract(vec1);
        float f = (float)Math.atan2(vec3.z, vec3.x);
        float f2 = (float)Math.toDegrees(f);
        if (f2 < 0.0f) {
            f2 += 360.0f;
        }
        return f2;
    }

    @NotNull
    public static Optional<Vec3> intersect(@NotNull Vec3 origin, @NotNull Vec3 direction, @NotNull AABB box) {
        double d;
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7 = 1.0 / direction.x;
        double d8 = 1.0 / direction.y;
        double d9 = 1.0 / direction.z;
        double d10 = (box.minX - origin.x) * d7;
        double d11 = (box.maxX - origin.x) * d7;
        if (d7 >= 0.0) {
            d6 = d10;
            d5 = d11;
        } else {
            d6 = d11;
            d5 = d10;
        }
        double d12 = (box.minY - origin.y) * d8;
        double d13 = (box.maxY - origin.y) * d8;
        if (d8 >= 0.0) {
            d4 = d12;
            d3 = d13;
        } else {
            d4 = d13;
            d3 = d12;
        }
        if (d6 > d3 || d4 > d5) {
            return Optional.empty();
        }
        if (d4 > d6) {
            d6 = d4;
        }
        if (d3 < d5) {
            d5 = d3;
        }
        double d14 = (box.minZ - origin.z) * d9;
        double d15 = (box.maxZ - origin.z) * d9;
        if (d9 >= 0.0) {
            d2 = d14;
            d = d15;
        } else {
            d2 = d15;
            d = d14;
        }
        if (d6 > d || d2 > d5) {
            return Optional.empty();
        }
        if (d2 > d6) {
            d6 = d2;
        }
        if (d < d5) {
            d5 = d;
        }
        if (d6 < 0.0 && d5 < 0.0) {
            return Optional.empty();
        }
        double d16 = d6 >= 0.0 ? d6 : d5;
        Vec3 vec3 = origin.add(direction.scale(d16));
        return Optional.of(vec3);
    }

    public static float lerpf2(float min, float max, float t) {
        return min + (max - min) * t;
    }

    public static double lerpd(double min, double max, double t) {
        return min + (max - min) * t;
    }

    public static float wrapDegrees(float angle) {
        if ((angle %= 360.0f) > 180.0f) {
            angle -= 360.0f;
        } else if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static float yawDifference(@NotNull Entity entity1, @NotNull Entity entity2) {
        return MathUtils.yawDifference(entity1.getX(), entity1.getZ(), entity2.getX(), entity2.getZ(), entity2.getYRot());
    }

    public static float yawDifference(double fromX, double fromZ, double toX, double toZ, float toYaw) {
        float f = (toYaw % 360.0f + 360.0f) % 360.0f;
        double d = fromZ - toZ;
        double d2 = fromX - toX;
        float f2 = (float)Math.toDegrees(Math.atan2(d, d2)) + 90.0f;
        float f3 = (f2 + 360.0f) % 360.0f;
        float f4 = Math.abs(f - f3);
        if (f4 > 180.0f) {
            f4 = 360.0f - f4;
        }
        return f4 / 180.0f;
    }

    public static float getTickDelta(@NotNull Minecraft minecraft) {
        return minecraft.getTimer().getGameTimeDeltaPartialTick(true);
    }

    public static float normalRevClampSlope(float min, float max, float t) {
        if (t <= min) {
            return 1.0f;
        }
        if (t >= max) {
            return 0.0f;
        }
        return 1.0f - (t - min) / (max - min);
    }

    public static int roundDiv5(float value) {
        float f = value / 100.0f;
        return Math.round(f * 20.0f);
    }

    public static float snapToMultiple(float value, float step) {
        return (float)Math.round(value / step) * step;
    }

    public static boolean withinDistance(@NotNull BlockPos blockPos, @NotNull Vec3 vecPos, double distance) {
        double d;
        double d2 = (double)blockPos.getX() + 0.5 - vecPos.x;
        double d3 = d2 * d2 + (d = (double)blockPos.getZ() + 0.5 - vecPos.z) * d;
        return d3 <= distance * distance;
    }

    public static float getScaledBoxTextWidth(@NotNull String text, @NotNull Font font, float scale, int maxWidth, float maxScale) {
        float f = (float)font.width(text) * scale;
        if (f <= (float)maxWidth) {
            return scale;
        }
        float f2 = (float)maxWidth / (float)font.width(text);
        return Math.min(f2, maxScale);
    }

    @NotNull
    public static Vec3 directionVec(@NotNull Direction direction, float magnitude) {
        return switch (direction) {
            default -> throw new MatchException(null, null);
            case Direction.UP -> new Vec3(0.0, (double)magnitude, 0.0);
            case Direction.DOWN -> new Vec3(0.0, (double)(-magnitude), 0.0);
            case Direction.NORTH -> new Vec3(0.0, 0.0, (double)(-magnitude));
            case Direction.EAST -> new Vec3((double)magnitude, 0.0, 0.0);
            case Direction.SOUTH -> new Vec3(0.0, 0.0, (double)magnitude);
            case Direction.WEST -> new Vec3((double)(-magnitude), 0.0, 0.0);
        };
    }
}

