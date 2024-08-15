package com.neptunecentury.fixedlevels;

import blue.endless.jankson.Comment;
import net.minecraft.network.packet.CustomPayload;

/**
 * The configuration class that stores the options for the mod that can be persisted
 */
public class LevelConfig implements IConfig {
    @Comment("Enables custom exp levels. Disable to use vanilla calculations.")
    public boolean useCustomExpLevels = true;
    @Comment("Curve mode calculation is XPToNextLevel = (baseXPForOneLevel + (experienceLevel * curveModeMultiplier)).")
    public boolean curveMode = false;
    @Comment("The amount of exp to go from level 0 to level 1. If curve mode is off, this amount is for every level.")
    public int baseXPForOneLevel = 30;
    @Comment("The multiplier used in the curve mode calculation.")
    public int curveModeMultiplier = 2;
    @Comment("When enabled, limits the amount of exp needed for the next level for vanilla and custom curve mode.")
    public boolean useExpCap = false;
    @Comment("This sets the max amount of exp needed for the next level if using vanilla or custom curve mode.")
    public int maxExpForNextLevel = 107;

    /**
     * Constructor
     */
    public LevelConfig() {
    }

    /**
     * Creates a custom payload to send to the client
     *
     * @return The custom payload created for the configuration object
     */
    public CustomPayload createPayload() {
        return new ConfigPayload(useCustomExpLevels, curveMode, baseXPForOneLevel, curveModeMultiplier, useExpCap, maxExpForNextLevel);
    }

}
