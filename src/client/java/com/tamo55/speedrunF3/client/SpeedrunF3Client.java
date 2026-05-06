package com.tamo55.speedrunF3.client;

import com.tamo55.speedrunF3.client.hud.SpeedrunHud;
import com.tamo55.speedrunF3.config.SpeedrunConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

public class SpeedrunF3Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Yeni HUD'ı sisteme kaydediyoruz
        HudRenderCallback.EVENT.register(new SpeedrunHud());

        // Ayarları oyun içinden yönetebilmek için temel komutu ekliyoruz
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("speedrunf3")
                .then(ClientCommandManager.literal("reload").executes(context -> {
                    SpeedrunConfig.reload();
                    context.getSource().sendFeedback(Text.literal("§aSpeedrun-F3: Ayarlar yenilendi!"));
                    return 1;
                }))
                .then(ClientCommandManager.literal("reset").executes(context -> {
                    SpeedrunConfig.getInstance().reset();
                    context.getSource().sendFeedback(Text.literal("§aSpeedrun-F3: Varsayılana dönüldü!"));
                    return 1;
                }))
            );
        });
    }
}
