package net.uku3lig.ukulib.config.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
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
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

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

        this.layout.addHeader(this.title, this.textRenderer);

        entrypointList = this.layout.addBody(new EntrypointList(this.client, this.width, this.layout));
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

        DirectionalLayoutWidget footer = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        footer.add(ButtonWidget.builder(Text.translatable("ukulib.config.title"), button -> this.client.setScreen(new UkulibConfigScreen(this))).build());
        footer.add(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).build());

        this.layout.forEachChild(this::addDrawableChild);
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
        this.entrypointList.position(this.width, this.layout);
    }
}
