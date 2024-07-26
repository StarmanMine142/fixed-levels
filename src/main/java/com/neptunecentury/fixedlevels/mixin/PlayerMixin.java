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
            method = "Lnet/minecraft/entity/player/PlayerEntity;getNextLevelExperience()I",
            cancellable = true)
    private void mixinGetNextLevelExperience(CallbackInfoReturnable<Integer> cir) {
        // Check if the mixin has been enabled by the server
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
        } else {
            expRequired = cfg.baseXPForOneLevel;
        }

        cir.setReturnValue(expRequired);
        cir.cancel();

    }
}