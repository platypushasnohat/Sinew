package com.platypushasnohat.sinew.debug;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

//DEBUG
//visible without sight and audible to everyone. without sight it should be seen but unclickable, no F3 hitbox, arrows pass through
public class SpectralTestVisibleZombie extends SpectralTestZombie {

    public SpectralTestVisibleZombie(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isVisibleWithoutSight(Player viewer) {
        return true;
    }

    @Override
    public boolean canBeHeardBy(Player listener) {
        return true;
    }
}
