package com.platypushasnohat.sinew.events.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ScreenShakeEvent {

    private final Vec3 position;
    private final int duration;
    private final float degree;
    private final float distance;
    private final boolean groundRequired;
    private int age;

    public ScreenShakeEvent(Vec3 position, int duration, float degree, float distance, boolean groundRequired) {
        this.position = position;
        this.duration = duration;
        this.degree = degree;
        this.distance = distance;
        this.groundRequired = groundRequired;
        this.age = 0;
    }

    public float getDegree(Entity camera, float partialTicks) {
        double cameraDistance = this.position.distanceTo(camera.position());
        if (cameraDistance < this.distance && (!this.groundRequired || camera.onGround())) {
            return (1.0F - (float) (cameraDistance / this.distance)) * this.degree * (float) Math.sin(((this.age + partialTicks) / this.duration) * Math.PI);
        } else {
            return 0.0F;
        }
    }

    public void tick() {
        this.age++;
    }

    public boolean isDone() {
        return this.age >= this.duration;
    }
}