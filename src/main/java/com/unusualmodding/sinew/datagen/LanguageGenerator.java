package com.unusualmodding.sinew.datagen;

import com.unusualmodding.sinew.Sinew;
import com.unusualmodding.sinew.attributes.SinewAttributes;
import com.unusualmodding.sinew.damage_types.SinewDamageTypes;
import com.unusualmodding.sinew.mob_effects.SinewMobEffects;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.text.WordUtils;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class LanguageGenerator extends LanguageProvider {

    public LanguageGenerator(PackOutput output) {
        super(output, Sinew.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.translateDamageType(SinewDamageTypes.ELECTRIC, player -> player + " met a shocking end", (player, entity) -> player + " was zapped by " + entity);
        this.translateEffect(SinewMobEffects.ELECTRIFIED, "Inflicts electric damage over time while in water or rain; higher levels do more damage per second.");
        this.translateEffect(SinewMobEffects.STENCH, "Makes the player stinky, prevents the player from eating");
        this.translateAttribute(SinewAttributes.STEALTH);
        this.translateAttribute(SinewAttributes.JUMP_POWER);
        this.translateAttribute(SinewAttributes.AIR_SPEED);
        this.translateAttribute(SinewAttributes.EXPERIENCE_GAIN);
        this.translateAttribute(SinewAttributes.SUMMON_DAMAGE);
        this.translateAttribute(SinewAttributes.SUMMON_DURATION);
    }

    @Override
    public @NotNull String getName() {
        return Sinew.MODID + " Languages: en_us";
    }

    public void translateSound(Supplier<? extends SoundEvent> key, String subtitle){
        add("subtitles.sinew." + key.get().getLocation().getPath(), subtitle);
    }

    private void translateEnchantment(RegistryObject<? extends Enchantment> enchantment, String name, String desc) {
        String descId = enchantment.get().getDescriptionId();
        this.add(descId, name);
        this.add(descId + ".desc", desc);
    }

    private void translateEffect(RegistryObject<? extends MobEffect> effect, String desc) {
        this.add(effect.get(), toUpper(ForgeRegistries.MOB_EFFECTS, effect));
        this.add(effect.get().getDescriptionId() + ".description", desc);
    }

    private void translatePotion(RegistryObject<? extends Potion> potion, String effect) {
        String name = ForgeRegistries.POTIONS.getKey(potion.get()).getPath();
        this.add("item.minecraft.potion.effect." + name, "Potion of " + effect);
        this.add("item.minecraft.splash_potion.effect." + name, "Splash Potion of " + effect);
        this.add("item.minecraft.tipped_arrow.effect." + name, "Arrow of " + effect);
        this.add("item.minecraft.lingering_potion.effect." + name, "Lingering Potion of " + effect);
        this.add("item.caverns_and_chasms.tether_potion.effect." + name, "Tether Potion of " + effect);
    }

    private void translateAttribute(RegistryObject<? extends Attribute> attribute) {
        this.add(attribute.get().getDescriptionId(), toUpper(ForgeRegistries.ATTRIBUTES, attribute));
    }

    private void translateDamageType(ResourceKey<DamageType> source, Function<String, String> death, BiFunction<String, String, String> killed) {
        String msgId = source.location().getPath();
        this.add("death.attack." + msgId, death.apply("%1$s"));
        this.add("death.attack." + msgId + ".player", killed.apply("%1$s", "%2$s"));
    }

    private void translateEnchantmentWithDesc(Enchantment enchantment, String description) {
        String name = ForgeRegistries.ENCHANTMENTS.getKey(enchantment).getPath();
        this.add(enchantment, formatEnchantment(name));
        this.add(enchantment.getDescriptionId() + ".desc", description);
    }

    private String formatEnchantment(String path) {
        return WordUtils.capitalizeFully(path.replace("_", " ")).replace("Of ", "of ");
    }

    public void translateCreativeTab(CreativeModeTab key, String name){
        add(key.getDisplayName().getString(), name);
    }

    private static <T> String toUpper(IForgeRegistry<T> registry, RegistryObject<? extends T> object) {
        return toUpper(registry.getKey(object.get()).getPath());
    }

    private static String toUpper(String string) {
        return StringUtils.capitaliseAllWords(string.replace('_', ' '));
    }
}