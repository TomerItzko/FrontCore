/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.corpse.render;

import com.boehmod.blockfront.client.corpse.ClientCorpseManager;
import com.boehmod.blockfront.client.corpse.CorpseContext;
import com.boehmod.blockfront.client.corpse.physics.CorpsePartPhysics;
import com.boehmod.blockfront.common.net.packet.BFSpawnCorpsePacket;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface IRagdollEntityRenderer<T extends Entity> {
    @NotNull
    public Collection<CorpsePartPhysics> createParts(@NotNull ClientCorpseManager var1, @NotNull BFSpawnCorpsePacket.Context var2, @NotNull T var3, @NotNull CorpseContext var4);

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull T var1);
}

