package com.platypushasnohat.sinew.tags;

import com.platypushasnohat.sinew.Sinew;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class SinewEntityTags {

    public static final TagKey<EntityType<?>> SWEET_BERRY_BUSH_IMMUNE = modEntityTag("sweet_berry_bush_immune");

    public static final TagKey<EntityType<?>> POST_NETHER_SPAWNS = modEntityTag("post_nether_spawns");
    public static final TagKey<EntityType<?>> POST_END_SPAWNS = modEntityTag("post_end_spawns");

    private static TagKey<EntityType<?>> modEntityTag(String name) {
        return entityTag(Sinew.MOD_ID, name);
    }

    private static TagKey<EntityType<?>> commonEntityTag(String name) {
        return entityTag("c", name);
    }

    public static TagKey<EntityType<?>> entityTag(String modid, String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
}
