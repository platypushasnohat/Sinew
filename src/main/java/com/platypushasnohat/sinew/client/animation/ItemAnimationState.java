package com.platypushasnohat.sinew.client.animation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class ItemAnimationState {

    private long lastTime = Long.MAX_VALUE;
    private long accumulatedTime;
    private Entity entity;

    public void start(int tickCount, Entity entity) {
        this.lastTime = (long) tickCount * 1000L / 20L;
        this.accumulatedTime = 0L;
        this.entity = entity;
    }

    public void startIfStopped(int tickCount, Entity entity) {
        if (!this.isStarted()) {
            this.start(tickCount, entity);
        }
    }

    public void animateWhen(boolean condition, int tickCount, Entity entity) {
        if (condition) {
            this.startIfStopped(tickCount, entity);
        }
        else {
            this.stop(entity);
        }
    }

    public void stop(Entity entity) {
        if (!this.matchesViewingEntity(entity)) {
            return;
        }
        this.lastTime = Long.MAX_VALUE;
    }

    public void ifStarted(Consumer<ItemAnimationState> consumer) {
        if (this.isStarted()) {
            consumer.accept(this);
        }
    }

    public void updateTime(float ageInTicks, float speed) {
        if (this.isStarted()) {
            long l = Mth.lfloor(ageInTicks * 1000.0F / 20.0F);
            this.accumulatedTime += (long)((float)(l - this.lastTime) * speed);
            this.lastTime = l;
        }
    }

    public long getAccumulatedTime() {
        return this.accumulatedTime;
    }

    public boolean isStarted() {
        return this.lastTime != Long.MAX_VALUE;
    }

    public boolean matchesViewingEntity(Entity entity) {
        return this.entity == entity;
    }
}