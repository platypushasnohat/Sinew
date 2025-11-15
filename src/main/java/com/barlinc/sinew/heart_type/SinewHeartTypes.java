package com.barlinc.sinew.heart_type;

import com.barlinc.sinew.mob_effects.SinewMobEffects;
import net.minecraft.client.gui.Gui;

import static com.barlinc.sinew.Sinew.modPrefix;

public class SinewHeartTypes {


    public static final SinewHeartType ELECTRIFIED = HeartTypeBuilder.create()
            .atlas(modPrefix("textures/gui/heart_type/electrified.png"))
            .applies((player, blinking) -> player.hasEffect(SinewMobEffects.ELECTRIFIED.get()) && !blinking)
            .drawFor((type) -> type != Gui.HeartType.CONTAINER && type != Gui.HeartType.ABSORBING && type != Gui.HeartType.FROZEN)
            .build();


    public static final SinewHeartType STENCH = HeartTypeBuilder.create()
            .atlas(modPrefix("textures/gui/heart_type/electrified.png"))
            .applies((player, blinking) -> player.hasEffect(SinewMobEffects.STENCH.get()) && !blinking)
            .drawFor((type) -> type != Gui.HeartType.CONTAINER && type != Gui.HeartType.ABSORBING && type != Gui.HeartType.FROZEN)
            .build();


    public static void register() {}
}
