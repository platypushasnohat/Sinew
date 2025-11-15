package com.barlinc.sinew.world.structure.rule_test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.barlinc.sinew.world.SinewRuleTests;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

/**
 * Tests a random float (0–1) against a given threshold using a comparison operator.
 */
public class ChanceRuleTest extends ExtendedRuleTest {

    public static final Codec<ChanceRuleTest> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.FLOAT.fieldOf("chance").forGetter(t -> t.chance),
            ComparisonOperator.CODEC.optionalFieldOf("operator", ComparisonOperator.LESS_THAN).forGetter(t -> t.operator)
    ).apply(i, ChanceRuleTest::new));

    private final float chance;
    private final ComparisonOperator operator;


    public ChanceRuleTest(float chance) {
        this(chance, ComparisonOperator.LESS_THAN);
    }

    public ChanceRuleTest(float chance, ComparisonOperator operator) {
        this.chance = chance;
        this.operator = operator;
    }

    @Override
    public boolean test(BlockState state, RandomSource random) {
        return true;
    }

    @Override
    public boolean extendedTest(BlockPos pos, BlockState state, LevelReader levelReader, RandomSource random) {
        float roll = random.nextFloat();
        return operator.compare(roll, chance);
    }

    @Override
    protected RuleTestType<?> getType() {
        return SinewRuleTests.CHANCE_RULE_TEST.get();
    }

    /**
     * Enum for comparison operations between two floats.
     */
    public enum ComparisonOperator {
        GREATER_THAN(">"),
        GREATER_OR_EQUAL(">="),
        LESS_THAN("<"),
        LESS_OR_EQUAL("<="),
        EQUAL("="),
        NOT_EQUAL("!=");

        public static final Codec<ComparisonOperator> CODEC =
                Codec.STRING.xmap(ComparisonOperator::fromString, ComparisonOperator::symbol);

        private final String symbol;

        ComparisonOperator(String symbol) {
            this.symbol = symbol;
        }

        public String symbol() {
            return symbol;
        }

        /** Compare two floats according to this operator. */
        public boolean compare(float value, float target) {
            return switch (this) {
                case GREATER_THAN -> value > target;
                case GREATER_OR_EQUAL -> value >= target;
                case LESS_THAN -> value < target;
                case LESS_OR_EQUAL -> value <= target;
                case EQUAL -> value == target;
                case NOT_EQUAL -> value != target;
            };
        }

        /** Safe parser for JSON string input. */
        public static ComparisonOperator fromString(String s) {
            if (s == null) return LESS_THAN;
            return switch (s.trim()) {
                case ">" -> GREATER_THAN;
                case ">=" -> GREATER_OR_EQUAL;
                case "<" -> LESS_THAN;
                case "<=" -> LESS_OR_EQUAL;
                case "=", "==" -> EQUAL;
                case "!=", "<>" -> NOT_EQUAL;
                default -> LESS_THAN;
            };
        }
    }
}