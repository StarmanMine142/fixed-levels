package com.neptunecentury.fixedlevels;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedLevels implements ModInitializer {
	// Public fields
	public static final String MOD_ID = "fixed-levels";
	// Private fields
	private static final Logger _logger = LoggerFactory.getLogger(MOD_ID);
	// Create static instance of the config manager and load the config file.
	private static final ConfigManager _cfgManager = new ConfigManager(MOD_ID, _logger);

	@Override
	public void onInitialize() {
		// Load configuration
		_cfgManager.load();

		// Register commands
		FixedLevelCommands.registerCommands("fixedlevels");

	}

	/**
	 * Gets the instance of the config manager
	 * @return An instance of the config manager
	 */
	public static ConfigManager getConfigManager(){
		return _cfgManager;
	}

}