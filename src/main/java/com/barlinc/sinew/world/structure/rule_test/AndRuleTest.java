package com.barlinc.sinew.world.structure.rule_test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.barlinc.sinew.world.SinewRuleTests;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.List;

/**
 * Logical AND over a list of rule tests.
 * - If a child is an ExtendedRuleTest, uses its extendedTest(world,pos,...) path.
 * - Otherwise falls back to RuleTest#test(state, random).
 */
public class AndRuleTest extends ExtendedRuleTest {

    public static final Codec<AndRuleTest> CODEC = RecordCodecBuilder.create(i -> i.group(
            RuleTest.CODEC.listOf().fieldOf("rules").forGetter(t -> t.children)
    ).apply(i, AndRuleTest::new));

    private final List<RuleTest> children;

    public AndRuleTest(List<RuleTest> children) {
        this.children = children;
    }

    @Override
    public boolean test(BlockState state, RandomSource random) {
        return true;
    }

    @Override
    protected RuleTestType<?> getType() {
        return SinewRuleTests.AND_RULE_TEST.get();
    }

    @Override
    public boolean extendedTest(BlockPos pos, BlockState state, LevelReader levelReader, RandomSource random) {
        if (children == null || children.isEmpty()) return true;

        for (RuleTest rt : children) {
            boolean pass;
            if (rt instanceof ExtendedRuleTest ert) {
                pass = ert.extendedTest(pos, state, levelReader, random);
            } else {
                pass = rt.test(state, random);
            }
            if (!pass) return false;
        }
        return true;
    }
}