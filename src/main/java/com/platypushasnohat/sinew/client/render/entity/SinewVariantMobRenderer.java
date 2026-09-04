package com.platypushasnohat.sinew.client.render.entity;

import com.platypushasnohat.sinew.entity.variant.SinewVariantMob;
import com.platypushasnohat.sinew.entity.variant.VariantRenderType;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.client.NeoForgeRenderTypes;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SinewVariantMobRenderer<T extends Mob & SinewVariantMob, M extends EntityModel<T>> extends SinewMobRenderer<T, M> {

    public SinewVariantMobRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    public static <T extends Mob & SinewVariantMob, M extends EntityModel<T>> EntityRendererProvider<T> createBasicVariantRenderer(ModelLayerLocation layer, Function<ModelPart, M> modelFactory, float shadowRadius) {
        return context -> new SinewVariantMobRenderer<>(context, modelFactory.apply(context.bakeLayer(layer)), shadowRadius);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.getVariantTexture();
    }

    @Nullable
    @Override
    protected RenderType getRenderType(T entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        ResourceLocation textureLocation = this.getTextureLocation(entity);
        if (translucent) {
            return RenderType.itemEntityTranslucentCull(textureLocation);
        }
        else if (bodyVisible) {
            return this.getVariantRenderType(entity.getVariantRenderType(), textureLocation);
        }
        else {
            return super.getRenderType(entity, false, false, glowing);
        }
    }

    public RenderType getVariantRenderType(VariantRenderType type, ResourceLocation texture) {
        return switch (type) {
            case ENTITY_CUTOUT -> RenderType.entityCutout(texture);
            case ENTITY_CUTOUT_NO_CULL -> RenderType.entityCutoutNoCull(texture);
            case ENTITY_CUTOUT_MIPPED -> NeoForgeRenderTypes.getEntityCutoutMipped(texture);
            case ENTITY_TRANSLUCENT -> RenderType.entityTranslucent(texture);
            case ENTITY_TRANSLUCENT_CULL -> RenderType.entityTranslucentCull(texture);
            case ENTITY_TRANSLUCENT_EMISSIVE -> RenderType.entityTranslucentEmissive(texture);
        };
    }
}
