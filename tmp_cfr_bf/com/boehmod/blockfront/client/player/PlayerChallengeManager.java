/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.player;

import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class PlayerChallengeManager {
    public static final int field_1273 = 100;
    private int field_1274;
    private int field_1275 = 0;

    public void update(@NotNull Minecraft minecraft, @NotNull ClientConnectionManager clientConnectionManager) {
        this.field_1275 = this.field_1274;
        if (this.field_1274++ >= 100) {
            this.field_1274 = 0;
            UUID uUID = minecraft.getUser().getProfileId();
            clientConnectionManager.getRequester().push(RequestType.PLAYER_CHALLENGES, uUID);
        }
    }

    public float method_928(float f) {
        float f2 = MathUtils.lerpf1(this.field_1274, this.field_1275, f);
        return f2 / 100.0f;
    }

    public int method_927() {
        return -(this.field_1274 - 100) / 20 + 1;
    }
}

