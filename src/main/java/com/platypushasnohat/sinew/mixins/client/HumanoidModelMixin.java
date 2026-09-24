package com.platypushasnohat.sinew.mixins.client;

import com.platypushasnohat.sinew.events.custom.PlayerPoseEvent;
import com.platypushasnohat.sinew.events.custom.PoseHandEvent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin extends Model {

    public HumanoidModelMixin(Function<ResourceLocation, RenderType> renderType) {
        super(renderType);
    }

    @Inject(at = @At("HEAD"), method = "poseRightArm", cancellable = true)
    private void sinew$poseRightArm(LivingEntity entity, CallbackInfo ci) {
        PoseHandEvent event = new PoseHandEvent(entity, (HumanoidModel<?>) ((Model) this), false);
        NeoForge.EVENT_BUS.post(event);
        if (event.getResult() == TriState.TRUE) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "poseLeftArm", cancellable = true)
    private void sinew$poseLeftArm(LivingEntity entity, CallbackInfo ci) {
        PoseHandEvent event = new PoseHandEvent(entity, (HumanoidModel<?>) ((Model) this), true);
        NeoForge.EVENT_BUS.post(event);
        if (event.getResult() == TriState.TRUE) {
            ci.cancel();
        }
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"), cancellable = true)
    public void sinew$posePlayer(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        PlayerPoseEvent event = new PlayerPoseEvent(entity, (HumanoidModel<?>) ((Model) this));
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}