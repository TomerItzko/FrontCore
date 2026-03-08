package red.vuis.mercfrontcore.mixin;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.CommandUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import red.vuis.mercfrontcore.client.data.config.AddonClientConfig;
import red.vuis.mercfrontcore.client.data.config.MatchHudStyle;
import red.vuis.mercfrontcore.command.bf.AssetCommandValidatorsEx;
import red.vuis.mercfrontcore.ex.AbstractGameEx;
import red.vuis.mercfrontcore.util.AddonUtils;
import red.vuis.mercfrontcore.util.property.PropertyHandleResult;

@Mixin(AbstractGame.class)
public abstract class AbstractGameMixin implements AbstractGameEx {
	@Shadow
	@Final
	@NotNull
	public AssetCommandBuilder baseCommand;
	
	@Shadow
	@NotNull
	protected String type;
	@Unique
	private boolean mercfrontcore$forceClientConfig = false;
	@Unique
	private AddonClientConfig.Data mercfrontcore$clientConfig = new AddonClientConfig.Data();
	
	@Inject(
		method = "<init>(Lcom/boehmod/blockfront/common/BFAbstractManager;Ljava/lang/String;Ljava/lang/String;)V",
		at = @At("TAIL")
	)
	private void addCommands(BFAbstractManager<?, ?, ?> manager, String type, String displayName, CallbackInfo ci) {
		baseCommand.subCommand("mercfrontcore", mercfrontcore$addFrontutilCommands(new AssetCommandBuilder()));
	}
	
	@Unique
	private AssetCommandBuilder mercfrontcore$addFrontutilCommands(AssetCommandBuilder baseCommand) {
		baseCommand.subCommand("forceClientConfig", new AssetCommandBuilder((context, args) -> {
			CommandOutput output = context.getSource().output;
			
			var value = AddonUtils.parse(Boolean::parseBoolean, args[0]);
			if (value.isEmpty()) {
				CommandUtils.sendBfa(output, Text.translatable("mercfrontcore.message.command.error.value.boolean"));
			}
			
			mercfrontcore$forceClientConfig = value.orElseThrow();
			CommandUtils.sendBfa(output,
				mercfrontcore$forceClientConfig ?
					Text.translatable("mercfrontcore.message.command.game.mercfrontcore.forceClientConfig.success.enabled") :
					Text.translatable("mercfrontcore.message.command.game.mercfrontcore.forceClientConfig.success.disabled")
			);
		}).validator(
			AssetCommandValidatorsEx.count("value")
		));
		
		baseCommand.subCommand("clientConfig", new AssetCommandBuilder((context, args) -> {
			CommandOutput output = context.getSource().output;
			
			String property = args[0];
			String value = args[1];
			
			PropertyHandleResult result = AddonClientConfig.Data.PROPERTIES.handle(mercfrontcore$clientConfig, property, value);
			assert result != PropertyHandleResult.ERROR_TYPE;
			switch (result) {
				case SUCCESS -> CommandUtils.sendBfa(output, Text.translatable(
					"mercfrontcore.message.command.game.clientConfig.success",
					Text.literal(property).fillStyle(BFStyles.LIME),
					Text.literal(value).fillStyle(BFStyles.LIME)
				));
				case ERROR_PROPERTY -> CommandUtils.sendBfa(output, Text.translatable(
					"mercfrontcore.message.command.error.property.missing",
					Text.literal(property).fillStyle(BFStyles.LIME)
				));
				case ERROR_PARSE -> CommandUtils.sendBfa(output, Text.translatable(
					"mercfrontcore.message.command.error.property.parse",
					Text.literal(value).fillStyle(BFStyles.LIME),
					Text.literal(property).fillStyle(BFStyles.LIME)
				));
			}
		}).validator(
			AssetCommandValidatorsEx.count("property", "value")
		));
		
		return baseCommand;
	}
	
	@Inject(
		method = "writeFDS",
		at = @At("TAIL")
	)
	private void writeCustomFDS(FDSTagCompound root, CallbackInfo ci) {
		root.setBoolean("mercfrontcore_forceClientConfig", mercfrontcore$forceClientConfig);
		root.setTagCompound("mercfrontcore_clientConfig", Util.make(new FDSTagCompound(), clientConfigRoot -> {
			clientConfigRoot.setString("matchHudStyle", mercfrontcore$clientConfig.getMatchHudStyle().toString());
			clientConfigRoot.setBoolean("renderCorpses", mercfrontcore$clientConfig.isRenderCorpses());
			clientConfigRoot.setBoolean("enableDeathFade", mercfrontcore$clientConfig.isEnableDeathFade());
			clientConfigRoot.setInteger("killFeedLines", mercfrontcore$clientConfig.getKillFeedLines());
		}));
	}
	
	@Inject(
		method = "readFDS",
		at = @At("TAIL")
	)
	private void readCustomFDS(FDSTagCompound root, CallbackInfo ci) {
		mercfrontcore$forceClientConfig = root.getBoolean("mercfrontcore_forceClientConfig", false);
		FDSTagCompound clientConfigRoot = root.getTagCompound("mercfrontcore_clientConfig", new FDSTagCompound());
		mercfrontcore$clientConfig = new AddonClientConfig.Data(
			AddonUtils.retrieveEnumOrDefault(MatchHudStyle.values(), clientConfigRoot.getString("matchHudStyle"), AddonClientConfig.Data.MATCH_HUD_STYLE_DEFAULT),
			clientConfigRoot.getBoolean("renderCorpses", AddonClientConfig.Data.RENDER_CORPSES_DEFAULT),
			clientConfigRoot.getBoolean("enableDeathFade", AddonClientConfig.Data.ENABLE_DEATH_FADE_DEFAULT),
			clientConfigRoot.getInteger("killFeedLines", AddonClientConfig.Data.KILL_FEED_LINES_DEFAULT)
		);
	}
	
	@Inject(
		method = "write",
		at = @At("TAIL")
	)
	private void writeCustomBuf(ByteBuf buf, CallbackInfo ci) {
		buf.writeBoolean(mercfrontcore$forceClientConfig);
		AddonClientConfig.Data.PACKET_CODEC.encode(buf, mercfrontcore$clientConfig);
	}
	
	@Inject(
		method = "read",
		at = @At("TAIL")
	)
	private void readCustomBuf(ByteBuf buf, CallbackInfo ci) {
		mercfrontcore$forceClientConfig = buf.readBoolean();
		mercfrontcore$clientConfig = AddonClientConfig.Data.PACKET_CODEC.decode(buf);
	}
	
	@Override
	public boolean mercfrontcore$isForceClientConfig() {
		return mercfrontcore$forceClientConfig;
	}
	
	@Override
	public AddonClientConfig.Data mercfrontcore$getClientConfig() {
		return mercfrontcore$clientConfig;
	}
}
