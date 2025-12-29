package net.uku3lig.ukulib.config.impl;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.uku3lig.ukulib.config.screen.CloseableScreen;
import net.uku3lig.ukulib.utils.PlatformUkutils;

/**
 * Ukulib's config screen. Shows all the mods that have integrated with Ukulib.
 */
public final class ModListScreen extends CloseableScreen {
    private EntrypointList entrypointList;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

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

        this.layout.addTitleHeader(this.title, this.font);

        entrypointList = this.layout.addToContents(new EntrypointList(this.minecraft, this.width, this.layout));
        entrypointList.addAll(PlatformUkutils.INSTANCE.getConfigMods(), this);

        LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footer.addChild(Button.builder(Component.translatable("ukulib.config.title"), _ -> this.minecraft.setScreen(new UkulibConfigScreen(this))).build());
        footer.addChild(Button.builder(CommonComponents.GUI_DONE, _ -> this.onClose()).build());

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        this.entrypointList.updateSize(this.width, this.layout);
    }
}
