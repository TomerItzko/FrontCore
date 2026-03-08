/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.tag.IHasCapturePoints;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCapturePoint<P extends AbstractGamePlayerManager<?>>
extends FDSPose {
    public static final int field_3233 = 12;
    private static final ResourceLocation field_3226 = BFRes.loc("textures/misc/bfneutralicon.png");
    private static final int field_3234 = 3;
    private static final int field_3235 = 320;
    private final float field_3230 = (float)((double)0.9f + (double)0.2f * Math.random());
    public String name;
    private Component field_6362 = Component.empty();
    private Component field_6364 = Component.empty();
    @Nullable
    public GameTeam cbTeam = null;
    @Nullable
    public GameTeam cpTeam = null;
    @Nullable
    public GameTeam field_6361 = null;
    public int captureTimer = 0;
    @Nullable
    public ResourceLocation icon = null;
    public boolean isBeingCaptured = false;
    public int field_3225 = 0;
    public float field_3231 = 0.0f;
    public float field_3232 = 0.0f;
    @NotNull
    protected Object2IntMap<String> field_6363 = new Object2IntOpenHashMap();
    private int field_3239 = 0;
    @NotNull
    private AABB captureArea;
    @NotNull
    private final P gameManager;

    public AbstractCapturePoint(@NotNull P p) {
        this.captureArea = this.createCaptureArea();
        this.gameManager = p;
    }

    public AbstractCapturePoint(@NotNull P p, @NotNull String string, @NotNull FDSTagCompound fDSTagCompound) {
        this(p);
        this.readFromFDS(fDSTagCompound.getTagCompound(string));
    }

    public AbstractCapturePoint(@NotNull P p, @NotNull ByteBuf byteBuf) throws IOException {
        this(p);
        this.read(byteBuf);
    }

    public AbstractCapturePoint(@NotNull P gameManager, @NotNull Player player, @NotNull String name) {
        super(player);
        this.gameManager = gameManager;
        this.name = name;
        this.captureArea = this.createCaptureArea();
    }

    @NotNull
    private AABB createCaptureArea() {
        return new AABB(this.position, this.position).inflate((double)0.55f, (double)1.1f, (double)0.55f).move(0.0, (double)2.4f, 0.0);
    }

    @NotNull
    public AABB getCaptureArea() {
        return this.captureArea;
    }

    public void onUpdate(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull AbstractGame<?, ?, ?> game, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        GameTeam gameTeam;
        int n;
        Object object;
        Object object2;
        IHasCapturePoints iHasCapturePoints;
        int n2 = 3;
        if (game instanceof IHasCapturePoints) {
            iHasCapturePoints = (IHasCapturePoints)((Object)game);
            n2 = iHasCapturePoints.method_3398(this);
        }
        iHasCapturePoints = game.getPlayerManager();
        List<ServerPlayer> list = this.playersInSurroundingArea(level, n2, players);
        List<BotEntity> list2 = this.getGameBotsInArea(game, level, n2);
        this.isBeingCaptured = false;
        this.field_6363.clear();
        if (list.isEmpty() && list2.isEmpty()) {
            return;
        }
        if (!list.isEmpty()) {
            for (ServerPlayer object3 : list) {
                if (!this.method_5506((LivingEntity)object3)) continue;
                object2 = object3.getUUID();
                object = dataHandler.getPlayerData((Player)object3);
                Object object4 = ((AbstractGamePlayerManager)((Object)iHasCapturePoints)).getPlayerTeam((UUID)object2);
                if (object4 == null || object3.tickCount <= 20 || !object3.isAlive() || ((BFAbstractPlayerData)object).isOutOfGame()) continue;
                String mutableComponent = ((GameTeam)object4).getName();
                this.field_6363.put((Object)mutableComponent, this.field_6363.getOrDefault((Object)mutableComponent, 0) + 1);
            }
        }
        if (!list2.isEmpty()) {
            for (BotEntity botEntity : list2) {
                if (!this.method_5506((LivingEntity)botEntity) || (object2 = botEntity.getTeam()) == null || botEntity.tickCount <= 20 || !botEntity.isAlive()) continue;
                object = ((GameTeam)object2).getName();
                this.field_6363.put(object, this.field_6363.getOrDefault(object, 0) + 1);
            }
        }
        Object object5 = null;
        boolean bl = false;
        int n3 = 0;
        for (Object object4 : this.field_6363.object2IntEntrySet()) {
            int n4 = object4.getIntValue();
            if (n4 > n) {
                if (object5 != null) {
                    n3 += n;
                }
                object5 = (String)object4.getKey();
                n = n4;
                continue;
            }
            n3 += n4;
        }
        object = null;
        if (object5 != null && n > n3) {
            object = ((AbstractGamePlayerManager)((Object)iHasCapturePoints)).getTeamByName((String)object5);
        }
        if (object != null && !((GameTeam)object).equals(this.cbTeam)) {
            int n5 = n - n3;
            for (int i = 0; i < n5; ++i) {
                if (this.captureTimer < 12) {
                    this.cpTeam = object;
                    this.isBeingCaptured = true;
                    ++this.captureTimer;
                }
                if (this.captureTimer < 12) continue;
                gameTeam = this.cbTeam;
                this.cbTeam = object;
                this.captureTimer = 0;
                if (game instanceof IHasCapturePoints) {
                    IHasCapturePoints iHasCapturePoints2 = (IHasCapturePoints)((Object)game);
                    iHasCapturePoints2.method_3397(manager, (Level)level, this, ((GameTeam)object).getName(), gameTeam != null ? gameTeam.getName() : "", players);
                }
                break;
            }
        } else {
            this.captureTimer = 0;
            this.cpTeam = null;
        }
        if (this.captureTimer > 2) {
            if (this.field_6361 == null || !this.field_6361.equals(this.cpTeam)) {
                if (this.cbTeam != null) {
                    String string = this.name != null ? this.name : "Unknown";
                    MutableComponent mutableComponent = Component.literal((String)string).withStyle(ChatFormatting.GRAY);
                    gameTeam = Component.translatable((String)"bf.message.gamemode.notification.point.losing.msg", (Object[])new Object[]{mutableComponent}).withColor(0xFFFFFF);
                    BFUtils.sendFancyMessage(this.cbTeam, BFUtils.OBJECTIVE_PREFIX, (Component)gameTeam);
                }
                this.field_6361 = this.cpTeam;
            }
        } else {
            this.field_6361 = null;
        }
    }

    protected abstract boolean method_5506(@NotNull LivingEntity var1);

    @OnlyIn(value=Dist.CLIENT)
    public void method_5501(@NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        this.captureArea = this.createCaptureArea();
        double d = Mth.sqrt((float)((float)localPlayer.distanceToSqr(this.position.x, this.position.y, this.position.z)));
        String string = String.format("%.02f", d);
        Style style = this.cbTeam != null ? this.cbTeam.getStyleText() : Style.EMPTY.withColor(ChatFormatting.WHITE);
        this.field_6364 = Component.literal((String)string).withStyle(style).append((Component)Component.literal((String)"m").withColor(0xFFFFFF));
        if (abstractGame.getStatus() != GameStatus.GAME) {
            return;
        }
        this.updateIcon();
        this.field_3232 = this.field_3231;
        this.field_3231 = Mth.lerp((float)0.4f, (float)this.field_3231, (float)this.captureTimer);
        if (this.field_3239 > 0) {
            --this.field_3239;
        }
        if (this.isBeingCaptured) {
            if (this.field_3225++ > 40) {
                this.field_3225 = 0;
            } else if (this.field_3225 > 0 && this.field_3225 % 5 == 0) {
                clientLevel.playLocalSound(this.position.x, this.position.y, this.position.z, (SoundEvent)BFSounds.MATCH_CAPTUREPOINT_TICK.get(), SoundSource.PLAYERS, 1.0f, 1.0f, false);
            }
            if (this.field_3239 == 0 && BFClientSettings.AUDIO_CAPTURE_POINT_ALARM.isEnabled()) {
                this.field_3239 = (int)(320.0 - 5.0 * Math.random());
                clientLevel.playLocalSound(this.position.x, this.position.y, this.position.z, (SoundEvent)BFSounds.MATCH_CAPTUREPOINT_SIREN.get(), SoundSource.AMBIENT, 15.0f, this.field_3230, false);
            }
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_5497(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Frustum frustum, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull Camera camera, float f) {
        if (!BFClientSettings.UI_RENDER_WAYPOINTS.isEnabled()) {
            return;
        }
        if (!frustum.isVisible(this.captureArea)) {
            return;
        }
        PoseStack poseStack = guiGraphics.pose();
        float f2 = Mth.sin((float)(f / 5.0f));
        float f3 = Math.max(this.isBeingCaptured ? 0.5f * f2 : 0.5f, 0.01f);
        int n = this.method_3143(abstractGame);
        ResourceLocation resourceLocation = this.getIcon();
        poseStack.pushPose();
        BFRendering.tintedBillboardTexture(poseStack, camera, guiGraphics, field_3226, this.position.x, this.position.y + (double)2.3f, this.position.z, 50, 50, f3, n, false);
        if (resourceLocation != null) {
            BFRendering.billboardTexture(poseStack, camera, guiGraphics, resourceLocation, this.position.x, this.position.y + (double)2.3f, this.position.z, 50, 50, f3, false);
        }
        double d = this.position.y + (double)1.6f;
        BFRendering.component(poseStack, font, camera, guiGraphics, this.field_6364, this.position.x, d, this.position.z);
        double d2 = this.position.y + 3.5;
        BFRendering.component(poseStack, font, camera, guiGraphics, this.field_6362, this.position.x, d2, this.position.z, 2.0f);
        poseStack.popPose();
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setString("title", this.name);
        fDSTagCompound.setString("cb", this.cbTeam != null ? this.cbTeam.getName() : "");
        fDSTagCompound.setString("cp", this.cpTeam != null ? this.cpTeam.getName() : "");
        fDSTagCompound.setInteger("ct", this.captureTimer);
        fDSTagCompound.setBoolean("fl", this.isBeingCaptured);
    }

    public void method_5496(@NotNull FDSTagCompound fDSTagCompound, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager) {
        super.readFromFDS(fDSTagCompound);
        this.captureArea = this.createCaptureArea();
        this.name = fDSTagCompound.getString("title", "Unknown");
        String string = fDSTagCompound.getString("cb", "");
        String string2 = fDSTagCompound.getString("cp", "");
        this.cbTeam = string.isEmpty() ? null : abstractGamePlayerManager.getTeamByName(string);
        this.cpTeam = string2.isEmpty() ? null : abstractGamePlayerManager.getTeamByName(string2);
        this.field_6361 = null;
        this.captureTimer = fDSTagCompound.getInteger("ct");
        this.isBeingCaptured = fDSTagCompound.getBoolean("fl");
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.captureArea = this.createCaptureArea();
        this.name = fDSTagCompound.getString("title", "Unknown");
        this.cbTeam = null;
        this.cpTeam = null;
        this.field_6361 = null;
        this.captureTimer = fDSTagCompound.getInteger("ct");
        this.isBeingCaptured = fDSTagCompound.getBoolean("fl");
    }

    @Override
    public void read(@NotNull ByteBuf byteBuf) throws IOException {
        super.read(byteBuf);
        this.captureArea = this.createCaptureArea();
        this.name = IPacket.readString((ByteBuf)byteBuf);
        String string = IPacket.readString((ByteBuf)byteBuf);
        this.captureTimer = byteBuf.readInt();
        this.isBeingCaptured = IPacket.readBoolean((ByteBuf)byteBuf);
        String string2 = IPacket.readString((ByteBuf)byteBuf);
        this.cbTeam = string.isEmpty() ? null : ((AbstractGamePlayerManager)this.gameManager).getTeamByName(string);
        this.cpTeam = string2.isEmpty() ? null : ((AbstractGamePlayerManager)this.gameManager).getTeamByName(string2);
        this.field_6361 = null;
        this.field_6362 = Component.literal((String)this.name.toUpperCase(Locale.ROOT)).withStyle(BFStyles.BOLD);
    }

    @Override
    public void write(@NotNull ByteBuf byteBuf) throws IOException {
        super.write(byteBuf);
        IPacket.writeString((ByteBuf)byteBuf, (String)this.name);
        IPacket.writeString((ByteBuf)byteBuf, (String)(this.cbTeam != null ? this.cbTeam.getName() : ""));
        byteBuf.writeInt(this.captureTimer);
        IPacket.writeBoolean((ByteBuf)byteBuf, (boolean)this.isBeingCaptured);
        IPacket.writeString((ByteBuf)byteBuf, (String)(this.cpTeam != null ? this.cpTeam.getName() : ""));
    }

    @Override
    public void writeNamedFDS(@NotNull String nameTag, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound(nameTag);
        this.writeToFDS(fDSTagCompound);
        root.setTagCompound(nameTag, fDSTagCompound);
    }

    @Nullable
    public ResourceLocation getIcon() {
        return this.icon;
    }

    private void updateIcon() {
        if (this.cbTeam == null) {
            this.icon = null;
            return;
        }
        this.icon = BFRes.loc("textures/gui/game/capturepoint/" + this.cbTeam.getName().toLowerCase(Locale.ROOT) + ".png");
    }

    public int method_3143(@NotNull AbstractGame<?, ?, ?> abstractGame) {
        return this.cbTeam != null ? Objects.requireNonNull(this.cbTeam.getStyleText().getColor()).getValue() : 0xFFFFFF;
    }

    @Nullable
    public GameTeam getCbTeam() {
        return this.cbTeam;
    }

    @Nullable
    public GameTeam getCpTeam() {
        return this.cpTeam;
    }

    public boolean method_5499(@Nullable GameTeam gameTeam) {
        if (gameTeam == null || this.cbTeam == null) {
            return false;
        }
        return this.cbTeam.equals(gameTeam);
    }

    public boolean method_5502(@Nullable GameTeam gameTeam) {
        if (gameTeam == null || this.cpTeam == null) {
            return false;
        }
        return this.cpTeam.equals(gameTeam);
    }

    public void reset() {
        this.cpTeam = null;
        this.field_6361 = null;
        this.cbTeam = null;
        this.captureTimer = 0;
        this.isBeingCaptured = false;
        this.field_6363.clear();
    }

    public int method_5500(@NotNull String string) {
        return this.field_6363.getOrDefault((Object)string, 0);
    }

    public int method_5498(@Nullable GameTeam gameTeam) {
        if (gameTeam == null) {
            return 0;
        }
        return this.field_6363.getOrDefault((Object)gameTeam.getName(), 0);
    }

    @NotNull
    public Map<String, Integer> method_5508() {
        return new HashMap<String, Integer>((Map<String, Integer>)this.field_6363);
    }
}

