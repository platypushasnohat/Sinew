package com.barlinc.sinew.datagen;

import com.barlinc.sinew.Sinew;
import com.barlinc.sinew.attributes.SinewAttributes;
import com.barlinc.sinew.damage_types.SinewDamageTypes;
import com.barlinc.sinew.mob_effects.SinewMobEffects;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

public class LanguageGenerator extends SinewLanguageProvider {

    public LanguageGenerator(PackOutput output) {
        super(Sinew.MOD_ID, output);
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
        return Sinew.MOD_ID + " Languages: en_us";
    }
}