package dev.tomerdev.mercfrontcore.client.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.boehmod.blockfront.common.gun.GunFireMode;
import com.boehmod.blockfront.common.gun.GunTriggerSpawnType;
import com.boehmod.blockfront.common.item.GunItem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import dev.tomerdev.mercfrontcore.client.widget.EnumButtonWidget;
import dev.tomerdev.mercfrontcore.client.widget.FloatFieldWidget;
import dev.tomerdev.mercfrontcore.client.widget.IntegerFieldWidget;
import dev.tomerdev.mercfrontcore.client.widget.ItemPreview;
import dev.tomerdev.mercfrontcore.client.widget.Widgets;
import dev.tomerdev.mercfrontcore.data.GunModifier;
import dev.tomerdev.mercfrontcore.setup.GunModifierIndex;
import dev.tomerdev.mercfrontcore.util.math.FloatBounds;
import dev.tomerdev.mercfrontcore.util.math.IntBounds;

import static dev.tomerdev.mercfrontcore.client.widget.WidgetDim.centeredDim;
import static dev.tomerdev.mercfrontcore.client.widget.WidgetDim.dim;
import static dev.tomerdev.mercfrontcore.client.widget.WidgetDim.sqrCenteredDim;

public class GunModifierEditorScreen extends AddonScreen {
	private static final Text C_BUTTON_BACK = Text.translatable("gui.back");
	private static final Text C_BUTTON_CLOSE_AND_SAVE = Text.translatable("mercfrontcore.screen.gun.modifier.editor.button.closeAndSave");
	private static final Text C_HEADER = Text.translatable("mercfrontcore.screen.gun.modifier.editor.header");
	
	private final GunItem item;
	private final String itemId;
	private final RegistryEntry<Item> itemEntry;
	private final GunModifier defaultModifier;
	private GunModifier modifier;
	
	public GunModifierEditorScreen(GunItem item) {
		super(C_HEADER);
		this.item = item;
		this.itemId = Registries.ITEM.getId(item).toString();
		this.itemEntry = Registries.ITEM.getEntry(item);
		this.defaultModifier = GunModifierIndex.DEFAULT.get(itemEntry);
		this.modifier = AddonClientData.getInstance().tempGunModifiers.getOrDefault(itemEntry, GunModifier.EMPTY);
	}
	
	@Override
	protected void init() {
		super.init();
		assert client != null;
		
		addDrawable(new ItemPreview(sqrCenteredDim(width / 2, 80, 40)).setItemStack(new ItemStack(item)));
		
		addDrawableChild(Widgets.button(
			Ammo.C_HEADER,
			centeredDim(width / 2, 120, 100, 20),
			button -> client.setScreen(new Ammo())
		));
		addDrawableChild(Widgets.button(
			Camera.C_HEADER,
			centeredDim(width / 2, 145, 100, 20),
			button -> client.setScreen(new Camera())
		));
		addDrawableChild(Widgets.button(
			Damage.C_HEADER,
			centeredDim(width / 2, 170, 100, 20),
			button -> client.setScreen(new Damage())
		));
		addDrawableChild(Widgets.button(
			FireModes.C_HEADER,
			centeredDim(width / 2, 195, 100, 20),
			button -> client.setScreen(new FireModes())
		));
		addDrawableChild(Widgets.button(
			Spread.C_HEADER,
			centeredDim(width / 2, 220, 100, 20),
			button -> client.setScreen(new Spread())
		));
		addDrawableChild(Widgets.button(
			Other.C_HEADER,
			centeredDim(width / 2, 245, 100, 20),
			button -> client.setScreen(new Other())
		));
		
		addDrawableChild(Widgets.button(
			C_BUTTON_CLOSE_AND_SAVE,
			centeredDim(width / 2, height - 20, 90, 20),
			button -> close()
		));
	}
	
	@Override
	public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		
		drawText(C_HEADER, width / 2, 20, true);
		
