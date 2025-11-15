package com.barlinc.sinew.datagen;

import com.barlinc.sinew.utils.SinewTextUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class SinewLanguageProvider extends LanguageProvider {

    public final String modid;

    public SinewLanguageProvider(String modid, PackOutput output) {
        super(output, modid, "en_us");
        this.modid = modid;
    }

    public void forBlock(Supplier<? extends Block> block) {
        this.addBlock(block, SinewTextUtils.createTranslation(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath()));
    }

    public void forItem(Supplier<? extends Item> item) {
        this.addItem(item, SinewTextUtils.createTranslation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.get())).getPath()));
    }

    public void forEntity(Supplier<? extends EntityType<?>> entity) {
        this.addEntityType(entity, SinewTextUtils.createTranslation(Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity.get())).getPath()));
    }

    public void translateBiome(ResourceKey<Biome> biome) {
        String name = biome.location().getPath();
        this.add("biome." + modid + "." + name, SinewTextUtils.createTranslation(name));
    }

    public void translateEffect(RegistryObject<? extends MobEffect> effect, String desc) {
        this.add(effect.get(), toUpper(ForgeRegistries.MOB_EFFECTS, effect));

        // JEED compat
        this.add(effect.get().getDescriptionId() + ".description", desc);
    }

    public void translatePotion(RegistryObject<? extends Potion> potion, String effect) {
        String name = ForgeRegistries.POTIONS.getKey(potion.get()).getPath();
        this.add("item.minecraft.potion.effect." + name, "Potion of " + effect);
        this.add("item.minecraft.splash_potion.effect." + name, "Splash Potion of " + effect);
        this.add("item.minecraft.tipped_arrow.effect." + name, "Arrow of " + effect);
        this.add("item.minecraft.lingering_potion.effect." + name, "Lingering Potion of " + effect);

        // Caverns & Chasms compat
        this.add("item.caverns_and_chasms.tether_potion.effect." + name, "Tether Potion of " + effect);
    }

    public void translateAttribute(RegistryObject<? extends Attribute> attribute) {
        this.add(attribute.get().getDescriptionId(), toUpper(ForgeRegistries.ATTRIBUTES, attribute));
    }

    public void translateDamageType(ResourceKey<DamageType> source, Function<String, String> death, BiFunction<String, String, String> killed) {
        String msgId = source.location().getPath();
        this.add("death.attack." + msgId, death.apply("%1$s"));
        this.add("death.attack." + msgId + ".player", killed.apply("%1$s", "%2$s"));
    }

    public void translatePainting(String name, String author) {
        this.add("painting." + modid + "." + name + ".title",  SinewTextUtils.createTranslation(name));
        this.add("painting." + modid + "." + name + ".author",  author);
    }

    public void translateMusicDisc(Supplier<? extends Item> item, String description) {
        String disc = item.get().getDescriptionId();
        this.add(disc, "Music Disc");
        this.add(disc + ".desc", description);
    }

    public void translateCreativeTab(CreativeModeTab key, String name){
        this.add(key.getDisplayName().getString(), name);
    }

    // utils
    private static <T> String toUpper(IForgeRegistry<T> registry, RegistryObject<? extends T> object) {
        return toUpper(registry.getKey(object.get()).getPath());
    }

    private static String toUpper(String string) {
        return SinewTextUtils.createTranslation(string);
    }
}
