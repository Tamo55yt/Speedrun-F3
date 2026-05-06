package com.tamo55.speedrunF3.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SpeedrunConfig {
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "speedrun-f3.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public float hudScale = 0.9f;
    public int xOffset = 5;
    public int yOffset = 5;
    public int backgroundColor = 0x70000000; // Biraz daha şeffaf
    public int textColor = 0xFFFFFFFF;
    public boolean textShadow = true;
    public String alignment = "left"; // left veya right
    public boolean showBackground = true;
    public boolean hudEnabled = true; // Keybind ile kontrol edilecek
    public boolean showPortalHelper = true;
    public boolean showBiome = true;
    public boolean showEntities = true;
    public boolean showFacing = true;
    public boolean showXYZ = true;
    public boolean showChunk = true;

    private static SpeedrunConfig instance;

    public static SpeedrunConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static void reload() {
        instance = load();
    }

    public static SpeedrunConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, SpeedrunConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SpeedrunConfig config = new SpeedrunConfig();
        config.save();
        return config;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        this.hudScale = 0.9f;
        this.xOffset = 5;
        this.yOffset = 5;
        this.backgroundColor = 0x90000000;
        this.textColor = 0xFFFFFFFF;
        this.showPortalHelper = true;
        this.showBiome = true;
        this.showEntities = true;
        this.showFacing = true;
        this.showXYZ = true;
        this.showChunk = true;
        save();
    }
}
