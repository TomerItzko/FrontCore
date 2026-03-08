/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.ac;

import com.boehmod.blockfront.common.ac.BFAbstractAntiCheat;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.ac.BFServerScreenshotManager;

public class BFServerAntiCheat
extends BFAbstractAntiCheat<BFServerManager, BFServerScreenshotManager> {
    public BFServerAntiCheat() {
        super(new BFServerScreenshotManager());
    }
}

