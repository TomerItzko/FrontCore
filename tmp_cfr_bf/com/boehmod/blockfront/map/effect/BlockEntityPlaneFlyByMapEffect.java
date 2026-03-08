/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.RegistryUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BlockEntityPlaneFlyByMapEffect
extends AbstractMapEffect {
    private final ObjectList<BF_728> field_3074 = new ObjectArrayList();
    private final ObjectList<Supplier<? extends BlockEntityType<?>>> blockEntities = new ObjectArrayList();
    private final ObjectList<Supplier<? extends Block>> blocks = new ObjectArrayList();
    private int spawnInterval;
    private float speed;
    private Vec3 min0;
    private Vec3 min1;
    private Vec3 max0;
    private Vec3 max1;
    @Nullable
    private Supplier<? extends SoundEvent> soundEvent = BFSounds.MATCH_MAPEFFECT_PLANE_FLYBY_LOW;
    @Nullable
    private Supplier<? extends SimpleParticleType> field_3079 = null;
    private int field_3081 = 0;
    private double soundDistanceMin = 128.0;

    public BlockEntityPlaneFlyByMapEffect() {
        this(0, 0.0f, Vec3.ZERO, 0, 0);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    public BlockEntityPlaneFlyByMapEffect(int n, float f, @NotNull Vec3 vec3, int n2, int n3) {
        this(n, f, vec3.add((double)n2, (double)n3, (double)n2), vec3.add((double)n2, (double)(-n3), (double)(-n2)), vec3.add((double)(-n2), (double)(-n3), (double)n2), vec3.add((double)(-n2), (double)n3, (double)(-n2)));
    }

    public BlockEntityPlaneFlyByMapEffect(int n, float f, @NotNull Vec3 vec3, @NotNull Vec3 vec32, @NotNull Vec3 vec33, @NotNull Vec3 vec34) {
        this.spawnInterval = n;
        this.speed = f;
        this.min0 = vec3;
        this.min1 = vec32;
        this.max0 = vec33;
        this.max1 = vec34;
    }

    public BlockEntityPlaneFlyByMapEffect method_3077(@NotNull Supplier<? extends SimpleParticleType> supplier) {
        this.field_3079 = supplier;
        return this;
    }

    public BlockEntityPlaneFlyByMapEffect method_3075(double d) {
        this.soundDistanceMin = d;
        return this;
    }

    public BlockEntityPlaneFlyByMapEffect method_3080(@NotNull Supplier<? extends SoundEvent> supplier) {
        this.soundEvent = supplier;
        return this;
    }

    public BlockEntityPlaneFlyByMapEffect method_3078(@NotNull Supplier<? extends BlockEntityType<?>> supplier, @NotNull DeferredHolder<Block, ? extends Block> deferredHolder) {
        this.blockEntities.add(supplier);
        this.blocks.add(deferredHolder);
        return this;
    }

    private void method_3081() {
        if (this.blockEntities.isEmpty()) {
            return;
        }
        RandomSource randomSource = RandomSource.create();
        float f = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.min0.x), (float)((float)this.min1.x));
        float f2 = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.min0.y), (float)((float)this.min1.y));
        float f3 = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.min0.z), (float)((float)this.min1.z));
        float f4 = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.max0.x), (float)((float)this.max1.x));
        float f5 = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.max0.y), (float)((float)this.max1.y));
        float f6 = Mth.randomBetween((RandomSource)randomSource, (float)((float)this.max0.z), (float)((float)this.max1.z));
        int n = randomSource.nextInt(this.blockEntities.size());
        Supplier supplier = (Supplier)this.blockEntities.get(n);
        BlockState blockState = ((Block)((Supplier)this.blocks.get(n)).get()).defaultBlockState();
        BlockEntity blockEntity = ((BlockEntityType)supplier.get()).create(BlockPos.ZERO, blockState);
        if (blockEntity != null) {
            this.field_3074.add((Object)new BF_728(this.soundEvent, this.speed, blockEntity, new Vec3((double)f, (double)f2, (double)f3), new Vec3((double)f4, (double)f5, (double)f6), this.soundDistanceMin, this.field_3079));
        }
    }

    @Override
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        this.field_3074.removeIf(bF_728 -> bF_728.method_3083(minecraft, manager, player, level));
        if (this.field_3081-- <= 0) {
            this.field_3081 = this.spawnInterval;
            int n = ThreadLocalRandom.current().nextInt(3);
            for (int i = 0; i < n; ++i) {
                this.method_3081();
            }
        }
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
        this.field_3074.forEach(bF_728 -> bF_728.method_3082(poseStack, minecraft, level, delta));
    }

    @Override
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
        for (BF_728 bF_728 : this.field_3074) {
            BFRendering.billboardLine(camera, poseStack, bF_728.field_3087, bF_728.field_3095, 1.0f, 0xFF4444);
            BFRendering.billboardLine(camera, poseStack, bF_728.field_3095, bF_728.field_3086, 1.0f, 0x44FF44);
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
        fDSTagCompound.setInteger("spawnInterval", this.spawnInterval);
        fDSTagCompound.setFloat("speed", this.speed);
        fDSTagCompound.setDouble("min0x", this.min0.x);
        fDSTagCompound.setDouble("min0y", this.min0.y);
        fDSTagCompound.setDouble("min0z", this.min0.z);
        fDSTagCompound.setDouble("min1x", this.min1.x);
        fDSTagCompound.setDouble("min1y", this.min1.y);
        fDSTagCompound.setDouble("min1z", this.min1.z);
        fDSTagCompound.setDouble("max0x", this.max0.x);
        fDSTagCompound.setDouble("max0y", this.max0.y);
        fDSTagCompound.setDouble("max0z", this.max0.z);
        fDSTagCompound.setDouble("max1x", this.max1.x);
        fDSTagCompound.setDouble("max1y", this.max1.y);
        fDSTagCompound.setDouble("max1z", this.max1.z);
        if (this.soundEvent != null && (string = RegistryUtils.getSoundEventId(this.soundEvent.get())) != null) {
            fDSTagCompound.setString("soundEvent", string);
        }
        fDSTagCompound.setDouble("soundDistanceMin", this.soundDistanceMin);
        int n = this.blocks.size();
        fDSTagCompound.setInteger("blockCount", n);
        for (int i = 0; i < n; ++i) {
            Supplier supplier = (Supplier)this.blockEntities.get(i);
            Supplier supplier2 = (Supplier)this.blocks.get(i);
            String string2 = RegistryUtils.getBlockEntityTypeId(supplier);
            fDSTagCompound.setBoolean("blockEntityExists" + i, string2 != null);
            if (string2 == null) continue;
            fDSTagCompound.setString("blockEntity" + i, string2);
            fDSTagCompound.setString("block" + i, RegistryUtils.getBlockId((Block)supplier2.get()));
        }
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        this.spawnInterval = fDSTagCompound.getInteger("spawnInterval");
        this.speed = fDSTagCompound.getFloat("speed");
        this.min0 = new Vec3(fDSTagCompound.getDouble("min0x"), fDSTagCompound.getDouble("min0y"), fDSTagCompound.getDouble("min0z"));
        this.min1 = new Vec3(fDSTagCompound.getDouble("min1x"), fDSTagCompound.getDouble("min1y"), fDSTagCompound.getDouble("min1z"));
        this.max0 = new Vec3(fDSTagCompound.getDouble("max0x"), fDSTagCompound.getDouble("max0y"), fDSTagCompound.getDouble("max0z"));
        this.max1 = new Vec3(fDSTagCompound.getDouble("max1x"), fDSTagCompound.getDouble("max1y"), fDSTagCompound.getDouble("max1z"));
        String string = fDSTagCompound.getString("soundEvent");
        if (string != null) {
            this.soundEvent = RegistryUtils.retrieveSoundEvent(string);
        }
        this.soundDistanceMin = fDSTagCompound.getDouble("soundDistanceMin");
        this.blockEntities.clear();
        this.blocks.clear();
        int n = fDSTagCompound.getInteger("blockCount", 0);
        for (int i = 0; i < n; ++i) {
            if (!fDSTagCompound.getBoolean("blockEntityExists" + i)) continue;
            this.blockEntities.add(RegistryUtils.retrieveBlockEntity(fDSTagCompound.getString("blockEntity" + i)));
            this.blocks.add(RegistryUtils.retrieveBlock(fDSTagCompound.getString("block" + i)));
        }
    }

    private static class BF_728 {
        @Nullable
        private final Supplier<? extends SimpleParticleType> field_3092;
        private final float field_3093;
        private final BlockEntity field_3089;
        private final Vec3 field_3086;
        @Nullable
        private final Supplier<? extends SoundEvent> field_3094;
        private final double field_3088;
        private final Vec3 field_3087;
        private Vec3 field_3095;
        private Vec3 field_3096;
        private boolean field_3090 = false;
        private boolean field_3091 = false;

        public BF_728(@Nullable Supplier<? extends SoundEvent> supplier, float f, @NotNull BlockEntity blockEntity, @NotNull Vec3 vec3, @NotNull Vec3 vec32, double d, @Nullable Supplier<? extends SimpleParticleType> supplier2) {
            this.field_3093 = f;
            this.field_3089 = blockEntity;
            this.field_3094 = supplier;
            this.field_3088 = d;
            if (Math.random() < 0.5) {
                this.field_3086 = vec32;
                this.field_3095 = this.field_3096 = vec3;
                this.field_3087 = this.field_3096;
            } else {
                this.field_3086 = vec3;
                this.field_3095 = this.field_3096 = vec32;
                this.field_3087 = this.field_3096;
            }
            this.field_3092 = supplier2;
            BlockEntity blockEntity2 = this.field_3089;
            if (blockEntity2 instanceof BFBlockEntity) {
                BFBlockEntity bFBlockEntity = (BFBlockEntity)blockEntity2;
                bFBlockEntity.method_1887();
            }
        }

        public void method_3084(@NotNull Minecraft minecraft) {
            this.field_3090 = true;
            if (this.field_3094 != null) {
                minecraft.getSoundManager().play((SoundInstance)new BF_729(this, this.field_3094.get(), (float)((double)0.9f + (double)0.2f * Math.random()), 0.25f));
            }
        }

        public boolean method_3083(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
            this.field_3096 = this.field_3095;
            this.field_3095 = MathUtils.moveTowards(this.field_3095, this.field_3086, this.field_3093);
            double d = Mth.sqrt((float)((float)localPlayer.distanceToSqr(this.field_3095.x, this.field_3095.y, this.field_3095.z)));
            double d2 = this.field_3093 * 4.0f * 20.0f;
            if (!this.field_3090) {
                if (this.field_3088 != -1.0) {
                    if (d <= this.field_3088) {
                        this.method_3084(minecraft);
                    }
                } else if (d <= d2) {
                    this.method_3084(minecraft);
                }
            }
            Vec2 vec2 = new Vec2((float)this.field_3095.x, (float)this.field_3095.z);
            Vec2 vec22 = new Vec2((float)localPlayer.getX(), (float)localPlayer.getZ());
            float f = Mth.sqrt((float)vec2.distanceToSqr(vec22));
            if (!this.field_3091 && f <= 20.0f) {
                this.field_3091 = true;
                ShakeManager.applyShake(ShakeNodePresets.field_1918);
            }
            if (this.field_3092 != null) {
                ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, this.field_3092.get(), this.field_3095.x, this.field_3095.y, this.field_3095.z, 0.0, 0.0, 0.0);
            }
            return this.field_3095.distanceTo(this.field_3086) <= 1.0;
        }

        public void method_3082(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull ClientLevel clientLevel, float f) {
            if (this.field_3089 == null) {
                return;
            }
            Vec3 vec3 = this.field_3086.subtract(this.field_3095).normalize();
            float f2 = (float)Math.toDegrees(Math.asin(-vec3.y));
            float f3 = (float)Math.toDegrees(Math.atan2(vec3.x, vec3.z));
            Vec3 vec32 = MathUtils.lerp(this.field_3095, this.field_3096, f);
            BFRendering.blockEntity(minecraft, clientLevel, poseStack, this.field_3089, vec32.x, vec32.y, vec32.z, f2, f3, f);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class BF_729
    extends AbstractTickableSoundInstance {
        private final BF_728 field_3097;

        public BF_729(@NotNull BF_728 bF_728, @NotNull SoundEvent soundEvent, float f, float f2) {
            super(soundEvent, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
            this.field_3097 = bF_728;
            this.looping = false;
            this.pitch = f;
            this.volume = f2;
            this.x = (float)bF_728.field_3095.x;
            this.y = (float)bF_728.field_3095.y;
            this.z = (float)bF_728.field_3095.z;
            this.attenuation = SoundInstance.Attenuation.NONE;
        }

        public void tick() {
            if (this.field_3097 != null) {
                this.x = this.field_3097.field_3095.x;
                this.y = this.field_3097.field_3095.y;
                this.z = this.field_3097.field_3095.z;
            }
        }
    }
}

