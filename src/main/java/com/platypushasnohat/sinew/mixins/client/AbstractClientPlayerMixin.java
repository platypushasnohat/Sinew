package com.platypushasnohat.sinew.mixins.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.platypushasnohat.sinew.supporter.SupporterCapes;
import com.platypushasnohat.sinew.supporter.SupporterCapesClient;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @ModifyReturnValue(method = "getSkin", at = @At("RETURN"))
    private PlayerSkin sinew$supporterCape(PlayerSkin skin) {
        PlayerSkin result = skin;
        if (SupporterCapesClient.shouldShowCape((AbstractClientPlayer) (Object) this)) {
            result = new PlayerSkin(skin.texture(), skin.textureUrl(), SupporterCapes.CAPE_TEXTURE, SupporterCapes.CAPE_TEXTURE, skin.model(), skin.secure());
        }
        return result;
    }
}
