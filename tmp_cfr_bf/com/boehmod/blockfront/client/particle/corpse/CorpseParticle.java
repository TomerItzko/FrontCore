/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.particle.corpse;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.corpse.physics.CorpsePartPhysics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

public abstract class CorpseParticle {
    public abstract boolean update(@NotNull Minecraft var1, @NotNull BFClientManager var2, @NotNull ClientLevel var3, @NotNull CorpsePartPhysics var4, @NotNull Vector3dc var5);
}

