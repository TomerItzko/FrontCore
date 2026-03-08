package red.vuis.mercfrontcore.mixin;

import java.util.List;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.CommandUtils;
import io.netty.buffer.ByteBuf;
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

import red.vuis.mercfrontcore.command.bf.AddonAssetCommands;
import red.vuis.mercfrontcore.command.bf.AssetCommandValidatorsEx;
import red.vuis.mercfrontcore.command.bf.InfoFunctions;
import red.vuis.mercfrontcore.command.bf.MapCommands;
import red.vuis.mercfrontcore.util.AddonUtils;
import red.vuis.mercfrontcore.util.property.PropertyHandleResult;

@Mixin(MapEnvironment.class)
public abstract class MapEnvironmentMixin {
	@Shadow
	@Final
	private @NotNull AssetCommandBuilder command;
	@Shadow
	@Final
	private @NotNull List<AbstractMapEffect> mapEffects;
	
	@Shadow
	public abstract String getName();
	
	@Shadow
	@Final
	private String name;
	
	@Inject(
		method = "<init>(Ljava/lang/String;)V",
		at = @At("TAIL")
	)
	private void addCommands(String name, CallbackInfo ci) {
		mercfrontcore$addCommands();
	}
	
	@Inject(
		method = "<init>(Lio/netty/buffer/ByteBuf;)V",
		at = @At("TAIL")
	)
	private void addCommands(ByteBuf buf, CallbackInfo ci) {
		mercfrontcore$addCommands();
	}
	
	@Inject(
		method = "<init>(Lcom/boehmod/bflib/fds/tag/FDSTagCompound;)V",
		at = @At("TAIL")
	)
	private void addCommands(FDSTagCompound root, CallbackInfo ci) {
		mercfrontcore$addCommands();
	}
	
	@Unique
	private void mercfrontcore$addCommands() {
		command.subCommand("mapEffect", mercfrontcore$addMapEffectCommands(new AssetCommandBuilder()));
	}
	
