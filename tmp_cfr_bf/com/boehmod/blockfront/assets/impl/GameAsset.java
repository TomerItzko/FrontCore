/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.assets.impl;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetCommandValidators;
import com.boehmod.blockfront.assets.IAsset;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.CommandUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class GameAsset
implements IAsset {
    @Nullable
    private AbstractGame<?, ?, ?> game;
    @NotNull
    private final BFAbstractManager<?, ?, ?> manager;
    private boolean isInRotation = false;
    @NotNull
    private final AssetCommandBuilder command = new AssetCommandBuilder().subCommand("rotation", new AssetCommandBuilder((commandContext, stringArray) -> {
        this.isInRotation = stringArray[0].equals("true");
        String string = this.isInRotation ? "Added game " + this.game.name + " to map rotation" : "Removed game " + this.game.name + " from map rotation";
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)string));
    }).validator(AssetCommandValidators.count(new String[]{"true/false"})));

    public GameAsset(@NotNull BFAbstractManager<?, ?, ?> manager) {
        this(null, manager);
    }

    public GameAsset(@Nullable AbstractGame<?, ?, ?> game, @NotNull BFAbstractManager<?, ?, ?> manager) {
        this.game = game;
        this.manager = manager;
    }

    @Nullable
    public AbstractGame<?, ?, ?> getGame() {
        return this.game;
    }

    public boolean isInRotation() {
        return this.isInRotation;
    }

    @Nullable
    private AbstractGame<?, ?, ?> getOrCreateGame(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull String name) {
        if (name.isEmpty()) {
            return null;
        }
        BFGameType bFGameType = BFGameType.getByName(name);
        if (bFGameType == null) {
            return null;
        }
        Class<AbstractGame<?, ?, ?>> clazz = bFGameType.getGameClass();
        try {
            return clazz.getDeclaredConstructor(BFAbstractManager.class).newInstance(manager);
        }
        catch (Exception exception) {
            BFLog.logThrowable("Failed to create new game asset for game %s (Class type: %s)", exception, name, clazz.getName());
            return null;
        }
    }

    @Override
    public void readFDS(@NotNull FDSTagCompound root) {
        String string = root.getString("gameType");
        if (string == null) {
            return;
        }
        this.game = this.getOrCreateGame(this.manager, string);
        FDSTagCompound fDSTagCompound = root.getTagCompound("gameTag");
        if (fDSTagCompound == null) {
            return;
        }
        if (this.game != null) {
            this.game.readAllFDS(fDSTagCompound);
        }
        this.isInRotation = root.getBoolean("inRotation", true);
        if (this.game != null) {
            this.game.reset(null);
        }
    }

    @Override
    public void writeFDS(@NotNull FDSTagCompound root) {
        root.setString("gameType", this.game != null ? this.game.type : "");
        FDSTagCompound fDSTagCompound = new FDSTagCompound("gameTag");
        this.game.writeAllFDS(fDSTagCompound);
        root.setTagCompound("gameTag", fDSTagCompound);
        root.setBoolean("inRotation", this.isInRotation);
    }

    @Override
    @NotNull
    public AssetCommandBuilder getCommand() {
        if (this.game != null) {
            return this.command.inherit(this.game.getCommand());
        }
        return this.command;
    }

    @Override
    public void sendErrorMessages(@NotNull CommandSource source) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        if (this.game != null) {
            this.game.getErrorMessages((List<MutableComponent>)objectArrayList);
        }
        for (MutableComponent mutableComponent : objectArrayList) {
            CommandUtils.sendBfaWarn(source, (Component)mutableComponent);
        }
    }
}

