package com.barlinc.sinew.world;

import com.barlinc.sinew.Sinew;
import com.barlinc.sinew.world.structure.processor.IfStructureProcessor;
import com.barlinc.sinew.world.structure.processor.MarkerProcessor;
import com.barlinc.sinew.world.structure.processor.ReplaceBlockProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SinewStructureProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSOR_TYPE = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, Sinew.MOD_ID);

    public static final RegistryObject<StructureProcessorType<ReplaceBlockProcessor>> REPLACE_BLOCK_PROCESSOR = STRUCTURE_PROCESSOR_TYPE.register("replace_block_processor", () -> ()-> ReplaceBlockProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<IfStructureProcessor>> IF_PROCESSOR = STRUCTURE_PROCESSOR_TYPE.register("if_processor", () -> ()-> IfStructureProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<MarkerProcessor>> MARKER_PROCESSOR = STRUCTURE_PROCESSOR_TYPE.register("marker_processor", () -> ()-> MarkerProcessor.CODEC);

}
