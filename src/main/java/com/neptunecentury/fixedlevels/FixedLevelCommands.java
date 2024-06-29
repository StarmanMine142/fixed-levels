package com.neptunecentury.fixedlevels;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;


public class FixedLevelCommands {

    public static void registerCommands(String commandName) {

        final ConfigManager _cfgManager = FixedLevels.getConfigManager();

        // Register the command tree
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal(commandName)
                    .then(CommandManager.literal("query")
                            .then(CommandManager.literal("baseXPForOneLevel")
                                    .executes(context -> {
                                                var cfg = _cfgManager.getConfig();

                                                context.getSource().sendFeedback(() -> Text.literal("%s baseXPForOneLevel is currently set to: %s".formatted(commandName, cfg.baseXPForOneLevel)), false);
                                                return 1;
                                            }
                                    )
                            )
                            .then(CommandManager.literal("curveMode")
                                    .executes(context -> {
                                                var cfg = _cfgManager.getConfig();

                                                context.getSource().sendFeedback(() -> Text.literal("%s curveMode is currently set to: %s".formatted(commandName, cfg.curveMode)), false);
                                                return 1;
                                            }
                                    )
                            )
                            .then(CommandManager.literal("curveModeMultiplier")
                                    .executes(context -> {
                                                var cfg = _cfgManager.getConfig();

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
                                                        var cfg = _cfgManager.getConfig();
                                                        // Get new value from command arg
                                                        final int value = IntegerArgumentType.getInteger(context, "value");
                                                        // Set new value
                                                        cfg.baseXPForOneLevel = value;
                                                        // Update the config file
                                                        _cfgManager.save();
                                                        context.getSource().sendFeedback(() -> Text.literal("%s baseXPForOneLevel is now set to: %s".formatted(commandName, value)), true);
                                                        return 1;
                                                    }
                                            )

                                    )
                            )
                            .then(CommandManager.literal("curveMode")
                                    .then(CommandManager.argument("value", BoolArgumentType.bool())
                                            .executes(context -> {
                                                        var cfg = _cfgManager.getConfig();
                                                        // Get new value from command arg
                                                        final boolean value = BoolArgumentType.getBool(context, "value");
                                                        // Set new value
                                                        cfg.curveMode = value;
                                                        // Update the config file
                                                        _cfgManager.save();
                                                        context.getSource().sendFeedback(() -> Text.literal("%s curveMode is now set to: %s".formatted(commandName, value)), true);
                                                        return 1;
                                                    }
                                            )

                                    )
                            )
                            .then(CommandManager.literal("curveModeMultiplier")
                                    .then(CommandManager.argument("value", IntegerArgumentType.integer())
                                            .executes(context -> {
                                                        var cfg = _cfgManager.getConfig();
                                                        // Get new value from command arg
                                                        final int value = IntegerArgumentType.getInteger(context, "value");
                                                        // Set new value
                                                        cfg.curveModeMultiplier = value;
                                                        // Update the config file
                                                        _cfgManager.save();
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
