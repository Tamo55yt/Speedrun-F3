package com.tamo55.speedrunF3;

import com.tamo55.speedrunF3.config.SpeedrunConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeedrunF3 implements ModInitializer {
    public static final String MOD_ID = "speedrun-f3";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Konfigürasyonu yükle ve dosyayı oluştur (yoksa)
        SpeedrunConfig.getInstance();
        LOGGER.info("Speedrun-F3 initialized!");
    }
}
