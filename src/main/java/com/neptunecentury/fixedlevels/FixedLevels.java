package com.neptunecentury.fixedlevels;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main entry point. Almost everything needed to manage the runtime is here
 */
public class FixedLevels implements ModInitializer {
    // Public fields
    public static final Identifier CONFIG_PACKET_ID = Identifier.of("fixed-levels", "config");
    public static final String MOD_ID = "fixed-levels";
    // Private fields
    private static final Logger _logger = LoggerFactory.getLogger(MOD_ID);
    // Create static instance of the config manager and load the config file.
    private static final ConfigManager<LevelConfig> _cfgManager = new ConfigManager<>(MOD_ID, _logger);
    // Store server-side config separately when client is connected to a dedicated server.
    private static LevelConfig _serverCfg = null;
    private static boolean _isEnabled = false;

    // Is there seriously no other way to get the server instance statically?
    private static MinecraftServer _server;

    @Override
    public void onInitialize() {
        // Load configuration
        _cfgManager.load(LevelConfig.class);

        // Register commands
        FixedLevelCommands.registerCommands("fixedlevels");

        // Register the custom payload
        PayloadTypeRegistry.playS2C().register(ConfigPayload.ID, ConfigPayload.CODEC);

        // Register event when player joins server
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            // A player has joined the server.
            // Set the server instance
            _server = server;

            // Initialize the mod
            initialize(server, handler.player);

        });

    }

    /**
     * Sends the server config to the client or enables the client if single player mode
     * @param server The server instance
     * @param player The player. Null if the server should send config to all player
     */
    public static void initialize(MinecraftServer server, ServerPlayerEntity player){
        if (server == null){
            return;
        }

        // Get the currently loaded config
        var cfg = FixedLevels.getConfigManager().getConfig();

        // Set the enabled flag to true if this is single player or dedicated server and if the mod is enabled
        FixedLevels.setEnabled(cfg.enabled && (server.isSingleplayer() || server.isDedicated()));

        // Check if single player mode or if this is the dedicated server. If dedicated, then the
        // mixin will be enabled here. Otherwise, the mixin will be enabled when it receives
        // the config packet from the server.
        if (server.isSingleplayer()) {
            // Single player mode uses its own config
            _logger.info("[{}] Single player mode detected", MOD_ID);
            // Clear out the server config settings so that we use client config settings
            FixedLevels.useServerConfig(null);

            return;
        }

        _logger.info("[{}] Sending config to clients.", MOD_ID);

        // Dispatch the config to the client
        if (player != null) {
            ConfigDispatcher.dispatch(server, player, cfg);
        } else {
            ConfigDispatcher.dispatch(server, cfg);
        }
    }

    /**
     * Gets the instance of the config manager
     *
     * @return An instance of the config manager
     */
    public static ConfigManager<LevelConfig> getConfigManager() {
        return _cfgManager;
    }

    /**
     * Gets the server-side config that was sent to client
     *
     * @return The server config
     */
    public static LevelConfig getServerConfig() {
        return _serverCfg;
    }

    /**
     * Sets the server config to use that was received from the server
     *
     * @param serverCfg The server config
     */
    public static void useServerConfig(LevelConfig serverCfg) {
        _serverCfg = serverCfg;
    }

    /**
     * Gets whether the mixin is enabled. If connected to a server that does not have this mod installed,
     * the mixin will disable itself.
     *
     * @return Whether the mixin is enabled
     */
    public static boolean isEnabled() {
        return _isEnabled;
    }

    /**
     * Sets the enabled state of the mixin.
     *
     * @param value Whether the mixin is enabled
     */
    public static void setEnabled(boolean value) {
        _isEnabled = value;
        _logger.info("[{}] Mixin has been {}.", MOD_ID, value ? "enabled" : "disabled");
    }

    /**
     * Gets the instance of the server the client is connected to.
     *
     * @return The server instance
     */
    public static MinecraftServer getServer() {
        return _server;
    }

    /**
     * Gets the logger instance for the mod
     *
     * @return The logger instance
     */
    public static Logger getLogger() {
        return _logger;
    }

}