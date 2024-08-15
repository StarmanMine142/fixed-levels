package com.neptunecentury.fixedlevels.mixin;

import com.neptunecentury.fixedlevels.FixedLevels;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerMixin {

    @Shadow
    public int experienceLevel;

    @Inject(at = @At("HEAD"),
            method = "getNextLevelExperience()I",
            cancellable = true)
    private void mixinGetNextLevelExperience(CallbackInfoReturnable<Integer> cir) {

        if (!FixedLevels.isEnabled()) {
            return;
        }
        // Check if we got a config from the server. If so, we need to use it instead.
        var cfg = FixedLevels.getServerConfig();
        if (cfg == null) {
            cfg = FixedLevels.getConfigManager().getConfig();
        }

        int expRequired;
        if (cfg.curveMode) {
            expRequired = experienceLevel == 0 ? cfg.baseXPForOneLevel : cfg.baseXPForOneLevel + (experienceLevel * cfg.curveModeMultiplier);
            // Check if usExpCap is enabled. If so, we need to make sure the required exp doesn't exceed the user's set value.
            if (cfg.useExpCap && cfg.maxExpForNextLevel > 0 && expRequired > cfg.maxExpForNextLevel){
                expRequired = cfg.maxExpForNextLevel;
            }

        } else {
            expRequired = cfg.baseXPForOneLevel;
        }

        cir.setReturnValue(expRequired);
        cir.cancel();

    }

    /**
     * Mixin to get vanilla calculation when custom exp levels is off.
     * @param cir The cir arg
     */
    @Inject(at = @At("RETURN"), method = "getNextLevelExperience()I", cancellable = true)
    private  void mixinGetNextLevelExperienceReturn(CallbackInfoReturnable<Integer> cir){
        // This uses the return mixin and gets the required exp. Since the mixin at head cancels when custom exp
        // levels is enabled, then this return mixin won't get fired. But if it is not canceled, then this means
        // that this is the vanilla calculation.
        // Check if we got a config from the server. If so, we need to use it instead.
        var cfg = FixedLevels.getServerConfig();
        if (cfg == null) {
            cfg = FixedLevels.getConfigManager().getConfig();
        }

        // Get the vanilla calculation for exp required and cap it at max if it's enabled.
        int expRequired = cir.getReturnValue();
        if (cfg.useExpCap && cfg.maxExpForNextLevel > 0 && expRequired > cfg.maxExpForNextLevel){
            expRequired = cfg.maxExpForNextLevel;
        }

        // Return the exp required for next level.
        cir.setReturnValue(expRequired);

    }

}

