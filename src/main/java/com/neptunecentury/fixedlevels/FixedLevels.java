package com.neptunecentury.fixedlevels;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class FixedLevels implements ModInitializer {
	public static final String MOD_ID = "fixed-levels";

	@Override
	public void onInitialize() {
		// Register the config class and the serializer to use
		AutoConfig.register(LevelConfig.class, JanksonConfigSerializer::new);
		// Register commands
		FixedLevelCommands.registerCommands("fixedlevels");
	}
}