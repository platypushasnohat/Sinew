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
 * Logical OR over a list of rule tests.
 * - If a child is an ExtendedRuleTest, uses extendedTest(world,pos,...)
 * - Otherwise falls back to RuleTest#test(state, random)
 * - Short-circuits on the first success
 */
public class OrRuleTest extends ExtendedRuleTest {
    public static final Codec<OrRuleTest> CODEC = RecordCodecBuilder.create(i -> i.group(
            RuleTest.CODEC.listOf().fieldOf("rules").forGetter(t -> t.children)
    ).apply(i, OrRuleTest::new));

    private final List<RuleTest> children;

    public OrRuleTest(List<RuleTest> children) {
        this.children = children;
    }

    /** Keep permissive — actual check happens in extendedTest(...) */
    @Override
    public boolean test(BlockState state, RandomSource random) {
        return true;
    }

    @Override
    protected RuleTestType<?> getType() {
        return SinewRuleTests.OR_RULE_TEST.get();
    }

    @Override
    public boolean extendedTest(BlockPos pos, BlockState state, LevelReader levelReader, RandomSource random) {
        if (children == null || children.isEmpty()) return false;

        for (RuleTest rt : children) {
            boolean pass;
            if (rt instanceof ExtendedRuleTest ert) {
                pass = ert.extendedTest(pos, state, levelReader, random);
            } else {
                pass = rt.test(state, random);
            }
            if (pass) return true;
        }
        return false;
    }
}