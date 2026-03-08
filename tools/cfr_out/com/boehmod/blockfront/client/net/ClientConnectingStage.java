/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.connection.ConnectionStatus
 *  it.unimi.dsi.fastutil.Pair
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.net;

import com.boehmod.bflib.cloud.connection.ConnectionStatus;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.EllipsisUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public enum ClientConnectingStage {
    CONNECTING("bf.message.intro.online.connecting", "bf.message.intro.online.connecting.tip", "textures/gui/intro/stage_connecting.png"){

        @Override
        @NotNull
        public Pair<Component, Component> getDisplayComponents(@NotNull Component component, @NotNull Component component2, float f) {
            return Pair.of((Object)Component.translatable((String)this.getTitleKey(), (Object[])new Object[]{component, component2}).withColor(8159560), (Object)Component.translatable((String)this.getTipKey(), (Object[])new Object[]{component}).withStyle(ChatFormatting.GRAY));
        }
    }
    ,
    ENCRYPTING("bf.message.intro.online.encrypting", "bf.message.intro.online.encrypting.tip", "textures/gui/intro/stage_encrypting.png"){

        @Override
        @NotNull
        public Pair<Component, Component> getDisplayComponents(@NotNull Component component, @NotNull Component component2, float f) {
            return Pair.of((Object)Component.translatable((String)this.getTitleKey(), (Object[])new Object[]{component, component2}).withColor(8159560), (Object)Component.translatable((String)this.getTipKey(), (Object[])new Object[]{component}).withStyle(ChatFormatting.GRAY));
        }
    }
    ,
    VERIFYING("bf.message.intro.online.verifying", "bf.message.intro.online.verifying.tip", "textures/gui/intro/stage_verifying.png"){

        @Override
        @NotNull
        public Pair<Component, Component> getDisplayComponents(@NotNull Component component, @NotNull Component component2, float f) {
            return Pair.of((Object)Component.translatable((String)this.getTitleKey(), (Object[])new Object[]{component, component2, EllipsisUtils.cyclingEllipsis(f)}).withColor(8159560), (Object)Component.translatable((String)this.getTipKey()).withStyle(ChatFormatting.GRAY));
        }
    }
    ,
    SYNCING("bf.message.intro.online.syncing", "bf.message.intro.online.syncing.tip", "textures/gui/intro/stage_syncing.png"){

        @Override
        @NotNull
        public Pair<Component, Component> getDisplayComponents(@NotNull Component component, @NotNull Component component2, float f) {
            return Pair.of((Object)Component.translatable((String)this.getTitleKey(), (Object[])new Object[]{component, EllipsisUtils.cyclingEllipsis(f)}).withColor(8159560), (Object)Component.translatable((String)this.getTipKey()).withStyle(ChatFormatting.GRAY));
        }
    };

    @NotNull
    private final String titleKey;
    @NotNull
    private final String tipKey;
    @NotNull
    final ResourceLocation iconTexture;

    private ClientConnectingStage(@NotNull String titleKey, String tipKey, String iconTexture) {
        this.titleKey = titleKey;
        this.tipKey = tipKey;
        this.iconTexture = BFRes.loc(iconTexture);
    }

    @NotNull
    public String getTitleKey() {
        return this.titleKey;
    }

    @NotNull
    public String getTipKey() {
        return this.tipKey;
    }

    @NotNull
    public ResourceLocation getIconTexture() {
        return this.iconTexture;
    }

    @NotNull
    public abstract Pair<Component, Component> getDisplayComponents(@NotNull Component var1, @NotNull Component var2, float var3);

    @NotNull
    public static ClientConnectingStage getCurrentStage(@NotNull ConnectionStatus status) {
        return switch (status) {
            default -> throw new MatchException(null, null);
            case ConnectionStatus.CLOSED -> CONNECTING;
            case ConnectionStatus.CONNECTED_NOT_ENCRYPTED -> ENCRYPTING;
            case ConnectionStatus.CONNECTED_NOT_VERIFIED -> VERIFYING;
            case ConnectionStatus.CONNECTED_VERIFIED -> SYNCING;
        };
    }
}

