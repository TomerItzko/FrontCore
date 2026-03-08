/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun;

import com.boehmod.blockfront.registry.BFSounds;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class GunSoundConfig
implements Cloneable {
    public static final GunSoundConfig DEFAULT = new GunSoundConfig();
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> fire = BFSounds.ITEM_GUN_THOMPSON_FIRE;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> pre = null;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> flamethrowerPost = null;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> reload = null;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> reloadEmpty = null;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> lastRound = null;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> corebassClose = BFSounds.ITEM_GUN_SHARED_COREBASS_CLOSE_LMG;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> corebassDistant = BFSounds.ITEM_GUN_SHARED_COREBASS_LMG_DISTANT;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> echoDistantMono = BFSounds.ITEM_GUN_SHARED_ECHO_DISTANT_SMG;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> echoDistantStereo = BFSounds.ITEM_GUN_SHARED_ECHO_DISTANT_SMG_STEREO;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> echoDistantIndoors = BFSounds.ITEM_GUN_SHARED_ECHO_DISTANT_INDOORS;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> echoClose = BFSounds.ITEM_GUN_SHARED_ECHO_CLOSE;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> echoCloseSelf = BFSounds.ITEM_GUN_SHARED_ECHO_CLOSE_SELF;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> echoCloseIndoors = BFSounds.ITEM_GUN_SHARED_ECHO_CLOSE_INDOORS;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> echoCloseIndoorsSelf = BFSounds.ITEM_GUN_SHARED_ECHO_CLOSE_INDOORS_SELF;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> echoCloseIndoorsOpen = BFSounds.ITEM_GUN_SHARED_ECHO_CLOSE_INDOORS_SELF_OPEN;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> lowFreq = BFSounds.ITEM_GUN_SHARED_LFE_LMG;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> arm = BFSounds.ITEM_GUN_SHARED_FOLEY_ARM_LMG;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> towards = BFSounds.ITEM_GUN_SHARED_TOWARDS_SMG;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> rattle = BFSounds.ITEM_GUN_SHARED_RATTLE_SMG;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> add = BFSounds.ITEM_GUN_SHARED_ADD_PISTOL;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> reloadBolt = null;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> bulletLandDefault = null;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> bulletLandWood = null;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> bulletLandMetal = null;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> bulletLandDirt = null;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> bulletLandWater = null;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> zoomIn = BFSounds.ITEM_GUN_SHARED_ZOOM_PISTOL_IN;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> zoomOut = BFSounds.ITEM_GUN_SHARED_ZOOM_PISTOL_OUT;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> equip = BFSounds.ITEM_GUN_SHARED_FOLEY_EQUIP_SMG;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> bulletPass = BFSounds.ITEM_GUN_BULLET_PASS;
    private float field_3909 = 0.0f;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> impactMono = null;
    private DeferredHolder<SoundEvent, SoundEvent> impactStereo = null;
    private boolean field_3907 = false;
    private float field_3910 = 1.0f;
    private int triggerDelay = 2;
    private boolean field_3908 = true;

    @NotNull
    public GunSoundConfig method_3983(boolean bl) {
        this.field_3908 = bl;
        return this;
    }

    public boolean method_3989() {
        return this.field_3908;
    }

    @NotNull
    public GunSoundConfig bulletLand(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder2, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder3, DeferredHolder<SoundEvent, SoundEvent> deferredHolder4, DeferredHolder<SoundEvent, SoundEvent> deferredHolder5) {
        this.bulletLandDefault = deferredHolder;
        this.bulletLandWood = deferredHolder2;
        this.bulletLandMetal = deferredHolder3;
        this.bulletLandDirt = deferredHolder4;
        this.bulletLandWater = deferredHolder5;
        return this;
    }

    @NotNull
    public GunSoundConfig pre(DeferredHolder<SoundEvent, SoundEvent> pre, boolean bl) {
        this.pre = pre;
        this.field_3907 = bl;
        return this;
    }

    @NotNull
    public GunSoundConfig pre(@NotNull DeferredHolder<SoundEvent, SoundEvent> pre, boolean bl, int triggerDelay) {
        this.triggerDelay = triggerDelay;
        return this.pre(pre, bl);
    }

    @NotNull
    public GunSoundConfig reload(@Nullable DeferredHolder<SoundEvent, SoundEvent> reload, @Nullable DeferredHolder<SoundEvent, SoundEvent> reloadEmpty) {
        this.reload = reload;
        this.reloadEmpty = reloadEmpty;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getFire() {
        return this.fire;
    }

    @NotNull
    public GunSoundConfig fire(@Nullable DeferredHolder<SoundEvent, SoundEvent> fire) {
        this.fire = fire;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getReload() {
        return this.reload;
    }

    public GunSoundConfig reload(@Nullable DeferredHolder<SoundEvent, SoundEvent> reload) {
        return this.reload(reload, reload);
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getReloadEmpty() {
        return this.reloadEmpty;
    }

    @NotNull
    public GunSoundConfig reloadEmpty(@Nullable DeferredHolder<SoundEvent, SoundEvent> reloadEmpty) {
        this.reloadEmpty = reloadEmpty;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getLastRound() {
        return this.lastRound;
    }

    @NotNull
    public GunSoundConfig lastRound(@Nullable DeferredHolder<SoundEvent, SoundEvent> lastRound) {
        this.lastRound = lastRound;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getPre() {
        return this.pre;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getFlamethrowerPost() {
        return this.flamethrowerPost;
    }

    @NotNull
    public GunSoundConfig flamethrowerPost(@NotNull DeferredHolder<SoundEvent, SoundEvent> flamethrowerPost) {
        this.flamethrowerPost = flamethrowerPost;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getCorebassClose() {
        return this.corebassClose;
    }

    @NotNull
    public GunSoundConfig corebassClose(@Nullable DeferredHolder<SoundEvent, SoundEvent> corebassClose) {
        this.corebassClose = corebassClose;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getCorebassDistant() {
        return this.corebassDistant;
    }

    @NotNull
    public GunSoundConfig corebassDistant(@Nullable DeferredHolder<SoundEvent, SoundEvent> corebassDistant) {
        this.corebassDistant = corebassDistant;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getEchoDistantMono() {
        return this.echoDistantMono;
    }

    @NotNull
    public GunSoundConfig echoDistant(@Nullable DeferredHolder<SoundEvent, SoundEvent> echoDistantMono, @Nullable DeferredHolder<SoundEvent, SoundEvent> echoDistantStereo) {
        this.echoDistantMono = echoDistantMono;
        this.echoDistantStereo = echoDistantStereo;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getEchoDistantStereo() {
        return this.echoDistantStereo;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getEchoDistantIndoors() {
        return this.echoDistantIndoors;
    }

    @NotNull
    public GunSoundConfig echoDistantIndoors(@Nullable DeferredHolder<SoundEvent, SoundEvent> echoDistantIndoors) {
        this.echoDistantIndoors = echoDistantIndoors;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getEchoClose() {
        return this.echoClose;
    }

    @NotNull
    public GunSoundConfig echoClose(@Nullable DeferredHolder<SoundEvent, SoundEvent> echoClose, @Nullable DeferredHolder<SoundEvent, SoundEvent> echoCloseSelf) {
        this.echoClose = echoClose;
        this.echoCloseSelf = echoCloseSelf;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getEchoCloseSelf() {
        return this.echoCloseSelf;
    }

    @NotNull
    public GunSoundConfig echoCloseIndoors(@Nullable DeferredHolder<SoundEvent, SoundEvent> echoCloseIndoors, @Nullable DeferredHolder<SoundEvent, SoundEvent> echoCloseIndoorsSelf) {
        this.echoCloseIndoors = echoCloseIndoors;
        this.echoCloseIndoorsSelf = echoCloseIndoorsSelf;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getEchoCloseIndoors() {
        return this.echoCloseIndoors;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getEchoCloseIndoorsSelf() {
        return this.echoCloseIndoorsSelf;
    }

    @NotNull
    public GunSoundConfig echoCloseIndoorsOpen(@Nullable DeferredHolder<SoundEvent, SoundEvent> echoCloseIndoorsOpen) {
        this.echoCloseIndoorsOpen = echoCloseIndoorsOpen;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getEchoCloseIndoorsOpen() {
        return this.echoCloseIndoorsOpen;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getLowFreq() {
        return this.lowFreq;
    }

    @NotNull
    public GunSoundConfig lowFreq(@Nullable DeferredHolder<SoundEvent, SoundEvent> lowFreq) {
        this.lowFreq = lowFreq;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getArm() {
        return this.arm;
    }

    @NotNull
    public GunSoundConfig arm(@Nullable DeferredHolder<SoundEvent, SoundEvent> arm) {
        this.arm = arm;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getTowards() {
        return this.towards;
    }

    @NotNull
    public GunSoundConfig towards(@Nullable DeferredHolder<SoundEvent, SoundEvent> towards) {
        this.towards = towards;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getRattle() {
        return this.rattle;
    }

    @NotNull
    public GunSoundConfig rattle(@Nullable DeferredHolder<SoundEvent, SoundEvent> rattle) {
        this.rattle = rattle;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getAdd() {
        return this.add;
    }

    @NotNull
    public GunSoundConfig add(@Nullable DeferredHolder<SoundEvent, SoundEvent> add) {
        this.add = add;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getReloadBolt() {
        return this.reloadBolt;
    }

    @NotNull
    public GunSoundConfig reloadBolt(@Nullable DeferredHolder<SoundEvent, SoundEvent> reloadBolt) {
        this.reloadBolt = reloadBolt;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> method_3976(@NotNull ClientLevel clientLevel, BlockPos blockPos, @NotNull BlockState blockState, @NotNull SoundType soundType) {
        if (clientLevel.isWaterAt(blockPos)) {
            return this.bulletLandWater;
        }
        if (soundType.equals(SoundType.WOOD) || soundType.equals(SoundType.BAMBOO_WOOD) || soundType.equals(SoundType.CHERRY_WOOD) || soundType.equals(SoundType.NETHER_WOOD)) {
            return this.bulletLandWood;
        }
        if (soundType.equals(SoundType.METAL)) {
            return this.bulletLandMetal;
        }
        if (soundType.equals(SoundType.GRASS) || soundType.equals(SoundType.MOSS) || soundType.equals(SoundType.MUD) || soundType.equals(SoundType.PACKED_MUD)) {
            return this.bulletLandDirt;
        }
        return this.bulletLandDefault;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getBulletLandDefault() {
        return this.bulletLandDefault;
    }

    @NotNull
    public GunSoundConfig bulletLandDefault(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        this.bulletLandDefault = deferredHolder;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getBulletLandWood() {
        return this.bulletLandWood;
    }

    @NotNull
    public GunSoundConfig bulletLandWood(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        this.bulletLandWood = deferredHolder;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getBulletLandMetal() {
        return this.bulletLandMetal;
    }

    @NotNull
    public GunSoundConfig bulletLandMetal(@Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        this.bulletLandMetal = deferredHolder;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getZoomIn() {
        return this.zoomIn;
    }

    @NotNull
    public GunSoundConfig zoomIn(@Nullable DeferredHolder<SoundEvent, SoundEvent> zoomIn) {
        this.zoomIn = zoomIn;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getZoomOut() {
        return this.zoomOut;
    }

    @NotNull
    public GunSoundConfig zoomOut(@Nullable DeferredHolder<SoundEvent, SoundEvent> zoomOut) {
        this.zoomOut = zoomOut;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getImpactMono() {
        return this.impactMono;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getImpactStereo() {
        return this.impactStereo;
    }

    @NotNull
    public GunSoundConfig impact(@Nullable DeferredHolder<SoundEvent, SoundEvent> impactMono, @Nullable DeferredHolder<SoundEvent, SoundEvent> impactStereo, float f) {
        this.impactMono = impactMono;
        this.impactStereo = impactStereo;
        this.field_3909 = f;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getEquip() {
        return this.equip;
    }

    @NotNull
    public GunSoundConfig equip(@Nullable DeferredHolder<SoundEvent, SoundEvent> equip) {
        this.equip = equip;
        return this;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getBulletPass() {
        return this.bulletPass;
    }

    @NotNull
    public GunSoundConfig bulletPass(@Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        this.bulletPass = deferredHolder;
        return this;
    }

    public boolean method_3990() {
        return this.field_3907;
    }

    public float method_3984() {
        return this.field_3910;
    }

    public GunSoundConfig method_3975(float f) {
        this.field_3910 = f;
        return this;
    }

    public int getTriggerDelay() {
        return this.triggerDelay;
    }

    public GunSoundConfig clone() {
        try {
            return (GunSoundConfig)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new AssertionError("Clone not supported", cloneNotSupportedException);
        }
    }

    public float method_3985() {
        return this.field_3909;
    }

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        return this.clone();
    }
}

