/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.multiplayer.ClientPacketListener
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.prompt.text;

import com.boehmod.blockfront.client.screen.prompt.text.BFTextPromptScreen;
import com.boehmod.blockfront.client.screen.staff.PunishPlayerScreen;
import com.boehmod.blockfront.util.BFLog;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class StaffPunishPromptScreen
extends BFTextPromptScreen {
    private static final Component field_1270 = Component.translatable((String)"bf.message.prompt.ingame.punish.title");
    private static final Component field_1271 = Component.translatable((String)"bf.message.prompt.ingame.punish");
    private static final Component field_1272 = Component.translatable((String)"bf.screen.staff");

    public StaffPunishPromptScreen(@NotNull Screen screen) {
        super(screen, field_1270);
        this.addFilter(BFTextPromptScreen.Filter.MINECRAFT_UUID);
        this.addFilter(BFTextPromptScreen.Filter.MINECRAFT_USERNAME);
        this.method_1084(field_1271);
        this.method_1094(true);
    }

    @Override
    public void confirm() {
        UUID uUID = null;
        String string = this.method_1096();
        if (string.contains("-")) {
            try {
                uUID = UUID.fromString(string);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                BFLog.logThrowable("Failed to parse UUID from input: " + string, illegalArgumentException, new Object[0]);
            }
        }
        if (uUID == null && this.minecraft.player != null) {
            ClientPacketListener clientPacketListener = this.minecraft.player.connection;
            Collection collection = clientPacketListener.getListedOnlinePlayers();
            for (PlayerInfo playerInfo : collection) {
                GameProfile gameProfile = playerInfo.getProfile();
                if (!gameProfile.getName().equalsIgnoreCase(string)) continue;
                uUID = gameProfile.getId();
                break;
            }
        }
        if (uUID != null) {
            this.minecraft.setScreen((Screen)new PunishPlayerScreen(field_1272, uUID));
        } else {
            BFLog.logError("Failed to find player with input: " + string + " (Is the player online?)", new Object[0]);
            this.method_1081();
        }
    }
}

