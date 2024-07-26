package com.neptunecentury.fixedlevels;

import blue.endless.jankson.Comment;
import net.minecraft.network.packet.CustomPayload;

/**
 * The configuration class that stores the options for the mod that can be persisted
 */
public class LevelConfig implements IConfig {
    @Comment("Enables or disables the mod.")
    public boolean enabled = true;
    @Comment("Curve mode calculation is XPToNextLevel = (baseXPForOneLevel + (experienceLevel * curveModeMultiplier)).")
    public boolean curveMode = false;
    @Comment("The amount of experience to go from level 0 to level 1. If curve mode is off, this amount is for every level.")
    public int baseXPForOneLevel = 30;
    @Comment("The multiplier used in the curve mode calculation.")
    public int curveModeMultiplier = 2;

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
        return new ConfigPayload(curveMode, baseXPForOneLevel, curveModeMultiplier);
    }

}
