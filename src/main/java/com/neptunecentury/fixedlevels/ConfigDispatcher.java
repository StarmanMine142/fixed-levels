package com.neptunecentury.fixedlevels;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Dispatches config settings to the client
 */
public class ConfigDispatcher {

    /**
     * Sends the config to the client
     *
     * @param server       The Minecraft server instance
     * @param playerEntity The player entity to send to
     * @param cfg          The IConfig configuration to send
     */
    public static void dispatch(MinecraftServer server, ServerPlayerEntity playerEntity, IConfig cfg) {
        var payload = cfg.createPayload();
        // Send the config to a specific player
        server.execute(() -> {
            ServerPlayNetworking.send(playerEntity, payload);

        });
    }

    /**
     * Sends the config to the client
     *
     * @param server The Minecraft server instance
     * @param cfg    The IConfig configuration to send
     */
    public static void dispatch(MinecraftServer server, IConfig cfg) {
        var payload = cfg.createPayload();
        // Get all player entities on the server
        var playerEntities = server.getPlayerManager().getPlayerList();
        for (var playerEntity : playerEntities) {
            // For each player, send the config. The player may not have this mod installed
            // on their client, so in that case, the packet will get ignored.
            server.execute(() -> {
                ServerPlayNetworking.send(playerEntity, payload);

            });
        }

    }
}
