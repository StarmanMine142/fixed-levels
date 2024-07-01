package com.neptunecentury.fixedlevels;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Dispatches config settings to the client
 */
public class ConfigDispatcher {

    /**
     * Creates a custom payload to send to the client
     * @param cfg The configuration object to send
     * @return The custom payload created for the configuration object
     */
    private static CustomPayload createPayload(LevelConfig cfg){
        return new ConfigPayload(cfg.curveMode, cfg.baseXPForOneLevel, cfg.curveModeMultiplier);
    }

    public static void dispatch(MinecraftServer server, ServerPlayerEntity playerEntity, LevelConfig cfg){
        var payload = createPayload(cfg);
        // Send the config to a specific player
        server.execute(() -> {
            ServerPlayNetworking.send(playerEntity, payload);

        });
    }

    public static void dispatch(MinecraftServer server, LevelConfig cfg){
        var payload = createPayload(cfg);
        // Get all player entities on the server
        var playerEntities = server.getPlayerManager().getPlayerList();
        for(var playerEntity: playerEntities){
            // For each player, send the config. The player may not have this mod installed
            // on their client, so in that case, the packet will get ignored.
            server.execute(() -> {
                ServerPlayNetworking.send(playerEntity, payload);

            });
        }

    }
}
