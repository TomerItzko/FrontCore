/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.IModifyPlayerModel;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BF_229<T extends AbstractClientPlayer>
extends PlayerModel<T> {
    private final boolean field_1393;

    public BF_229(@NotNull ModelPart modelPart, boolean bl) {
        super(modelPart, bl);
        this.field_1393 = bl;
    }

    public boolean method_1020() {
        return this.field_1393;
    }

    public void setupAnim(@NotNull T t, float f, float f2, float f3, float f4, float f5) {
        Item item;
        ItemStack itemStack = t.getMainHandItem();
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof GunItem) {
            ((AbstractClientPlayer)t).swinging = false;
        }
        this.head.zRot = 0.0f;
        super.setupAnim(t, f, f2, f3, f4, f5);
        Minecraft minecraft = Minecraft.getInstance();
        float f6 = BFRendering.getRenderTime() + (float)t.getId();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        UUID uUID = t.getUUID();
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData((Player)t);
        if (!itemStack.isEmpty() && (item = itemStack.getItem()) instanceof IModifyPlayerModel) {
            IModifyPlayerModel iModifyPlayerModel = (IModifyPlayerModel)item;
            float f7 = MathUtils.getTickDelta(minecraft);
            boolean bl = uUID.equals(minecraft.getUser().getProfileId());
            iModifyPlayerModel.modifyPlayerModel(minecraft, bl, this, bFClientPlayerData, (AbstractClientPlayer)t, f6, f7);
        }
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.hat.copyFrom(this.head);
        this.setAllVisible(true);
        if (bFClientManager.getGameClient() != null || t instanceof FakePlayer) {
            float f8 = 0.92f;
            this.hat.xScale = 0.92f;
            this.hat.yScale = 0.92f;
            this.hat.zScale = 0.92f;
            this.jacket.visible = false;
            this.leftSleeve.visible = false;
            this.rightSleeve.visible = false;
            this.leftPants.visible = false;
            this.rightPants.visible = false;
        }
    }

    public /* synthetic */ void setupAnim(@NotNull LivingEntity livingEntity, float f, float f2, float f3, float f4, float f5) {
        this.setupAnim((AbstractClientPlayer)livingEntity, f, f2, f3, f4, f5);
    }

    public /* synthetic */ void setupAnim(@NotNull Entity entity, float f, float f2, float f3, float f4, float f5) {
        this.setupAnim((AbstractClientPlayer)entity, f, f2, f3, f4, f5);
    }
}

