/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.ac;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.ac.BFClientAntiCheatThread;
import com.boehmod.blockfront.client.ac.BFClientScreenshot;
import com.boehmod.blockfront.client.ac.check.IAntiCheatCheck;
import com.boehmod.blockfront.client.ac.check.ModsCountCheck;
import com.boehmod.blockfront.client.ac.check.ModsDirectoryCheck;
import com.boehmod.blockfront.client.ac.check.PackagesCheck;
import com.boehmod.blockfront.client.ac.check.ResourcePacksCheck;
import com.boehmod.blockfront.client.ac.check.SpriteTransparencyCheck;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.ac.BFAbstractAntiCheat;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public final class BFClientAntiCheat
extends BFAbstractAntiCheat<BFClientManager, BFClientScreenshot> {
    public static final boolean field_3439 = false;
    public static boolean enabled = false;
    @NotNull
    private final ObjectList<IAntiCheatCheck<BFClientAntiCheat>> checks = new ObjectArrayList();
    private boolean startupChecksShouldRun = true;
    private boolean isActive = false;

    public BFClientAntiCheat(@NotNull BFClientManager manager) {
        super(new BFClientScreenshot());
        this.addCheck(new ModsCountCheck());
        this.addCheck(new ModsDirectoryCheck());
        this.addCheck(new ResourcePacksCheck());
        this.addCheck(new SpriteTransparencyCheck());
        this.addCheck(new PackagesCheck());
        new BFClientAntiCheatThread(manager, this).start();
    }

    public boolean isActive() {
        return this.isActive;
    }

    private void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void startupChecksShouldRun() {
        this.startupChecksShouldRun = true;
    }

    private void addCheck(@NotNull IAntiCheatCheck<BFClientAntiCheat> check) {
        this.checks.add(check);
    }

    private void doRuntimeChecks(@NotNull Minecraft minecraft) {
        if (this.isActive()) {
            return;
        }
        if (this.anyActiveForStage(Stage.RUNTIME, minecraft)) {
            this.setActive(true);
        }
    }

    public void doStartupChecks() {
        if (this.isActive()) {
            return;
        }
        if (this.anyActiveForStage(Stage.STARTUP, Minecraft.getInstance())) {
            this.setActive(true);
        }
    }

    private boolean anyActiveForStage(@NotNull Stage stage, @NotNull Minecraft minecraft) {
        for (IAntiCheatCheck iAntiCheatCheck : this.checks) {
            if (iAntiCheatCheck.getStage() != stage && iAntiCheatCheck.getStage() != Stage.ALL || !iAntiCheatCheck.run(minecraft, this)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void onUpdate(@NotNull BFClientManager bFClientManager) {
        super.onUpdate(bFClientManager);
        if (!enabled) {
            return;
        }
        if (this.startupChecksShouldRun) {
            this.startupChecksShouldRun = false;
            this.doStartupChecks();
        }
        this.doRuntimeChecks(Minecraft.getInstance());
    }

    @Override
    public /* synthetic */ void onUpdate(@NotNull BFAbstractManager manager) {
        this.onUpdate((BFClientManager)manager);
    }

    public static enum Stage {
        RUNTIME,
        STARTUP,
        ALL;

    }
}

