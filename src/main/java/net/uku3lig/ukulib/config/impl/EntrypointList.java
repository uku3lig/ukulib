package net.uku3lig.ukulib.config.impl;

import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.uku3lig.ukulib.api.UkulibAPI;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A widget to display the list of mods that have integrated with Ukulib. <br>
 * Most the code is basically copied from {@link net.minecraft.client.gui.widget.ButtonListWidget} lol
 */
final class EntrypointList extends ElementListWidget<EntrypointList.ModEntry> {
    public EntrypointList(MinecraftClient minecraftClient, int x, int y, int width, int height, int m) {
        super(minecraftClient, x, y, width, height, m);
    }

    public void addAll(Collection<EntrypointContainer<UkulibAPI>> containers, Screen parent) {
        containers.stream().map(container -> new ModEntry(container, this.width, parent)).forEach(this::addEntry);
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    public final class ModEntry extends ElementListWidget.Entry<ModEntry> {
        private final ButtonWidget button;

        public ModEntry(EntrypointContainer<UkulibAPI> container, int width, Screen parent) {
            button = new ModButton(container, width, parent);
        }

        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(button);
        }

        @Override
        public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            button.y = y;
            button.render(mouseX, mouseY, tickDelta);
        }
    }

    /**
     * Simple button that has a cool tooltip :D
     * EDIT: 1.15 and lower is shit.
     */
    private final class ModButton extends ButtonWidget {
        public ModButton(EntrypointContainer<UkulibAPI> container, int width, Screen parent) {
            super(width / 2 - 100, 0, 200, 20, container.getProvider().getMetadata().getName(),
                    b -> minecraft.openScreen(container.getEntrypoint().supplyConfigScreen().apply(parent)));
        }
    }
}
