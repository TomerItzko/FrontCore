package red.vuis.mercfrontcore.mixin.client;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.client.render.game.element.TeamProgressGameElement;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import red.vuis.mercfrontcore.client.data.config.AddonClientConfig;
import red.vuis.mercfrontcore.client.data.config.MatchHudStyle;

@Mixin(TeamProgressGameElement.class)
public abstract class TeamProgressGameElementMixin<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>> extends ClientGameElement<G, P> {
	@Shadow
	@Final
	private boolean field_473;
	@Shadow
	@Final
	private int field_474;
	@Shadow
	private int field_475;
	@Shadow
	@Final
	private @NotNull String field_477;
	
	@Shadow
	private @NotNull Text field_476;
	
	public TeamProgressGameElementMixin(int width) {
		super(width);
	}
	
	@SuppressWarnings("deprecation")
	@Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lcom/boehmod/blockfront/client/render/game/element/ClientGameElement;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;IIF)V",
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
	private void lessModernRender(DrawContext context, MatrixStack matrices, TextRenderer textRenderer, int x, int y, float delta, CallbackInfo ci) {
		if (AddonClientConfig.getMatchHudStyle() != MatchHudStyle.LESS_MODERN) {
			return;
		}
		ci.cancel();
		
		int areaWidth = method_490(textRenderer) - 4;
		float barWidth = (float) field_475 / (float) field_474 * areaWidth;
		int barColor = field_477.equals("Axis") ? 0x7E3831 : 0x7C8148;
		
		BFRendering.rectangle(context, x + 2, y + 2, areaWidth, 11, ColorReferences.COLOR_BLACK_SOLID, 0.4f);
		if (field_473) {
			BFRendering.rectangle(matrices, context, x + areaWidth - barWidth + 2, y + 2, barWidth, 11, barColor, 0.5f);
			BFRendering.drawStringWithShadow(textRenderer, context, field_476, x + areaWidth - textRenderer.getWidth(field_476) + 1, y + 4, ColorReferences.COLOR_WHITE_SOLID);
		} else {
			BFRendering.rectangle(matrices, context, x + 2, y + 2, barWidth, 11, barColor, 0.5f);
			BFRendering.drawStringWithShadow(textRenderer, context, field_476, x + 4, y + 4, ColorReferences.COLOR_WHITE_SOLID);
		}
	}
}
