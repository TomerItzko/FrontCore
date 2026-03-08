/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.PlayerRank
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.match;

import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.common.item.GrenadeFragItem;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.tag.IHasClasses;
import com.boehmod.blockfront.unnamed.BF_197;
import com.boehmod.blockfront.unnamed.BF_208;
import com.boehmod.blockfront.unnamed.BF_209;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.FormatUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMatchSelectClassScreen<G extends AbstractGame<G, ?, ?>, C extends AbstractGameClient<G, ?>>
extends BFMenuScreen {
    private static final Component field_1229 = Component.translatable((String)"bf.screen.ingame.select.class");
    private static final Component field_1230 = Component.translatable((String)"bf.message.select.class");
    private static final ResourceLocation field_1219 = BFRes.loc("textures/gui/game/loadout/required_rank.png");
    private static final ResourceLocation field_1220 = BFRes.loc("textures/gui/menu/icons/friends.png");
    private static final ResourceLocation field_1221 = BFRes.loc("textures/gui/game/loadout/required_exp.png");
    private static final Component field_1231 = Component.translatable((String)"bf.message.select.class.tip.click.left").withStyle(ChatFormatting.GRAY);
    private static final Component field_1232 = Component.translatable((String)"bf.message.select.class.tip.click.right").withStyle(ChatFormatting.GRAY);
    private static final Component field_1233 = Component.translatable((String)"bf.message.select.class.tip", (Object[])new Object[]{field_1231, field_1232}).withStyle(ChatFormatting.DARK_GRAY);
    private static final Component field_1234 = Component.literal((String)"< Back");
    private static final Component field_1235 = Component.translatable((String)"bf.message.selected");
    private static final Component field_1236 = Component.translatable((String)"bf.message.class");
    protected static final int field_1222 = 110;
    protected static final int field_1223 = 20;
    protected static final int field_1224 = 100;
    protected static final int field_1225 = 160;
    protected static final int field_1226 = 39;
    protected static final int field_1227 = 140;
    @NotNull
    protected final GameTeam field_1216;
    @NotNull
    protected final G field_1217;
    @NotNull
    protected final C field_1218;
    @Nullable
    protected final Screen field_1238;
    @Nullable
    protected BF_208<BF_197> field_1215;
    @Nullable
    protected BF_197 field_1214 = null;
    @Nullable
    protected Component errorMessage = null;
    protected int errorTimer = 0;

    public AbstractMatchSelectClassScreen(@Nullable Screen screen, @NotNull G g, @NotNull C c2, @NotNull GameTeam gameTeam) {
        super(field_1229);
        this.field_1238 = screen;
        this.field_1216 = gameTeam;
        this.field_1217 = g;
        this.field_1218 = c2;
    }

    public void showErrorMessage(@NotNull Component message) {
        this.errorMessage = message;
        this.errorTimer = 60;
    }

    public void method_894() {
        this.minecraft.setScreen(this.field_1238);
    }

    @Nullable
    public BF_197 method_895() {
        return this.field_1214;
    }

    public void method_897(@NotNull MatchClass matchClass, @NotNull ObjectList<Loadout> objectList, @NotNull String[] stringArray, int n) {
        if (this.field_1215 == null) {
            return;
        }
        this.field_1215.method_950(new BF_197(this, matchClass, objectList, stringArray, 110, 20, n));
    }

    private void method_900(@NotNull LocalPlayer localPlayer, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, int n, int n2, int n3, int n4, float f, float f2) {
        if (this.field_1214 == null) {
            return;
        }
        Loadout loadout = this.field_1214.method_905();
        ItemStack itemStack = localPlayer.getMainHandItem();
        FakePlayer fakePlayer = BFRendering.ENVIRONMENT.getPlayer(this.minecraft);
        if (!loadout.getPrimary().isEmpty()) {
            fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, loadout.getPrimary());
        }
        BFRendering.enableScissor(guiGraphics, n3 + this.method_903(), n4, 100, 160);
        AbstractGame<?, ?, ?> abstractGame = this.manager.getGame();
        if (abstractGame == null) {
            return;
        }
        AbstractGameClient<?, ?> abstractGameClient = this.manager.getGameClient();
        if (abstractGameClient == null) {
            return;
        }
        GameTeam gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(localPlayer.getUUID());
        if (gameTeam == null) {
            return;
        }
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        DivisionData divisionData = gameTeam.getDivisionData(abstractGame);
        BFRendering.centeredTexture(poseStack, guiGraphics, divisionData.getCountry().getNationIcon(), n3 + 50, n4 + 80 - 10, 85, 85, 0.0f, 0.5f);
        BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, n3 + 50, n4 + 80 + 20, 100, 100);
        BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, n3 + 50, n4 + 80 + 20, 100, 100);
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 100.0f);
        int n5 = 6 * (loadout.hashCode() % 5);
        float f3 = 5.0f * Mth.sin((float)(f / 40.0f));
        float f4 = 15.0f * Mth.sin((float)(f / 50.0f));
        BFRendering.entity(this.playerDataHandler, poseStack, guiGraphics, this.minecraft, (LivingEntity)fakePlayer, n3 + 50, n4 + 160 + 60, 90.0f, f3, (float)(-n5) + f4, f2);
        poseStack.popPose();
        guiGraphics.disableScissor();
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 200.0f);
        this.method_899(poseStack, guiGraphics, gameTeam, n, n2, n3, n4);
        this.method_5485(poseStack, guiGraphics, abstractGameClient, localPlayer, playerCloudData, loadout, n3, n4);
        poseStack.popPose();
    }

    private void method_899(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull GameTeam gameTeam, int n, int n2, int n3, int n4) {
        int n5;
        IHasClasses iHasClasses;
        if (this.field_1214 == null) {
            return;
        }
        int n6 = 60;
        BFRendering.rectangleGradient(guiGraphics, n3, n4 + 100, 100, 60, ColorReferences.COLOR_BLACK_TRANSPARENT, ColorReferences.COLOR_BLACK_SOLID);
        String string = this.field_1214.method_906().getDisplayTitle();
        MutableComponent mutableComponent = Component.translatable((String)string).withStyle(ChatFormatting.BOLD).withStyle(gameTeam.getStyleText());
        BFRendering.drawString(this.font, guiGraphics, (Component)mutableComponent, n - 20, n2 - 65);
        Object object = this.field_1217;
        boolean bl = object instanceof IHasClasses && (iHasClasses = (IHasClasses)object).method_3401();
        int n7 = bl ? -15 : 0;
        object = FormatUtils.parseMarkup(this.font, FormatUtils.joinWithSpaces((List<String>)new ObjectArrayList(Arrays.asList(this.field_1214.method_907()))), 190);
        int n8 = object.size();
        poseStack.pushPose();
        poseStack.translate(0.0f, (float)(n7 - n8 * 5), 0.0f);
        for (n5 = 0; n5 < n8; ++n5) {
            BFRendering.component2d(poseStack, this.font, guiGraphics, (Component)Component.literal((String)((String)object.get(n5))), n - 22, n4 + 160 + n5 * 5, 0.5f);
        }
        poseStack.popPose();
        n5 = n - 22;
        int n9 = n2 + 64;
        G g = this.field_1217;
        if (g instanceof IHasClasses) {
            IHasClasses iHasClasses2 = (IHasClasses)g;
            if (bl) {
                MutableComponent mutableComponent2;
                int n10 = this.field_1214.method_909();
                MutableComponent mutableComponent3 = Component.literal((String)String.valueOf(n10)).withStyle(ChatFormatting.WHITE);
                int n11 = iHasClasses2.method_3400(this.field_1214.method_906());
                if (n11 > 0) {
                    MutableComponent mutableComponent4 = Component.literal((String)String.valueOf(n11)).withStyle(ChatFormatting.WHITE);
                    mutableComponent2 = Component.translatable((String)"bf.message.class.limit", (Object[])new Object[]{mutableComponent3, mutableComponent4});
                } else {
                    mutableComponent2 = mutableComponent3;
                }
                BFRendering.drawString(this.font, guiGraphics, (Component)mutableComponent2, n5 + 18, n9 + 4);
                BFRendering.texture(poseStack, guiGraphics, field_1220, n5, n9, 16, 16);
            }
        }
    }

    private void method_5485(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull AbstractGameClient<?, ?> abstractGameClient, @NotNull LocalPlayer localPlayer, PlayerCloudData playerCloudData, @NotNull Loadout loadout, int n, int n2) {
        int n3;
        int n4;
        int n5;
        boolean bl;
        String string;
        IHasClasses iHasClasses;
        Object object;
        block9: {
            block8: {
                if (this.field_1214 == null) {
                    return;
                }
                object = this.field_1217;
                if (!(object instanceof IHasClasses)) break block8;
                iHasClasses = (IHasClasses)object;
                if (abstractGameClient.method_2720()) break block9;
            }
            return;
        }
        object = this.field_1214.method_906();
        if (object == null) {
            return;
        }
        int n7 = 0;
        if (iHasClasses.method_3403()) {
            PlayerRank playerRank = ((MatchClass)((Object)object)).getMinRankRequired();
            boolean n6 = ((MatchClass)((Object)object)).canPlayerUse(this.playerDataHandler, (Player)localPlayer);
            if (playerRank != PlayerRank.RECRUIT && !n6) {
                string = Component.translatable((String)"bf.message.selected.required.rank", (Object[])new Object[]{((MatchClass)((Object)object)).getMinRankRequired().getTitle()}).withColor(0).withStyle(ChatFormatting.BOLD);
                int string2 = 8;
                bl = true;
                n5 = 98;
                int mutableComponent = n + 1;
                n4 = n2 + 160;
                n3 = 6;
                BFRendering.rectangleWithDarkShadow(guiGraphics, mutableComponent, n4, 98, 8, ColorReferences.COLOR_THEME_YELLOW_SOLID);
                BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, (Component)string, mutableComponent + 49 + 3, n4 + 2, 0.5f);
                BFRendering.texture(poseStack, guiGraphics, field_1219, mutableComponent + 1, n4 + 1, 6, 6);
                n7 += 8;
            }
        }
        int n10 = loadout.getMinimumXp();
        if (iHasClasses.method_3402() && n10 != 0) {
            int n6 = playerCloudData.getExpForClass(((Enum)object).ordinal());
            string = StringUtils.formatLong(n6);
            String string2 = StringUtils.formatLong(n10);
            bl = n6 >= n10;
            n5 = bl ? ColorReferences.COLOR_THEME_YELLOW_SOLID : ColorReferences.COLOR_BLACK_SOLID + BFRendering.RED_CHAT_COLOR;
            MutableComponent mutableComponent = Component.literal((String)(string + " / " + string2 + " EXP")).withColor(0).withStyle(ChatFormatting.BOLD);
            n4 = 15;
            n3 = 1;
            int n8 = 98;
            int n9 = n + 1;
            int n11 = n2 + 160 + n7;
            int n12 = 13;
            int n13 = 6;
            BFRendering.rectangleWithDarkShadow(guiGraphics, n9, n11, 98, 15, n5);
            BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, (Component)mutableComponent, n9 + 49 + 6, n11 + 2, 0.5f);
            BFRendering.texture(poseStack, guiGraphics, field_1221, n9 + 1, n11 + 1, 13, 13);
            int n14 = 70;
            int n15 = 4;
            int n16 = n11 + 15 - 7;
            int n17 = n9 + 49 - 35 + 6;
            BFRendering.rectangle(guiGraphics, n17, n16, 70, 4, ColorReferences.COLOR_BLACK_SOLID);
            BFRendering.rectangle(guiGraphics, n17 + 1, n16 + 1, 68, 2, ColorReferences.COLOR_WHITE_SOLID, 0.4f);
            float f = (float)Mth.clamp((int)n6, (int)0, (int)n10) / (float)n10;
            float f2 = 68.0f * f;
            BFRendering.rectangle(poseStack, guiGraphics, (float)(n17 + 1), (float)(n16 + 1), f2, 2.0f, ColorReferences.COLOR_WHITE_SOLID);
        }
    }

    public abstract void method_896(@NotNull BF_197 var1);

    @Override
    public void method_774() {
        super.method_774();
        int n = this.width / 2;
        int n2 = this.height / 2;
        int n3 = n - 110 - 30;
        int n4 = n2 - 80;
        this.field_1215 = new BF_209<BF_197>(n3 + this.method_903(), n4, 110, 159, this);
        this.method_764(this.field_1215.method_547());
    }

    @Override
    public void init() {
        super.init();
        int n = this.width / 2;
        BFButton bFButton = new BFButton(n - 150, 18, 40, 10, field_1234, button -> this.method_894()).displayType(BFButton.DisplayType.NONE);
        this.addRenderableWidget((GuiEventListener)bFButton);
    }

    @Override
    public boolean keyPressed(int n, int n2, int n3) {
        this.method_894();
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.manager.getCinematics().isSequencePlaying()) {
            this.minecraft.setScreen(null);
            return;
        }
        if (this.errorTimer > 0) {
            --this.errorTimer;
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.renderBackground(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        int n3 = BFRendering.translucentBlack();
        int n4 = this.width / 2;
        int n5 = this.height / 2;
        BFRendering.rectangleWithDarkShadow(guiGraphics, 0, 0, this.width, 30, n3);
        BFRendering.rectangleWithDarkShadow(guiGraphics, 0, this.height - 30, this.width, 30, n3);
        int n6 = this.width / 2;
        int n7 = this.height - 20;
        BFRendering.centeredString(this.font, guiGraphics, field_1233, n6, n7);
        poseStack.pushPose();
        poseStack.translate((float)this.method_903(), 0.0f, 0.0f);
        int n8 = n4 - 110 - 30;
        int n9 = n5 - 80;
        BFRendering.rectangleWithDarkShadow(guiGraphics, n8, n9, 110, 160, n3);
        poseStack.popPose();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        Object object;
        int n3;
        int n4;
        int n5;
        super.render(guiGraphics, n, n2, f);
        LocalPlayer localPlayer = this.minecraft.player;
        if (localPlayer == null) {
            return;
        }
        float f2 = BFRendering.getRenderTime();
        PoseStack poseStack = guiGraphics.pose();
        int n6 = BFRendering.translucentBlack();
        int n7 = this.width / 2;
        int n8 = this.height / 2;
        if (this.errorTimer > 0 && this.errorMessage != null) {
            List list = this.font.split((FormattedText)this.errorMessage, 220);
            n5 = list.size();
            n4 = 10 - n5;
            for (n3 = 0; n3 < n5; ++n3) {
                object = (FormattedCharSequence)list.get(n3);
                float f3 = (float)this.font.width((FormattedCharSequence)object) / 2.0f;
                guiGraphics.drawString(this.font, (FormattedCharSequence)object, (int)((float)n7 - f3), n4 + n3 * 10, 0xFFFFFF);
            }
        } else {
            BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, field_1230, n7, 10, 1.5f);
        }
        poseStack.pushPose();
        poseStack.translate((float)this.method_903(), 0.0f, 0.0f);
        if (this.field_1215 != null) {
            for (BF_197 bF_197 : this.field_1215.method_962()) {
                if (!bF_197.method_992() || this.field_1214 == bF_197) continue;
                this.field_1214 = bF_197;
                BFClientPlayerData bFClientPlayerData = this.playerDataHandler.getPlayerData(this.minecraft);
                bFClientPlayerData.method_1146();
            }
        }
        int n9 = n7 - 25;
        n5 = n8 - 80;
        BFRendering.rectangleWithDarkShadow(guiGraphics, n9, n5, 100, 160, n6);
        BFRendering.drawString(this.font, guiGraphics, field_1235, n9 + 5, n5 + 5);
        n4 = n7 + 80;
        n3 = n8 - 60;
        BFRendering.rectangleWithDarkShadow(guiGraphics, n4, n3, 39, 140, n6);
        BFRendering.drawString(this.font, guiGraphics, field_1236, n4 + 5, n3 + 5);
        if (this.field_1214 != null && this.field_1214.method_905() != null) {
            this.method_900(localPlayer, guiGraphics, poseStack, n7, n8, n9, n5, f2, f);
            object = this.field_1214.method_905();
            ObjectArrayList objectArrayList = new ObjectArrayList();
            objectArrayList.add(((Loadout)object).getPrimary());
            objectArrayList.add(((Loadout)object).getSecondary());
            objectArrayList.add(((Loadout)object).getMelee());
            objectArrayList.add(((Loadout)object).getOffHand());
            objectArrayList.addAll(((Loadout)object).getExtra());
            int n10 = objectArrayList.size();
            for (int i = 0; i < n10; ++i) {
                ItemStack itemStack = (ItemStack)objectArrayList.get(i);
                if (itemStack == null) continue;
                float f4 = 1.5f;
                if (itemStack.getItem() instanceof GrenadeFragItem) {
                    f4 = 1.0f;
                }
                BFRendering.item(poseStack, guiGraphics, itemStack, (float)n4 + 19.5f, n3 + 20 + i * 15, f4);
            }
        }
        for (Renderable renderable : this.renderables) {
            if (!(renderable instanceof BFButton)) continue;
            BFButton bFButton = (BFButton)renderable;
            bFButton.method_379(this.minecraft, poseStack, this.font, guiGraphics, n, n2, f2);
        }
        poseStack.popPose();
    }

    public int method_903() {
        return 0;
    }
}

