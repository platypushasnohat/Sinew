package com.barlinc.sinew.world.structure.rule_test;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public abstract class ExtendedRuleTest extends RuleTest {
    public boolean extendedTest(BlockPos pos, BlockState state, LevelReader levelReader, RandomSource randomSource) {
       return test(state, randomSource);
    }
}