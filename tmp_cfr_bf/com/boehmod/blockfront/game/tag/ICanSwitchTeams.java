/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.tag;

import com.boehmod.blockfront.common.BFAbstractManager;
import java.util.UUID;
import javax.annotation.CheckForNull;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface ICanSwitchTeams {
    @CheckForNull
    public Component getSwitchTeamMessage(@NotNull ServerPlayer var1);

    public void playerSwitchTeam(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull ServerLevel var2, @NotNull ServerPlayer var3, @NotNull UUID var4);

    public int getTeamSwitchCooldown();
}

