/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.ArgumentBuilder
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.ChatFormatting
 *  net.minecraft.commands.CommandBuildContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.SharedSuggestionProvider
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.commands.arguments.ResourceArgument
 *  net.minecraft.core.Holder$Reference
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.command;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.MatchEntity;
import com.boehmod.blockfront.common.entity.ObungaNextbotEntity;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.ac.bullet.SuspiciousShotManager;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.CommandUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BFCoreCommand {
    private static final Component NO_GAME_MAP_ERROR = Component.translatable((String)"bf.message.command.game.admin.changemap.error.nogamemap");
    private static final Component NOT_IN_GAME_ERROR = Component.translatable((String)"bf.message.command.game.player.error.notingame");
    private static final Component DISABLED_ERROR = Component.translatable((String)"bf.message.command.game.player.error.disabled");
    private static final Component FAIL_ERROR = Component.translatable((String)"bf.message.command.game.player.error.fail");
    private static final Component IN_GAME_ERROR = Component.translatable((String)"bf.message.command.game.player.error.ingame");
    private static final String field_6901 = "bf.message.command.game.admin.toggle.suspicion.success";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        LiteralArgumentBuilder literalArgumentBuilder = Commands.literal((String)"bf");
        literalArgumentBuilder.then(Commands.literal((String)"join").then(Commands.argument((String)"game", (ArgumentType)StringArgumentType.word()).suggests((context, suggestions) -> {
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            assert (bFAbstractManager != null) : "Mod manager is null!";
            Set<String> set = bFAbstractManager.getGames().keySet();
            return SharedSuggestionProvider.suggest(set, (SuggestionsBuilder)suggestions);
        }).executes(context -> {
            BFCoreCommand.runJoin((CommandContext<CommandSourceStack>)context, StringArgumentType.getString((CommandContext)context, (String)"game"));
            return 1;
        })));
        literalArgumentBuilder.then(Commands.literal((String)"leave").executes(context -> {
            BFCoreCommand.runLeave((CommandContext<CommandSourceStack>)context);
            return 1;
        }));
        LiteralArgumentBuilder literalArgumentBuilder2 = (LiteralArgumentBuilder)Commands.literal((String)"admin").requires(CommandUtils::isAdmin);
        literalArgumentBuilder2.then(Commands.literal((String)"vehicle").then(Commands.literal((String)"spawn").then(Commands.argument((String)"entity", (ArgumentType)ResourceArgument.resource((CommandBuildContext)buildContext, (ResourceKey)Registries.ENTITY_TYPE)).executes(context -> {
            BFCoreCommand.runVehicleSpawn((CommandContext<CommandSourceStack>)context, ResourceArgument.getSummonableEntityType((CommandContext)context, (String)"entity"));
            return 1;
        }))));
        literalArgumentBuilder2.then(Commands.literal((String)"nextbot").executes(context -> {
            BFCoreCommand.runNextbot((CommandContext<CommandSourceStack>)context);
            return 1;
        }));
        literalArgumentBuilder2.then(Commands.literal((String)"toggle").then(Commands.literal((String)"teamSizes").executes(context -> {
            BFCoreCommand.runToggleTeamSizes((CommandContext<CommandSourceStack>)context);
            return 1;
        })));
        literalArgumentBuilder2.then(Commands.literal((String)"toggleSuspicion").then(Commands.argument((String)"player", (ArgumentType)EntityArgument.player()).executes(context -> {
            BFCoreCommand.runToggleSuspicion((CommandContext<CommandSourceStack>)context, EntityArgument.getPlayer((CommandContext)context, (String)"player"));
            return 1;
        })));
        literalArgumentBuilder2.then(((LiteralArgumentBuilder)Commands.literal((String)"stage").then(Commands.literal((String)"next").executes(context -> {
            BFCoreCommand.runStageNext((CommandContext<CommandSourceStack>)context);
            return 1;
        }))).then(Commands.literal((String)"reset").executes(context -> {
            BFCoreCommand.runReset((CommandContext<CommandSourceStack>)context);
            return 1;
        })));
        literalArgumentBuilder2.then(Commands.literal((String)"pause").executes(context -> {
            BFCoreCommand.runPause((CommandContext<CommandSourceStack>)context);
            return 1;
        }));
        literalArgumentBuilder2.then(Commands.literal((String)"map").then(Commands.literal((String)"change").then(Commands.argument((String)"game", (ArgumentType)StringArgumentType.word()).suggests((context, suggestions) -> {
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            assert (bFAbstractManager != null) : "Mod manager is null!";
            String[] stringArray = bFAbstractManager.getGames().keySet().toArray(new String[0]);
            return SharedSuggestionProvider.suggest((String[])stringArray, (SuggestionsBuilder)suggestions);
        }).executes(context -> {
            BFCoreCommand.runMapChange((CommandContext<CommandSourceStack>)context, StringArgumentType.getString((CommandContext)context, (String)"game"));
            return 1;
        }))));
        literalArgumentBuilder.then((ArgumentBuilder)literalArgumentBuilder2);
        dispatcher.register(literalArgumentBuilder);
    }

    private static void runToggleTeamSizes(CommandContext<CommandSourceStack> commandContext) {
        Object object = ((CommandSourceStack)commandContext.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        UUID uUID = serverPlayer.getUUID();
        AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer(uUID);
        if (abstractGame != null) {
            abstractGame.togglePlayerLimit();
            boolean bl = abstractGame.hasPlayerLimit();
            MutableComponent mutableComponent = Component.literal((String)(bl ? "enabled" : "disabled")).withColor(0xFFFFFF);
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.command.game.admin.toggle.team.size.limit.success.all", (Object[])new Object[]{serverPlayer.getScoreboardName(), mutableComponent});
            BFUtils.sendFancyMessage(((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs(), BFUtils.ADMIN_PREFIX, (Component)mutableComponent2);
        } else {
            CommandUtils.sendBf(serverPlayer, NOT_IN_GAME_ERROR);
        }
    }

    private static void runJoin(@NotNull CommandContext<CommandSourceStack> commandContext, @NotNull String string) {
        AbstractGame<?, ?, ?> abstractGame;
        boolean bl;
        Object object = ((CommandSourceStack)commandContext.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        Object h = ((BFAbstractManager)object).getPlayerDataHandler();
        UUID uUID = serverPlayer.getUUID();
        PlayerCloudData playerCloudData = ((PlayerDataHandler)h).getCloudProfile((Player)serverPlayer);
        boolean bl2 = bl = playerCloudData.hasPermission("staff.ingame.admin") || ((CommandSourceStack)commandContext.getSource()).hasPermission(3);
        if (!bl && object instanceof BFServerManager && ((BFServerManager)((Object)(abstractGame = (BFServerManager)object))).isMatchMakingEnabled()) {
            CommandUtils.sendBf(serverPlayer, DISABLED_ERROR);
            return;
        }
        abstractGame = ((BFAbstractManager)object).retrieveGame(string);
        if (abstractGame != null) {
            if (((BFAbstractManager)object).isPlayerInGame(uUID)) {
                CommandUtils.sendBf(serverPlayer, IN_GAME_ERROR);
                return;
            }
            if (!((BFAbstractManager)object).assignPlayerToGame(serverPlayer.serverLevel(), serverPlayer, abstractGame)) {
                CommandUtils.sendBf(serverPlayer, FAIL_ERROR);
                return;
            }
            BFLog.log("Player '%s' joined game '%s'", serverPlayer.getScoreboardName(), string);
        } else {
            CommandUtils.sendBf(serverPlayer, (Component)Component.translatable((String)"bf.message.command.game.player.error.nogame", (Object[])new Object[]{string}));
        }
    }

    private static void runLeave(@NotNull CommandContext<CommandSourceStack> commandContext) {
        AbstractGame<?, ?, ?> abstractGame;
        boolean bl;
        Object object = ((CommandSourceStack)commandContext.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        Object h = ((BFAbstractManager)object).getPlayerDataHandler();
        UUID uUID = serverPlayer.getUUID();
        PlayerCloudData playerCloudData = ((PlayerDataHandler)h).getCloudProfile((Player)serverPlayer);
        boolean bl2 = bl = playerCloudData.hasPermission("staff.ingame.admin") || ((CommandSourceStack)commandContext.getSource()).hasPermission(3);
        if (!bl && object instanceof BFServerManager && ((BFServerManager)((Object)(abstractGame = (BFServerManager)object))).isMatchMakingEnabled()) {
            CommandUtils.sendBf(serverPlayer, DISABLED_ERROR);
            return;
        }
        abstractGame = ((BFAbstractManager)object).getGameWithPlayer(uUID);
        if (abstractGame != null) {
            ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).removePlayer((BFAbstractManager<?, ?, ?>)object, serverPlayer.serverLevel(), serverPlayer);
            BFLog.log("Player '%s' left game '%s'", serverPlayer.getScoreboardName(), abstractGame.getName());
        } else {
            CommandUtils.sendBf(serverPlayer, NOT_IN_GAME_ERROR);
        }
    }

    private static void runVehicleSpawn(@NotNull CommandContext<CommandSourceStack> commandContext, @NotNull Holder.Reference<EntityType<?>> reference) {
        Object object = ((CommandSourceStack)commandContext.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        UUID uUID = serverPlayer.getUUID();
        AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer(uUID);
        Level level = serverPlayer.level();
        if (abstractGame != null) {
            MatchEntity matchEntity;
            Entity entity = ((EntityType)reference.value()).create(level);
            if (entity instanceof MatchEntity) {
                matchEntity = (MatchEntity)entity;
                matchEntity.setGame(abstractGame);
                level.addFreshEntity(entity);
                entity.setPos(serverPlayer.position());
            }
            matchEntity = Component.translatable((String)"bf.message.command.game.admin.vehicle.spawn.success.all", (Object[])new Object[]{serverPlayer.getScoreboardName(), entity.getScoreboardName()});
            BFUtils.sendFancyMessage(((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs(), BFUtils.ADMIN_PREFIX, (Component)matchEntity);
        } else {
            CommandUtils.sendBf(serverPlayer, NOT_IN_GAME_ERROR);
        }
    }

    private static void runReset(@NotNull CommandContext<CommandSourceStack> commandContext) {
        Object object = ((CommandSourceStack)commandContext.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        UUID uUID = serverPlayer.getUUID();
        AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer(uUID);
        if (abstractGame != null) {
            abstractGame.getStageManager().initFirstStage();
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.command.game.admin.stage.reset.success.all", (Object[])new Object[]{serverPlayer.getScoreboardName()});
            BFUtils.sendFancyMessage(((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs(), BFUtils.ADMIN_PREFIX, (Component)mutableComponent);
        }
    }

    private static void runToggleSuspicion(CommandContext<CommandSourceStack> commandContext, ServerPlayer serverPlayer) {
        Object object = ((CommandSourceStack)commandContext.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer2 = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        if (!(object instanceof BFServerManager)) {
            return;
        }
        BFServerManager bFServerManager = (BFServerManager)object;
        BFServerPlayerData bFServerPlayerData = (BFServerPlayerData)((ServerPlayerDataHandler)bFServerManager.getPlayerDataHandler()).getPlayerData((Player)serverPlayer);
        SuspiciousShotManager suspiciousShotManager = bFServerPlayerData.getSuspiciousShotManager();
        suspiciousShotManager.method_5850(!suspiciousShotManager.method_5851());
        boolean bl = suspiciousShotManager.method_5851();
        MutableComponent mutableComponent = Component.literal((String)(bl ? "Enabled" : "Disabled")).withStyle(bl ? ChatFormatting.GREEN : ChatFormatting.RED);
        MutableComponent mutableComponent2 = Component.literal((String)serverPlayer.getScoreboardName()).withStyle(ChatFormatting.WHITE);
        MutableComponent mutableComponent3 = Component.translatable((String)field_6901, (Object[])new Object[]{mutableComponent, mutableComponent2});
        CommandUtils.sendBf(serverPlayer2, (Component)mutableComponent3);
    }

    private static void runStageNext(@NotNull CommandContext<CommandSourceStack> commandContext) {
        Object object = ((CommandSourceStack)commandContext.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer(serverPlayer.getUUID());
        if (abstractGame != null) {
            abstractGame.getStageManager().setCanChangeStage();
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.command.game.admin.stage.next.success.all", (Object[])new Object[]{serverPlayer.getScoreboardName()});
            BFUtils.sendFancyMessage(((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs(), BFUtils.ADMIN_PREFIX, (Component)mutableComponent);
        }
    }

    private static void runNextbot(@NotNull CommandContext<CommandSourceStack> commandContext) {
        Object object = ((CommandSourceStack)commandContext.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        UUID uUID = serverPlayer.getUUID();
        AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer(uUID);
        Level level = serverPlayer.level();
        if (abstractGame != null) {
            ObungaNextbotEntity obungaNextbotEntity = (ObungaNextbotEntity)((EntityType)BFEntityTypes.OBUNGA.get()).create(level);
            if (obungaNextbotEntity == null) {
                return;
            }
            obungaNextbotEntity.setGame(abstractGame);
            level.addFreshEntity((Entity)obungaNextbotEntity);
            obungaNextbotEntity.setPos(serverPlayer.position());
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.command.game.admin.nextbot.success.all", (Object[])new Object[]{serverPlayer.getScoreboardName()});
            BFUtils.sendFancyMessage(((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs(), BFUtils.ADMIN_PREFIX, (Component)mutableComponent);
        }
    }

    private static void runPause(@NotNull CommandContext<CommandSourceStack> commandContext) {
        Object object = ((CommandSourceStack)commandContext.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        UUID uUID = serverPlayer.getUUID();
        AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer(uUID);
        if (abstractGame != null) {
            abstractGame.setPaused(!abstractGame.isPaused());
            String string = abstractGame.isPaused() ? "pause" : "unpause";
            MutableComponent mutableComponent = Component.translatable((String)String.format("bf.message.command.game.admin.%s.success.all", string), (Object[])new Object[]{serverPlayer.getScoreboardName()});
            BFUtils.sendFancyMessage(((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs(), BFUtils.ADMIN_PREFIX, (Component)mutableComponent);
        } else {
            CommandUtils.sendBf(serverPlayer, NOT_IN_GAME_ERROR);
        }
    }

    private static void runMapChange(@NotNull CommandContext<CommandSourceStack> commandContext, @NotNull String string) {
        Object object = ((CommandSourceStack)commandContext.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        UUID uUID = serverPlayer.getUUID();
        AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer(uUID);
        if (abstractGame == null) {
            CommandUtils.sendBf(serverPlayer, NOT_IN_GAME_ERROR);
            return;
        }
        if (!abstractGame.changeMap((BFAbstractManager<?, ?, ?>)object, serverPlayer.serverLevel(), string)) {
            CommandUtils.sendBf(serverPlayer, NO_GAME_MAP_ERROR);
            return;
        }
        Set<UUID> set = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs();
        MutableComponent mutableComponent = Component.translatable((String)"bf.message.command.game.admin.changemap.success.all", (Object[])new Object[]{serverPlayer.getScoreboardName(), string});
        BFUtils.sendFancyMessage(set, BFUtils.ADMIN_PREFIX, (Component)mutableComponent);
    }
}

