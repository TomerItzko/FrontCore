/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.blockfront.util.BFRes;
import java.util.Locale;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class InfectedCharacter {
    private final int id;
    @NotNull
    private final String name;
    @NotNull
    private final ResourceLocation texture;

    public InfectedCharacter(int id, @NotNull String name) {
        this.id = id;
        this.name = name;
        this.texture = BFRes.loc("textures/skins/game/infected/" + name.toLowerCase(Locale.ROOT) + ".png");
    }

    @NotNull
    public ResourceLocation getTexture() {
        return this.texture;
    }

    public int getId() {
        return this.id;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        InfectedCharacter infectedCharacter = (InfectedCharacter)other;
        return this.id == infectedCharacter.id && Objects.equals(this.name, infectedCharacter.name);
    }

    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    public String toString() {
        return "InfectedCharacter[id=" + this.id + ", name=" + this.name + "]";
    }
}

