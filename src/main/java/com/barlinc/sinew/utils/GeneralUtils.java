package com.barlinc.sinew.utils;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;

public class GeneralUtils {
    // Weighted Random from: https://stackoverflow.com/a/6737362
    public static <T> T getRandomEntry(List<Pair<T, Integer>> rlList, RandomSource random) {
        double totalWeight = 0.0;

        // Compute the total weight of all items together.
        for (Pair<T, Integer> pair : rlList) {
            totalWeight += pair.getSecond();
        }

        // Now choose a random item.
        int index = 0;
        for (double randomWeightPicked = random.nextFloat() * totalWeight; index < rlList.size() - 1; ++index) {
            randomWeightPicked -= rlList.get(index).getSecond();
            if (randomWeightPicked <= 0.0) break;
        }

        return rlList.get(index).getFirst();
    }


    public static BlockState copyBlockProperties(BlockState oldBlockState, BlockState newBlockState) {
        for (Property<?> property : oldBlockState.getProperties()) {
            if (newBlockState.hasProperty(property)) {
                newBlockState = getStateWithProperty(newBlockState, oldBlockState, property);
            }
        }
        return newBlockState;
    }

    public static <T extends Comparable<T>> BlockState getStateWithProperty(BlockState state, BlockState stateToCopy, Property<T> property) {
        return state.setValue(property, stateToCopy.getValue(property));
    }
}
