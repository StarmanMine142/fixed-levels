package com.neptunecentury.fixedlevels;

import blue.endless.jankson.Comment;

public class LevelConfig {
    @Comment("Curve mode calculation is XPToNextLevel = (baseXPForOneLevel + (experienceLevel * curveModeMultiplier)).")
    public boolean curveMode = false;
    @Comment("The amount of experience to go from level 0 to level 1. If curve mode is off, this amount is for every level.")
    public int baseXPForOneLevel = 30;
    @Comment("The multiplier used in the curve mode calculation.")
    public int curveModeMultiplier = 2;

    /**
     * Constructor
     */
    public LevelConfig() {}



}
