/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.match.summary;

import com.boehmod.blockfront.client.gui.widget.ExpProgressWidget;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFStyles;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class MatchRankSummaryScreen
extends MatchSummaryScreen {
    private static final Component field_800 = Component.translatable((String)"bf.message.game.match.summary.progress.subtitle").withStyle(BFStyles.BOLD);
    private static final Component field_801 = Component.translatable((String)"bf.message.game.match.summary.progress.subtitle").withStyle(ChatFormatting.BOLD);
    private static final Component field_802 = Component.translatable((String)"bf.message.game.match.summary.progress.subtitle.tip");
    private static final int field_6651 = 290;
    private static final int field_6652 = 40;
    private static final int field_799 = 200;
    @NotNull
    private final ExpProgressWidget field_6650;
    private boolean field_6805 = false;

    public MatchRankSummaryScreen(@NotNull UUID uUID, @NotNull AbstractGameClient<?, ?> abstractGameClient) {
        super(uUID);
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        int n = abstractGameClient.getSummaryManager().getPlayerExp();
        int n2 = playerCloudData.getExp();
        this.field_6650 = new ExpProgressWidget(0, 0, 0, 0, this, n, n2);
    }

    @Override
    public void method_774() {
        super.method_774();
        int n = this.width / 2;
        int n2 = this.height / 2;
        int n3 = 290;
        int n4 = 40;
        int n5 = n - 145;
        int n6 = n2 - 20;
        this.field_6650.method_551(n5);
        this.field_6650.method_552(n6);
        this.field_6650.method_553(290);
        this.field_6650.method_554(40);
        this.method_764(this.field_6650);
    }

    @Override
    @NotNull
    protected SoundEvent method_642() {
        return (SoundEvent)BFSounds.GUI_GAME_SUMMARY_INTRO_PROGRESS.get();
    }

    @Override
    protected int method_649() {
        return 200;
    }

    @Override
    @NotNull
    protected Component method_645() {
        return field_800;
    }

    @Override
    @NotNull
    protected Component method_646() {
        return field_801;
    }

    @Override
    @NotNull
    protected Component method_647() {
        return field_802;
    }

    @Override
    public void method_5746() {
        super.method_5746();
        this.field_6805 = true;
    }

    @Override
    public boolean isFinished() {
        if (this.field_6805) {
            return true;
        }
        return this.field_6650.method_5627() && super.isFinished();
    }
}

