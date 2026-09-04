package com.platypushasnohat.sinew.datagen.client;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class SinewLanguageProvider extends LanguageProvider {

    public final String modId;

    public SinewLanguageProvider(PackOutput output, String modId) {
        super(output, modId, "en_us");
        this.modId = modId;
    }

    public void forBlock(Supplier<? extends Block> block) {
        this.addBlock(block, this.format(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block.get())).getPath()));
    }

    public void forItem(Supplier<? extends Item> item) {
        this.addItem(item, this.format(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.get())).getPath()));
    }

    public void forEntity(Supplier<? extends EntityType<?>> entity) {
        this.addEntityType(entity, this.format(Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(entity.get())).getPath()));
    }

    public void addBlocks(Block... blocks) {
        List.of(blocks).forEach((block -> this.add(block, format(BuiltInRegistries.BLOCK.getKey(block)))));
    }

    public void addStorageBlocks(Block... blocks) {
        List.of(blocks).forEach((block -> this.add(block, "Block of " + format(BuiltInRegistries.BLOCK.getKey(block)).replace(" Block", ""))));
    }

    public void addItems(Item... items) {
        List.of(items).forEach((item -> this.add(item, format(BuiltInRegistries.ITEM.getKey(item)))));
    }

    public void addMusicDisc(Item item, String description) {
        this.add(item, "Music Disc");
        this.add(item.getDescriptionId() + ".desc", description);
    }

    public void addDamageType(String suffix, String value) {
        this.add("death.attack." + this.modId + "." + suffix, value);
    }

    public void addCreativeTab(CreativeModeTab key, String name){
        this.add(key.getDisplayName().getString(), name);
    }

    public void addSound(Supplier<? extends SoundEvent> key, String subtitle){
        this.add("subtitles." + this.modId + "." + key.get().getLocation().getPath(), subtitle);
    }

    public String format(ResourceLocation name) {
        return this.format(name.getPath());
    }

    @SuppressWarnings("deprecation")
    public String format(String path) {
        return WordUtils.capitalizeFully(path.replace("_", " ")).replace(" Of ", " of ");
    }
}
