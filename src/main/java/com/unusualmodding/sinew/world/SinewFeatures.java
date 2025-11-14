package com.unusualmodding.sinew.world;

import com.unusualmodding.sinew.Sinew;
import com.unusualmodding.sinew.world.feature.NbtFeature;
import com.unusualmodding.sinew.world.feature.config.NbtFeatureConfig;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SinewFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Sinew.MODID);

    public static final RegistryObject<Feature<NbtFeatureConfig>> NBT_FEATURE = FEATURES.register("nbt_feature", () -> new NbtFeature<>(NbtFeatureConfig.CODEC));

}
