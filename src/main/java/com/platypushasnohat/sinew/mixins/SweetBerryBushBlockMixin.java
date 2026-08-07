package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.tags.SinewEntityTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SweetBerryBushBlock.class)
public class SweetBerryBushBlockMixin {

    @Redirect(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getType()Lnet/minecraft/world/entity/EntityType;"))
    private EntityType<?> sinew$redirectBerryBushImmunityCheck(Entity entity) {
        // If our tag says the entity is immune, return FOX so the original code sees it as immune.
        if (entity.getType().is(SinewEntityTags.SWEET_BERRY_BUSH_IMMUNE)) {
            return EntityType.FOX;
        }
        return entity.getType();
    }
}

