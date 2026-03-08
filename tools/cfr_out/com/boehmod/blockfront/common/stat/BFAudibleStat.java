/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.stat;

import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.util.BFUtils;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BFAudibleStat
extends BFStat {
    @NotNull
    private final String field_3783;
    private final int field_3784;
    @NotNull
    private final ChatFormatting field_3781;
    @Nullable
    private final DeferredHolder<SoundEvent, SoundEvent> field_3782;

    BFAudibleStat(@NotNull String string, @NotNull String string2, int n, @NotNull ChatFormatting chatFormatting, @Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        super(string);
        this.field_3783 = string2;
        this.field_3784 = n;
        this.field_3781 = chatFormatting;
        this.field_3782 = deferredHolder;
    }

    BFAudibleStat(@NotNull String string, @NotNull String string2, int n, @NotNull ChatFormatting chatFormatting) {
        this(string, string2, n, chatFormatting, null);
    }

    public void method_3918(@NotNull UUID uUID, int n) {
        if (this.field_3782 != null) {
            BFUtils.playSound(uUID, (SoundEvent)this.field_3782.get(), SoundSource.NEUTRAL);
        }
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(n)).withColor(0xFFFFFF);
        BFUtils.sendPopupMessage(uUID, new BFPopup((Component)Component.translatable((String)this.field_3783, (Object[])new Object[]{mutableComponent}).withStyle(this.field_3781), this.field_3784));
    }
}

