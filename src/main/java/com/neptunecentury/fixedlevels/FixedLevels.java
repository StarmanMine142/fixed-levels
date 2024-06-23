package com.neptunecentury.fixedlevels;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedLevels implements ModInitializer {
	public static final String MOD_ID = "fixed-levels";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean curveMode = false;

	@Override
	public void onInitialize() {
		AutoConfig.register(LevelConfig.class, JanksonConfigSerializer::new);
	}
}