package dev.tomerdev.mercfrontcore.client.screen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.boehmod.blockfront.common.item.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import dev.tomerdev.mercfrontcore.client.widget.AutoCompleteTextFieldWidget;
import dev.tomerdev.mercfrontcore.client.widget.ItemPreview;
import dev.tomerdev.mercfrontcore.client.widget.WeaponEditContainer;
import dev.tomerdev.mercfrontcore.client.widget.Widgets;
import dev.tomerdev.mercfrontcore.data.WeaponExtraSettings;
import dev.tomerdev.mercfrontcore.net.packet.GiveGunPacket;
import dev.tomerdev.mercfrontcore.setup.GunSkinIndex;

import static dev.tomerdev.mercfrontcore.client.widget.WidgetDim.centeredDim;
import static dev.tomerdev.mercfrontcore.client.widget.WidgetDim.dim;
import static dev.tomerdev.mercfrontcore.client.widget.WidgetDim.sqrCenteredDim;
import static dev.tomerdev.mercfrontcore.client.widget.WidgetDim.sqrDim;
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
    private boolean syncedOptionsApplied = false;
	
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
		
		discoverTypeNames();
		GunSkinIndex.ensureInitialized();
		GunSkinIndex.getSkinNames(item).ifPresent(skinNames::addAll);
		if (skinNames.isEmpty()) {
			skinNames.addAll(
				GunSkinIndex.SKINS.values().stream()
					.flatMap(map -> map.keySet().stream())
					.distinct()
					.sorted(String::compareToIgnoreCase)
					.collect(Collectors.toList())
			);
		}
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
		
		AutoCompleteTextFieldWidget magTypeBox = addDrawableChild(new AutoCompleteTextFieldWidget(
			textRenderer,
			width / 2 - 110,
			110,
			90,
			20,
			() -> magTypeNames
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
                refreshOptionsFromSynced();
                cycleTextField(magTypeBox, magTypeNames);
            }
		));
		
		AutoCompleteTextFieldWidget barrelTypeBox = addDrawableChild(new AutoCompleteTextFieldWidget(
			textRenderer,
			width / 2 - 110,
			150,
			90,
			20,
			() -> barrelTypeNames
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
                refreshOptionsFromSynced();
                cycleTextField(barrelTypeBox, barrelTypeNames);
            }
		));
		
		AutoCompleteTextFieldWidget skinBox = addDrawableChild(new AutoCompleteTextFieldWidget(
			textRenderer,
			width / 2 - 110,
			190,
			90,
			20,
			() -> skinNames
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
                refreshOptionsFromSynced();
                cycleTextField(skinBox, skinNames);
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
        refreshOptionsFromSynced();
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

    private void replaceWithSynced(dev.tomerdev.mercfrontcore.net.packet.GunExtraOptionsPacket.GunOptions synced) {
        magTypeNames.clear();
        barrelTypeNames.clear();
        skinNames.clear();

        magTypeNames.add("default");
        barrelTypeNames.add("default");
        mergeDistinct(magTypeNames, synced.magTypes());
        mergeDistinct(barrelTypeNames, synced.barrelTypes());
        mergeDistinct(skinNames, synced.skins());
        skinNames.removeIf(skin -> !isRenderableSkin(skin));

        magTypeNames.sort(String::compareToIgnoreCase);
        barrelTypeNames.sort(String::compareToIgnoreCase);
        skinNames.sort(String::compareToIgnoreCase);
    }

    private void refreshOptionsFromSynced() {
        if (syncedOptionsApplied) {
            return;
        }

        var synced = AddonClientData.getInstance().getGunExtraOptions(Registries.ITEM.getId(item));
        if (synced == null) {
            return;
        }
        if (synced.magTypes().isEmpty() && synced.barrelTypes().isEmpty() && synced.skins().isEmpty()) {
            return;
        }
        replaceWithSynced(synced);
        syncedOptionsApplied = true;
    }

	private void discoverTypeNames() {
		magTypeNames.clear();
		barrelTypeNames.clear();
		magTypeNames.add("default");
		barrelTypeNames.add("default");

		var synced = AddonClientData.getInstance().getGunExtraOptions(Registries.ITEM.getId(item));
		if (synced != null && (!synced.magTypes().isEmpty() || !synced.barrelTypes().isEmpty() || !synced.skins().isEmpty())) {
			replaceWithSynced(synced);
            syncedOptionsApplied = true;
            return;
		}

		collectMapKeysByFieldName(item, "magIdMap", magTypeNames);
		collectMapKeysByFieldName(item, "barrelIdMap", barrelTypeNames);
		collectMapKeysByType(item, "com.boehmod.blockfront.common.gun.GunMagType", magTypeNames);
		collectMapKeysByType(item, "com.boehmod.blockfront.common.gun.GunBarrelType", barrelTypeNames);
		collectStringValues(item, "mag", magTypeNames);
		collectStringValues(item, "barrel", barrelTypeNames);

		magTypeNames.sort(String::compareToIgnoreCase);
		barrelTypeNames.sort(String::compareToIgnoreCase);
	}

	private static void collectStringValues(Object target, String keyword, List<String> out) {
		for (var method : target.getClass().getMethods()) {
			if (method.getParameterCount() != 0) {
				continue;
			}
			String lowerName = method.getName().toLowerCase(Locale.ROOT);
			if (!lowerName.contains(keyword)) {
				continue;
			}
			try {
				Object value = method.invoke(target);
				if (value instanceof Collection<?> collection) {
					for (Object element : collection) {
						addValue(out, element);
					}
				} else if (value instanceof Object[] array) {
					for (Object element : array) {
						addValue(out, element);
					}
				} else {
					addValue(out, value);
				}
			} catch (Throwable ignored) {
			}
		}
	}

	private static void addValue(List<String> out, Object value) {
		if (value == null) {
			return;
		}
		String text = value.toString();
		if (text.isBlank() || out.contains(text)) {
			return;
		}
		out.add(text);
	}

	private static void collectMapKeysByType(Object target, String valueTypeClassName, List<String> out) {
		Class<?> valueType;
		try {
			valueType = Class.forName(valueTypeClassName);
		} catch (Throwable ignored) {
			return;
		}

		Class<?> current = target.getClass();
		while (current != null && current != Object.class) {
			for (var field : current.getDeclaredFields()) {
				try {
					field.setAccessible(true);
					Object value = field.get(target);
					if (!(value instanceof Map<?, ?> map) || map.isEmpty()) {
						continue;
					}
					Object firstValue = map.values().iterator().next();
					if (firstValue == null) {
						continue;
					}
					if (!valueType.isInstance(firstValue)) {
						continue;
					}
					for (Object key : map.keySet()) {
						addValue(out, key);
					}
				} catch (Throwable ignored) {
				}
			}
			current = current.getSuperclass();
		}
	}

	private static void collectMapKeysByFieldName(Object target, String fieldName, List<String> out) {
		Class<?> current = target.getClass();
		while (current != null && current != Object.class) {
			try {
				var field = current.getDeclaredField(fieldName);
				field.setAccessible(true);
				Object value = field.get(target);
				if (value instanceof Map<?, ?> map) {
					for (Object key : map.keySet()) {
						addValue(out, key);
					}
				}
				return;
			} catch (NoSuchFieldException ignored) {
				current = current.getSuperclass();
			} catch (Throwable ignored) {
				return;
			}
		}
	}

	private static void cycleTextField(AutoCompleteTextFieldWidget field, List<String> values) {
		if (values.isEmpty()) {
			return;
		}

		String current = field.getText();
		int index = -1;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).equalsIgnoreCase(current)) {
				index = i;
				break;
			}
		}

		int nextIndex = index < 0 ? 0 : (index + 1) % values.size();
		field.setText(values.get(nextIndex));
	}

    private static boolean mergeDistinct(List<String> target, List<String> source) {
        if (source == null || source.isEmpty()) {
            return false;
        }
        boolean changed = false;
        for (String value : source) {
            if (value == null || value.isBlank() || target.contains(value)) {
                continue;
            }
            target.add(value);
            changed = true;
        }
        return changed;
    }

    private boolean isRenderableSkin(String skin) {
        if (skin == null || skin.isBlank() || "default".equalsIgnoreCase(skin)) {
            return true;
        }
        String itemPath = Registries.ITEM.getId(item).getPath();
        String normalized = skin.toLowerCase(Locale.ROOT).replace(".", "").replace("-", "");
        Identifier texture = Identifier.of("bf", "textures/item/" + itemPath + "_" + normalized + ".png");
        return MinecraftClient.getInstance().getResourceManager().getResource(texture).isPresent();
    }
}
