package com.platypushasnohat.sinew.tags;

import com.platypushasnohat.sinew.Sinew;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SinewBlockTags {

    public static final TagKey<Block> MINEABLE_WITH_SHEARS = modBlockTag("mineable/shears");

    private static TagKey<Block> modBlockTag(String name) {
        return blockTag(Sinew.MOD_ID, name);
    }

    private static TagKey<Block> commonBlockTag(String name) {
        return blockTag("c", name);
    }

    public static TagKey<Block> blockTag(String modid, String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
}
