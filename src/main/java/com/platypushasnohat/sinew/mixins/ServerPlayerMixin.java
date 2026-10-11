package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.tags.SinewDamageTypeTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean sinew$cancelLayeredDeathMessage(GameRules instance, GameRules.Key<GameRules.BooleanValue> key) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (player.getLastDamageSource() != null && player.getLastDamageSource().is(SinewDamageTypeTags.IS_LAYERED)) {
            return false;
        }
        return player.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES);
    }
}
