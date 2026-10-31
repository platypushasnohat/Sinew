package com.platypushasnohat.sinew.utils;

import com.platypushasnohat.sinew.events.ClientEvents;
import com.platypushasnohat.sinew.events.custom.ScreenShakeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SinewClientProxy extends SinewCommonProxy {

    public static final List<ScreenShakeEvent> SCREEN_SHAKE_EVENTS = new ArrayList<>();
    public static List<UUID> blockedEntityRenders = new ArrayList<>();

    @Override
    public Player getClientSidePlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public boolean isKeyDown(int keyType) {
        Options options = Minecraft.getInstance().options;
        if (keyType == 0) {
            return options.keyJump.isDown();
        }
        if (keyType == 1) {
            return options.keySprint.isDown();
        }
        if (keyType == 2) {
            return options.keyAttack.isDown();
        }
        if (keyType == 3) {
            return options.keyShift.isDown();
        }
        return false;
    }

    @Override
    public void blockRenderingEntity(UUID uuid) {
        blockedEntityRenders.add(uuid);
    }

    @Override
    public void releaseRenderingEntity(UUID uuid) {
        blockedEntityRenders.remove(uuid);
    }

    @Override
    public boolean isFirstPersonPlayer(Entity entity) {
        return entity.equals(Minecraft.getInstance().cameraEntity) && Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }

    @Override
    public void screenShake(ScreenShakeEvent event) {
        ClientEvents.SCREEN_SHAKE_EVENTS.add(event);
    }
}
