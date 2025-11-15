package com.barlinc.sinew.world.structure.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.barlinc.sinew.world.SinewStructureProcessors;
import com.barlinc.sinew.world.structure.rule_test.ExtendedRuleTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.material.FluidState;

import java.util.Optional;

/**
 * Generic data-driven marker replacer.
 * Replaces structure data markers (e.g. "fossil", "loot", "ore_marker") with
 * specified blocks, optionally assigning loot tables and applying world-aware rule tests.
 */
public class MarkerProcessor extends StructureProcessor {

    public static final Codec<MarkerProcessor> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("marker_name").forGetter(p -> p.markerName),
            ExtendedRuleTest.CODEC.optionalFieldOf("rule").forGetter(p -> p.rule),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("normal_block").forGetter(p -> p.normalBlock),
            BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("deep_block").forGetter(p -> p.deepBlock),
            ResourceLocation.CODEC.optionalFieldOf("normal_loot").forGetter(p -> p.normalLoot),
            ResourceLocation.CODEC.optionalFieldOf("deep_loot").forGetter(p -> p.deepLoot),
            Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(p -> p.chance),
            Codec.INT.optionalFieldOf("deep_y", 0).forGetter(p -> p.deepThreshold) // 👈 new
    ).apply(i, MarkerProcessor::new));

    private final String markerName;
    private final Optional<RuleTest> rule;

    private final Block normalBlock;
    private final Optional<Block> deepBlock;
    private final Optional<ResourceLocation> normalLoot;
    private final Optional<ResourceLocation> deepLoot;
    private final float chance;
    private final int deepThreshold;

    public MarkerProcessor(String markerName, Optional<RuleTest> rule, Block normalBlock, Optional<Block> deepBlock, Optional<ResourceLocation> normalLoot, Optional<ResourceLocation> deepLoot, float chance, int deepThreshold) {
        this.markerName = markerName;
        this.rule = rule;
        this.normalBlock = normalBlock;
        this.deepBlock = deepBlock;
        this.normalLoot = normalLoot;
        this.deepLoot = deepLoot;
        this.chance = chance;
        this.deepThreshold = deepThreshold;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos pivot, BlockPos relative, StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo placed, StructurePlaceSettings settings) {

        BlockPos pos = placed.pos();
        StructureTemplate.StructureBlockInfo empty = new StructureTemplate.StructureBlockInfo(pos, Blocks.AIR.defaultBlockState(), new CompoundTag());

        BlockState state = placed.state();

        if (!state.is(Blocks.STRUCTURE_BLOCK)) return placed;

        CompoundTag tag = placed.nbt();
        if (tag == null || !tag.contains("mode") || !tag.contains("metadata")) return empty;

        // Validate that it's a DATA marker
        StructureMode mode = StructureMode.valueOf(tag.getString("mode"));
        if (mode != StructureMode.DATA) return empty;

        String meta = tag.getString("metadata");
        if (!meta.equals(markerName)) return empty;

        RandomSource random = settings.getRandom(pos);

//        if (rule.isPresent() && rule.get() instanceof ExtendedRuleTest extendedRuleTest  && !extendedRuleTest.extendedTest(pos, placed.state(), level, random)) return empty;

        boolean deep = pos.getY() <= deepThreshold;
        Block block = deep && deepBlock.isPresent() ? deepBlock.get() : normalBlock;

        if (!isValidPlacement(level, pos, block)) return empty;
        CompoundTag compoundTag = new CompoundTag();
        Optional<ResourceLocation> lootTable = deep && deepLoot.isPresent() ? deepLoot : normalLoot;
        lootTable.ifPresent(loc -> {
            compoundTag.putString("LootTable", loc.toString());
            compoundTag.putLong("LootTableSeed", random.nextLong());
        });

        return new StructureTemplate.StructureBlockInfo(pos, block.defaultBlockState(), compoundTag.isEmpty() ? null : compoundTag);
    }

    private boolean isValidPlacement(LevelReader level, BlockPos pos, Block target) {
        if (level.isOutsideBuildHeight(pos)) return false;
        BlockState below = level.getBlockState(pos.below());
        FluidState fluidStateBelow = level.getFluidState(pos.below());
        if (!below.isFaceSturdy(level, pos.below(), Direction.UP)) return false;
        BlockState current = level.getBlockState(pos);
        return !current.isAir() && !current.is(target) && !fluidStateBelow.is(FluidTags.WATER) && !fluidStateBelow.is(FluidTags.LAVA);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return SinewStructureProcessors.MARKER_PROCESSOR.get();
    }
}