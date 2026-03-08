/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.match.kill;

public enum KillEntryType {
    DEFAULT(-11184811, -12303292),
    SELF(0x77FFFFFF, 0x55FFFFFF),
    SELF_DEATH(2013227112, 1442801768);

    private final int outlineColorTop;
    private final int outlineColorBottom;

    private KillEntryType(int outlineColorTop, int outlineColorBottom) {
        this.outlineColorTop = outlineColorTop;
        this.outlineColorBottom = outlineColorBottom;
    }

    public int getOutlineColorTop() {
        return this.outlineColorTop;
    }

    public int getOutlineColorBottom() {
        return this.outlineColorBottom;
    }
}

