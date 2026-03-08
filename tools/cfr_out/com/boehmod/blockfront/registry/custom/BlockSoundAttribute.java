/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.core.Registry
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.RegistryBuilder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.registry.custom;

import com.boehmod.blockfront.client.gui.layer.MatchGuiLayer;
import com.boehmod.blockfront.util.BFRes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.NotNull;

public class BlockSoundAttribute {
    @NotNull
    private static final ResourceKey<Registry<BlockSoundAttribute>> KEY = ResourceKey.createRegistryKey((ResourceLocation)BFRes.loc("block_impact_attribute"));
    @NotNull
    public static final Registry<BlockSoundAttribute> REGISTRY = new RegistryBuilder(KEY).sync(true).create();
    @NotNull
    private final DeferredHolder<SoundEvent, SoundEvent> field_4091;
    @NotNull
    private final DeferredHolder<SoundEvent, SoundEvent> field_4092;
    @Nullable
    private final MatchGuiLayer.BF_114.BF_115 field_4090;
    private boolean field_4097 = false;
    private boolean field_4095 = false;
    private boolean field_4096 = false;
    @Nonnull
    private final ObjectList<BF_999> field_4098 = new ObjectArrayList();
    @Nonnull
    private final ObjectList<BF_999> field_7003 = new ObjectArrayList();

    public BlockSoundAttribute(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder2) {
        this(deferredHolder, deferredHolder2, null);
    }

    public BlockSoundAttribute(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder2, @Nullable MatchGuiLayer.BF_114.BF_115 bF_115) {
        this.field_4091 = deferredHolder;
        this.field_4092 = deferredHolder2;
        this.field_4090 = bF_115;
    }

    @NotNull
    public BlockSoundAttribute method_4250(int n, DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> deferredHolder) {
        this.field_4098.add((Object)new BF_999(n, deferredHolder));
        return this;
    }

    @NotNull
    public BlockSoundAttribute method_5935(int n, DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> deferredHolder) {
        this.field_7003.add((Object)new BF_999(n, deferredHolder));
        return this;
    }

    @NotNull
    public List<BF_999> method_4247() {
        return this.field_4098;
    }

    @NotNull
    public List<BF_999> method_5934() {
        return this.field_7003;
    }

    @NotNull
    public BlockSoundAttribute method_4249() {
        this.field_4097 = true;
        return this;
    }

    @NotNull
    public BlockSoundAttribute method_4251() {
        this.field_4095 = true;
        return this;
    }

    public boolean method_4254() {
        return this.field_4095;
    }

    @NotNull
    public BlockSoundAttribute method_4255() {
        this.field_4096 = true;
        return this;
    }

    public boolean method_4252() {
        return this.field_4096;
    }

    @NotNull
    public DeferredHolder<SoundEvent, SoundEvent> method_4256() {
        return this.field_4091;
    }

    @NotNull
    public DeferredHolder<SoundEvent, SoundEvent> method_4257() {
        return this.field_4092;
    }

    @Nullable
    public MatchGuiLayer.BF_114.BF_115 method_4248() {
        return this.field_4090;
    }

    public boolean method_4253() {
        return this.field_4097;
    }

    public record BF_999(int count, DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> particleType) {
    }
}

