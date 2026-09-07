package com.platypushasnohat.sinew.events.custom;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.util.TriState;

public class PoseHandEvent extends Event {

    private final LivingEntity entity;
    private final HumanoidModel<?> humanoidModel;
    private final boolean leftHanded;
    private TriState result;

    public PoseHandEvent(LivingEntity entity, HumanoidModel<?> humanoidModel, boolean leftHanded) {
        this.result = TriState.DEFAULT;
        this.entity = entity;
        this.humanoidModel = humanoidModel;
        this.leftHanded = leftHanded;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public HumanoidModel<?> getHumanoidModel() {
        return this.humanoidModel;
    }

    public boolean isLeftHanded() {
        return this.leftHanded;
    }

    public void setResult(TriState result) {
        this.result = result;
    }

    public TriState getResult() {
        return this.result;
    }
}