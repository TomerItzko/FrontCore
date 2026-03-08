/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.entity.IntegratedGameEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_201;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class VendorEntity
extends IntegratedGameEntity {
    private int field_2750 = 0;
    private int field_2751 = 4;
    private float field_2748;
    private float field_2749 = 0.0f;

    public VendorEntity(EntityType<? extends VendorEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder getMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.0).add(Attributes.MAX_HEALTH, Double.MAX_VALUE);
    }

    public boolean isPushable() {
        return false;
    }

    private void method_2485() {
        this.field_2751 = 3;
        this.field_2750 = ThreadLocalRandom.current().nextInt(3);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        Level level = this.level();
        if (level.isClientSide()) {
            this.field_2749 = this.field_2748;
            if (Math.random() < 0.03) {
                this.method_2485();
            }
            if (this.field_2751 > 0) {
                --this.field_2751;
            }
            float f = 0.0f;
            float f2 = 0.4f;
            if (this.field_2750 > 0) {
                f = this.field_2750 == 1 ? f2 : -f2;
            }
            this.field_2748 = Mth.lerp((float)0.3f, (float)this.field_2748, (float)f);
        }
    }

    public boolean isAttackable() {
        return false;
    }

    @OnlyIn(value=Dist.CLIENT)
    @NotNull
    public InteractionResult interactAt(Player player, @NotNull Vec3 vec3, @NotNull InteractionHand interactionHand) {
        if (!player.level().isClientSide()) {
            return super.interactAt(player, vec3, interactionHand);
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame instanceof InfectedGame) {
            InfectedGame infectedGame = (InfectedGame)abstractGame;
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.ENTITY_GUNDEALER_OPEN.get()), (float)1.0f, (float)1.0f));
            minecraft.setScreen((Screen)new BF_201(minecraft.screen, infectedGame.shopItems, infectedGame));
        }
        return super.interactAt(player, vec3, interactionHand);
    }

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
    }

    public int method_2486() {
        return this.field_2750;
    }

    public int method_2487() {
        return this.field_2751;
    }

    public float method_2488(float f) {
        return MathUtils.lerpf1(this.field_2748, this.field_2749, f);
    }

    @NotNull
    public String getScoreboardName() {
        return String.valueOf(ChatFormatting.RED) + "Infected";
    }

    public ResourceLocation getTexture() {
        String string = this.method_2487() > 0 ? "_blink" : String.valueOf(this.method_2486());
        return BFRes.loc("textures/models/entities/vendor/skin" + string + ".png");
    }

    public void knockback(double d, double d2, double d3) {
    }
}

