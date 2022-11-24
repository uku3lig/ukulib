package net.uku3lig.ukulib.config.impl;

import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
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

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 32;
    }

    public final class ModEntry extends ElementListWidget.Entry<ModEntry> {
        private final ButtonWidget button;

        public ModEntry(EntrypointContainer<UkulibAPI> container, int width, Screen parent) {
            button = ButtonWidget.builder(Text.of(container.getProvider().getMetadata().getName()),
                            b -> client.setScreen(container.getEntrypoint().supplyConfigScreen().apply(parent)))
                    .dimensions(width / 2 - 100, 0, 200, 20)
                    .tooltip(Tooltip.of(Text.of(container.getProvider().getMetadata().getDescription())))
                    .build();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return Collections.singletonList(button);
        }

        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(button);
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            button.setY(y);
            button.render(matrices, mouseX, mouseY, tickDelta);
        }
    }
}
