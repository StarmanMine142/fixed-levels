package com.neptunecentury.fixedlevels;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = FixedLevels.MOD_ID)
public class LevelConfig implements ConfigData {
    @Comment("Curve mode calculation is XPToNextLevel = (baseXPForOneLevel + (experienceLevel * curveModeMultiplier)). ")
    public boolean curveMode = false;
    @Comment("The amount of experience to go from level 0 to level 1. If curve mode is off, this amount is for every level.")
    public int baseXPForOneLevel = 30;
    @Comment("The multiplier used in the curve mode calculation.")
    public int curveModeMultiplier = 2;
}
