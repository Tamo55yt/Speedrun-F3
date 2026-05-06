package com.tamo55.speedrunF3.client.hud;

import com.tamo55.speedrunF3.config.SpeedrunConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpeedrunHud implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        SpeedrunConfig config = SpeedrunConfig.getInstance();
        
        // HUD ancak ayar açıksa VE (DebugEnabled (F3) aktifse VEYA manuel enabled ise) görünür
        if (config.hudEnabled && !client.options.hudHidden && client.getDebugHud().shouldShowDebugHud()) {
            renderHud(client, drawContext, config);
        }
    }

    private void renderHud(MinecraftClient client, DrawContext drawContext, SpeedrunConfig config) {
        if (client.player == null || client.world == null) return;

        TextRenderer textRenderer = client.textRenderer;
        List<String> lines = gatherData(client, config);

        if (lines.isEmpty()) return;

        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().scale(config.hudScale, config.hudScale);

        int screenWidth = client.getWindow().getScaledWidth();
        int y = (int) (config.yOffset / config.hudScale);
        int lineHeight = textRenderer.fontHeight + 2;

        for (String line : lines) {
            int width = textRenderer.getWidth(line);
            int x;
            
            if ("right".equalsIgnoreCase(config.alignment)) {
                x = (int) ((screenWidth - config.xOffset) / config.hudScale) - width;
            } else {
                x = (int) (config.xOffset / config.hudScale);
            }

            if (config.showBackground) {
                drawContext.fill(x - 2, y - 1, x + width + 2, y + textRenderer.fontHeight, config.backgroundColor);
            }
            drawContext.drawText(textRenderer, line, x, y, config.textColor, config.textShadow);
            y += lineHeight;
        }

        drawContext.getMatrices().popMatrix();
    }

    private List<String> gatherData(MinecraftClient client, SpeedrunConfig config) {
        List<String> data = new ArrayList<>();
        if (client.player == null || client.world == null) return data;

        Entity player = client.player;
        BlockPos pos = player.getBlockPos();

        // 1. Entity (E) Sayısı
        if (config.showEntities) {
            try {
                String entityString = client.worldRenderer.getEntitiesDebugString();
                java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d+/\\d+)").matcher(entityString);
                if (matcher.find()) {
                    data.add("E: " + matcher.group(1));
                } else {
                    data.add("E: " + entityString.split(",")[0].replace("Entities: ", ""));
                }
            } catch (Exception e) {
                data.add("E: N/A");
            }
        }

        // 2. Facing (Yön ve Açılar)
        if (config.showFacing) {
            Direction direction = player.getHorizontalFacing();
            float yaw = net.minecraft.util.math.MathHelper.wrapDegrees(player.getYaw());
            float pitch = net.minecraft.util.math.MathHelper.wrapDegrees(player.getPitch());
            data.add(String.format(Locale.ROOT, "%s (%.1f / %.1f)", 
                    direction.asString().toLowerCase(Locale.ROOT), yaw, pitch));
        }

        // 3. XYZ Koordinatları
        if (config.showXYZ) {
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();
            data.add(String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", x, y, z));
            
            if (config.showPortalHelper) {
                if (client.world.getRegistryKey() == net.minecraft.world.World.NETHER) {
                    data.add(String.format(Locale.ROOT, " -> OW: %.0f, %.0f", x * 8, z * 8));
                } else if (client.world.getRegistryKey() == net.minecraft.world.World.OVERWORLD) {
                    data.add(String.format(Locale.ROOT, " -> N: %.0f, %.0f", x / 8, z / 8));
                }
            }
        }

        // 4. Biome
        if (config.showBiome) {
            String biomeId = client.world.getBiome(pos).getKey().map(key -> key.getValue().getPath()).orElse("unknown");
            String biomeFormatted = java.util.Arrays.stream(biomeId.split("_"))
                    .map(s -> s.isEmpty() ? "" : s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1))
                    .reduce((a, b) -> a + " " + b)
                    .orElse(biomeId);
            data.add(String.format("Biome: %s", biomeFormatted));
        }

        // 5. Chunk (c) Verisi
        if (config.showChunk) {
            int cx = pos.getX() & 15;
            int cy = pos.getY() & 15;
            int cz = pos.getZ() & 15;
            int chunkX = pos.getX() >> 4;
            int chunkZ = pos.getZ() >> 4;
            data.add(String.format("Chunk: %d %d %d (%d %d)", cx, cy, cz, chunkX, chunkZ));
        }

        return data;
    }
}
