package com.platypushasnohat.sinew.mixins.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.platypushasnohat.sinew.supporter.SupporterCape;
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
        SupporterCape cape = SupporterCapesClient.getShownCape((AbstractClientPlayer) (Object) this);
        if (cape != SupporterCape.NONE) {
            result = new PlayerSkin(skin.texture(), skin.textureUrl(), cape.getTexture(), cape.getTexture(), skin.model(), skin.secure());
        }
        return result;
    }
}
