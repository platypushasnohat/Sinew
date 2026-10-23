package com.platypushasnohat.sinew.entity.spectral;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpectralFade {

    private final Entity entity;
    private final int duration;
    private final Map<UUID, Integer> lastPerceived = new HashMap<>();
    private int ticks;
    private int prevTicks;

    public SpectralFade(Entity entity) {
        this(entity, 50);
    }

    public SpectralFade(Entity entity, int duration) {
        this.entity = entity;
        this.duration = duration;
    }

    public void tick() {
        for (Player player : this.entity.level().players()) {
            if (SpectralSight.canPerceive(player, this.entity)) {
                this.lastPerceived.put(player.getUUID(), this.entity.tickCount);
            }
        }
        if (this.lastPerceived.values().removeIf(tick -> this.entity.tickCount - tick > this.duration) && !this.entity.level().isClientSide) {
            SpectralSight.refreshTracking(this.entity);
        }
        if (this.entity.level().isClientSide) {
            this.prevTicks = this.ticks;
            this.ticks = Mth.clamp(this.ticks + (SpectralSight.isPerceivedByClient(this.entity) ? 1 : -1), 0, this.duration);
        }
    }

    public boolean isVisibleTo(Player viewer) {
        return this.lastPerceived.containsKey(viewer.getUUID());
    }

    public float get(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevTicks, this.ticks) / this.duration;
    }
}
