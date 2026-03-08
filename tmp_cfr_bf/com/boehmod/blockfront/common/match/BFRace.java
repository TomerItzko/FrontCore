/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.match;

public enum BFRace {
    CAUCASIAN("caucasian"),
    BLACK("black"),
    ASIAN("asian");

    private final String skin;

    private BFRace(String skin) {
        this.skin = skin;
    }

    public String getSkin() {
        return this.skin;
    }
}

