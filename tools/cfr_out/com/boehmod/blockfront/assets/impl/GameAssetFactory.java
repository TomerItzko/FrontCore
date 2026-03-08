/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.assets.impl;

import com.boehmod.blockfront.assets.AssetFactory;
import com.boehmod.blockfront.assets.impl.GameAsset;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.util.BFLog;
import java.lang.reflect.InvocationTargetException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GameAssetFactory
extends AssetFactory<GameAsset> {
    @Override
    @Nullable
    public GameAsset create(boolean bl, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull String string, @NotNull String string2) {
        BFGameType bFGameType = BFGameType.getByName(string2);
        if (bFGameType == null) {
            return null;
        }
        Class<AbstractGame<?, ?, ?>> clazz = bFGameType.getGameClass();
        try {
            AbstractGame<?, ?, ?> abstractGame = clazz.getDeclaredConstructor(BFAbstractManager.class).newInstance(bFAbstractManager);
            abstractGame.setName(string);
            abstractGame.reset(null);
            return new GameAsset(abstractGame, bFAbstractManager);
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            BFLog.logThrowable("Failed to create new game asset from game type %s for game %s (Class type: %s)", reflectiveOperationException, string2, string, clazz.getName());
            throw new RuntimeException(reflectiveOperationException);
        }
    }
}

