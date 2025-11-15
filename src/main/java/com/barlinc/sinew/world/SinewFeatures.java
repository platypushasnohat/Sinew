package com.barlinc.sinew.world;

import com.barlinc.sinew.Sinew;
import com.barlinc.sinew.world.feature.NbtFeature;
import com.barlinc.sinew.world.feature.config.NbtFeatureConfig;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SinewFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Sinew.MOD_ID);

    public static final RegistryObject<Feature<NbtFeatureConfig>> NBT_FEATURE = FEATURES.register("nbt_feature", () -> new NbtFeature<>(NbtFeatureConfig.CODEC));

}
