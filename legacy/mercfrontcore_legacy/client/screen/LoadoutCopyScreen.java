package red.vuis.mercfrontcore.client.screen;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import red.vuis.mercfrontcore.client.data.AddonClientData;
import red.vuis.mercfrontcore.client.widget.Widgets;
import red.vuis.mercfrontcore.setup.LoadoutIndex;

import static red.vuis.mercfrontcore.client.widget.WidgetDim.centeredDim;
import static red.vuis.mercfrontcore.client.widget.WidgetDim.sqrCenteredDim;

public class LoadoutCopyScreen extends AddonScreen {
	private static final Text C_BUTTON_APPLY = Text.translatable("mercfrontcore.screen.generic.button.apply");
	private static final Text C_BUTTON_BACK = Text.translatable("gui.back");
	private static final Text C_CHECKBOX_EXCLUDE = Text.translatable("mercfrontcore.screen.loadout.copy.checkbox.exclude");
	private static final Text C_HEADER = Text.translatable("mercfrontcore.screen.loadout.copy.header");
	private static final Text C_MESSAGE = Text.translatable("mercfrontcore.screen.loadout.copy.message");
	
	private final LoadoutEditorScreen editor;
	private CheckboxWidget excludeCheckbox;
	
	public LoadoutCopyScreen(LoadoutEditorScreen editor) {
		super(C_HEADER);
		this.editor = editor;
	}
	
	@Override
	protected void init() {
		super.init();
		
		excludeCheckbox = addDrawableChild(Widgets.checkbox(
			textRenderer, sqrCenteredDim(width / 2, 100, 20), true
		));
		
		addDrawableChild(Widgets.button(
			C_BUTTON_BACK,
			centeredDim(width / 2 - 50, height - 20, 90, 20),
			button -> MinecraftClient.getInstance().setScreen(editor)
		));
		addDrawableChild(Widgets.button(
			C_BUTTON_APPLY,
			centeredDim(width / 2 + 50, height - 20, 90, 20),
			button -> {
				AddonClientData clientData = AddonClientData.getInstance();
				Map<MatchClass, List<Loadout>> loadoutsToCopy = new EnumMap<>(MatchClass.class);
				List<String> targetSkins = new ObjectArrayList<>();
				
				for (Map.Entry<LoadoutIndex.Identifier, List<Loadout>> entry : clientData.tempLoadouts.entrySet()) {
					LoadoutIndex.Identifier id = entry.getKey();
					BFCountry country = id.country();
					String skin = id.skin();
					
					if (country == editor.selection.country) {
						if (skin.equals(editor.selection.getSkin())) {
							loadoutsToCopy.put(id.matchClass(), entry.getValue());
						} else if (
							!(excludeCheckbox.isChecked() &&
								((country == BFCountry.UNITED_STATES && skin.equals("continental")) ||
									(country == BFCountry.GREAT_BRITAIN && skin.equals("regulars"))))
						) {
							targetSkins.add(skin);
						}
					}
				}
				
				loadoutsToCopy.forEach((matchClass, loadouts) -> {
					for (String skin : targetSkins) {
						List<Loadout> originalLoadouts = clientData.tempLoadouts.computeIfAbsent(
							new LoadoutIndex.Identifier(editor.selection.country, skin, matchClass),
							k -> new ObjectArrayList<>()
						);
						originalLoadouts.clear();
						loadouts.stream().map(LoadoutIndex::cloneLoadout).forEach(originalLoadouts::add);
					}
				});
				
				MinecraftClient.getInstance().setScreen(new LoadoutEditorScreen());
			}
		));
	}
	
	@Override
	public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		
		drawText(C_HEADER, width / 2, 20, true);
		drawText(C_MESSAGE, width / 2, 40, true);
		
		drawText(C_CHECKBOX_EXCLUDE, width / 2, 80, true);
	}
}
