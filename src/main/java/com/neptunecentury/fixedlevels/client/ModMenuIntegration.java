package com.neptunecentury.fixedlevels.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // If Cloth Config API is installed, then use that to generate a nice looking config screen for the client
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return ClothConfigIntegration::generateScreen;
        }

        return null;
    }

}
