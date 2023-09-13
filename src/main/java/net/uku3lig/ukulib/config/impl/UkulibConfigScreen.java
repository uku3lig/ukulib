package net.uku3lig.ukulib.config.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.utils.Ukutils;

import java.util.List;

/**
 * Ukulib's config screen. Shows all the mods that have integrated with Ukulib.
 */
public final class UkulibConfigScreen extends GameOptionsScreen {
    private EntrypointList entrypointList;

    /**
     * Creates a config screen.
     * @param parent The parent screen
     */
    public UkulibConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.of("Ukulib Config"));
    }

    @Override
    protected void init() {
        super.init();

        List<EntrypointContainer<UkulibAPI>> containers = FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class);
        entrypointList = new EntrypointList(this.client, this.width, this.height, 32, this.height - 32, 25);
        entrypointList.addAll(containers, this);
        this.addSelectableChild(entrypointList);

        this.addDrawableChild(Ukutils.doneButton(this.width, this.height, this.parent));
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);

        entrypointList.render(drawContext, mouseX, mouseY, delta);
        drawContext.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
    }
}
