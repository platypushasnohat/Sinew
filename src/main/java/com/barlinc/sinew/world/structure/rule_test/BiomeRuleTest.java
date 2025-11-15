package com.barlinc.sinew.world.structure.rule_test;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.barlinc.sinew.world.SinewRuleTests;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

/**
 * Matches if the placement position's biome is in a given biome tag OR equals a specific biome key.
 * Requires a processor that calls {@link #extendedTest(BlockPos, BlockState, LevelReader, RandomSource)}.
 */
public class BiomeRuleTest extends ExtendedRuleTest {
    public static final Codec<BiomeRuleTest> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.either(
                    // biome tag, e.g. #minecraft:is_forest
                    TagKey.hashedCodec(Registries.BIOME),
                    // biome key, e.g. minecraft:plains
                    ResourceKey.codec(Registries.BIOME)
            ).fieldOf("biome").forGetter(r -> r.biome)
    ).apply(i, BiomeRuleTest::new));

    private final Either<TagKey<Biome>, ResourceKey<Biome>> biome;

    public BiomeRuleTest(Either<TagKey<Biome>, ResourceKey<Biome>> biome) {
        this.biome = biome;
    }

    /**
     * Vanilla RuleProcessor calls this (no world/pos). We return true so that the
     * world-aware check can be done in extendedTest by your custom processor.
     */
    @Override
    public boolean test(BlockState state, RandomSource random) {
        return true;
    }

    @Override
    protected RuleTestType<?> getType() {
        return SinewRuleTests.BIOME_RULE_TEST.get();
    }

    @Override
    public boolean extendedTest(BlockPos pos, BlockState state, LevelReader levelReader, RandomSource randomSource) {
        Holder<Biome> holder = levelReader.getBiome(pos);
        return biome.map(
                holder::is,
                holder::is
        );
    }
}