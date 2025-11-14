package com.unusualmodding.sinew.world.structure.rule_test;

import com.mojang.serialization.Codec;
import com.unusualmodding.sinew.world.SinewRuleTests;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.Optional;

public class DimensionRuleTest extends ExtendedRuleTest {
    public static final Codec<DimensionRuleTest> CODEC =
            ResourceKey.codec(Registries.DIMENSION).xmap(DimensionRuleTest::new, t -> t.dimension);

    private final ResourceKey<Level> dimension;

    private transient ResourceKey<DimensionType> cachedTargetDimTypeKey;

    public DimensionRuleTest(ResourceKey<Level> dimension) {
        this.dimension = dimension;
    }

    @Override
    public boolean test(BlockState state, RandomSource random) {
        return true;
    }

    @Override
    protected RuleTestType<?> getType() {
        return SinewRuleTests.DIMENSION_RULE_TEST.get();
    }

    @Override
    public boolean extendedTest(BlockPos pos, BlockState state, LevelReader levelReader, RandomSource random) {
        if (levelReader == null) return false;


        final ResourceKey<DimensionType> targetTypeKey = getTargetDimensionTypeKey(levelReader);
        if (targetTypeKey == null) return false;

        return targetTypeKey.location().getNamespace().equals(dimension.location().getNamespace()) ;
    }


    /** Gets (and caches) the ResourceKey of the target dimension's DimensionType. */
    private ResourceKey<DimensionType> getTargetDimensionTypeKey(LevelReader levelReader) {
        if (cachedTargetDimTypeKey != null) return cachedTargetDimTypeKey;
        final Optional<ResourceKey<DimensionType>> keyOpt = levelReader.registryAccess().registry(Registries.DIMENSION_TYPE).flatMap(registry -> registry.getResourceKey(levelReader.dimensionType()));
        cachedTargetDimTypeKey = keyOpt.orElse(null);
        return cachedTargetDimTypeKey;
    }
}