package io.daxcess.spectatorchat.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.daxcess.spectatorchat.data.GroupMode;
import io.daxcess.spectatorchat.data.SpectatorChatData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.function.Predicate;

public final class SpectatorChatCommand {
    private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(obj -> Component.literal("Unknown or invalid group mode specified"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, Predicate<CommandSourceStack> hasPermission) {
        dispatcher.register(
                Commands.literal("spectatorchat")
                        .requires(hasPermission)
                        .then(Commands.literal("groupmode")
                                .then(Commands.argument("mode", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            for (var val : GroupMode.values())
                                                builder.suggest(val.name().toLowerCase());

                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> {
                                            var modeString = StringArgumentType.getString(ctx, "mode").toUpperCase();
                                            var mode = Arrays.stream(GroupMode.values()).filter(a -> a.name().equals(modeString)).findFirst();

                                            if (mode.isEmpty())
                                                throw ERROR_INVALID.create(null);

                                            SpectatorChatData.get(ctx.getSource().getServer()).setGroupMode(mode.get());

                                            ctx.getSource().sendSuccess(() -> Component.literal("Group mode has been updated to " + modeString), false);

                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("enable")
                                .executes(ctx -> {
                                    SpectatorChatData.get(ctx.getSource().getServer()).setEnabled(true);

                                    ctx.getSource().sendSuccess(() -> Component.literal("Global spectator voicechat has been enabled"), false);

                                    return 1;
                                })
                        )
                        .then(Commands.literal("disable")
                                .executes(ctx -> {
                                    SpectatorChatData.get(ctx.getSource().getServer()).setEnabled(false);

                                    ctx.getSource().sendSuccess(() -> Component.literal("Global spectator voicechat has been disabled"), false);

                                    return 1;
                                })
                        ));
    }
}