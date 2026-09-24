package com.platypushasnohat.sinew.entity.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public class SinewPathfindingUtils {

    public float getSurfacePathfindingFavor(BlockPos pos, LevelReader level) {
        int y = Math.abs(level.getMaxBuildHeight()) - pos.getY();
        return 1.0F / (float) (y == 0 ? 1 : y);
    }

    public static float getDepthPathfindingFavor(BlockPos pos, LevelReader level) {
        int y = pos.getY() + Math.abs(level.getMinBuildHeight());
        return 1.0F / (float) (y == 0 ? 1 : y);
    }
}
