package com.platypushasnohat.sinew.mixins.client;

import com.platypushasnohat.sinew.supporter.SupporterCapesClient;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkinCustomizationScreen.class)
public abstract class SkinCustomizationScreenMixin extends OptionsSubScreen {

    private SkinCustomizationScreenMixin(Screen lastScreen, Options options, Component title) {
        super(lastScreen, options, title);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    private void sinew$addSupporterCapeButton(CallbackInfo ci) {
        if (this.list != null && SupporterCapesClient.isLocalSupporter()) {
            this.list.addSmall(CycleButton.onOffBuilder(SupporterCapesClient.isEnabled()).create(Component.translatable("options.sinew.supporterCape"), (button, value) -> SupporterCapesClient.setEnabled(value)), null);
        }
    }
}
