package com.platypushasnohat.sinew.datagen.client;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public abstract class SinewItemModelProvider extends ItemModelProvider {

    public SinewItemModelProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
    }

    public void item(DeferredHolder<? extends ItemLike, ?> item, String type) {
        this.withExistingParent(name(item.get()), "item/" + type).texture("layer0", itemTexture(item.get()));
    }

    public ItemModelBuilder item(DeferredHolder<? extends ItemLike, ?> item, String path, String type) {
        return this.withExistingParent(name(item.get()), "item/" + type).texture("layer0", ResourceLocation.fromNamespaceAndPath(this.modid, "item/" + path));
    }

    public ItemModelBuilder item(ResourceLocation location, String type) {
        return this.withExistingParent(location.getPath(), "item/" + type).texture("layer0", ResourceLocation.fromNamespaceAndPath(this.modid, "item/" + location.getPath()));
    }

    @SafeVarargs
    public final void generatedItem(DeferredHolder<? extends ItemLike, ?>... items) {
        for (DeferredHolder<? extends ItemLike, ?> item : items) {
            this.item(item, "generated");
        }
    }

    @SafeVarargs
    public final void handheldItem(DeferredHolder<? extends ItemLike, ?>... items) {
        for (DeferredHolder<? extends ItemLike, ?> item : items) {
            this.item(item, "handheld");
        }
    }

    public static ResourceLocation key(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    public static String name(ItemLike item) {
        return key(item).getPath();
    }

    public static ResourceLocation itemTexture(ItemLike item) {
        ResourceLocation name = key(item);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.ITEM_FOLDER + "/" + name.getPath());
    }
}
