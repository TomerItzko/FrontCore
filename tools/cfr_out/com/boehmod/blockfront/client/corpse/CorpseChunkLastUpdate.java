/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.SectionPos
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.corpse;

import net.minecraft.core.SectionPos;
import org.jetbrains.annotations.NotNull;

record CorpseChunkLastUpdate(@NotNull SectionPos pos, long lastInhabitedTick) {
}

