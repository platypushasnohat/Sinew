package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.tags.SinewDamageTypeTags;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TamableAnimal.class)
public class TamableAnimalMixin {

    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean sinew$cancelLayeredDeathMessageTamed(GameRules instance, GameRules.Key<GameRules.BooleanValue> key) {
        TamableAnimal animal = (TamableAnimal) (Object) this;
        if (animal.getLastDamageSource() != null && animal.getLastDamageSource().is(SinewDamageTypeTags.IS_LAYERED)) {
            return false;
        }
        return animal.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES);
    }
}
