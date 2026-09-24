package com.platypushasnohat.sinew.events.custom;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.util.TriState;

public class PlayerPoseEvent extends Event implements ICancellableEvent {

    private final LivingEntity entity;
    private final HumanoidModel<?> humanoidModel;
    private TriState result;

    public PlayerPoseEvent(LivingEntity entity, HumanoidModel<?> humanoidModel) {
        this.result = TriState.DEFAULT;
        this.entity = entity;
        this.humanoidModel = humanoidModel;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public HumanoidModel<?> getHumanoidModel() {
        return this.humanoidModel;
    }

    public void setResult(TriState result) {
        this.result = result;
    }

    public TriState getResult() {
        return this.result;
    }
}