	@Unique
	private AssetCommandBuilder mercfrontcore$addMapEffectCommands(AssetCommandBuilder baseCommand) {
		baseCommand.subCommand("edit", new AssetCommandBuilder((context, args) -> {
			CommandOutput output = context.getSource().output;
			
			var indexParse = AddonAssetCommands.parseIndex(output, args[0], mapEffects, false);
			if (indexParse == null) {
				return;
			}
			int index = indexParse.leftInt();
			Text indexText = indexParse.right();
			AbstractMapEffect mapEffect = mapEffects.get(index);
			
			String property = args[1];
			String value = args[2];
			
			PropertyHandleResult result = MapCommands.MAP_EFFECT_PROPERTIES.handle(mapEffect, property, value);
			switch (result) {
				case SUCCESS -> CommandUtils.sendBfa(output, Text.translatable(
					"mercfrontcore.message.command.map.mapEffect.edit.success",
					Text.literal(property).fillStyle(BFStyles.LIME),
					Text.literal(value).fillStyle(BFStyles.LIME),
					indexText
				));
				case ERROR_PROPERTY -> CommandUtils.sendBfa(output, Text.translatable(
					"mercfrontcore.message.command.map.mapEffect.edit.error.property",
					Text.literal(property).fillStyle(BFStyles.LIME),
					indexText
				));
				case ERROR_PARSE -> CommandUtils.sendBfa(output, Text.translatable(
					"mercfrontcore.message.command.map.mapEffect.edit.error.parse",
					Text.literal(value).fillStyle(BFStyles.LIME),
					Text.literal(property).fillStyle(BFStyles.LIME),
					indexText
				));
				case ERROR_TYPE -> CommandUtils.sendBfa(output, Text.translatable(
					"mercfrontcore.message.command.map.mapEffect.edit.error.type",
					indexText
				));
			}
		}).validator(
			AssetCommandValidatorsEx.count("index", "property", "value")
		));
		
		baseCommand.subCommand("list", new AssetCommandBuilder((context, args) -> {
			AddonAssetCommands.genericList(
				context,
				this::getName,
				"mercfrontcore.message.command.map.mapEffect.list.none",
				"mercfrontcore.message.command.map.mapEffect.list.header",
				mapEffects,
				InfoFunctions::mapEffect
			);
		}));
		
		baseCommand.subCommand("properties", new AssetCommandBuilder((context, args) -> {
			CommandOutput output = context.getSource().output;
			
			var indexParse = AddonAssetCommands.parseIndex(output, args[0], mapEffects, false);
			if (indexParse == null) {
				return;
			}
			int index = indexParse.leftInt();
			Text indexText = indexParse.right();
			AbstractMapEffect mapEffect = mapEffects.get(index);
			
			List<String> properties = MapCommands.MAP_EFFECT_PROPERTIES.getProperties(mapEffect);
			
			if (properties.isEmpty()) {
				CommandUtils.sendBfa(output, Text.translatable(
					"mercfrontcore.message.command.map.mapEffect.properties.none",
					indexText
				));
			} else {
				CommandUtils.sendBfa(output, Text.translatable(
					"mercfrontcore.message.command.map.mapEffect.properties.header",
					indexText
				));
				CommandUtils.sendBfa(output, Text.literal(AddonUtils.listify(properties)));
			}
		}).validator(
			AssetCommandValidatorsEx.count("index")
		));
		
		baseCommand.subCommand("remove", new AssetCommandBuilder((context, args) -> {
			AddonAssetCommands.genericRemove(
				context, args,
				this::getName,
				"mercfrontcore.message.command.map.mapEffect.remove.success",
				mapEffects
			);
		}).validator(
			AssetCommandValidatorsEx.count("index")
		));
		
		baseCommand.subCommand("types", new AssetCommandBuilder((context, args) -> {
			AddonAssetCommands.genericList(
				context,
				this::getName,
				"mercfrontcore.message.command.map.mapEffect.list.none",
				"mercfrontcore.message.command.map.mapEffect.types.header",
				mapEffects,
				InfoFunctions::mapEffectType
			);
		}));
		
		AssetCommandBuilder addCommand = new AssetCommandBuilder();
		
		addCommand.subCommand(
			"bulletTracerSpawner",
			new AssetCommandBuilder((context, args) -> {
				Text nameText = Text.literal(name).fillStyle(BFStyles.LIME);
				CommandOutput source = context.getSource().output;
				
				if (!(args.length == 6 || args.length == 11)) {
					CommandUtils.sendBfaWarn(source, Text.translatable("mercfrontcore.message.command.error.args.count"));
					CommandUtils.sendBfaWarn(source, Text.literal("x, y, z, endPosX, endPosY, endPosZ"));
					CommandUtils.sendBfaWarn(source, Text.literal("x, y, z, endPosX, endPosY, endPosZ, chance, playSound, spreadX, spreadY, spreadZ"));
					return;
				}
				
				var mapEffect = MapCommands.parseBulletTracerSpawner(args);
				if (mapEffect == null) {
					CommandUtils.sendBfa(source, Text.translatable("mercfrontcore.message.command.map.mapEffect.add.bulletTracerSpawner.error", nameText));
					return;
				}
				mapEffects.add(mapEffect);
				
				CommandUtils.sendBfa(source, Text.translatable("mercfrontcore.message.command.map.mapEffect.add.bulletTracerSpawner.success", nameText));
			}));
		
		addCommand.subCommand(
			"loopingSoundPoint",
			new AssetCommandBuilder((context, args) -> {
				Text nameText = Text.literal(name).fillStyle(BFStyles.LIME);
				CommandOutput source = context.getSource().output;
				
				var mapEffect = MapCommands.parseLoopingSoundPoint(args);
				if (mapEffect == null) {
					CommandUtils.sendBfa(source, Text.translatable("mercfrontcore.message.command.map.mapEffect.add.loopingSoundPoint.error", nameText));
					return;
				}
				mapEffects.add(mapEffect);
				
				CommandUtils.sendBfa(source, Text.translatable("mercfrontcore.message.command.map.mapEffect.add.loopingSoundPoint.success", nameText));
			}).validator(
				AssetCommandValidatorsEx.count("sound", "maxTick", "x", "y", "z")
			));
		
		addCommand.subCommand(
			"particleEmitter",
			new AssetCommandBuilder((context, args) -> {
				Text nameText = Text.literal(name).fillStyle(BFStyles.LIME);
				CommandOutput source = context.getSource().output;
				
				if (!(args.length == 5 || args.length == 7)) {
					CommandUtils.sendBfaWarn(source, Text.translatable("mercfrontcore.message.command.error.args.count"));
					CommandUtils.sendBfaWarn(source, Text.literal("particle, maxTick, x, y, z"));
					CommandUtils.sendBfaWarn(source, Text.literal("particle, maxTick, x, y, z, sound, soundVolume"));
					return;
				}
				
				var mapEffect = MapCommands.parseParticleEmitter(args);
				if (mapEffect == null) {
					CommandUtils.sendBfa(source, Text.translatable("mercfrontcore.message.command.map.mapEffect.add.particleEmitter.error", nameText));
					return;
				}
				mapEffects.add(mapEffect);
				
				CommandUtils.sendBfa(source, Text.translatable("mercfrontcore.message.command.map.mapEffect.add.particleEmitter.success", nameText));
			}));
		
		baseCommand.subCommand("add", addCommand);
		
		return baseCommand;
	}
}
