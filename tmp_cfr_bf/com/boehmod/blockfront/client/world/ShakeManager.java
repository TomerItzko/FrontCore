/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.world;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.world.ShakeApplier;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.math.MathUtils;
import com.boehmod.blockfront.util.math.ShakeNodeData;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class ShakeManager {
    @NotNull
    private static final List<ShakeApplier> ACTIVE_SHAKES = Collections.synchronizedList(new ObjectArrayList());
    private static float shakeX = 0.0f;
    private static float shakeY = 0.0f;
    private static float shakeZ = 0.0f;
    private static float prevShakeX = 0.0f;
    private static float prevShakeY = 0.0f;
    private static float prevShakeZ = 0.0f;

    public static void applyInstruction(@NotNull String instr) {
        String[] stringArray = instr.substring(6, instr.length() - 2).split(",");
        if (stringArray.length == 9) {
            try {
                float f = Float.parseFloat(stringArray[0].trim());
                float f2 = Float.parseFloat(stringArray[1].trim());
                float f3 = Float.parseFloat(stringArray[2].trim());
                float f4 = Float.parseFloat(stringArray[3].trim());
                float f5 = Float.parseFloat(stringArray[4].trim());
                float f6 = Float.parseFloat(stringArray[5].trim());
                float f7 = Float.parseFloat(stringArray[6].trim());
                float f8 = Float.parseFloat(stringArray[7].trim());
                int n = Integer.parseInt(stringArray[8].trim());
                ShakeManager.applyShake(new ShakeNodeData(f, f2, f3, f4, f5, f6, f7, f8, n));
            }
            catch (NumberFormatException numberFormatException) {
                BFLog.logError("Failed to parse shake instruction: " + instr, new Object[0]);
            }
        }
    }

    public static void applyShake(@NotNull ShakeNodeData data) {
        ShakeManager.applyShake(new ShakeApplier(data));
    }

    public static void applyShake(@NotNull ShakeNodeData data, float amplitude) {
        ShakeManager.applyShake(new ShakeApplier(data).setAmplitude(amplitude));
    }

    @OnlyIn(value=Dist.CLIENT)
    private static void applyShake(@NotNull ShakeApplier applier) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        if (bFClientManager.getCinematics().isSequencePlaying()) {
            return;
        }
        ACTIVE_SHAKES.add(applier);
    }

    public static void applyShake(@NotNull ShakeNodeData data, @NotNull LocalPlayer player, @NotNull Vec3 center, float radius) {
        Vec3 vec3 = player.position();
        float f = (float)Math.sqrt(vec3.distanceToSqr(center.x, center.y, center.z));
        float f2 = 1.0f - f / radius;
        f2 = Math.max(0.0f, Math.min(f2, 1.0f));
        ShakeManager.applyShake(data, f2);
    }

    @NotNull
    public static Vector3f getDelta(float t) {
        return new Vector3f(MathUtils.lerpf1(shakeX, prevShakeX, t), MathUtils.lerpf1(shakeY, prevShakeY, t), MathUtils.lerpf1(shakeZ, prevShakeZ, t));
    }

    public static void update(float t) {
        ACTIVE_SHAKES.removeIf(applier -> applier.update(t));
        prevShakeX = shakeX;
        prevShakeY = shakeY;
        prevShakeZ = shakeZ;
        Vector3f vector3f = ShakeManager.getCumulativeShake();
        shakeX = vector3f.x;
        shakeY = vector3f.y;
        shakeZ = vector3f.z;
    }

    @NotNull
    public static Vector3f getCumulativeShake() {
        Vector3f vector3f = new Vector3f();
        for (ShakeApplier shakeApplier : ACTIVE_SHAKES) {
            vector3f.add((Vector3fc)shakeApplier.getCurrentShake());
        }
        return vector3f;
    }
}

