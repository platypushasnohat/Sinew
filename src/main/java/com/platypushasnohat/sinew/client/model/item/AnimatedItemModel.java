package com.platypushasnohat.sinew.client.model.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.sinew.client.animation.ItemAnimationState;
import com.platypushasnohat.sinew.client.animation.ItemKeyframeAnimations;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.entity.animation.json.AnimationHolder;
import net.neoforged.neoforge.client.entity.animation.json.AnimationLoader;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.function.Function;

public abstract class AnimatedItemModel<I extends Item> extends Model {

    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    public AnimatedItemModel() {
        this(RenderType::entityCutoutNoCull);
    }

    public AnimatedItemModel(Function<ResourceLocation, RenderType> renderType) {
        super(renderType);
    }

    protected static AnimationHolder getAnimation(ResourceLocation resourceLocation) {
        return AnimationLoader.INSTANCE.getAnimationHolder(resourceLocation);
    }

    public abstract void setupAnim(Entity entity, I item, ItemStack stack, ItemDisplayContext displayContext, float ageInTicks);

    public abstract ModelPart root();

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
        this.root().render(poseStack, consumer, packedLight, packedOverlay, color);
    }

    public Optional<ModelPart> getAnyDescendantWithName(String name) {
        return name.equals("root") ? Optional.of(this.root()) : this.root().getAllParts().filter((part) -> part.hasChild(name)).findFirst().map((part) -> part.getChild(name));
    }

    protected void animate(Entity entity, ItemAnimationState animationState, AnimationDefinition animationDefinition, float ageInTicks) {
        this.animate(entity, animationState, animationDefinition, ageInTicks, 1.0F);
    }

    protected void animate(Entity entity, ItemAnimationState animationState, AnimationHolder animation, float ageInTicks) {
        this.animate(entity, animationState, animation.get(), ageInTicks);
    }

    protected void animate(Entity entity, ItemAnimationState animationState, AnimationDefinition animationDefinition, float ageInTicks, float speed) {
        animationState.updateTime(ageInTicks, speed);
        animationState.ifStarted((state) -> ItemKeyframeAnimations.animate(this, animationDefinition, state.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE));
    }

    protected void animate(Entity entity, ItemAnimationState animationState, AnimationHolder animation, float ageInTicks, float speed) {
        this.animate(entity, animationState, animation.get(), ageInTicks, speed);
    }

    protected void applyStatic(AnimationDefinition animationDefinition) {
        ItemKeyframeAnimations.animate(this, animationDefinition, 0L, 1.0F, ANIMATION_VECTOR_CACHE);
    }

    protected void applyStatic(AnimationHolder animation) {
        this.applyStatic(animation.get());
    }
}