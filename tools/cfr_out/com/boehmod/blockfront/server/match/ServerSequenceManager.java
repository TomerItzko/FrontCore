/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.server.match;

import com.boehmod.blockfront.client.camera.CutscenePoint;
import com.boehmod.blockfront.game.GameSequence;
import com.boehmod.blockfront.server.match.ChunkForceLoader;
import com.boehmod.blockfront.util.math.FDSPose;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jetbrains.annotations.NotNull;

public class ServerSequenceManager {
    @NotNull
    private final ObjectList<FDSPose> field_6269 = new ObjectArrayList();
    private int field_6270 = 0;
    @NotNull
    private final ChunkForceLoader chunkLoader = new ChunkForceLoader();

    public boolean method_5452(@NotNull FDSPose fDSPose) {
        if (this.field_6270 <= 0) {
            return false;
        }
        if (this.field_6269.isEmpty()) {
            return false;
        }
        if (((FDSPose)this.field_6269.getFirst()).equals(fDSPose)) {
            this.field_6269.removeFirst();
            return true;
        }
        return false;
    }

    public void setCurrentSequence(@NotNull GameSequence sequence) {
        this.method_5454();
        int n = 0;
        for (CutscenePoint cutscenePoint : sequence.method_2185()) {
            this.field_6269.add((Object)cutscenePoint.getStart());
            n += (int)(cutscenePoint.getDurationSeconds() * 20.0f);
        }
        this.field_6270 = n += 20;
    }

    public void onUpdate() {
        if (this.field_6270 > 0) {
            --this.field_6270;
            if (this.field_6270 == 0) {
                this.method_5454();
            }
        }
    }

    private void method_5454() {
        this.field_6269.clear();
        this.field_6270 = 0;
    }

    @NotNull
    public ChunkForceLoader getChunkLoader() {
        return this.chunkLoader;
    }

    public boolean method_5455() {
        return this.field_6270 > 0;
    }
}

