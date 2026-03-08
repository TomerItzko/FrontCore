/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.ac;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.util.BFLog;
import org.jetbrains.annotations.NotNull;

public final class BFClientAntiCheatThread
extends Thread {
    @NotNull
    private final BFClientManager manager;
    @NotNull
    private final BFClientAntiCheat antiCheat;

    public BFClientAntiCheatThread(@NotNull BFClientManager manager, @NotNull BFClientAntiCheat antiCheat) {
        this.manager = manager;
        this.antiCheat = antiCheat;
    }

    @Override
    public void run() {
        while (true) {
            this.antiCheat.onUpdate(this.manager);
            try {
                Thread.sleep(80L);
                continue;
            }
            catch (InterruptedException interruptedException) {
                BFLog.logThrowable("An exception was encountered.", interruptedException, new Object[0]);
                continue;
            }
            break;
        }
    }
}

