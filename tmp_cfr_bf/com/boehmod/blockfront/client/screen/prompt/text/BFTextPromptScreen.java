/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.prompt.text;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.bflib.common.PatternReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.SidebarScreen;
import com.boehmod.blockfront.client.screen.prompt.BFPromptScreen;
import com.boehmod.blockfront.registry.BFSounds;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Rectangle;
import java.util.EnumSet;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public abstract class BFTextPromptScreen
extends BFPromptScreen {
    private static final Component CLOSE_LABEL = Component.translatable((String)"bf.menu.button.close");
    private static final Component CONFIRM_LABEL = Component.translatable((String)"bf.menu.button.confirm");
    private static final Component EMPTY = Component.translatable((String)"bf.textprompt.empty");
    private static final Component ERROR_ANYTHING = Component.translatable((String)"bf.textprompt.error.anything");
    private static final Component ERROR_ALPHANUMERIC = Component.translatable((String)"bf.textprompt.error.alphanumeric");
    private static final Component ERROR_NUMERIC = Component.translatable((String)"bf.textprompt.error.numeric");
    private static final Component ERROR_COMMON = Component.translatable((String)"bf.textprompt.error.common");
    private static final Component ERROR_MC_UUID = Component.translatable((String)"bf.textprompt.error.mc.uuid");
    private static final Component ERROR_MC_USERNAME = Component.translatable((String)"bf.textprompt.error.mc.username");
    private static final Component ERROR_UPPERCASE = Component.translatable((String)"bf.textprompt.error.uppercase");
    private static final Component ERROR_LOWERCASE = Component.translatable((String)"bf.textprompt.error.lowercase");
    private static final Component ERROR_MC_RESOURCE = Component.translatable((String)"bf.textprompt.error.mc.resource");
    private static final Component ERROR_IP_ADDRESS = Component.translatable((String)"bf.textprompt.error.mc.resource");
    private static final Component ERROR_CLAN_NAME = Component.translatable((String)"bf.textprompt.error.clan.name", (Object[])new Object[]{2, 4});
    @NotNull
    private final EnumSet<Filter> filters = EnumSet.noneOf(Filter.class);
    public EditBox field_1534;
    private int field_1537 = -1;
    public int field_1538 = 200;
    public int field_1539 = 20;
    public String field_1532 = "";
    private boolean field_1535 = false;
    public boolean field_1536 = false;
    protected int field_1540;
    protected int field_1541;
    @Nullable
    protected Component field_1557 = null;
    private int field_1542 = 0;

    public BFTextPromptScreen(@Nullable Screen screen, @NotNull Component component) {
        super(screen, component);
    }

    @NotNull
    public BFTextPromptScreen addFilter(@NotNull Filter filter) {
        this.filters.add(filter);
        return this;
    }

    @NotNull
    public BFTextPromptScreen method_1094(boolean bl) {
        this.field_1535 = bl;
        return this;
    }

    @NotNull
    public BFTextPromptScreen setMaxLength(int n) {
        this.field_1537 = n;
        return this;
    }

    @NotNull
    public BFTextPromptScreen method_1095(@NotNull Filter[] filterArray) {
        for (Filter filter : filterArray) {
            this.addFilter(filter);
        }
        return this;
    }

    public abstract void confirm();

    public void method_1090() {
        String string = this.field_1534.getValue();
        this.field_1557 = null;
        if (string.isEmpty() && !this.field_1536) {
            this.field_1557 = EMPTY;
        }
        boolean bl = false;
        if (!string.isEmpty()) {
            for (Filter filter : this.filters) {
                if (!filter.isValid(this.minecraft, this, string)) {
                    this.field_1557 = filter.getErrorMessage();
                    continue;
                }
                bl = true;
                break;
            }
        }
        if (bl) {
            this.field_1557 = null;
        }
        if (this.field_1537 != -1 && string.length() > this.field_1537) {
            this.field_1557 = Component.translatable((String)"bf.textprompt.error.length", (Object[])new Object[]{this.field_1537});
        }
        if (this.field_1557 != null) {
            this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_LOBBY_CANCEL.get()), (float)1.0f));
            return;
        }
        this.method_1081();
        this.confirm();
    }

    public int method_1088() {
        return this.field_1537;
    }

    @NotNull
    public String method_1096() {
        return this.field_1534.getValue();
    }

    @Override
    public void init() {
        super.init();
        int n = this.width / 2 - this.field_1538 / 2;
        int n2 = 85 + 11 * this.field_1518.size();
        this.field_1534 = new EditBox(this.font, n, n2, this.field_1538, this.field_1539, (Component)Component.empty());
        this.field_1534.setMaxLength(this.field_1537 == -1 ? 999 : this.field_1537);
        this.field_1534.setValue(this.field_1532);
        this.field_1534.setFocused(true);
    }

    @Override
    public void tick() {
        super.tick();
        if (BFTextPromptScreen.method_771(this.minecraft)) {
            if (this.field_1542 >= 15) {
                this.field_1534.deleteChars(-1);
            } else {
                ++this.field_1542;
            }
        } else {
            this.field_1542 = 0;
        }
        if (BFTextPromptScreen.method_766(this.minecraft)) {
            this.method_1090();
        }
    }

    @Override
    public boolean keyPressed(int n, int n2, int n3) {
        this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_TYPEWRITER.get()), (float)1.0f, (float)1.0f));
        this.field_1534.keyPressed(n, n2, n3);
        return super.keyPressed(n, n2, n3);
    }

    public boolean charTyped(char c2, int n) {
        this.field_1534.charTyped(c2, n);
        return super.charTyped(c2, n);
    }

    @Override
    public boolean mouseClicked(double d, double d2, int n) {
        this.field_1534.mouseClicked(d, d2, n);
        Rectangle rectangle = new Rectangle(this.field_1540, this.field_1541, this.field_1538, this.field_1539);
        if (rectangle.contains(d, d2)) {
            return true;
        }
        return super.mouseClicked(d, d2, n);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2 - 95;
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        this.field_1534.render(guiGraphics, n, n2, f);
        this.method_1087(guiGraphics, n3, 80);
        int n4 = 60 + 11 * this.field_1518.size() + this.field_1539 + 70;
        if (this.field_1557 != null) {
            MutableComponent mutableComponent = this.field_1557.copy().withStyle(ChatFormatting.RED);
            BFRendering.centeredString(this.font, guiGraphics, (Component)mutableComponent, n3 + this.field_1538 / 2, n4);
        }
        poseStack.popPose();
    }

    @Override
    public void method_758() {
        int n = this.width / 2 - 95;
        int n2 = 85 + 11 * this.field_1518.size() + this.field_1539 + 6;
        this.addRenderableWidget((GuiEventListener)new BFButton(n + 2, n2, 80, 20, CLOSE_LABEL, button -> this.method_1081()).textStyle(BFButton.TextStyle.SHADOW).displayType(BFButton.DisplayType.SHADOW));
        this.addRenderableWidget((GuiEventListener)new BFButton(n + 190 - 80 - 2, n2, 80, 20, CONFIRM_LABEL, button -> this.method_1090()).textStyle(BFButton.TextStyle.SHADOW).displayType(BFButton.DisplayType.SHADOW).method_395(ColorReferences.COLOR_TEAM_ALLIES_SOLID).method_397(ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID));
    }

    @Override
    protected void method_1081() {
        Screen screen = this.parentScreen;
        if (screen instanceof SidebarScreen) {
            SidebarScreen sidebarScreen = (SidebarScreen)screen;
            sidebarScreen.field_1030 = true;
        }
        super.method_1081();
    }

    int method_1080() {
        return 20;
    }

    public static enum Filter {
        ANYTHING(PatternReferences.ANYTHING, ERROR_ANYTHING),
        ALPHANUMERIC(PatternReferences.ALPHANUMERIC, ERROR_ALPHANUMERIC),
        NUMERIC(PatternReferences.NUMERIC, ERROR_NUMERIC),
        COMMON(PatternReferences.COMMON, ERROR_COMMON),
        MINECRAFT_UUID(PatternReferences.UUID, ERROR_MC_UUID),
        MINECRAFT_USERNAME(PatternReferences.MINECRAFT_USERNAME, ERROR_MC_USERNAME),
        NO_UPPERCASE(PatternReferences.NO_UPPERCASE, ERROR_UPPERCASE),
        NO_LOWERCASE(PatternReferences.NO_LOWERCASE, ERROR_LOWERCASE),
        MINECRAFT_RESOURCE(PatternReferences.MINECRAFT_RESOURCE, ERROR_MC_RESOURCE),
        IP_ADDRESS(PatternReferences.IP_ADDRESS, ERROR_IP_ADDRESS),
        CLAN_NAME(PatternReferences.CLAN_NAME, ERROR_CLAN_NAME);

        @NotNull
        private final Pattern pattern;
        @NotNull
        private final Component errorMessage;

        private Filter(Pattern pattern, Component component) {
            this.pattern = pattern;
            this.errorMessage = component;
        }

        public boolean isValid(@NotNull Minecraft minecraft, @NotNull BFTextPromptScreen bFTextPromptScreen, @NotNull String string) {
            if (this == MINECRAFT_USERNAME && !bFTextPromptScreen.field_1535 && minecraft.getUser().getName().equalsIgnoreCase(string)) {
                return false;
            }
            return this.pattern.matcher(string).matches();
        }

        @NotNull
        public Component getErrorMessage() {
            return this.errorMessage;
        }
    }
}

