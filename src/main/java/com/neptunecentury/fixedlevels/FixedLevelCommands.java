package com.neptunecentury.fixedlevels;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public class FixedLevelCommands {

    public static void registerCommands(String commandName) {

        // Get instance of the config holder for our config file
        var configHolder = AutoConfig.getConfigHolder(LevelConfig.class);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal(commandName)
                    .then(CommandManager.literal("query")
                            .then(CommandManager.literal("baseXPForOneLevel")
                                    .executes(context -> {
                                                LevelConfig cfg = configHolder.getConfig();

                                                context.getSource().sendFeedback(() -> Text.literal("%s baseXPForOneLevel is currently set to: %s".formatted(commandName, cfg.baseXPForOneLevel)), false);
                                                return 1;
                                            }
                                    )
                            )
                            .then(CommandManager.literal("curveMode")
                                    .executes(context -> {
                                                LevelConfig cfg = configHolder.getConfig();

                                                context.getSource().sendFeedback(() -> Text.literal("%s curveMode is currently set to: %s".formatted(commandName, cfg.curveMode)), false);
                                                return 1;
                                            }
                                    )
                            )
                            .then(CommandManager.literal("curveModeMultiplier")
                                    .executes(context -> {
                                                LevelConfig cfg = configHolder.getConfig();

                                                context.getSource().sendFeedback(() -> Text.literal("%s curveModeMultiplier is currently set to: %s".formatted(commandName, cfg.curveModeMultiplier)), false);
                                                return 1;
                                            }
                                    )
                            )
                    )
                    .then(CommandManager.literal("set")
                            .requires(source -> source.hasPermissionLevel(4))
                            .then(CommandManager.literal("baseXPForOneLevel")
                                    .then(CommandManager.argument("value", IntegerArgumentType.integer())
                                            .executes(context -> {
                                                        LevelConfig cfg = configHolder.getConfig();
                                                        // Get new value from command arg
                                                        final int value = IntegerArgumentType.getInteger(context, "value");
                                                        // Set new value
                                                        cfg.baseXPForOneLevel = value;
                                                        // Update the config file
                                                        configHolder.save();
                                                        context.getSource().sendFeedback(() -> Text.literal("%s baseXPForOneLevel is now set to: %s".formatted(commandName, value)), true);
                                                        return 1;
                                                    }
                                            )

                                    )
                            )
                            .then(CommandManager.literal("curveMode")
                                    .then(CommandManager.argument("value", BoolArgumentType.bool())
                                            .executes(context -> {
                                                        LevelConfig cfg = configHolder.getConfig();
                                                        // Get new value from command arg
                                                        final boolean value = BoolArgumentType.getBool(context, "value");
                                                        // Set new value
                                                        cfg.curveMode = value;
                                                        // Update the config file
                                                        configHolder.save();
                                                        context.getSource().sendFeedback(() -> Text.literal("%s curveMode is now set to: %s".formatted(commandName, value)), true);
                                                        return 1;
                                                    }
                                            )

                                    )
                            )
                            .then(CommandManager.literal("curveModeMultiplier")
                                    .then(CommandManager.argument("value", IntegerArgumentType.integer())
                                            .executes(context -> {
                                                        LevelConfig cfg = configHolder.getConfig();
                                                        // Get new value from command arg
                                                        final int value = IntegerArgumentType.getInteger(context, "value");
                                                        // Set new value
                                                        cfg.curveModeMultiplier = value;
                                                        // Update the config file
                                                        configHolder.save();
                                                        context.getSource().sendFeedback(() -> Text.literal("%s curveModeMultiplier is now set to: %s".formatted(commandName, value)), true);
                                                        return 1;
                                                    }
                                            )

                                    )
                            )
                    )

            );
        });
    }
}
