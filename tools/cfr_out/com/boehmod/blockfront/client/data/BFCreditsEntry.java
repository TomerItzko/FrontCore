/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.data;

import com.boehmod.blockfront.common.data.BFResource;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public record BFCreditsEntry(String name, String uuid, String translation) {
    @NotNull
    public static final Codec<BFCreditsEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.STRING.fieldOf("name").forGetter(BFCreditsEntry::name), (App)Codec.STRING.fieldOf("uuid").forGetter(BFCreditsEntry::uuid), (App)Codec.STRING.fieldOf("translation").forGetter(BFCreditsEntry::translation)).apply((Applicative)instance, BFCreditsEntry::new));
    @NotNull
    public static final BFCreditsEntry DEFAULT = new BFCreditsEntry("default", "143b454d-2366-4eee-b930-76b0e55a8f0a", "unknown");

    public static class Resource
    extends BFResource<BFCreditsEntry> {
        public Resource(@NotNull Minecraft minecraft) {
            super(minecraft.getResourceManager(), "credits", "credits.json", CODEC, DEFAULT, BFCreditsEntry::uuid);
        }
    }
}

