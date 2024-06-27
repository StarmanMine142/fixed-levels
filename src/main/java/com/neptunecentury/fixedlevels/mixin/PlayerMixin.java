package com.neptunecentury.fixedlevels.mixin;

import com.neptunecentury.fixedlevels.LevelConfig;
import me.shedaniel.autoconfig.AutoConfig;
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
		LevelConfig cfg = AutoConfig.getConfigHolder(LevelConfig.class).getConfig();
		if(cfg.curveMode)
		{
			cir.setReturnValue(experienceLevel == 0 ? cfg.baseXPForOneLevel : cfg.baseXPForOneLevel + (experienceLevel * cfg.curveModeMultiplier));
		}
		else
		{
			cir.setReturnValue(cfg.baseXPForOneLevel);
		}

		cir.cancel();
	}
}