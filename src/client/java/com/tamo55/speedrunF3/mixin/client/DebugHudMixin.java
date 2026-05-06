package com.tamo55.speedrunF3.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(DrawContext drawContext, CallbackInfo ci) {
        // Vanilla F3 menüsünün çizilmesini tamamen engelliyoruz.
        // Kendi HUD'ımız SpeedrunHud sınıfında çiziliyor.
        ci.cancel();
    }
}
