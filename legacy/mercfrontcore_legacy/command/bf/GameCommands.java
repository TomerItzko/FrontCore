package red.vuis.mercfrontcore.command.bf;

import java.util.List;
import java.util.function.BiFunction;

import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.CommandUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import red.vuis.mercfrontcore.util.AddonCommandUtils;
import red.vuis.mercfrontcore.util.AddonUtils;

public final class GameCommands {
	private GameCommands() {
	}
	
	public static <T extends AbstractCapturePoint<?>> void capturePointCommands(AssetCommandBuilder base, List<T> capturePoints, BiFunction<PlayerEntity, String, T> constructor) {
		base.subCommand("list", AddonAssetCommands.genericList(
			"mercfrontcore.message.command.game.cpoint.list.none",
			"mercfrontcore.message.command.game.cpoint.list.header",
			capturePoints,
			InfoFunctions::capturePoint
		));
		
		base.subCommand("remove", AddonAssetCommands.genericRemove(
			"mercfrontcore.message.command.game.cpoint.remove.success",
			capturePoints
		));
		
		base.subCommand("tp", AddonAssetCommands.genericTeleport(
			(capturePoint, indexComponent) -> Text.translatable("mercfrontcore.message.command.game.cpoint.tp.success", capturePoint.name, indexComponent),
			capturePoints
		));
		
		base.subCommand("insert", insertCapturePoint(
			capturePoints,
			constructor
		));
		
		base.subCommand("rename", renameCapturePoint(
			capturePoints
		));
		
		base.subCommand("moveIndex", moveCapturePointIndex(
			capturePoints
		));
		
		base.subCommand("autoname", autonameCapturePoints(
			capturePoints
		));
	}
	
	public static <T extends AbstractCapturePoint<?>> AssetCommandBuilder insertCapturePoint(List<T> capturePoints, BiFunction<PlayerEntity, String, T> constructor) {
		return new AssetCommandBuilder((context, args) -> {
			ServerPlayerEntity player = AddonCommandUtils.getContextPlayer(context);
			if (player == null) {
				return;
			}
			
			var indexParse = AddonAssetCommands.parseIndex(player, args[0], capturePoints, true);
			if (indexParse == null) {
				return;
			}
			int index = indexParse.leftInt();
			Text indexComponent = indexParse.right();
			String name = args[1];
			
			capturePoints.add(index, constructor.apply(player, name));
			CommandUtils.sendBfa(player, Text.translatable("mercfrontcore.message.command.game.cpoint.insert.success", name, indexComponent, capturePoints.size()));
		}).validator(
			AssetCommandValidatorsEx.count("index", "name")
		);
	}
	
	public static <T extends AbstractCapturePoint<?>> AssetCommandBuilder renameCapturePoint(List<T> capturePoints) {
		return new AssetCommandBuilder((context, args) -> {
			CommandOutput output = context.getSource().output;
			
			var indexParse = AddonAssetCommands.parseIndex(output, args[0], capturePoints, false);
			if (indexParse == null) {
				return;
			}
			int index = indexParse.leftInt();
			Text indexComponent = indexParse.right();
			String name = args[1];
			
			capturePoints.get(index).name = name;
			CommandUtils.sendBfa(output, Text.translatable("mercfrontcore.message.command.game.cpoint.rename.success", indexComponent, name));
		}).validator(
			AssetCommandValidatorsEx.count("index", "name")
		);
	}
	
	public static <T extends AbstractCapturePoint<?>> AssetCommandBuilder moveCapturePointIndex(List<T> capturePoints) {
		return new AssetCommandBuilder((context, args) -> {
			ServerPlayerEntity player = AddonCommandUtils.getContextPlayer(context);
			if (player == null) {
				return;
			}
			
			var indexParse = AddonAssetCommands.parseIndex(player, args[0], capturePoints, false);
			if (indexParse == null) {
				return;
			}
			int index = indexParse.leftInt();
			Text indexComponent = indexParse.right();
			
			T capturePoint = capturePoints.get(index);
			Text cpNameComponent = Text.literal(capturePoint.name).fillStyle(BFStyles.LIME);
			Text positionComponent = Text.literal(AddonUtils.formatVec3(player.getPos())).fillStyle(BFStyles.LIME);
			
			AddonUtils.setPoseFromEntity(capturePoint, player);
			CommandUtils.sendBfa(player, Text.translatable("mercfrontcore.message.command.game.cpoint.moveIndex.success", cpNameComponent, indexComponent, positionComponent));
		}).validator(
			AssetCommandValidatorsEx.count("index")
		);
	}
	
	public static <T extends AbstractCapturePoint<?>> AssetCommandBuilder autonameCapturePoints(List<T> capturePoints) {
		return new AssetCommandBuilder((context, args) -> {
			CommandOutput output = context.getSource().output;
			
			if (capturePoints.size() > 26) {
				CommandUtils.sendBfa(output, Text.translatable("mercfrontcore.message.command.game.cpoint.autoname.error.count"));
				return;
			}
			
			for (int i = 0; i < capturePoints.size(); i++) {
				capturePoints.get(i).name = String.valueOf((char) ('A' + i));
			}
			CommandUtils.sendBfa(output, Text.translatable("mercfrontcore.message.command.game.cpoint.autoname.success"));
		});
	}
}
