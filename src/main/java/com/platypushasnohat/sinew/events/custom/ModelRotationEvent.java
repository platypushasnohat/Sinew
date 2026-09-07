package com.platypushasnohat.sinew.events.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

@OnlyIn(Dist.CLIENT)
public class ModelRotationEvent extends Event implements ICancellableEvent {

    private final LivingEntity entity;
    private final EntityModel<LivingEntity> entityModel;
    private final PoseStack poseStack;

    public ModelRotationEvent(LivingEntity entity, EntityModel<LivingEntity> entityModel, PoseStack poseStack) {
        this.entity = entity;
        this.entityModel = entityModel;
        this.poseStack = poseStack;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public EntityModel<LivingEntity> getEntityModel() {
        return this.entityModel;
    }

    public PoseStack getPoseStack() {
        return this.poseStack;
    }
}