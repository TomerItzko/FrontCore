/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.FloatArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.server.level.ServerPlayer
 */
package com.boehmod.blockfront.common.command;

import com.boehmod.blockfront.common.net.packet.BFShakeNodePacket;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.ShakeNodeData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class ShakeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)Commands.literal((String)"bf").then(Commands.literal((String)"shake").then(Commands.argument((String)"pitchAmplitude", (ArgumentType)FloatArgumentType.floatArg()).then(Commands.argument((String)"yawAmplitude", (ArgumentType)FloatArgumentType.floatArg()).then(Commands.argument((String)"rollAmplitude", (ArgumentType)FloatArgumentType.floatArg()).then(Commands.argument((String)"pitchFrequency", (ArgumentType)FloatArgumentType.floatArg()).then(Commands.argument((String)"yawFrequency", (ArgumentType)FloatArgumentType.floatArg()).then(Commands.argument((String)"rollFrequency", (ArgumentType)FloatArgumentType.floatArg()).then(Commands.argument((String)"easeInLerp", (ArgumentType)FloatArgumentType.floatArg()).then(Commands.argument((String)"easeOutLerp", (ArgumentType)FloatArgumentType.floatArg()).then(Commands.argument((String)"idleTime", (ArgumentType)IntegerArgumentType.integer()).executes(context -> {
            ShakeCommand.run((CommandContext<CommandSourceStack>)context, FloatArgumentType.getFloat((CommandContext)context, (String)"pitchAmplitude"), FloatArgumentType.getFloat((CommandContext)context, (String)"yawAmplitude"), FloatArgumentType.getFloat((CommandContext)context, (String)"rollAmplitude"), FloatArgumentType.getFloat((CommandContext)context, (String)"pitchFrequency"), FloatArgumentType.getFloat((CommandContext)context, (String)"yawFrequency"), FloatArgumentType.getFloat((CommandContext)context, (String)"rollFrequency"), FloatArgumentType.getFloat((CommandContext)context, (String)"easeInLerp"), FloatArgumentType.getFloat((CommandContext)context, (String)"easeOutLerp"), IntegerArgumentType.getInteger((CommandContext)context, (String)"idleTime"));
            return 1;
        })))))))))));
        dispatcher.register(literalArgumentBuilder);
    }

    private static void run(CommandContext<CommandSourceStack> context, float pitchAmplitude, float yawAmplitude, float rollAplitude, float pitchFrequency, float yawFrequency, float rollFrequency, float easeInLerp, float easeOutLerp, int idleTime) {
        Object object = ((CommandSourceStack)context.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = new ShakeNodeData(pitchAmplitude, yawAmplitude, rollAplitude, pitchFrequency, yawFrequency, rollFrequency, easeInLerp, easeOutLerp, idleTime);
        BFShakeNodePacket bFShakeNodePacket = new BFShakeNodePacket((ShakeNodeData)object);
        PacketUtils.sendToPlayer(bFShakeNodePacket, serverPlayer);
    }
}

