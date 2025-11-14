package com.unusualmodding.sinew.world.structure.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unusualmodding.sinew.world.SinewStructureProcessors;
import com.unusualmodding.sinew.world.structure.rule_test.ExtendedRuleTest;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Optional;

public class IfStructureProcessor extends StructureProcessor {

    public static final Codec<IfStructureProcessor> CODEC = RecordCodecBuilder.create(i -> i.group(
            RuleTest.CODEC.fieldOf("rule").forGetter(p -> p.rule),
            StructureProcessorType.SINGLE_CODEC.fieldOf("processor").forGetter(p -> p.thenProcessor),
            StructureProcessorType.SINGLE_CODEC.optionalFieldOf("else_processor").forGetter(p -> p.elseProcessor)
    ).apply(i, IfStructureProcessor::new));

    private final RuleTest rule;                            // expects an ExtendedRuleTest for world-aware checks
    private final StructureProcessor thenProcessor;         // run when rule passes
    private final Optional<StructureProcessor> elseProcessor; // optional else

    public IfStructureProcessor(RuleTest rule, StructureProcessor thenProcessor, Optional<StructureProcessor> elseProcessor) {
        this.rule = rule;
        this.thenProcessor = thenProcessor;
        this.elseProcessor = elseProcessor;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(
            LevelReader level,
            BlockPos pivot,                 // structure pivot (template origin in world)
            BlockPos relativePos,           // block pos relative to pivot
            StructureTemplate.StructureBlockInfo originalInfo, // template block
            StructureTemplate.StructureBlockInfo placedInfo,   // block about to be placed
            StructurePlaceSettings settings
    ) {
        if (placedInfo == null) return null;

        final BlockPos worldPos = placedInfo.pos();
        final BlockState state = placedInfo.state();
        final RandomSource random = settings.getRandom(worldPos);

        // Evaluate rule (prefer ExtendedRuleTest with world context)
        final boolean passes = (rule instanceof ExtendedRuleTest ext)
                ? ext.extendedTest(worldPos, state, level, random)
                : rule.test(state, random);

        if (passes) {
            // Apply THEN processor
            return thenProcessor.processBlock(level, pivot, relativePos, originalInfo, placedInfo, settings);
        } else if (elseProcessor.isPresent()) {
            // Apply ELSE processor (if provided)
            return elseProcessor.get().processBlock(level, pivot, relativePos, originalInfo, placedInfo, settings);
        } else {
            // No else branch: leave block unchanged
            return placedInfo;
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return SinewStructureProcessors.IF_PROCESSOR.get();
    }
}