package net.uku3lig.ukulib.config.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.config.screen.CloseableScreen;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Ukulib's config screen. Shows all the mods that have integrated with Ukulib.
 */
public final class ModListScreen extends CloseableScreen {
    private EntrypointList entrypointList;

    /**
     * Creates a config screen.
     *
     * @param parent The parent screen
     */
    public ModListScreen(Screen parent) {
        super("Ukulib Config", parent);
    }

    @Override
    protected void init() {
        super.init();

        entrypointList = new EntrypointList(this.client, this.width, this.height, 32, this.height - 32, 36);
        Map<ModContainer, UnaryOperator<Screen>> containers = new LinkedHashMap<>();

        FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class)
                .forEach(entry -> {
                    if (entry.getEntrypoint().supplyConfigScreen() != null)
                        containers.put(entry.getProvider(), entry.getEntrypoint().supplyConfigScreen());

                    entry.getEntrypoint().getProvidedConfigScreens().forEach((modId, screen) -> {
                        if (screen != null) {
                            FabricLoader.getInstance().getModContainer(modId).ifPresent(c -> containers.put(c, screen));
                        }
                    });
                });

        entrypointList.addAll(containers, this);
        this.addSelectableChild(entrypointList);

        MinecraftClient mc = MinecraftClient.getInstance();
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> mc.setScreen(this.parent))
                .dimensions(this.width / 2 - 155, this.height - 27, 150, 20)
                .build());
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("ukulib.config.title"), button -> mc.setScreen(new UkulibConfigScreen(this)))
                .dimensions(this.width / 2 + 5, this.height - 27, 150, 20)
                .build());
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);
        entrypointList.render(drawContext, mouseX, mouseY, delta);
        drawContext.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
        super.render(drawContext, mouseX, mouseY, delta);
    }
}
