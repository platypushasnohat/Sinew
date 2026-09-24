package com.platypushasnohat.sinew.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class SinewCommonProxy {

    public Player getClientSidePlayer() {
        return null;
    }

    public boolean isKeyDown(int keyType) {
        return false;
    }

    public void blockRenderingEntity(UUID uuid) {
    }

    public void releaseRenderingEntity(UUID uuid) {
    }

    public boolean isFirstPersonPlayer(Entity entity) {
        return false;
    }
}
