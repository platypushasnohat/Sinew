package com.barlinc.sinew.mixins;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructureTemplate.class)
public interface StructureTemplateAccessor {

    @Accessor("palettes")
    List<StructureTemplate.Palette> sinew$getBlocks();

    @Accessor("entityInfoList")
    List<StructureTemplate.StructureEntityInfo> sinew$getEntityInfoList();
}