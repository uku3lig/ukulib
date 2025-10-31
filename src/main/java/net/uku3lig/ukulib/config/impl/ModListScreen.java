package net.uku3lig.ukulib.config.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
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

        entrypointList = new EntrypointList(this.minecraft, this.width, this.height - 64, 32, 36);
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
        this.addWidget(entrypointList);

        Minecraft mc = Minecraft.getInstance();
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> mc.setScreen(this.parent))
                .bounds(this.width / 2 - 155, this.height - 27, 150, 20)
                .build());
        this.addRenderableWidget(Button.builder(Component.translatable("ukulib.config.title"), button -> mc.setScreen(new UkulibConfigScreen(this)))
                .bounds(this.width / 2 + 5, this.height - 27, 150, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        entrypointList.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(font, title, width / 2, 20, 0xFFFFFFFF);
    }
}
