/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.geo;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudResourceLocation;
import com.boehmod.bflib.cloud.common.item.pattern.SkinPatternUtils;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.GunAimingTickable;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.geo.model.BFWeaponItemGeoModel;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.entity.BFPlayerRenderer;
import com.boehmod.blockfront.client.render.geo.gun.AbstractGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.AimposGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.ArmsGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.BarrelGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.EjectGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.MagRotationGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.MagazineGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.MainGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.MuzzleFlashGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.OverheatGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.RevolverGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.ScopeGunGeoPart;
import com.boehmod.blockfront.client.render.geo.gun.StickersGunGeoPart;
import com.boehmod.blockfront.client.render.type.WeaponOverlayRenderType;
import com.boehmod.blockfront.client.screen.armory.ArmoryInspectScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.boehmod.blockfront.unnamed.BF_1163;
import com.boehmod.blockfront.unnamed.BF_229;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.floats.Float2ObjectMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.awt.Point;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.RenderUtil;

public class BFWeaponItemRenderer<T extends BFWeaponItem<T>>
extends GeoItemRenderer<T> {
    @NotNull
    private static final ResourceLocation[] field_6661 = new ResourceLocation[]{BFRes.loc("textures/models/items/blood0.png"), BFRes.loc("textures/models/items/blood1.png"), BFRes.loc("textures/models/items/blood2.png"), BFRes.loc("textures/models/items/blood3.png")};
    @NotNull
    private static final ResourceLocation[] field_6662 = new ResourceLocation[]{BFRes.loc("textures/models/items/mud0.png"), BFRes.loc("textures/models/items/mud1.png"), BFRes.loc("textures/models/items/mud2.png"), BFRes.loc("textures/models/items/mud3.png")};
    private static final int field_6516 = 40;
    private static final String field_6810 = "main";
    private static final String field_6811 = "main2";
    private static final String field_6812 = "left";
    private static final String field_6813 = "right";
    @Nonnull
    private static final String field_7010 = "PatternOffset";
    @Nonnull
    private static final String field_7011 = "HasPattern";
    @Nonnull
    private static final String field_7012 = "IsGui";
    @Nonnull
    private static final String field_7013 = "Silhouette";
    public static int field_6517 = 0;
    @NotNull
    private final Vector3f field_6515 = new Vector3f();
    @NotNull
    private final Quaternionf field_6514 = new Quaternionf();
    @Nullable
    private GuiGraphics graphics = null;
    private float field_6508 = 0.0f;
    private float field_6509 = 0.0f;
    private float field_6510 = 0.0f;
    private float field_6511 = 0.0f;
    private float field_6512 = 0.0f;
    private float field_6513 = 0.0f;
    @NotNull
    public final List<AbstractGunGeoPart<T>> parts = new ObjectArrayList();
    @Nullable
    private GeoBone field_6587 = null;
    @Nullable
    private GeoBone field_6588 = null;
    @Nullable
    private GeoBone field_6589 = null;
    @Nullable
    private GeoBone field_6590 = null;
    @NotNull
    private final Float2ObjectMap<ResourceLocation> field_6504 = new Float2ObjectOpenHashMap();
    private int field_6518 = -1;

    public BFWeaponItemRenderer() {
        this(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), new BFWeaponItemGeoModel());
    }

    public BFWeaponItemRenderer(@NotNull BlockEntityRenderDispatcher dispatcher, @NotNull EntityModelSet modelSet, @NotNull BFWeaponItemGeoModel<T> model) {
        super(dispatcher, modelSet, model);
        this.parts.add(new AimposGunGeoPart(this));
        this.parts.add(new ArmsGunGeoPart(this));
        this.parts.add(new ScopeGunGeoPart(this));
        this.parts.add(new RevolverGunGeoPart(this));
        this.parts.add(new OverheatGunGeoPart(this));
        this.parts.add(new MuzzleFlashGunGeoPart(this));
        this.parts.add(new EjectGunGeoPart(this));
        this.parts.add(new StickersGunGeoPart(this));
        this.parts.add(new MainGunGeoPart(this));
        this.parts.add(new MagazineGunGeoPart(this));
        this.parts.add(new BarrelGunGeoPart(this));
        this.parts.add(new MagRotationGunGeoPart(this));
    }

    private void method_1250(@NotNull PoseStack poseStack, float f, boolean bl) {
        float f2 = Math.min(MathUtils.lerpf1(GunAimingTickable.field_167, GunAimingTickable.field_168, f), 1.0f);
        float f3 = 1.0f - f2;
        poseStack.translate(this.field_6515.x * f2, this.field_6515.y * f2, this.field_6515.z * f2);
        poseStack.translate(0.05f * f3, -0.05f * f3, 0.0f);
        float f4 = 1.0f - MathUtils.lerpf1(PlayerTickable.field_153, PlayerTickable.field_154, f);
        float f5 = bl ? 10.0f : -10.0f;
        float f6 = f5 * f3 * f4;
        this.field_6514.identity().rotateZ((float)Math.toRadians(f6));
        poseStack.mulPose(this.field_6514);
        float f7 = MathUtils.lerpf1(GunItem.field_4041, GunItem.field_4042, f);
        poseStack.translate(0.0f, 0.0f, 0.02f * f7);
    }

    private boolean method_1248(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull T t, float f, @NotNull MutableInt mutableInt) {
        if (this.renderPerspective.firstPerson()) {
            float f2 = MathUtils.lerpf1(BF_1163.field_6615, BF_1163.field_6616, f);
            float f3 = MathUtils.lerpf1(BF_1163.field_6613, BF_1163.field_6614, f);
            poseStack.translate(0.0f, 0.0f, f2);
            this.field_6514.identity().rotateX((float)Math.toRadians(f3));
            poseStack.mulPose(this.field_6514);
        }
        return this.method_1249(minecraft, poseStack, clientPlayerDataHandler, t, mutableInt);
    }

    private boolean method_1249(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull T t, @NotNull MutableInt mutableInt) {
        GunScopeConfig gunScopeConfig;
        BFClientPlayerData bFClientPlayerData = clientPlayerDataHandler.getPlayerData(minecraft);
        if (t instanceof GunItem) {
            GunItem gunItem = (GunItem)t;
            gunScopeConfig = gunItem.getScopeConfig(this.currentItemStack);
        } else {
            gunScopeConfig = null;
        }
        GunScopeConfig gunScopeConfig2 = gunScopeConfig;
        for (AbstractGunGeoPart abstractGunGeoPart : this.parts) {
            if (!abstractGunGeoPart.method_1266(minecraft, poseStack, this.currentItemStack, mutableInt, gunScopeConfig2, this.renderPerspective, bFClientPlayerData)) continue;
            return true;
        }
        return false;
    }

    private void method_5588(@NotNull Minecraft minecraft, @NotNull BakedGeoModel bakedGeoModel) {
        if (this.field_6518 == field_6517) {
            return;
        }
        this.field_6518 = field_6517;
        bakedGeoModel.getBone(field_6810).ifPresent(geoBone -> {
            this.field_6587 = geoBone;
        });
        bakedGeoModel.getBone(field_6811).ifPresent(geoBone -> {
            this.field_6588 = geoBone;
        });
        bakedGeoModel.getBone(field_6812).ifPresent(geoBone -> {
            this.field_6589 = geoBone;
        });
        bakedGeoModel.getBone(field_6813).ifPresent(geoBone -> {
            this.field_6590 = geoBone;
        });
        BakedModel bakedModel = minecraft.getItemRenderer().getModel(this.currentItemStack, (Level)minecraft.level, null, 0);
        ItemTransform itemTransform = bakedModel.getTransforms().getTransform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
        GeoBone geoBone2 = bakedGeoModel.getBone("aimpos").orElse(null);
        if (geoBone2 != null) {
            this.field_6515.set(-geoBone2.getPivotX(), -geoBone2.getPivotY(), -geoBone2.getPivotZ()).mul(0.0625f);
            float f = this.field_6515.x;
            float f2 = this.field_6515.y;
            float f3 = this.field_6515.z;
            this.field_6515.set(-itemTransform.translation.x - 0.559f, -itemTransform.translation.y + 0.507f, -itemTransform.translation.z + 0.66f).add(f, f2, f3);
        } else {
            this.field_6515.set(0.0f, 0.0f, 0.0f);
        }
    }

    public void method_1246(@NotNull PoseStack poseStack, @NotNull GeoBone geoBone, @NotNull RenderType renderType, @NotNull MultiBufferSource multiBufferSource, @NotNull VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        try {
            super.renderRecursively(poseStack, (Item)((BFWeaponItem)this.animatable), geoBone, renderType, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
        }
        catch (IllegalStateException illegalStateException) {
            BFLog.log("Error rendering bone: " + geoBone.getName(), new Object[0]);
        }
    }

    private void method_1251(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, float f) {
        this.field_6508 += 40.0f;
        String string = geoBone.getName();
        if (string.contains("wobble")) {
            float f2 = this.field_6509;
            if (string.contains("bipod")) {
                f2 *= 1.0f - this.field_6512;
            }
            geoBone.setRotZ(f2);
        }
        for (AbstractGunGeoPart<BFWeaponItem> geoBone2 : this.parts) {
            geoBone2.method_1267(minecraft, poseStack, geoBone, itemStack, this.renderPerspective, (BFWeaponItem)this.animatable, this.field_6512, this.field_6513, this.field_6510, f);
        }
        for (GeoBone geoBone2 : geoBone.getChildBones()) {
            this.method_1251(minecraft, poseStack, geoBone2, itemStack, f);
        }
    }

    public void method_1247(@NotNull Minecraft minecraft, @Nullable AbstractGame<?, ?, ?> abstractGame, @Nullable AbstractGameClient<?, ?> abstractGameClient, @NotNull LocalPlayer localPlayer, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, boolean bl) {
        if (this.field_6587 == null) {
            return;
        }
        EntityRenderer entityRenderer = minecraft.getEntityRenderDispatcher().getRenderer((Entity)localPlayer);
        if (!(entityRenderer instanceof BFPlayerRenderer)) {
            return;
        }
        BFPlayerRenderer bFPlayerRenderer = (BFPlayerRenderer)entityRenderer;
        boolean bl2 = !localPlayer.getMainHandItem().isEmpty();
        boolean bl3 = !localPlayer.getOffhandItem().isEmpty();
        poseStack.pushPose();
        RenderUtil.prepMatrixForBone((PoseStack)poseStack, (GeoBone)this.field_6587);
        if (this.field_6588 != null) {
            RenderUtil.prepMatrixForBone((PoseStack)poseStack, (GeoBone)this.field_6588);
        }
        if (this.field_6589 != null && (bl ? !bl3 : !bl2)) {
            this.method_1238(abstractGame, abstractGameClient, localPlayer, poseStack, multiBufferSource, n, this.field_6589, bFPlayerRenderer, bl);
        }
        if (this.field_6590 != null) {
            this.method_1238(abstractGame, abstractGameClient, localPlayer, poseStack, multiBufferSource, n, this.field_6590, bFPlayerRenderer, !bl);
        }
        poseStack.popPose();
    }

    private void method_1238(@Nullable AbstractGame<?, ?, ?> abstractGame, @Nullable AbstractGameClient<?, ?> abstractGameClient, @NotNull LocalPlayer localPlayer, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, @NotNull GeoBone geoBone, @NotNull BFPlayerRenderer bFPlayerRenderer, boolean bl) {
        BF_229 bF_229;
        boolean bl2;
        poseStack.pushPose();
        RenderUtil.prepMatrixForBone((PoseStack)poseStack, (GeoBone)geoBone);
        EntityModel entityModel = bFPlayerRenderer.getModel();
        boolean bl3 = bl2 = entityModel instanceof BF_229 && (bF_229 = (BF_229)entityModel).method_1020();
        if (bl) {
            poseStack.translate(0.2f, 0.12f, 0.12f);
            this.field_6514.identity().rotateY((float)Math.toRadians(180.0));
            poseStack.mulPose(this.field_6514);
            if (bl2) {
                poseStack.translate(0.1f, 0.0f, 0.0f);
            }
        } else {
            poseStack.translate(0.2f, 0.12f, 0.12f);
        }
        bFPlayerRenderer.method_1356(abstractGame, abstractGameClient, (AbstractClientPlayer)localPlayer, poseStack, multiBufferSource, n, bl);
        poseStack.popPose();
    }

    public void defaultRender(PoseStack poseStack, T t, MultiBufferSource multiBufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer vertexConsumer, float f, float f2, int n) {
        MutableInt mutableInt;
        Minecraft minecraft = Minecraft.getInstance();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        if (this.method_1248(minecraft, poseStack, clientPlayerDataHandler, t, f2, mutableInt = new MutableInt(n))) {
            return;
        }
        super.defaultRender(poseStack, t, multiBufferSource, renderType, vertexConsumer, f, f2, mutableInt.intValue());
    }

    public void actuallyRender(PoseStack poseStack, T t, BakedGeoModel bakedGeoModel, @Nullable RenderType renderType, MultiBufferSource multiBufferSource, @Nullable VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        boolean bl2;
        boolean bl3;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        boolean bl4 = this.renderPerspective.firstPerson();
        this.method_5588(minecraft, bakedGeoModel);
        poseStack.pushPose();
        if (minecraft.screen instanceof ArmoryInspectScreen && this.renderPerspective == ItemDisplayContext.GUI) {
            this.field_6514.identity().rotateZ((float)Math.toRadians(ArmoryInspectScreen.field_949));
            poseStack.mulPose(this.field_6514);
            this.field_6514.identity().rotateY((float)Math.toRadians(ArmoryInspectScreen.field_950));
            poseStack.mulPose(this.field_6514);
        }
        boolean bl5 = bl3 = this.renderPerspective == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
        if (bl4) {
            this.method_1250(poseStack, f, bl3);
        }
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        this.graphics = new GuiGraphics(minecraft, bufferSource);
        this.graphics.pose().mulPose(poseStack.last().pose());
        float f2 = Mth.clamp((float)MathUtils.lerpf1(((BFWeaponItem)((Object)t)).field_3634, ((BFWeaponItem)((Object)t)).field_3635, f), (float)0.0f, (float)1.0f);
        this.field_6508 = BFRendering.getRenderTime();
        this.field_6511 = bl4 ? (GunItem.field_4019 ? 0.2f : 0.85f) : 1.0f;
        this.field_6512 = MathUtils.lerpf1(PlayerTickable.field_153, PlayerTickable.field_154, f);
        this.field_6513 = 1.0f - this.field_6512;
        this.field_6510 = bl4 ? Mth.sin((float)(this.field_6508 / 3.0f)) * f2 : 0.0f;
        this.field_6509 = bl4 ? this.field_6510 * ((BFWeaponItem)this.animatable).getWobbleIntensity() : 0.0f;
        int n4 = BFClientSettings.CONTENT_GORE.isEnabled() ? (Integer)this.currentItemStack.getOrDefault(BFDataComponents.BLOOD_AMOUNT, (Object)0) : 0;
        int n5 = (Integer)this.currentItemStack.getOrDefault(BFDataComponents.MUD_AMOUNT, (Object)0);
        RenderSystem.setShaderTexture((int)3, (ResourceLocation)field_6661[n4]);
        RenderSystem.setShaderTexture((int)4, (ResourceLocation)field_6662[n5]);
        ShaderInstance shaderInstance = WeaponOverlayRenderType.method_5940();
        if (shaderInstance != null) {
            bl2 = BFClientSettings.UI_RENDER_WEAPON_SILHOUETTE_TOGGLE.isEnabled() && (Boolean)this.currentItemStack.getOrDefault(BFDataComponents.DISPLAY_GUN.get(), (Object)false) != false;
            shaderInstance.safeGetUniform(field_7011).set(0);
            shaderInstance.safeGetUniform(field_7012).set(this.renderPerspective == ItemDisplayContext.GUI ? 1 : 0);
            shaderInstance.safeGetUniform(field_7013).set(bl2 ? 1 : 0);
        }
        if (bl2 = ((Boolean)this.currentItemStack.getOrDefault(BFDataComponents.HAS_PATTERN, (Object)false)).booleanValue()) {
            double d = (Double)this.currentItemStack.getOrDefault(BFDataComponents.MINT, (Object)0.0);
            int n6 = (Integer)this.currentItemStack.getOrDefault(BFDataComponents.PATTERN_WIDTH, (Object)1);
            int n7 = (Integer)this.currentItemStack.getOrDefault(BFDataComponents.PATTERN_HEIGHT, (Object)1);
            Point point = SkinPatternUtils.decodeUV((double)d, (int)n6, (int)n7);
            String string = ((String)this.currentItemStack.getOrDefault(BFDataComponents.PATTERN_NAME, (Object)"default")).toLowerCase(Locale.ROOT);
            ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(t);
            ResourceLocation resourceLocation2 = BFRes.loc("textures/item/patterns/" + string + ".png");
            RenderSystem.setShaderTexture((int)5, (ResourceLocation)resourceLocation2);
            ResourceLocation resourceLocation3 = BFRes.loc("textures/item/" + resourceLocation.getPath() + "_template.png");
            RenderSystem.setShaderTexture((int)6, (ResourceLocation)resourceLocation3);
            if (shaderInstance != null) {
                shaderInstance.safeGetUniform(field_7010).set((float)point.x, (float)point.y);
                shaderInstance.safeGetUniform(field_7011).set(1);
            }
        }
        super.actuallyRender(poseStack, t, bakedGeoModel, renderType, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
        if (bl4 && localPlayer != null) {
            this.method_1247(minecraft, abstractGame, abstractGameClient, localPlayer, poseStack, multiBufferSource, n, bl3);
        }
        poseStack.popPose();
    }

    public void renderRecursively(PoseStack poseStack, T t, GeoBone geoBone, RenderType renderType, MultiBufferSource multiBufferSource, VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        assert (this.graphics != null) : "Shared gui graphics is null!";
        Minecraft minecraft = Minecraft.getInstance();
        ResourceLocation resourceLocation = this.getTextureLocation(t);
        PoseStack poseStack2 = this.graphics.pose();
        poseStack2.pushPose();
        boolean bl2 = geoBone.getCubes().isEmpty();
        boolean bl3 = false;
        for (AbstractGunGeoPart<T> abstractGunGeoPart : this.parts) {
            if (!abstractGunGeoPart.shouldSkipRendering(minecraft, poseStack2, this.graphics, geoBone, this.currentItemStack, this.renderPerspective, t, bl2, n, n2, n3, this.field_6511, this.field_6508, f)) continue;
            bl3 = true;
            break;
        }
        if (!bl3) {
            this.method_1251(minecraft, poseStack2, geoBone, this.currentItemStack, f);
            vertexConsumer = multiBufferSource.getBuffer(renderType);
            this.method_1246(poseStack2, geoBone, renderType, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
        }
        for (AbstractGunGeoPart<T> abstractGunGeoPart : this.parts) {
            if (!abstractGunGeoPart.method_1265(minecraft, poseStack2, multiBufferSource, geoBone, t, this.currentItemStack, resourceLocation, this.renderPerspective, vertexConsumer, bl2, f, n, n2, n3)) continue;
            poseStack2.popPose();
            return;
        }
        poseStack2.popPose();
    }

    public void fireCompileRenderLayersEvent() {
    }

    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float f, int n) {
        return true;
    }

    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, MultiBufferSource multiBufferSource, float f, int n) {
    }

    public void updateAnimatedTextureFrame(T t) {
    }

    public long getInstanceId(T t) {
        if (this.renderPerspective == null || !this.renderPerspective.firstPerson()) {
            return -1L;
        }
        return t.hashCode();
    }

    public RenderType getRenderType(T t, ResourceLocation resourceLocation, @Nullable MultiBufferSource multiBufferSource, float f) {
        return WeaponOverlayRenderType.field_6665.apply(resourceLocation);
    }

    public ResourceLocation getTextureLocation(@NotNull T t) {
        if (this.currentItemStack == null) {
            return super.getTextureLocation(t);
        }
        float f = ((Float)this.currentItemStack.getOrDefault(BFDataComponents.SKIN_ID, (Object)Float.valueOf(0.0f))).floatValue();
        if (f <= 0.0f) {
            return super.getTextureLocation(t);
        }
        if (((Boolean)this.currentItemStack.getOrDefault(BFDataComponents.HAS_PATTERN, (Object)false)).booleanValue()) {
            return super.getTextureLocation(t);
        }
        ResourceLocation resourceLocation = (ResourceLocation)this.field_6504.get(f);
        if (resourceLocation != null) {
            return resourceLocation;
        }
        ResourceLocation resourceLocation2 = BuiltInRegistries.ITEM.getKey(t);
        CloudResourceLocation cloudResourceLocation = BFRes.toCloud(resourceLocation2);
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
        CloudItem cloudItem = CloudItem.getItemFromSkinId((CloudRegistry)cloudRegistry, (CloudResourceLocation)cloudResourceLocation, (float)f);
        if (cloudItem == null) {
            return super.getTextureLocation(t);
        }
        String string = cloudItem.getMinecraftItem().path();
        String string2 = cloudItem.getSuffixForDisplay().replace(".", "").replace("-", "");
        ResourceLocation resourceLocation3 = BFRes.loc(("textures/item/" + string + "_" + string2 + ".png").toLowerCase(Locale.ROOT));
        this.field_6504.put(f, (Object)resourceLocation3);
        return resourceLocation3;
    }

    public /* synthetic */ void updateAnimatedTextureFrame(Item item) {
        this.updateAnimatedTextureFrame((T)((Object)((BFWeaponItem)item)));
    }

    public /* synthetic */ void renderRecursively(PoseStack poseStack, Item item, GeoBone geoBone, RenderType renderType, MultiBufferSource multiBufferSource, VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        this.renderRecursively(poseStack, (T)((Object)((BFWeaponItem)item)), geoBone, renderType, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
    }

    public /* synthetic */ void actuallyRender(PoseStack poseStack, Item item, BakedGeoModel bakedGeoModel, @Nullable RenderType renderType, MultiBufferSource multiBufferSource, @Nullable VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        this.actuallyRender(poseStack, (T)((Object)((BFWeaponItem)item)), bakedGeoModel, renderType, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
    }

    public /* synthetic */ ResourceLocation getTextureLocation(@NotNull Item item) {
        return this.getTextureLocation((T)((Object)((BFWeaponItem)item)));
    }

    public /* synthetic */ long getInstanceId(Item item) {
        return this.getInstanceId((T)((Object)((BFWeaponItem)item)));
    }

    public /* synthetic */ void updateAnimatedTextureFrame(GeoAnimatable geoAnimatable) {
        this.updateAnimatedTextureFrame((T)((Object)((BFWeaponItem)geoAnimatable)));
    }

    public /* synthetic */ void renderRecursively(PoseStack poseStack, GeoAnimatable geoAnimatable, GeoBone geoBone, RenderType renderType, MultiBufferSource multiBufferSource, VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        this.renderRecursively(poseStack, (T)((Object)((BFWeaponItem)geoAnimatable)), geoBone, renderType, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
    }

    public /* synthetic */ void actuallyRender(PoseStack poseStack, GeoAnimatable geoAnimatable, BakedGeoModel bakedGeoModel, @Nullable RenderType renderType, MultiBufferSource multiBufferSource, @Nullable VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        this.actuallyRender(poseStack, (T)((Object)((BFWeaponItem)geoAnimatable)), bakedGeoModel, renderType, multiBufferSource, vertexConsumer, bl, f, n, n2, n3);
    }

    public /* synthetic */ void defaultRender(PoseStack poseStack, GeoAnimatable geoAnimatable, MultiBufferSource multiBufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer vertexConsumer, float f, float f2, int n) {
        this.defaultRender(poseStack, (BFWeaponItem)geoAnimatable, multiBufferSource, renderType, vertexConsumer, f, f2, n);
    }

    public /* synthetic */ long getInstanceId(GeoAnimatable geoAnimatable) {
        return this.getInstanceId((T)((Object)((BFWeaponItem)geoAnimatable)));
    }

    public /* synthetic */ RenderType getRenderType(GeoAnimatable geoAnimatable, ResourceLocation resourceLocation, @Nullable MultiBufferSource multiBufferSource, float f) {
        return this.getRenderType((BFWeaponItem)geoAnimatable, resourceLocation, multiBufferSource, f);
    }

    public /* synthetic */ ResourceLocation getTextureLocation(@NotNull GeoAnimatable geoAnimatable) {
        return this.getTextureLocation((T)((Object)((BFWeaponItem)geoAnimatable)));
    }
}

