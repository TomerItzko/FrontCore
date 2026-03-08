/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.cloud.common.AbstractClanData;
import com.boehmod.bflib.cloud.common.player.status.PlayerStatus;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.dropdown.ClanInviteDropdownScreen;
import com.boehmod.blockfront.client.screen.dropdown.ClanMemberDropdownScreen;
import com.boehmod.blockfront.client.screen.dropdown.FriendDropdownScreen;
import com.boehmod.blockfront.client.screen.dropdown.FriendRequestDropdownScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class BF_211
extends BF_218 {
    @NotNull
    private static final ResourceLocation field_1327 = BFRes.loc("textures/gui/invites/friend.png");
    public static final Component field_1329 = Component.translatable((String)"bf.status.friend.request").withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
    @NotNull
    private final Screen field_1331;
    @NotNull
    private final UUID field_1330;
    @NotNull
    private final PlayerCloudData field_1328;
    @NotNull
    private final BF_213 field_1325;

    public BF_211(@NotNull Screen screen, @NotNull UUID uUID, @NotNull BF_213 bF_213) {
        this.field_1331 = screen;
        this.field_1330 = uUID;
        this.field_1325 = bF_213;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        this.field_1328 = clientPlayerDataHandler.getCloudProfile(uUID);
    }

    @Override
    public void method_985(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull Font font, int n, int n2, float f, float f2) {
        super.method_985(poseStack, minecraft, bFClientManager, guiGraphics, clientPlayerDataHandler, font, n, n2, f, f2);
        if (this.method_992() && this.field_1325 == BF_213.FRIEND) {
            PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(this.field_1330);
            guiGraphics.renderTooltip(font, playerCloudData.method_1719(), Optional.empty(), n, n2);
        }
    }

    @Override
    public void method_983(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2, float f) {
        int n3 = this.height() - 2;
        this.field_1325.slotInfo.method_968(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, this.field_1330, this.field_1357 + 1, this.field_1358 + 1, n3, n3);
        BFRendering.method_197(clientPlayerDataHandler, guiGraphics, font, this.field_1330, this.field_1325, this.field_1357 + n3 + 1, this.field_1358, this.height(), this.method_992());
    }

    @Override
    public void method_982(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, int n, int n2, int n3) {
        int n4 = (int)this.field_1354.method_963(1.0f);
        switch (this.field_1325.ordinal()) {
            case 1: {
                FriendRequestDropdownScreen friendRequestDropdownScreen = new FriendRequestDropdownScreen(this.field_1331, this.field_1357 + this.method_989(), this.field_1358 - n4, 100, 15, this.field_1328);
                minecraft.setScreen((Screen)friendRequestDropdownScreen);
                break;
            }
            case 2: {
                ClanMemberDropdownScreen clanMemberDropdownScreen = new ClanMemberDropdownScreen(this.field_1331, this.field_1357 + this.method_989(), this.field_1358 - n4, 100, 15, this.field_1328);
                minecraft.setScreen((Screen)clanMemberDropdownScreen);
                break;
            }
            case 3: {
                ClanInviteDropdownScreen clanInviteDropdownScreen = new ClanInviteDropdownScreen(this.field_1331, this.field_1357 + this.method_989(), this.field_1358 - n4, 100, 15, this.field_1330);
                minecraft.setScreen((Screen)clanInviteDropdownScreen);
                break;
            }
            case 0: {
                FriendDropdownScreen friendDropdownScreen = new FriendDropdownScreen(this.field_1331, this.field_1357 + this.method_989(), this.field_1358 - n4, 100, 15, this.field_1328);
                minecraft.setScreen((Screen)friendDropdownScreen);
            }
        }
    }

    @Override
    public void method_987(@NotNull Minecraft minecraft) {
    }

    @Override
    public boolean method_990() {
        return false;
    }

    @Override
    public int height() {
        return 22;
    }

    @Override
    public int method_989() {
        return this.field_1354.method_558();
    }

    @Override
    public int method_975() {
        return 1;
    }

    public static enum BF_213 {
        FRIEND(new SlotInfo(){

            @Override
            public Component method_965(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID) {
                PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(uUID);
                return Component.literal((String)playerCloudData.getUsername());
            }

            @Override
            public Component method_967(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID, boolean bl) {
                PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(uUID);
                if (bl) {
                    return playerCloudData.method_1718().copy().withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
                }
                return playerCloudData.method_1718();
            }

            @Override
            public int method_966(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID, boolean bl) {
                PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(uUID);
                PlayerStatus playerStatus = playerCloudData.getStatus();
                int n = playerStatus.getOnlineStatus().getColor();
                return bl ? ColorReferences.COLOR_THEME_YELLOW_SOLID : n + -16777216;
            }

            @Override
            public void method_968(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, PoseStack poseStack, @NotNull GuiGraphics guiGraphics, UUID uUID, int n, int n2, int n3, int n4) {
                BFRendering.playerHead(minecraft, bFClientManager, poseStack, guiGraphics, clientPlayerDataHandler.getCloudProfile(uUID).getMcProfile(), n, n2, n4);
            }
        }),
        FRIEND_REQUEST(new SlotInfo(){

            @Override
            public Component method_965(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID) {
                PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(uUID);
                return Component.literal((String)playerCloudData.getUsername());
            }

            @Override
            public Component method_967(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID, boolean bl) {
                return field_1329;
            }

            @Override
            public int method_966(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID, boolean bl) {
                return bl ? ColorReferences.COLOR_THEME_YELLOW_SOLID : ColorReferences.COLOR_WHITE_SOLID;
            }

            @Override
            public void method_968(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, PoseStack poseStack, @NotNull GuiGraphics guiGraphics, UUID uUID, int n, int n2, int n3, int n4) {
                BFRendering.texture(poseStack, guiGraphics, field_1327, n, n2, n3, n4);
            }
        }),
        CLAN_MEMBER(new SlotInfo(){

            @Override
            public Component method_965(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID) {
                PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(uUID);
                return Component.literal((String)playerCloudData.getUsername());
            }

            @Override
            public Component method_967(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID, boolean bl) {
                PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(uUID);
                PlayerStatus playerStatus = playerCloudData.getStatus();
                int n = bl ? ColorReferences.COLOR_THEME_CLANS_HOVERED_SOLID : (playerStatus.getOnlineStatus().isOnline() ? ColorReferences.COLOR_THEME_CLANS_SOLID : ColorReferences.COLOR_THEME_CLANS_MUTED_SOLID);
                return playerCloudData.method_1718().copy().withColor(n);
            }

            @Override
            public int method_966(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID, boolean bl) {
                PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(uUID);
                PlayerStatus playerStatus = playerCloudData.getStatus();
                return bl ? ColorReferences.COLOR_THEME_CLANS_HOVERED_SOLID : (playerStatus.getOnlineStatus().isOnline() ? ColorReferences.COLOR_THEME_CLANS_SOLID : ColorReferences.COLOR_THEME_CLANS_MUTED_SOLID);
            }

            @Override
            public void method_968(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, PoseStack poseStack, @NotNull GuiGraphics guiGraphics, UUID uUID, int n, int n2, int n3, int n4) {
                GameProfile gameProfile = clientPlayerDataHandler.getCloudProfile(uUID).getMcProfile();
                BFRendering.playerHead(minecraft, bFClientManager, poseStack, guiGraphics, gameProfile, n, n2, n4);
            }
        }),
        CLAN_INVITE(new SlotInfo(){

            @Override
            public Component method_965(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID) {
                AbstractClanData abstractClanData = clientPlayerDataHandler.getClanData(uUID);
                return Component.literal((String)abstractClanData.getName());
            }

            @Override
            public Component method_967(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID, boolean bl) {
                AbstractClanData abstractClanData = clientPlayerDataHandler.getClanData(uUID);
                UUID uUID2 = abstractClanData.getOwner();
                PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(uUID2);
                return Component.literal((String)playerCloudData.getUsername());
            }

            @Override
            public int method_966(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull UUID uUID, boolean bl) {
                return bl ? ColorReferences.COLOR_THEME_YELLOW_SOLID : ColorReferences.COLOR_THEME_CLANS_HOVERED_SOLID;
            }

            @Override
            public void method_968(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, PoseStack poseStack, @NotNull GuiGraphics guiGraphics, UUID uUID, int n, int n2, int n3, int n4) {
                BFRendering.tintedTexture(poseStack, guiGraphics, field_1327, n, n2, n3, n4, 0.0f, 1.0f, ColorReferences.COLOR_THEME_CLANS_HOVERED_SOLID);
            }
        });

        private final SlotInfo slotInfo;

        private BF_213(SlotInfo slotInfo) {
            this.slotInfo = slotInfo;
        }

        public SlotInfo getSlotInfo() {
            return this.slotInfo;
        }
    }

    public static interface SlotInfo {
        public Component method_965(@NotNull ClientPlayerDataHandler var1, @NotNull UUID var2);

        public Component method_967(@NotNull ClientPlayerDataHandler var1, @NotNull UUID var2, boolean var3);

        public int method_966(@NotNull ClientPlayerDataHandler var1, @NotNull UUID var2, boolean var3);

        public void method_968(@NotNull Minecraft var1, @NotNull BFClientManager var2, ClientPlayerDataHandler var3, PoseStack var4, @NotNull GuiGraphics var5, UUID var6, int var7, int var8, int var9, int var10);
    }
}

