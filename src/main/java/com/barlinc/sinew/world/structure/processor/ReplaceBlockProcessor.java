package com.barlinc.sinew.world.structure.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.barlinc.sinew.utils.GeneralUtils;
import com.barlinc.sinew.world.SinewStructureProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class ReplaceBlockProcessor extends StructureProcessor {
    public static final Codec<ReplaceBlockProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("input_block").forGetter(config -> config.inputBlock),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("output_block").forGetter(config -> config.outputBlock),
            Codec.FLOAT.fieldOf("chance").forGetter(config -> config.chance)
    ).apply(instance, instance.stable(ReplaceBlockProcessor::new)));
    private final Block inputBlock;
    private final Block outputBlock;
    private final float chance;

    public ReplaceBlockProcessor(Block inputBlock, Block outputBlock, float chance) {
        this.inputBlock = inputBlock;
        this.outputBlock = outputBlock;
        this.chance = chance;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings) {
        RandomSource random = settings.getRandom(infoIn2.pos());

        if (infoIn2.state().getBlock() == inputBlock && random.nextFloat() >= chance) {
            BlockState newBlockState = outputBlock.defaultBlockState();
            newBlockState = GeneralUtils.copyBlockProperties(infoIn2.state(), outputBlock.defaultBlockState());
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), newBlockState, infoIn2.nbt());
        }
        return infoIn2;
    }


    @Override
    protected StructureProcessorType<?> getType() {
        return SinewStructureProcessors.REPLACE_BLOCK_PROCESSOR.get();
    }
}