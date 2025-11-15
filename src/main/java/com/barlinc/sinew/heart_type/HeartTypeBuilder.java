package com.barlinc.sinew.heart_type;

import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class HeartTypeBuilder {

    private ResourceLocation atlas;
    private BiPredicate<Player, Boolean> applies;
    private Predicate<Gui.HeartType> drawFor;

    public static HeartTypeBuilder create() {
        return new HeartTypeBuilder();
    }

    public HeartTypeBuilder atlas(ResourceLocation atlas) {
        this.atlas = atlas;
        return this;
    }

    public HeartTypeBuilder applies(BiPredicate<Player, Boolean> applies) {
        this.applies = applies;
        return this;
    }

    public HeartTypeBuilder drawFor(Predicate<Gui.HeartType> drawFor) {
        this.drawFor = drawFor;
        return this;
    }

    public SinewHeartType build() {
        ResourceLocation atlasFinal = this.atlas;
        BiPredicate<Player, Boolean> appliesFinal = this.applies;
        Predicate<Gui.HeartType> drawForFinal = this.drawFor;

        SinewHeartType type = new SinewHeartType() {
            @Override
            public ResourceLocation getAtlas() {
                return atlasFinal;
            }

            @Override
            public boolean applies(Player player, boolean blinking) {
                return appliesFinal.test(player, blinking);
            }

            @Override
            public boolean drawForHeartType(Gui.HeartType type) {
                return drawForFinal.test(type);
            }
        };

        HeartTypeRegistry.register(type);
        return type;
    }
}
