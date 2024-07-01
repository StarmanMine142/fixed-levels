package com.neptunecentury.fixedlevels;

import com.mojang.serialization.Codec;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;


public class FixedLevels implements ModInitializer {
    public static final Identifier CONFIG_PACKET_ID = Identifier.of("fixed-levels", "config");
    public static final Identifier CONFIG_SEND_CONFIG = Identifier.of("fixed-levels", "send-config");
    // Public fields
    public static final String MOD_ID = "fixed-levels";
    // Private fields
    public static final Logger _logger = LoggerFactory.getLogger(MOD_ID);
    // Create static instance of the config manager and load the config file.
    private static final ConfigManager<LevelConfig> _cfgManager = new ConfigManager<LevelConfig>(MOD_ID, _logger);

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
            // Set the server instance
            setServer(server);

            // Check if single player mode or if this is the dedicated server. If so, then the
            // mixin will be enabled here. Otherwise, the mixin will be enabled when it received
            // the config packet from the server (client side).
            FixedLevels.enable(server.isSingleplayer() || server.isDedicated());
            if (server.isSingleplayer()) {
                // Single player mode uses its own config
                _logger.info("[fixed-levels] Single player mode detected");
                // Clear out the server config settings so that we use client config settings
                FixedLevels.useServerConfig(null);

                return;
            }

            _logger.info("[fixed-levels] Client joined. Sending config to client.");

            // Send a packet to the client
            var cfg = FixedLevels.getConfigManager().getConfig();

            // Dispatch the config to the client
            ConfigDispatcher.dispatch(server, handler.player, cfg);

        });

        // Listen for when the client disconnects from the server
        ServerPlayConnectionEvents.DISCONNECT.register((phase, listener) -> {
            // Client is disconnected. Disable so that we can determine again later if it
            // should be enabled.
            FixedLevels.enable(false);
        });

    }

    /**
     * Gets the instance of the config manager
     *
     * @return An instance of the config manager
     */
    public static ConfigManager<LevelConfig> getConfigManager() {
        return _cfgManager;
    }

    public static LevelConfig getServerConfig() {
        return _serverCfg;
    }

    public static void useServerConfig(LevelConfig cfg) {
        _serverCfg = cfg;
    }

    public static boolean isEnabled() {
        return _isEnabled;
    }

    public static void enable(boolean value) {
        _isEnabled = value;
        _logger.info("[fixed-levels] Client enabled the mixin.");
    }

    public static void setServer(MinecraftServer server) {
        _server = server;
    }

    public static MinecraftServer getServer() {
        return _server;
    }

}