		drawText(Text.literal(itemId), width / 2, 60, true);
	}
	
	@Override
	public void close() {
		if (modifier.hasData()) {
			AddonClientData.getInstance().tempGunModifiers.put(itemEntry, modifier);
		}
		
		assert client != null;
		client.getMessageHandler().onGameMessage(
			Text.translatable(
				"mercfrontcore.screen.gun.modifier.editor.closeMessage",
				Text.literal("/frontcore gun modifier sync").formatted(Formatting.RED)
			),
			false
		);
		
		super.close();
	}
	
	private <T> T modifierPart(Function<GunModifier, Optional<T>> getter) {
		return getter.apply(modifier).orElseGet(() -> getter.apply(defaultModifier).orElseThrow());
	}
	
	private class Ammo extends AddonScreen {
		private static final Text C_HEADER = Text.translatable("mercfrontcore.screen.gun.modifier.editor.ammo.header");
		private static final Text C_LABEL_MAGAZINE = Text.translatable("mercfrontcore.screen.gun.modifier.editor.ammo.label.magazine");
		private static final Text C_LABEL_RESERVE = Text.translatable("mercfrontcore.screen.gun.modifier.editor.ammo.label.reserve");
		private static final IntBounds AMMO_BOUNDS = IntBounds.ofMin(0);
		
		private IntegerFieldWidget magazineField;
		private IntegerFieldWidget reserveField;
		
		public Ammo() {
			super(C_HEADER);
		}
		
		@Override
		protected void init() {
			super.init();
			
			GunModifier.Ammo ammo = modifierPart(GunModifier::ammo);
			
			magazineField = addDrawableChild(new IntegerFieldWidget(
				textRenderer,
				centeredDim(width / 2, 80, 100, 20),
				Text.empty(),
				AMMO_BOUNDS
			));
			magazineField.setInt(ammo.magazine());
			
			reserveField = addDrawableChild(new IntegerFieldWidget(
				textRenderer,
				centeredDim(width / 2, 120, 100, 20),
				Text.empty(),
				AMMO_BOUNDS
			));
			reserveField.setInt(ammo.reserve());
			
			addDrawableChild(Widgets.button(
				C_BUTTON_BACK,
				centeredDim(width / 2, height - 20, 90, 20),
				button -> close()
			));
		}
		
		@Override
		public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
			super.render(context, mouseX, mouseY, delta);
			
			drawText(C_HEADER, width / 2, 20, true);
			
			drawText(C_LABEL_MAGAZINE, width / 2, 60, true);
			drawText(C_LABEL_RESERVE, width / 2, 100, true);
		}
		
		@Override
		public void close() {
			GunModifier.Ammo newAmmo = new GunModifier.Ammo(magazineField.getInt(), reserveField.getInt());
			modifier = modifier.withAmmo(
				newAmmo.equals(defaultModifier.ammo().orElseThrow()) ? Optional.empty() : Optional.of(newAmmo)
			);
			
			assert client != null;
			client.setScreen(GunModifierEditorScreen.this);
		}
	}
	
	private class Camera extends AddonScreen {
		private static final Text C_HEADER = Text.translatable("mercfrontcore.screen.gun.modifier.editor.camera.header");
		private static final Text C_LABEL_CRAWLING_RECOIL = Text.translatable("mercfrontcore.screen.gun.modifier.editor.camera.label.crawlingRecoil");
		private static final Text C_LABEL_IDLE_RECOIL = Text.translatable("mercfrontcore.screen.gun.modifier.editor.camera.label.idleRecoil");
		private static final Text C_LABEL_JUMPING_RECOIL = Text.translatable("mercfrontcore.screen.gun.modifier.editor.camera.label.jumpingRecoil");
		private static final Text C_LABEL_RECOVER_SPEED = Text.translatable("mercfrontcore.screen.gun.modifier.editor.camera.label.recoverSpeed");
		private static final Text C_LABEL_SPRINTING_RECOIL = Text.translatable("mercfrontcore.screen.gun.modifier.editor.camera.label.sprintingRecoil");
		private static final Text C_LABEL_WALKING_RECOIL = Text.translatable("mercfrontcore.screen.gun.modifier.editor.camera.label.walkingRecoil");
		private static final FloatBounds RECOIL_BOUNDS = FloatBounds.ofMin(0);
		private static final FloatBounds RECOVER_SPEED_BOUNDS = FloatBounds.ofMin(0);
		
		private FloatFieldWidget
			crawlingRecoilField,
			idleRecoilField,
			walkingRecoilField,
			sprintingRecoilField,
			jumpingRecoilField,
			recoverSpeedField;
		
		public Camera() {
			super(C_HEADER);
		}
		
		@Override
		protected void init() {
			super.init();
			
			GunModifier.Camera camera = modifierPart(GunModifier::camera);
			
			crawlingRecoilField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2, 80, 100, 20),
				Text.empty(),
				RECOIL_BOUNDS
			));
			crawlingRecoilField.setFloat(camera.crawlingRecoil());
			
			idleRecoilField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2, 120, 100, 20),
				Text.empty(),
				RECOIL_BOUNDS
			));
			idleRecoilField.setFloat(camera.idleRecoil());
			
			walkingRecoilField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2, 160, 100, 20),
				Text.empty(),
				RECOIL_BOUNDS
			));
			walkingRecoilField.setFloat(camera.walkingRecoil());
			
			sprintingRecoilField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2, 200, 100, 20),
				Text.empty(),
				RECOIL_BOUNDS
			));
			sprintingRecoilField.setFloat(camera.sprintingRecoil());
			
			jumpingRecoilField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2, 240, 100, 20),
				Text.empty(),
				RECOIL_BOUNDS
			));
			jumpingRecoilField.setFloat(camera.jumpingRecoil());
			
			recoverSpeedField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2, 280, 100, 20),
				Text.empty(),
				RECOVER_SPEED_BOUNDS
			));
			recoverSpeedField.setFloat(camera.recoverSpeed());
			
			addDrawableChild(Widgets.button(
				C_BUTTON_BACK,
				centeredDim(width / 2, height - 20, 90, 20),
				button -> close()
			));
		}
		
		@Override
		public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
			super.render(context, mouseX, mouseY, delta);
			
			drawText(C_HEADER, width / 2, 20, true);
			
			drawText(C_LABEL_CRAWLING_RECOIL, width / 2, 60, true);
			drawText(C_LABEL_IDLE_RECOIL, width / 2, 100, true);
			drawText(C_LABEL_WALKING_RECOIL, width / 2, 140, true);
			drawText(C_LABEL_SPRINTING_RECOIL, width / 2, 180, true);
			drawText(C_LABEL_JUMPING_RECOIL, width / 2, 220, true);
			drawText(C_LABEL_RECOVER_SPEED, width / 2, 260, true);
		}
		
		@Override
		public void close() {
			GunModifier.Camera newCamera = new GunModifier.Camera(
				jumpingRecoilField.getFloat(),
				sprintingRecoilField.getFloat(),
				walkingRecoilField.getFloat(),
				idleRecoilField.getFloat(),
				crawlingRecoilField.getFloat(),
				recoverSpeedField.getFloat()
			);
			modifier = modifier.withCamera(
				newCamera.equals(defaultModifier.camera().orElseThrow()) ? Optional.empty() : Optional.of(newCamera)
			);
			
			assert client != null;
			client.setScreen(GunModifierEditorScreen.this);
		}
	}
	
	private class Damage extends AddonScreen {
		private static final Text C_HEADER = Text.translatable("mercfrontcore.screen.gun.modifier.editor.damage.header");
		private static final Text C_LABEL_BODY = Text.translatable("mercfrontcore.screen.gun.modifier.editor.damage.label.body");
		private static final Text C_LABEL_DISTANCE = Text.translatable("mercfrontcore.screen.gun.modifier.editor.damage.label.distance");
		private static final Text C_LABEL_HEAD = Text.translatable("mercfrontcore.screen.gun.modifier.editor.damage.label.head");
		private static final FloatBounds DISTANCE_BOUNDS = FloatBounds.ofMin(0);
		private static final FloatBounds BODY_BOUNDS = FloatBounds.ofMin(0);
		private static final FloatBounds HEAD_BOUNDS = FloatBounds.ofMin(0);
		
		private final List<EntryWidgetHolder> entries = new ArrayList<>();
		private ButtonWidget addButton, removeButton;
		
		public Damage() {
			super(C_HEADER);
		}
		
		@Override
		protected void init() {
			super.init();
			
			modifierPart(GunModifier::damage).forEach(this::addEntry);
			
			addButton = addDrawableChild(Widgets.button(
				Text.literal("+"),
				centeredDim(width / 2 - 60, 76 + entries.size() * 20, 120, 12),
				button -> {
					float distance = entries.isEmpty() ? 0 : entries.getLast().distance.getFloat();
					addEntry(new GunModifier.Damage(distance, 0, 0));
					updateNumButtons();
				}
			));
			removeButton = addDrawableChild(Widgets.button(
				Text.literal("-"),
				centeredDim(width / 2 + 60, 76 + entries.size() * 20, 120, 12),
				button -> {
					entries.removeLast().forEach(this::remove);
					updateNumButtons();
				}
			));
			updateNumButtons();
			
			addDrawableChild(Widgets.button(
				C_BUTTON_BACK,
				centeredDim(width / 2, height - 20, 90, 20),
				button -> close()
			));
		}
		
		@Override
		public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
			super.render(context, mouseX, mouseY, delta);
			
			drawText(C_HEADER, width / 2, 20, true);
			
			drawText(C_LABEL_DISTANCE, width / 2 - 80, 60, true);
			drawText(C_LABEL_BODY, width / 2, 60, true);
			drawText(C_LABEL_HEAD, width / 2 + 80, 60, true);
		}
		
		@Override
		public void close() {
			List<GunModifier.Damage> newDamage = entries.stream().map(holder -> new GunModifier.Damage(
				holder.body().getFloat(), holder.head().getFloat(), holder.distance().getFloat())
			).toList();
			modifier = modifier.withDamage(
				newDamage.equals(defaultModifier.damage().orElseThrow()) ? Optional.empty() : Optional.of(newDamage)
			);
			
			assert client != null;
			client.setScreen(GunModifierEditorScreen.this);
		}
		
		private void addEntry(GunModifier.Damage damage) {
			FloatFieldWidget distanceField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2 - 80, 80 + entries.size() * 20, 80, 20),
				Text.empty(),
				DISTANCE_BOUNDS
			));
			distanceField.setFloat(damage.minDist());
			
			FloatFieldWidget bodyField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2, 80 + entries.size() * 20, 80, 20),
				Text.empty(),
				BODY_BOUNDS
			));
			bodyField.setFloat(damage.body());
			
			FloatFieldWidget headField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2 + 80, 80 + entries.size() * 20, 80, 20),
				Text.empty(),
				HEAD_BOUNDS
			));
			headField.setFloat(damage.head());
			
			entries.add(new EntryWidgetHolder(distanceField, bodyField, headField));
		}
		
		private void updateNumButtons() {
			centeredDim(width / 2 - 60, 76 + entries.size() * 20, 120, 12).apply(addButton);
			centeredDim(width / 2 + 60, 76 + entries.size() * 20, 120, 12).apply(removeButton);
			removeButton.active = entries.size() > 1;
		}
		
		private record EntryWidgetHolder(
			@NotNull FloatFieldWidget distance,
			@NotNull FloatFieldWidget body,
			@NotNull FloatFieldWidget head
		) {
			public void forEach(Consumer<FloatFieldWidget> consumer) {
				consumer.accept(distance);
				consumer.accept(body);
				consumer.accept(head);
			}
		}
	}
	
	private class FireModes extends AddonScreen {
		private static final Text C_HEADER = Text.translatable("mercfrontcore.screen.gun.modifier.editor.fireModes.header");
		private static final Text C_LABEL_ENTITY = Text.translatable("mercfrontcore.screen.gun.modifier.editor.fireModes.label.entity");
		private static final Text C_LABEL_INSTANCES = Text.translatable("mercfrontcore.screen.gun.modifier.editor.fireModes.label.instances");
		private static final Text C_LABEL_MODE = Text.translatable("mercfrontcore.screen.gun.modifier.editor.fireModes.label.mode");
		private static final Text C_LABEL_TICKS = Text.translatable("mercfrontcore.screen.gun.modifier.editor.fireModes.label.ticks");
		private static final Text C_LABEL_TYPE = Text.translatable("mercfrontcore.screen.gun.modifier.editor.fireModes.label.type");
		private static final IntBounds INSTANCES_BOUNDS = IntBounds.ofMin(1);
		private static final IntBounds TICKS_BOUNDS = IntBounds.ofMin(1);
		
		private final List<EntryWidgetHolder> entries = new ArrayList<>();
		private ButtonWidget addButton, removeButton;
		private ButtonWidget backButton;
		
		public FireModes() {
			super(C_HEADER);
		}
		
		@Override
		protected void init() {
			super.init();
			
			backButton = addDrawableChild(Widgets.button(
				C_BUTTON_BACK,
				centeredDim(width / 2, height - 20, 90, 20),
				button -> close()
			));
			
			modifierPart(GunModifier::fireModes).forEach(this::addEntry);
			addButton = addDrawableChild(Widgets.button(
				Text.literal("+"),
				dim(width / 2 - 220, 70 + entries.size() * 20, 220, 12),
				button -> {
					addEntry(new GunModifier.FireMode(GunTriggerSpawnType.BULLET, GunFireMode.SEMI, 1, 1, Optional.empty()));
					updateNumButtons();
				}
			));
			removeButton = addDrawableChild(Widgets.button(
				Text.literal("-"),
				dim(width / 2, 70 + entries.size() * 20, 220, 12),
				button -> {
					entries.removeLast().forEach(this::remove);
					updateNumButtons();
				}
			));
		}
		
		@Override
		public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
			super.render(context, mouseX, mouseY, delta);
			
			drawText(C_HEADER, width / 2, 20, true);
			
			drawText(C_LABEL_TYPE, width / 2 - 180, 60, true);
			drawText(C_LABEL_MODE, width / 2 - 100, 60, true);
			drawText(C_LABEL_TICKS, width / 2 - 20, 60, true);
			drawText(C_LABEL_INSTANCES, width / 2 + 60, 60, true);
			drawText(C_LABEL_ENTITY, width / 2 + 160, 60, true);
		}
		
		@Override
		public void close() {
			List<GunModifier.FireMode> newFireModes = entries.stream().map(holder -> {
				Optional<RegistryEntry<EntityType<?>>> entity;
				if (holder.type().getEnum() == GunTriggerSpawnType.ENTITY) {
					entity = Optional.of(Registries.ENTITY_TYPE.getEntry(Registries.ENTITY_TYPE.get(Identifier.of(holder.entity().getText()))));
				} else {
					entity = Optional.empty();
				}
				return new GunModifier.FireMode(
					holder.type().getEnum(),
					holder.mode().getEnum(),
					holder.ticks().getInt(),
					holder.instances().getInt(),
					entity
				);
			}).toList();
			modifier = modifier.withFireModes(
				newFireModes.equals(defaultModifier.fireModes().orElseThrow()) ? Optional.empty() : Optional.of(newFireModes)
			);
			
			assert client != null;
			client.setScreen(GunModifierEditorScreen.this);
		}
		
		private void addEntry(GunModifier.FireMode fireMode) {
			EnumButtonWidget<GunTriggerSpawnType> typeButton = addDrawableChild(new EnumButtonWidget<>(
				GunTriggerSpawnType.values(),
				dim(width / 2 - 220, 70 + entries.size() * 20, 80, 20),
				new ButtonWidget.PressAction() {
					private final int index = entries.size();
					
					@Override
					public void onPress(ButtonWidget b) {
						@SuppressWarnings("unchecked")
						EnumButtonWidget<GunTriggerSpawnType> button = (EnumButtonWidget<GunTriggerSpawnType>) b;
						
						TextFieldWidget entityField = entries.get(index).entity();
						switch (button.getEnum()) {
							case BULLET -> {
								entityField.active = false;
								entityField.setText("");
							}
							case ENTITY -> {
								entityField.active = true;
							}
						}
					}
				}
			));
			typeButton.setEnum(fireMode.type());
			
			EnumButtonWidget<GunFireMode> modeButton = addDrawableChild(new EnumButtonWidget<>(
				GunFireMode.values(),
				dim(width / 2 - 140, 70 + entries.size() * 20, 80, 20)
			));
			modeButton.setEnum(fireMode.mode());
			
			IntegerFieldWidget ticksField = addDrawableChild(new IntegerFieldWidget(
				textRenderer,
				dim(width / 2 - 60, 70 + entries.size() * 20, 80, 20),
				C_LABEL_TICKS,
				TICKS_BOUNDS
			));
			ticksField.setInt(fireMode.ticks());
			
			IntegerFieldWidget instancesField = addDrawableChild(new IntegerFieldWidget(
				textRenderer,
				dim(width / 2 + 20, 70 + entries.size() * 20, 80, 20),
				C_LABEL_INSTANCES,
				INSTANCES_BOUNDS
			));
			instancesField.setInt(fireMode.numInstances());
			
			TextFieldWidget entityField = addDrawableChild(Widgets.textField(
				textRenderer,
				dim(width / 2 + 100, 70 + entries.size() * 20, 120, 20)
			));
			entityField.setChangedListener(s ->
				backButton.active = Optional.ofNullable(Identifier.tryParse(s)).map(Registries.ENTITY_TYPE::containsId).orElse(false)
			);
			if (fireMode.type() == GunTriggerSpawnType.ENTITY) {
				fireMode.entity().ifPresent(regEntry ->
					entityField.setText(Registries.ENTITY_TYPE.getId(regEntry.value()).toString())
				);
			} else {
				entityField.active = false;
			}
			
			entries.add(new EntryWidgetHolder(typeButton, modeButton, ticksField, instancesField, entityField));
		}
		
		private void updateNumButtons() {
			dim(width / 2 - 220, 70 + (entries.size() + 1) * 20, 220, 12).apply(addButton);
			dim(width / 2, 70 + (entries.size() + 1) * 20, 220, 12).apply(removeButton);
			removeButton.active = entries.size() > 1;
		}
		
		private record EntryWidgetHolder(
			@NotNull EnumButtonWidget<GunTriggerSpawnType> type,
			@NotNull EnumButtonWidget<GunFireMode> mode,
			@NotNull IntegerFieldWidget ticks,
			@NotNull IntegerFieldWidget instances,
			@NotNull TextFieldWidget entity
		) {
			public void forEach(Consumer<Element> consumer) {
				consumer.accept(type);
				consumer.accept(mode);
				consumer.accept(ticks);
				consumer.accept(instances);
				consumer.accept(entity);
			}
		}
	}
	
	private class Spread extends AddonScreen {
		private static final Text C_HEADER = Text.translatable("mercfrontcore.screen.gun.modifier.editor.spread.header");
		private static final Text C_LABEL_CRAWLING = Text.translatable("mercfrontcore.screen.gun.modifier.editor.spread.label.crawling");
		private static final Text C_LABEL_CRAWLING_ADS = Text.translatable("mercfrontcore.screen.gun.modifier.editor.spread.label.crawlingAds");
		private static final Text C_LABEL_IDLE = Text.translatable("mercfrontcore.screen.gun.modifier.editor.spread.label.idle");
		private static final Text C_LABEL_IDLE_ADS = Text.translatable("mercfrontcore.screen.gun.modifier.editor.spread.label.idleAds");
		private static final Text C_LABEL_JUMPING = Text.translatable("mercfrontcore.screen.gun.modifier.editor.spread.label.jumping");
		private static final Text C_LABEL_WALKING = Text.translatable("mercfrontcore.screen.gun.modifier.editor.spread.label.walking");
		private static final Text C_LABEL_WALKING_ADS = Text.translatable("mercfrontcore.screen.gun.modifier.editor.spread.label.walkingAds");
		private static final FloatBounds SPREAD_BOUNDS = FloatBounds.ofMin(0);
		
		private FloatFieldWidget
			crawlingField, crawlingAdsField,
			idleField, idleAdsField,
			walkingField, walkingAdsField,
			jumpingField;
		
		public Spread() {
			super(C_HEADER);
		}
		
		@Override
		protected void init() {
			super.init();
			
			GunModifier.Spread spread = modifierPart(GunModifier::spread);
			
			crawlingField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2 - 60, 80, 100, 20),
				Text.empty(),
				SPREAD_BOUNDS
			));
			crawlingField.setFloat(spread.crawling());
			
			crawlingAdsField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2 + 60, 80, 100, 20),
				Text.empty(),
				SPREAD_BOUNDS
			));
			crawlingAdsField.setFloat(spread.crawlingAds());
			
			idleField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2 - 60, 120, 100, 20),
				Text.empty(),
				SPREAD_BOUNDS
			));
			idleField.setFloat(spread.idle());
			
			idleAdsField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2 + 60, 120, 100, 20),
				Text.empty(),
				SPREAD_BOUNDS
			));
			idleAdsField.setFloat(spread.idleAds());
			
			walkingField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2 - 60, 160, 100, 20),
				Text.empty(),
				SPREAD_BOUNDS
			));
			walkingField.setFloat(spread.walking());
			
			walkingAdsField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2 + 60, 160, 100, 20),
				Text.empty(),
				SPREAD_BOUNDS
			));
			walkingAdsField.setFloat(spread.walkingAds());
			
			jumpingField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2, 200, 100, 20),
				Text.empty(),
				SPREAD_BOUNDS
			));
			jumpingField.setFloat(spread.jumping());
			
			addDrawableChild(Widgets.button(
				C_BUTTON_BACK,
				centeredDim(width / 2, height - 20, 90, 20),
				button -> close()
			));
		}
		
		@Override
		public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
			super.render(context, mouseX, mouseY, delta);
			
			drawText(C_HEADER, width / 2, 20, true);
			
			drawText(C_LABEL_CRAWLING, width / 2 - 60, 60, true);
			drawText(C_LABEL_CRAWLING_ADS, width / 2 + 60, 60, true);
			drawText(C_LABEL_IDLE, width / 2 - 60, 100, true);
			drawText(C_LABEL_IDLE_ADS, width / 2 + 60, 100, true);
			drawText(C_LABEL_WALKING, width / 2 - 60, 140, true);
			drawText(C_LABEL_WALKING_ADS, width / 2 + 60, 140, true);
			drawText(C_LABEL_JUMPING, width / 2, 180, true);
		}
		
		@Override
		public void close() {
			GunModifier.Spread newSpread = new GunModifier.Spread(
				jumpingField.getFloat(),
				walkingField.getFloat(), walkingAdsField.getFloat(),
				idleField.getFloat(), idleAdsField.getFloat(),
				crawlingField.getFloat(), crawlingAdsField.getFloat()
			);
			modifier = modifier.withSpread(
				newSpread.equals(defaultModifier.spread().orElseThrow()) ? Optional.empty() : Optional.of(newSpread)
			);
			
			assert client != null;
			client.setScreen(GunModifierEditorScreen.this);
		}
	}
	
	private class Other extends AddonScreen {
		private static final Text C_HEADER = Text.translatable("mercfrontcore.screen.gun.modifier.editor.other.header");
		private static final Text C_LABEL_WEIGHT = Text.translatable("mercfrontcore.screen.gun.modifier.editor.other.label.weight");
		private static final FloatBounds WEIGHT_BOUNDS = FloatBounds.ofMin(0);
		
		private FloatFieldWidget weightField;
		
		public Other() {
			super(C_HEADER);
		}
		
		@Override
		protected void init() {
			super.init();
			
			float weight = modifierPart(GunModifier::weight);
			
			weightField = addDrawableChild(new FloatFieldWidget(
				textRenderer,
				centeredDim(width / 2, 80, 100, 20),
				Text.empty(),
				WEIGHT_BOUNDS
			));
			weightField.setFloat(weight);
			
			addDrawableChild(Widgets.button(
				C_BUTTON_BACK,
				centeredDim(width / 2, height - 20, 90, 20),
				button -> close()
			));
		}
		
		@Override
		public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
			super.render(context, mouseX, mouseY, delta);
			
			drawText(C_HEADER, width / 2, 20, true);
			
			drawText(C_LABEL_WEIGHT, width / 2, 60, true);
		}
		
		@Override
		public void close() {
			float newWeight = weightField.getFloat();
			modifier = modifier.withWeight(
				newWeight == defaultModifier.weight().orElseThrow() ? Optional.empty() : Optional.of(newWeight)
			);
			
			assert client != null;
			client.setScreen(GunModifierEditorScreen.this);
		}
	}
}
