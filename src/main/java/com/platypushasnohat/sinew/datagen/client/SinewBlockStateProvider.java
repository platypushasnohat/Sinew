package com.platypushasnohat.sinew.datagen.client;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public abstract class SinewBlockStateProvider extends BlockStateProvider {

    public SinewBlockStateProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
    }

    public static String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    public static ResourceLocation prefix(String prefix, ResourceLocation location) {
        return ResourceLocation.fromNamespaceAndPath(location.getNamespace(), prefix + location.getPath());
    }

    public static ResourceLocation suffix(ResourceLocation location, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(location.getNamespace(), location.getPath() + suffix);
    }

    public static ResourceLocation remove(ResourceLocation location, String remove) {
        return ResourceLocation.fromNamespaceAndPath(location.getNamespace(), location.getPath().replace(remove, ""));
    }

    public ModelFile particle(Block block, ResourceLocation location) {
        return this.models().getBuilder(name(block)).texture("particle", location);
    }

    public ModelFile particle(DeferredHolder<? extends Block, ?> block, ResourceLocation location) {
        return this.particle(block.get(), location);
    }

    public void blockItem(Block block) {
        this.simpleBlockItem(block, new ModelFile.ExistingModelFile(blockTexture(block), this.models().existingFileHelper));
    }

    public void blockItem(DeferredHolder<Block, ?> block) {
        this.blockItem(block.get());
    }

    public void generatedItem(ItemLike item, String type) {
        this.generatedItem(item, item, type);
    }

    public void generatedItem(ItemLike item, ItemLike texture, String type) {
        this.generatedItem(item, prefix(type + "/", SinewItemModelProvider.key(texture)));
    }

    public void generatedItem(ItemLike item, ResourceLocation location) {
        this.itemModels().withExistingParent(BuiltInRegistries.ITEM.getKey(item.asItem()).getPath(), "item/generated").texture("layer0", location);
    }

    public void block(Block block) {
        this.simpleBlock(block);
        this.blockItem(block);
    }

    public void block(DeferredHolder<Block, ?> block) {
        this.block(block.get());
    }

    public void stairsBlock(Block block, Block stairs) {
        if (stairs instanceof StairBlock stairBlock) {
            this.stairsBlock(stairBlock, this.blockTexture(block));
            this.blockItem(stairs);
        }
    }

    public void slabBlock(Block block, Block slab) {
        if (slab instanceof SlabBlock slabBlock) {
            this.slabBlock(slabBlock, this.blockTexture(block), this.blockTexture(block));
            this.blockItem(slab);
        }
    }

    public void wallBlock(Block block, Block wall) {
        if (wall instanceof WallBlock wallBlock) {
            this.wallBlock(wallBlock, this.blockTexture(block));
            this.itemModels().getBuilder(name(wall)).parent(this.models().wallInventory(name(wall) + "_inventory", this.blockTexture(block)));
        }
    }

    public void axisBlock(DeferredHolder<Block, ?> block) {
        this.axisBlock(block, this.blockTexture(block.get()), suffix(this.blockTexture(block.get()), "_top"));
    }

    public void axisBlock(DeferredHolder<Block, ?> block, ResourceLocation sideTexture, ResourceLocation topTexture) {
        if (block.get() instanceof RotatedPillarBlock log) {
            this.axisBlock(log, sideTexture, topTexture);
            this.blockItem(block);
        }
    }
}
