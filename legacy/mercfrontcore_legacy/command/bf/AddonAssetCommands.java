package red.vuis.mercfrontcore.command.bf;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.CommandUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.demonwav.mcdev.annotations.Translatable;
import com.mojang.brigadier.context.CommandContext;
import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import red.vuis.mercfrontcore.util.AddonCommandUtils;
import red.vuis.mercfrontcore.util.AddonUtils;

public final class AddonAssetCommands {
	private AddonAssetCommands() {
	}
	
	public static @Nullable IntObjectPair<Text> parseIndex(CommandOutput output, String arg, Collection<?> items, boolean includeEnd) {
		var index = AddonUtils.parse(Integer::parseInt, arg);
		if (index.isEmpty()) {
			CommandUtils.sendBfa(output, Text.translatable("mercfrontcore.message.command.error.index.number"));
			return null;
		}
		
		Text indexComponent = Text.literal(Integer.toString(index.get())).fillStyle(BFStyles.LIME);
		if (index.get() < 0 || index.get() >= items.size() + (includeEnd ? 1 : 0)) {
			CommandUtils.sendBfa(output, Text.translatable("mercfrontcore.message.command.error.index.bounds", indexComponent));
			return null;
		}
		
		return new IntObjectImmutablePair<>(index.get(), indexComponent);
	}
	
	public static <T> void genericList(CommandContext<ServerCommandSource> context, Supplier<Text> noneMessage, Supplier<Text> headerMessage, List<T> items, Function<? super T, String> infoFunc) {
		CommandOutput output = context.getSource().output;
		
		if (items.isEmpty()) {
			CommandUtils.sendBfa(output, noneMessage.get());
			return;
		}
		
		CommandUtils.sendBfa(output, headerMessage.get());
		for (int i = 0; i < items.size(); i++) {
			T item = items.get(i);
			CommandUtils.sendBfa(output, Text.literal(String.format("%d: %s", i, infoFunc.apply(item))));
		}
	}
	
	public static <T> void genericList(CommandContext<ServerCommandSource> context, @Nullable Supplier<String> name, @Translatable String noneMessage, @Translatable String headerMessage, List<T> items, Function<? super T, String> infoFunc) {
		if (name != null) {
			Supplier<Text> nameText = () -> Text.literal(name.get()).fillStyle(BFStyles.LIME);
			genericList(
				context,
				() -> Text.translatable(noneMessage, nameText.get()),
				() -> Text.translatable(headerMessage, nameText.get()),
				items, infoFunc
			);
		} else {
			genericList(
				context,
				() -> Text.translatable(noneMessage),
				() -> Text.translatable(headerMessage),
				items, infoFunc
			);
		}
	}
	
	public static <T> void genericList(CommandContext<ServerCommandSource> context, @Translatable String noneMessage, @Translatable String headerMessage, List<T> items, Function<? super T, String> infoFunc) {
		genericList(context, null, noneMessage, headerMessage, items, infoFunc);
	}
	
	public static <T> AssetCommandBuilder genericList(@Translatable String noneMessage, @Translatable String headerMessage, List<T> items, Function<? super T, String> infoFunc) {
		return new AssetCommandBuilder(
			(context, args) -> genericList(context, noneMessage, headerMessage, items, infoFunc)
		);
	}
	
	public static void genericRemove(CommandContext<ServerCommandSource> context, String[] args, Function<Text, Text> successMessage, List<?> items) {
		CommandOutput output = context.getSource().output;
		
		var indexParse = parseIndex(output, args[0], items, false);
		if (indexParse == null) {
			return;
		}
		int index = indexParse.leftInt();
		Text indexComponent = indexParse.right();
		
		items.remove(index);
		CommandUtils.sendBfa(output, successMessage.apply(indexComponent));
	}
	
	public static void genericRemove(CommandContext<ServerCommandSource> context, String[] args, @Nullable Supplier<String> name, @Translatable String successMessage, List<?> items) {
		if (name != null) {
			Supplier<Text> nameText = () -> Text.literal(name.get()).fillStyle(BFStyles.LIME);
			genericRemove(
				context, args,
				indexComponent -> Text.translatable(successMessage, indexComponent, nameText.get()),
				items
			);
		} else {
			genericRemove(
				context, args,
				indexComponent -> Text.translatable(successMessage, indexComponent),
				items
			);
		}
	}
	
	public static void genericRemove(CommandContext<ServerCommandSource> context, String[] args, @Translatable String successMessage, List<?> items) {
		genericRemove(context, args, null, successMessage, items);
	}
	
	public static AssetCommandBuilder genericRemove(@Translatable String successMessage, List<?> items) {
		return new AssetCommandBuilder(
			(context, args) -> genericRemove(context, args, successMessage, items)
		).validator(AssetCommandValidatorsEx.count("index"));
	}
	
	public static <T extends FDSPose> void genericTeleport(CommandContext<ServerCommandSource> context, String[] args, BiFunction<? super T, Text, Text> successMessage, List<T> items) {
		ServerPlayerEntity player = AddonCommandUtils.getContextPlayer(context);
		if (player == null) {
			return;
		}
		
		var indexParse = AddonAssetCommands.parseIndex(player, args[0], items, false);
		if (indexParse == null) {
			return;
		}
		int index = indexParse.leftInt();
		Text indexComponent = indexParse.right();
		T item = items.get(index);
		
		AddonUtils.teleportBf(player, items.get(index));
		CommandUtils.sendBfa(player, successMessage.apply(item, indexComponent));
	}
	
	public static <T extends FDSPose> AssetCommandBuilder genericTeleport(BiFunction<? super T, Text, Text> successMessage, List<T> items) {
		return new AssetCommandBuilder(
			(context, args) -> genericTeleport(context, args, successMessage, items)
		).validator(AssetCommandValidatorsEx.count("index"));
	}
}
