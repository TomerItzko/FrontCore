/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  javax.annotation.Nullable
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.block.BlockRenderDispatcher
 *  net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance$Attenuation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.RegistryUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BlockPlaneFlyByMapEffect
extends AbstractMapEffect {
    @NotNull
    private final ObjectList<Supplier<Block>> field_3052 = new ObjectArrayList();
    @NotNull
    private final ObjectList<BF_725> field_3053 = new ObjectArrayList();
    private int field_3056;
    private float field_3054;
    private Vec3 field_3059;
    private Vec3 field_3060;
    private Vec3 field_3061;
    private Vec3 field_3062;
    private int field_3057 = -1;
    @Nullable
    private Supplier<? extends SoundEvent> field_3055 = BFSounds.MATCH_MAPEFFECT_PLANE_FLYBY_LOW;
    private int field_3058 = 0;

    public BlockPlaneFlyByMapEffect() {
        this(0, 0.0f, Vec3.ZERO, 0, 0);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    public BlockPlaneFlyByMapEffect(int n, float f, @NotNull Vec3 vec3, int n2, int n3) {
        this(n, f, vec3.add((double)n2, (double)n3, (double)n2), vec3.add((double)n2, (double)(-n3), (double)(-n2)), vec3.add((double)(-n2), (double)(-n3), (double)n2), vec3.add((double)(-n2), (double)n3, (double)(-n2)));
    }

    public BlockPlaneFlyByMapEffect(int n, float f, @NotNull Vec3 vec3, @NotNull Vec3 vec32, @NotNull Vec3 vec33, @NotNull Vec3 vec34) {
        this.field_3056 = n;
        this.field_3054 = f;
        this.field_3059 = vec3;
        this.field_3060 = vec32;
        this.field_3061 = vec33;
        this.field_3062 = vec34;
    }

    public BlockPlaneFlyByMapEffect method_3068(@NotNull Supplier<? extends SoundEvent> supplier) {
        this.field_3055 = supplier;
        return this;
    }

    public BlockPlaneFlyByMapEffect method_3070(@NotNull DeferredHolder<Block, Block> deferredHolder) {
        this.field_3052.add(deferredHolder);
        return this;
    }

    private void method_3071() {
        if (this.field_3052.isEmpty()) {
            return;
        }
        RandomSource randomSource = RandomSource.create();
        float f = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.field_3059.x), (float)((float)this.field_3060.x));
        float f2 = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.field_3059.y), (float)((float)this.field_3060.y));
        float f3 = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.field_3059.z), (float)((float)this.field_3060.z));
        float f4 = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.field_3061.x), (float)((float)this.field_3062.x));
        float f5 = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.field_3061.y), (float)((float)this.field_3062.y));
        float f6 = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.field_3061.z), (float)((float)this.field_3062.z));
        this.field_3053.add((Object)new BF_725(this.field_3055, this.field_3054, (Block)((Supplier)this.field_3052.getFirst()).get(), new Vec3((double)f, (double)f2, (double)f3), new Vec3((double)f4, (double)f5, (double)f6), this.field_3057));
    }

    @Override
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        this.field_3053.removeIf(bF_725 -> bF_725.method_3073(minecraft, player));
        if (this.field_3058++ >= this.field_3056) {
            this.field_3058 = 0;
            int n = ThreadLocalRandom.current().nextInt(3);
            for (int i = 0; i < n; ++i) {
                this.method_3071();
            }
        }
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
        for (BF_725 bF_725 : this.field_3053) {
            bF_725.method_3072(poseStack, level, dispatcher, buffer, delta);
        }
    }

    @Override
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
        for (BF_725 bF_725 : this.field_3053) {
            BFRendering.billboardLine(camera, poseStack, bF_725.field_3063, bF_725.field_3069, 1.0f, 0xFF4444);
            BFRendering.billboardLine(camera, poseStack, bF_725.field_3069, bF_725.field_3071, 1.0f, 0x44FF44);
        }
    }

    @Override
    public boolean requiresFancyGraphics() {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull RenderLevelStageEvent.Stage getRenderStage() {
        return RenderLevelStageEvent.Stage.AFTER_PARTICLES;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        String string;
        fDSTagCompound.setInteger("spawnInterval", this.field_3056);
        fDSTagCompound.setFloat("speed", this.field_3054);
        fDSTagCompound.setDouble("min0x", this.field_3059.x);
        fDSTagCompound.setDouble("min0y", this.field_3059.y);
        fDSTagCompound.setDouble("min0z", this.field_3059.z);
        fDSTagCompound.setDouble("min1x", this.field_3060.x);
        fDSTagCompound.setDouble("min1y", this.field_3060.y);
        fDSTagCompound.setDouble("min1z", this.field_3060.z);
        fDSTagCompound.setDouble("max0x", this.field_3061.x);
        fDSTagCompound.setDouble("max0y", this.field_3061.y);
        fDSTagCompound.setDouble("max0z", this.field_3061.z);
        fDSTagCompound.setDouble("max1x", this.field_3062.x);
        fDSTagCompound.setDouble("max1y", this.field_3062.y);
        fDSTagCompound.setDouble("max1z", this.field_3062.z);
        if (this.field_3055 != null && (string = RegistryUtils.getSoundEventId(this.field_3055.get())) != null) {
            fDSTagCompound.setString("soundEvent", string);
        }
        if (this.field_3057 != -1) {
            fDSTagCompound.setInteger("soundDistanceMin", this.field_3057);
        }
        int n = this.field_3052.size();
        fDSTagCompound.setInteger("blockCount", n);
        for (int i = 0; i < n; ++i) {
            Supplier supplier = (Supplier)this.field_3052.get(i);
            fDSTagCompound.setString("block" + i, RegistryUtils.getBlockId((Block)supplier.get()));
        }
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        this.field_3056 = fDSTagCompound.getInteger("spawnInterval");
        this.field_3054 = fDSTagCompound.getFloat("speed");
        this.field_3059 = new Vec3(fDSTagCompound.getDouble("min0x"), fDSTagCompound.getDouble("min0y"), fDSTagCompound.getDouble("min0z"));
        this.field_3060 = new Vec3(fDSTagCompound.getDouble("min1x"), fDSTagCompound.getDouble("min1y"), fDSTagCompound.getDouble("min1z"));
        this.field_3061 = new Vec3(fDSTagCompound.getDouble("max0x"), fDSTagCompound.getDouble("max0y"), fDSTagCompound.getDouble("max0z"));
        this.field_3062 = new Vec3(fDSTagCompound.getDouble("max1x"), fDSTagCompound.getDouble("max1y"), fDSTagCompound.getDouble("max1z"));
        String string = fDSTagCompound.getString("soundEvent");
        if (string != null) {
            this.field_3055 = RegistryUtils.retrieveSoundEvent(string);
        }
        this.field_3057 = fDSTagCompound.getInteger("soundDistanceMin", -1);
        this.field_3052.clear();
        int n = fDSTagCompound.getInteger("blockCount", 0);
        for (int i = 0; i < n; ++i) {
            this.field_3052.add(RegistryUtils.retrieveBlock(fDSTagCompound.getString("block" + i)));
        }
    }

    private static class BF_725 {
        private final float field_3067;
        private final Block field_3064;
        private final Vec3 field_3071;
        @Nullable
        private final Supplier<? extends SoundEvent> field_3066;
        private final int field_3068;
        private final Vec3 field_3063;
        private Vec3 field_3069;
        private Vec3 field_3070;
        private boolean field_3065 = false;

        public BF_725(@Nullable Supplier<? extends SoundEvent> supplier, float f, @NotNull Block block, @NotNull Vec3 vec3, @NotNull Vec3 vec32, int n) {
            this.field_3067 = f;
            this.field_3064 = block;
            this.field_3066 = supplier;
            this.field_3068 = n;
            if (Math.random() < 0.5) {
                this.field_3071 = vec32;
                this.field_3069 = this.field_3070 = vec3;
                this.field_3063 = this.field_3070;
            } else {
                this.field_3071 = vec3;
                this.field_3069 = this.field_3070 = vec32;
                this.field_3063 = this.field_3070;
            }
        }

        public void method_3074(@NotNull Minecraft minecraft) {
            this.field_3065 = true;
            if (this.field_3066 != null) {
                minecraft.getSoundManager().play((SoundInstance)new BF_726(this, this.field_3066.get(), (float)((double)0.9f + (double)0.2f * Math.random()), 0.25f));
            }
        }

        public boolean method_3073(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer) {
            this.field_3070 = this.field_3069;
            this.field_3069 = MathUtils.moveTowards(this.field_3069, this.field_3071, this.field_3067);
            if (!this.field_3065) {
                double d = Mth.sqrt((float)((float)localPlayer.distanceToSqr(this.field_3069.x, this.field_3069.y, this.field_3069.z)));
                double d2 = this.field_3067 * 4.0f * 20.0f;
                if ((float)this.field_3068 != -1.0f) {
                    if (d <= (double)this.field_3068) {
                        this.method_3074(minecraft);
                    }
                } else if (d <= d2) {
                    this.method_3074(minecraft);
                }
            }
            return this.field_3069.distanceTo(this.field_3071) <= 1.0;
        }

        public void method_3072(@NotNull PoseStack poseStack, @NotNull ClientLevel clientLevel, @NotNull BlockRenderDispatcher blockRenderDispatcher, @NotNull MultiBufferSource.BufferSource bufferSource, float f) {
            double d = this.field_3071.x - this.field_3069.x;
            double d2 = this.field_3071.y - this.field_3069.y;
            double d3 = this.field_3071.z - this.field_3069.z;
            double d4 = Mth.sqrt((float)((float)(d * d + d3 * d3)));
            float f2 = (float)(-(Mth.atan2((double)d2, (double)d4) * 57.2957763671875));
            float f3 = (float)(Mth.atan2((double)d3, (double)d) * 57.2957763671875) + 90.0f;
            Vec3 vec3 = MathUtils.lerp(this.field_3069, this.field_3070, f);
            BFRendering.block(clientLevel, blockRenderDispatcher, bufferSource, this.field_3064.defaultBlockState(), poseStack, vec3.x, vec3.y, vec3.z, f2, -f3, 0.0f);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class BF_726
    extends AbstractTickableSoundInstance {
        @NotNull
        private final BF_725 field_3072;

        public BF_726(@NotNull BF_725 bF_725, @NotNull SoundEvent soundEvent, float f, float f2) {
            super(soundEvent, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
            this.field_3072 = bF_725;
            this.looping = false;
            this.pitch = f;
            this.volume = f2;
            this.x = (float)bF_725.field_3069.x;
            this.y = (float)bF_725.field_3069.y;
            this.z = (float)bF_725.field_3069.z;
            this.attenuation = SoundInstance.Attenuation.NONE;
        }

        public void tick() {
            if (this.field_3072 != null) {
                this.x = this.field_3072.field_3069.x;
                this.y = this.field_3072.field_3069.y;
                this.z = this.field_3072.field_3069.z;
            }
        }
    }
}

