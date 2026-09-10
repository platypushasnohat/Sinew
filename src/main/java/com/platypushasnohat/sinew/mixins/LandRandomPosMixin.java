package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.config.SinewConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LandRandomPos.class)
public class LandRandomPosMixin {

	@ModifyVariable(method= "generateRandomPosTowardDirection", at = @At("STORE"), ordinal = 1)
	private static BlockPos sinew$fixRandomPosBias(BlockPos blockPos, PathfinderMob mob, int radius, boolean shortCircuit, BlockPos pos) {
		if (SinewConfig.FIX_LAND_RANDOM_POS.get()) {
			MutableBlockPos mutable = blockPos.mutable();
			for (int i = 0; i < radius; i++) {
				if (!GoalUtils.isNotStable(mob.getNavigation(), mutable)) {
					return mutable;
				}
				mutable.move(0, -1, 0);
			}
		}
		return blockPos;
	}
}