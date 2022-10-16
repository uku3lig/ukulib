package net.uku3lig.ukulib.config.impl;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.uku3lig.ukulib.api.UkulibAPI;

import java.util.List;

/**
 * Ukulib's config screen. Shows all the mods that have integrated with Ukulib.
 */
public final class UkulibConfigScreen extends Screen {
    private EntrypointList entrypointList;
    private final Screen parent;

    /**
     * Creates a config screen.
     * @param parent The parent screen
     */
    public UkulibConfigScreen(Screen parent) {
        super(new LiteralText("Ukulib Config"));
        this.parent = parent;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void init() {
        super.init();

        List<EntrypointContainer<UkulibAPI>> containers = FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class);
        entrypointList = new EntrypointList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
        entrypointList.addAll(containers, this);
        this.children.add(entrypointList);

        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, I18n.translate("gui.done"), button -> this.minecraft.openScreen(this.parent)));
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.entrypointList.render(mouseX, mouseY, delta);
        drawCenteredString(this.font, title.asFormattedString(), width / 2, 20, 0xFFFFFF);
        super.render(mouseX, mouseY, delta);
    }
}
