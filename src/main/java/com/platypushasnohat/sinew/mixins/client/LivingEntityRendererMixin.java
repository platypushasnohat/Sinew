package com.platypushasnohat.sinew.mixins.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//spectral fade in. vanilla's translucent render type
//used by: every spectral mob with getFadeInTicks above 0. only works for renderers that extend LivingEntityRenderer and don't override render
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;"), require = 0)
    private RenderType sinew$fadeSpectralRenderType(LivingEntityRenderer<?, ?> renderer, LivingEntity entity, boolean bodyVisible, boolean translucent, boolean glowing, Operation<RenderType> original) {
        return original.call(renderer, entity, bodyVisible, translucent || (bodyVisible && SpectralSight.getFadeIn(entity) < 1.0F), glowing);
    }

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"), require = 0)
    private void sinew$fadeSpectralAlpha(EntityModel<?> model, PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int color, Operation<Void> original, @Local(argsOnly = true) LivingEntity entity) {
        float fade = SpectralSight.getFadeIn(entity);
        if (fade < 1.0F) {
            int alpha = (int) (((color >>> 24) & 0xFF) * fade);
            original.call(model, poseStack, consumer, light, overlay, (alpha << 24) | (color & 0xFFFFFF));
        } else {
            original.call(model, poseStack, consumer, light, overlay, color);
        }
    }

    //layers can't be faded, so they appear once the body has finished fading in
    @WrapWithCondition(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/Entity;FFFFFF)V"), require = 0)
    private boolean sinew$hideSpectralLayers(RenderLayer<?, ?> layer, PoseStack poseStack, MultiBufferSource buffer, int light, Entity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        return SpectralSight.getFadeIn(entity) >= 1.0F;
    }
}
