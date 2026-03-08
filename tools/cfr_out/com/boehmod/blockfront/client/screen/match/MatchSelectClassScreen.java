/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.match;

import com.boehmod.blockfront.client.screen.match.AbstractMatchSelectClassScreen;
import com.boehmod.blockfront.client.settings.BFClientSettingsDisk;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.packet.BFGameChangeClassRequestPacket;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.unnamed.BF_197;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class MatchSelectClassScreen<G extends AbstractGame<G, ?, ?>, C extends AbstractGameClient<G, ?>>
extends AbstractMatchSelectClassScreen<G, C> {
    public MatchSelectClassScreen(@Nullable Screen screen, @NotNull GameTeam gameTeam, @NotNull G g, @NotNull C c2) {
        super(screen, g, c2, gameTeam);
    }

    @Override
    public void method_774() {
        super.method_774();
        Map<MatchClass, ObjectList<Loadout>> map = this.field_1218.method_2685(this.field_1216);
        Set<MatchClass> set = this.field_1217.getBannedClasses();
        for (Map.Entry<MatchClass, ObjectList<Loadout>> entry : map.entrySet()) {
            ObjectList<Loadout> objectList = entry.getValue();
            MatchClass matchClass = entry.getKey();
            if (matchClass == null || set.contains((Object)matchClass)) continue;
            int n = BFUtils.method_2899(this.field_1217, matchClass, this.field_1216);
            String string = I18n.get((String)matchClass.getDisplayTitle(), (Object[])new Object[0]);
            String string2 = I18n.get((String)matchClass.getDescription(), (Object[])new Object[0]);
            this.method_897(matchClass, objectList, new String[]{String.valueOf(ChatFormatting.BOLD) + string + String.valueOf(ChatFormatting.WHITE) + " - ", string2}, n);
        }
    }

    @Override
    public void method_896(@NotNull BF_197 bF_197) {
        Object object;
        UUID uUID;
        if (this.minecraft.player == null) {
            return;
        }
        Object p = this.field_1217.getPlayerManager();
        GameTeam gameTeam = ((AbstractGamePlayerManager)p).getPlayerTeam(uUID = this.minecraft.player.getUUID());
        if (gameTeam == null) {
            return;
        }
        DivisionData divisionData = gameTeam.getDivisionData(this.field_1217);
        Loadout loadout = divisionData.getLoadout(bF_197.method_906(), bF_197.method_910());
        if (loadout != null) {
            object = this.field_1217.getPlayerStatData(uUID);
            object.setInteger(BFStats.CLASS.getKey(), bF_197.method_906().ordinal());
            object.setInteger(BFStats.CLASS_INDEX.getKey(), bF_197.method_910());
            this.field_1218.field_2903 = 20;
        }
        BFClientSettingsDisk.write(this.manager);
        object = new BFGameChangeClassRequestPacket(bF_197.method_906(), bF_197.method_910());
        PacketUtils.sendToServer((CustomPacketPayload)object);
        this.method_894();
        GunItem.field_4059 = 20;
    }
}

