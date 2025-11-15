package com.barlinc.sinew.managers;
/*
 * This file is part of Lodestone, licensed under the LGPL-3.0.
 * Copyright (c) LodestarMC
 *
 * You may obtain a copy of the License at:
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * This file incorporates work covered by the LGPL licensed source at:
 * https://github.com/LodestarMC/Lodestone
 */

import com.barlinc.sinew.system.screenshake.ScreenshakeBuilder;
import com.barlinc.sinew.system.screenshake.ScreenshakeInstance;
import com.barlinc.sinew.utils.RandomHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraftforge.client.event.ViewportEvent;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ScreenshakeManager {

    private static final ArrayList<ScreenshakeInstance> INSTANCES = new ArrayList<>();

    private static float intensity;

    public static void computeAngles(ViewportEvent.ComputeCameraAngles event) {
        RandomSource random = Minecraft.getInstance().level.getRandom();
        if (intensity > 0) {
            float intensity = (float) (ScreenshakeManager.intensity); //todo * ClientConfig.SCREENSHAKE_INTENSITY.getConfigValue());
            float yaw = RandomHelper.randomBetween(random, 0, intensity * 2) * (random.nextBoolean() ? 1 : -1);
            float pitch = RandomHelper.randomBetween(random, 0, intensity * 2) * (random.nextBoolean() ? 1 : -1);
            event.setYaw(event.getYaw() + yaw);
            event.setPitch(event.getPitch() + pitch);
        }
    }

    public static void clientTick(ClientLevel level, Camera camera) {
        float intensitySum = 0;
        for (ScreenshakeInstance instance : INSTANCES) {
            instance.tick();
            intensitySum += instance.getStrength(camera);
        }
        intensity = intensitySum;
        INSTANCES.removeIf(ScreenshakeInstance::isExpired);
    }

    public static void addScreenshake(Consumer<ScreenshakeBuilder> constructor) {
        ScreenshakeBuilder builder = ScreenshakeBuilder.create();
        constructor.accept(builder);
        addScreenshake(builder.build());
    }

    public static void addScreenshake(ScreenshakeInstance instance) {
        INSTANCES.add(instance);
    }
}