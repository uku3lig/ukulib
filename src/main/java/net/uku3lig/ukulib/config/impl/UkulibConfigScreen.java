package net.uku3lig.ukulib.config.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.api.UkulibProvider;
import net.uku3lig.ukulib.utils.Ukutils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Ukulib's config screen. Shows all the mods that have integrated with Ukulib.
 */
public final class UkulibConfigScreen extends GameOptionsScreen {
    private EntrypointList entrypointList;

    /**
     * Creates a config screen.
     *
     * @param parent The parent screen
     */
    public UkulibConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.of("Ukulib Config"));
    }

    @Override
    protected void init() {
        super.init();

        entrypointList = new EntrypointList(this.client, this.width, this.height, 32, this.height - 32, 25);
        Map<ModContainer, UnaryOperator<Screen>> containers = new HashMap<>();

        FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class)
                .forEach(e -> containers.put(e.getProvider(), e.getEntrypoint().supplyConfigScreen()));

        FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibProvider.class)
                .stream()
                .flatMap(e -> e.getEntrypoint().getProvidedConfigScreens().entrySet().stream())
                .forEach(e -> FabricLoader.getInstance().getModContainer(e.getKey())
                        .ifPresent(c -> containers.put(c, e.getValue())));

        entrypointList.addAll(containers, this);
        this.addSelectableChild(entrypointList);

        this.addDrawableChild(Ukutils.doneButton(this.width, this.height, this.parent));
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(drawContext);
        entrypointList.render(drawContext, mouseX, mouseY, delta);
        drawContext.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
        super.render(drawContext, mouseX, mouseY, delta);
    }
}
