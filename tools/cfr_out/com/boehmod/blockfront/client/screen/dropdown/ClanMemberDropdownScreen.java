/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.AbstractClanData
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.clan.PacketClanKick
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.dropdown;

import com.boehmod.bflib.cloud.common.AbstractClanData;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.clan.PacketClanKick;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.dropdown.DropdownScreen;
import com.boehmod.blockfront.client.screen.profile.ProfileMainScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_211;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public final class ClanMemberDropdownScreen
extends DropdownScreen {
    private static final Component field_680 = Component.translatable((String)"bf.dropdown.text.profile");
    private static final Component field_681 = Component.translatable((String)"bf.dropdown.text.clan.kick").withStyle(ChatFormatting.RED);
    private static final Component field_682 = Component.translatable((String)"bf.dropdown.text.clan.kick.tip").withStyle(ChatFormatting.RED);
    @NotNull
    private final UUID field_683;
    @NotNull
    private final PlayerCloudData field_679;

    public ClanMemberDropdownScreen(@NotNull Screen screen, int n, int n2, int n3, int n4, @NotNull PlayerCloudData playerCloudData) {
        super(screen, n, n2, n3, n4, (Component)Component.translatable((String)"bf.screen.dropdown.friend", (Object[])new Object[]{playerCloudData.getUsername()}));
        this.field_683 = playerCloudData.getUUID();
        this.field_679 = playerCloudData;
        UUID uUID = this.minecraft.getUser().getProfileId();
        boolean bl = this.field_683.equals(uUID);
        MutableComponent mutableComponent = Component.literal((String)playerCloudData.getUsername());
        this.method_745(field_680, (Component)Component.translatable((String)"bf.dropdown.text.profile.tip", (Object[])new Object[]{mutableComponent}));
        PlayerCloudData playerCloudData2 = this.playerDataHandler.getCloudData(this.minecraft);
        AbstractClanData abstractClanData = playerCloudData2.getClanData();
        if (!bl && abstractClanData != null && abstractClanData.getOwner().equals(uUID)) {
            this.method_745(field_681, field_682);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        int n3 = 22;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        BFRendering.rectangle(guiGraphics, this.field_1015, this.field_1016 - 22, this.field_1017, 22, BFRendering.translucentBlack());
        BFRendering.rectangle(guiGraphics, this.field_1015, this.field_1016 - 22, this.field_1017, 22, BFRendering.translucentBlack());
        BFRendering.method_197(this.playerDataHandler, guiGraphics, this.font, this.field_683, BF_211.BF_213.CLAN_MEMBER, this.field_1015, this.field_1016 - 22, 22, false);
        int n4 = 2;
        int n5 = 18;
        BFRendering.playerHead(this.minecraft, this.manager, poseStack, guiGraphics, this.field_679.getMcProfile(), this.field_1015 + this.field_1017 - 20, this.field_1016 - 22 + 2, 18);
        poseStack.popPose();
    }

    @Override
    public void method_748(int n) {
        switch (n) {
            case 1: {
                this.minecraft.setScreen((Screen)new ProfileMainScreen(this.field_1036, this.field_679));
                break;
            }
            case 2: {
                String string = this.field_679.getUsername();
                ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketClanKick(string));
            }
        }
    }
}

