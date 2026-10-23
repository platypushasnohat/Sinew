package com.platypushasnohat.sinew.debug;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

//DEBUG
//invisible without sight but audible to everyone
public class SpectralTestAudibleZombie extends SpectralTestZombie {

    public SpectralTestAudibleZombie(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean canBeHeardBy(Player listener) {
        return true;
    }
}