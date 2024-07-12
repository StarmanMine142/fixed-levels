package com.neptunecentury.fixedlevels.client;

import com.neptunecentury.fixedlevels.ConfigPayload;
import com.neptunecentury.fixedlevels.FixedLevels;
import com.neptunecentury.fixedlevels.LevelConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

public class FixedLevelsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Listen for config payload from server if client is connected to dedicated server.
        ClientPlayNetworking.registerGlobalReceiver(ConfigPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                // Get the values from the payload and put them in a new LevelConfig instance.
                var serverCfg = new LevelConfig();
                serverCfg.curveMode = payload.curveMode();
                serverCfg.baseXPForOneLevel = payload.baseXPForOneLevel();
                serverCfg.curveModeMultiplier = payload.curveModeMultiplier();

                // Use the server's config
                FixedLevels.useServerConfig(serverCfg);
                FixedLevels.setEnabled(true);

                context.player().sendMessage(Text.literal("[%s] Using configuration from server".formatted(FixedLevels.MOD_ID)));

            });
        });

        // Listen to client disconnect event
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            // When client disconnects, disable the mixin because the next server might not
            // have this mod installed. When the new server sends the config, it will be enabled
            // again.
            FixedLevels.setEnabled(false);
            FixedLevels.getLogger().info("[{}] Client disconnected.", FixedLevels.MOD_ID);

        });

    }
}