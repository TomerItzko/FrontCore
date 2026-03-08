package red.vuis.mercfrontcore.mixin;

import java.util.Map;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.CommandUtils;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import red.vuis.mercfrontcore.data.bf.AddonMapAssetData;

@Mixin(MapAsset.class)
public abstract class MapAssetMixin {
	@Shadow
	public @NotNull Map<String, MapEnvironment> environments;
	@Shadow
	private @NotNull String name;
	@Shadow
	private @NotNull DivisionData alliesDivision;
	@Shadow
	private @NotNull DivisionData axisDivision;
	@Shadow
	@Final
	private @NotNull AssetCommandBuilder command;
	
	@Shadow
	public abstract @NotNull String getName();
	
	@Inject(
		method = "<init>(Ljava/lang/String;Ljava/lang/String;Lcom/boehmod/blockfront/common/match/DivisionData;Lcom/boehmod/blockfront/common/match/DivisionData;)V",
		at = @At("TAIL")
	)
	private void addCommands(String name, String author, DivisionData alliesDivision, DivisionData axisDivision, CallbackInfo ci) {
		mercfrontcore$addTeamsCommands(command.subCommands.get("teams"));
	}
	
	@Inject(
		method = "readFDS",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Map;isEmpty()Z",
			ordinal = 0
		),
		cancellable = true
	)
	private void migrateOldData(FDSTagCompound root, CallbackInfo ci) {
		if (!environments.isEmpty()) {
			return;
		}
		
		MapEnvironment env = new MapEnvironment(MapEnvironment.DEFAULT_NAME);
		AddonMapAssetData.readOldFDS(root, env);
		environments.put(env.getName(), env);
		
		ci.cancel();
	}
	
	@Unique
	private AssetCommandBuilder mercfrontcore$addTeamsCommands(AssetCommandBuilder baseCommand) {
		baseCommand.subCommand("list", new AssetCommandBuilder((context, args) -> {
			Text nameText = Text.literal(name).fillStyle(BFStyles.LIME);
			CommandOutput source = context.getSource().output;
			
			CommandUtils.sendBfa(source, Text.translatable(
				"mercfrontcore.message.command.map.teams.list.header",
				nameText
			));
			
			CommandUtils.sendBfa(source, Text.literal(String.format("Allies: %s (%s)", alliesDivision.getCountry().getName(), alliesDivision.getSkin())));
			CommandUtils.sendBfa(source, Text.literal(String.format("Axis: %s (%s)", axisDivision.getCountry().getName(), axisDivision.getSkin())));
		}));
		
		return baseCommand;
	}
}
