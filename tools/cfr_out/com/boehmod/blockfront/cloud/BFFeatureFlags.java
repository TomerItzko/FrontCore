/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.AbstractFeatureFlagManager
 *  javax.annotation.Nullable
 */
package com.boehmod.blockfront.cloud;

import com.boehmod.bflib.cloud.common.AbstractFeatureFlagManager;
import javax.annotation.Nullable;

public class BFFeatureFlags
extends AbstractFeatureFlagManager {
    @Nullable
    public static String currentEvent = null;
    public static boolean serverShotValidation = true;
    public static boolean serverShotValidationSpread = true;
    public static boolean serverShotValidationKick = true;
    public static boolean serverShotValidationReport = true;
    public static boolean field_7091 = true;

    public BFFeatureFlags() {
        this.updateFeatureFlags();
    }

    private void updateFeatureFlags() {
        currentEvent = null;
        if (this.isEnabled("event_halloween")) {
            currentEvent = "halloween";
        } else if (this.isEnabled("event_christmas")) {
            currentEvent = "christmas";
        }
        serverShotValidation = this.isEnabled("server_shot_validation");
        serverShotValidationSpread = this.isEnabled("server_shot_validation_spread");
        serverShotValidationKick = this.isEnabled("server_shot_validation_kick");
        serverShotValidationReport = this.isEnabled("server_shot_validation_report");
    }

    protected void featureFlagsChanged() {
        this.updateFeatureFlags();
    }
}

