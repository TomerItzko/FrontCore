/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.corpse;

import com.boehmod.blockfront.client.corpse.ClientCorpseManager;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public record CorpseContext(@NotNull ClientCorpseManager ragdollManager, @NotNull PoseStack transformStack, float scaleFactor) {
}

