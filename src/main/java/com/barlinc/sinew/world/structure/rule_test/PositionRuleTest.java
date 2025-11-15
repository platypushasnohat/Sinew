package com.barlinc.sinew.world.structure.rule_test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.barlinc.sinew.world.SinewRuleTests;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.Optional;

/**
 * Checks the block position against coordinate comparisons and/or a radial distance.
 * Each field (x, y, z, radius) is OPTIONAL; only provided ones are tested.
 */
public class PositionRuleTest extends ExtendedRuleTest {

    public static final Codec<PositionRuleTest> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.FLOAT.optionalFieldOf("x").forGetter(t -> t.x),
            Codec.FLOAT.optionalFieldOf("y").forGetter(t -> t.y),
            Codec.FLOAT.optionalFieldOf("z").forGetter(t -> t.z),
            Codec.FLOAT.optionalFieldOf("radius").forGetter(t -> t.radius),
            ComparisonOperator.CODEC.optionalFieldOf("operator", ComparisonOperator.LESS_THAN).forGetter(t -> t.operator)
    ).apply(i, PositionRuleTest::new));

    private final Optional<Float> x;
    private final Optional<Float> y;
    private final Optional<Float> z;
    private final Optional<Float> radius;
    private final ComparisonOperator operator;

    public PositionRuleTest(Optional<Float> x,
                            Optional<Float> y,
                            Optional<Float> z,
                            Optional<Float> radius,
                            ComparisonOperator operator) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.operator = operator;
    }

    @Override
    public boolean test(BlockState state, RandomSource random) {
        // world/pos-less path: always allow; real check happens in extendedTest
        return true;
    }

    @Override
    protected RuleTestType<?> getType() {
        return SinewRuleTests.POS_RULE_TEST.get();
    }

    @Override
    public boolean extendedTest(BlockPos pos, BlockState state, LevelReader levelReader, RandomSource random) {
        if (pos == null) return false;

        // Axis checks only if present
        if (x.isPresent() && !operator.compare(pos.getX(), x.get())) return false;
        if (y.isPresent() && !operator.compare(pos.getY(), y.get())) return false;
        if (z.isPresent() && !operator.compare(pos.getZ(), z.get())) return false;

        // Radius (distance from origin) if present
        if (radius.isPresent()) {
            float r = radius.get();

            // For non-equality ops, compare squared to avoid sqrt
            if (operator != ComparisonOperator.EQUAL && operator != ComparisonOperator.NOT_EQUAL) {
                long dx = pos.getX();
                long dy = pos.getY();
                long dz = pos.getZ();
                float dist2 = (float) (dx * dx + dy * dy + dz * dz);
                float r2 = r * r;

                boolean pass = switch (operator) {
                    case GREATER_THAN     -> dist2 >  r2;
                    case GREATER_OR_EQUAL -> dist2 >= r2;
                    case LESS_THAN        -> dist2 <  r2;
                    case LESS_OR_EQUAL    -> dist2 <= r2;
                    default               -> true; // EQUAL/NOT_EQUAL handled below
                };
                if (!pass) return false;
            } else {
                // Equality-based: compute actual distance (rarely useful due to float equality)
                float dist = (float) Math.sqrt(
                        (double) pos.getX() * pos.getX() +
                                (double) pos.getY() * pos.getY() +
                                (double) pos.getZ() * pos.getZ()
                );
                if (!operator.compare(dist, r)) return false;
            }
        }

        return true;
    }

    /** Same enum you used with ChanceRuleTest */
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
        ComparisonOperator(String symbol) { this.symbol = symbol; }
        public String symbol() { return symbol; }

        public boolean compare(float value, float target) {
            return switch (this) {
                case GREATER_THAN     -> value >  target;
                case GREATER_OR_EQUAL -> value >= target;
                case LESS_THAN        -> value <  target;
                case LESS_OR_EQUAL    -> value <= target;
                case EQUAL            -> value == target;     // consider epsilon if you truly need equality
                case NOT_EQUAL        -> value != target;
            };
        }

        public static ComparisonOperator fromString(String s) {
            if (s == null) return LESS_THAN;
            return switch (s.trim()) {
                case ">"  -> GREATER_THAN;
                case ">=" -> GREATER_OR_EQUAL;
                case "<"  -> LESS_THAN;
                case "<=" -> LESS_OR_EQUAL;
                case "=", "==" -> EQUAL;
                case "!=", "<>" -> NOT_EQUAL;
                default -> LESS_THAN;
            };
        }
    }
}