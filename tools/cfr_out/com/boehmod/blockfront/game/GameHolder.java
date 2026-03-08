/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.game.AbstractGame;
import javax.annotation.Nullable;

public interface GameHolder {
    @Nullable
    public AbstractGame<?, ?, ?> getGame();

    public void setGame(@Nullable AbstractGame<?, ?, ?> var1);
}

