package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.config.SinewConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(EndDragonFight.class)
public class EndDragonFightMixin {

    @Shadow
    @Nullable
    private UUID dragonUUID;

    @Shadow
    private boolean previouslyKilled;

    @Inject(at = @At("HEAD"), method = "setDragonKilled")
    public void sinew$setDragonKilled(EnderDragon dragon, CallbackInfo ci) {
        if (dragon.getUUID().equals(this.dragonUUID) && !this.previouslyKilled) {
            if (SinewConfig.SEND_PROGRESSION_MESSAGE.get()) {
                Sinew.LOGGER.info("End progression has been enabled");
                if (!dragon.level().isClientSide && dragon.getServer() != null) {
                    for (ServerPlayer player : dragon.getServer().getPlayerList().getPlayers()) {
                        MutableComponent message = Component.translatable("sinew.end_progression.enabled").withStyle(ChatFormatting.LIGHT_PURPLE);
                        player.sendSystemMessage(message);
                    }
                }
            }
        }
    }
}
