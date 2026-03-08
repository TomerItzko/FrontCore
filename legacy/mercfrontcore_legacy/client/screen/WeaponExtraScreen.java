package red.vuis.mercfrontcore.client.screen;

import java.util.ArrayList;
import java.util.List;

import com.boehmod.blockfront.common.item.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import red.vuis.mercfrontcore.client.widget.ItemPreview;
import red.vuis.mercfrontcore.client.widget.WeaponEditContainer;
import red.vuis.mercfrontcore.client.widget.Widgets;
import red.vuis.mercfrontcore.data.WeaponExtraSettings;
import red.vuis.mercfrontcore.net.packet.GiveGunPacket;
import red.vuis.mercfrontcore.setup.GunSkinIndex;

import static red.vuis.mercfrontcore.client.widget.WidgetDim.centeredDim;
import static red.vuis.mercfrontcore.client.widget.WidgetDim.dim;
import static red.vuis.mercfrontcore.client.widget.WidgetDim.sqrCenteredDim;
import static red.vuis.mercfrontcore.client.widget.WidgetDim.sqrDim;
public class WeaponExtraScreen extends AddonScreen {
	private static final Text C_BUTTON_BACK = Text.translatable("gui.back");
	private static final Text C_BUTTON_GIVE = Text.translatable("mercfrontcore.screen.generic.button.give");
	private static final Text C_HEADER = Text.translatable("mercfrontcore.screen.weapon.extra.header");
	private static final Text C_LABEL_BARRELTYPE = Text.translatable("mercfrontcore.screen.weapon.label.barrelType");
	private static final Text C_LABEL_MAGTYPE = Text.translatable("mercfrontcore.screen.weapon.label.magType");
	private static final Text C_LABEL_SCOPE = Text.translatable("mercfrontcore.screen.weapon.label.scope");
	private static final Text C_LABEL_SKIN = Text.translatable("mercfrontcore.screen.weapon.label.skin");
	
	private final @Nullable Screen parent;
	private final GunItem item;
	private final WeaponExtraSettings settings;
	private final @Nullable WeaponEditContainer container;
	
	private final List<String> magTypeNames = new ArrayList<>();
	private final List<String> barrelTypeNames = new ArrayList<>();
	private final List<String> skinNames = new ArrayList<>();
	
	private boolean give = false;
	private ItemPreview preview;
	
	public WeaponExtraScreen(@Nullable Screen parent, GunItem item) {
		this(parent, item, new WeaponExtraSettings());
	}
	
	public WeaponExtraScreen(@Nullable Screen parent, GunItem item, WeaponExtraSettings settings) {
		this(parent, item, settings, null);
	}
	
	public WeaponExtraScreen(@Nullable Screen parent, GunItem item, WeaponExtraSettings settings, @Nullable WeaponEditContainer container) {
		super(C_HEADER);
		this.parent = parent;
		this.item = item;
		this.settings = settings;
		this.container = container;
		
		GunSkinIndex.getSkinNames(item).ifPresent(skinNames::addAll);
	}
	
	@Override
	protected void init() {
		super.init();
		
		addDrawableChild(Widgets.checkbox(
			textRenderer,
			sqrCenteredDim(width / 2 - 70, 80, 20),
			settings.scope,
			(checkbox, b) -> {
				settings.scope = b;
				preview.setItemStack(settings.getItemStack(item));
			}
		));
		
		TextFieldWidget magTypeBox = addDrawableChild(Widgets.textField(
			textRenderer,
			dim(width / 2 - 110, 110, 90, 20)
		));
		magTypeBox.setText(settings.magType);
		magTypeBox.setChangedListener(s -> {
			settings.magType = s;
			preview.setItemStack(settings.getItemStack(item));
		});
		
		addDrawableChild(Widgets.button(
			Text.literal("+"),
			dim(width / 2 - 120, 110, 10, 20),
			button -> {
				if (magTypeNames.isEmpty()) {
					return;
				}
				int currentIndex = magTypeNames.indexOf(magTypeBox.getText());
				if (currentIndex < 0) {
					currentIndex = 0;
				}
				magTypeBox.setText(magTypeNames.get((currentIndex + 1) % magTypeNames.size()));
			}
		));
		
		TextFieldWidget barrelTypeBox = addDrawableChild(Widgets.textField(
			textRenderer,
			dim(width / 2 - 110, 150, 90, 20)
		));
		barrelTypeBox.setText(settings.barrelType);
		barrelTypeBox.setChangedListener(s -> {
			settings.barrelType = s;
			preview.setItemStack(settings.getItemStack(item));
		});
		
		addDrawableChild(Widgets.button(
			Text.literal("+"),
			dim(width / 2 - 120, 150, 10, 20),
			button -> {
				if (barrelTypeNames.isEmpty()) {
					return;
				}
				int currentIndex = barrelTypeNames.indexOf(barrelTypeBox.getText());
				if (currentIndex < 0) {
					currentIndex = 0;
				}
				barrelTypeBox.setText(barrelTypeNames.get((currentIndex + 1) % barrelTypeNames.size()));
			}
		));
		
		TextFieldWidget skinBox = addDrawableChild(Widgets.textField(
			textRenderer,
			dim(width / 2 - 110, 190, 90, 20)
		));
		skinBox.setText(settings.skin);
		skinBox.setChangedListener(s -> {
			settings.skin = "".equals(s) ? null : s;
			preview.setItemStack(settings.getItemStack(item));
		});
		
		ButtonWidget skinNextButton = addDrawableChild(Widgets.button(
			Text.literal("+"),
			dim(width / 2 - 120, 190, 10, 20),
			button -> {
				if (skinNames.isEmpty()) {
					return;
				}
				int currentIndex = skinNames.indexOf(skinBox.getText());
				if (currentIndex < 0) {
					currentIndex = 0;
				}
				skinBox.setText(skinNames.get((currentIndex + 1) % skinNames.size()));
			}
		));
		
		addDrawableChild(Widgets.checkbox(
			textRenderer,
			sqrDim(width / 2 - 140, 190, 20),
			settings.skin != null,
			(checkbox, b) -> {
				if (b) {
					skinBox.active = true;
					skinNextButton.active = true;
					if (!skinNames.isEmpty()) {
						skinBox.setText(skinNames.getFirst());
					}
				} else {
					skinBox.active = false;
					skinNextButton.active = false;
					skinBox.setText("");
				}
			}
		));
		
		preview = new ItemPreview(width / 2 + 20, 50, 100)
			.setItemStack(settings.getItemStack(item));
		
		addDrawableChild(Widgets.button(
			give ? C_BUTTON_GIVE : C_BUTTON_BACK,
			centeredDim(width / 2, height - 20, 120, 20),
			button -> {
				if (container != null) {
					container.refresh();
				}
				if (give) {
					PacketDistributor.sendToServer(new GiveGunPacket(item, settings));
				}
				MinecraftClient.getInstance().setScreen(parent);
			}
		));
	}
	
	@Override
	public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		preview.render(context, mouseX, mouseY, delta);
		
		drawText(C_HEADER, width / 2, 20, true);
		
		drawText(C_LABEL_SCOPE, width / 2 - 70, 60, true);
		drawText(C_LABEL_MAGTYPE, width / 2 - 70, 100, true);
		drawText(C_LABEL_BARRELTYPE, width / 2 - 70, 140, true);
		drawText(C_LABEL_SKIN, width / 2 - 70, 180, true);
	}
	
	public WeaponExtraScreen sendGivePacket() {
		give = true;
		return this;
	}
}
