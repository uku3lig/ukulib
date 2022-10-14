package net.uku3lig.ukulib.config.impl;

import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.OrderableTooltip;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.api.UkulibAPI;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public Optional<ClickableWidget> getHoveredButton(double mouseX, double mouseY) {
        for (ModEntry entry : this.children()) {
            if (entry.button.isMouseOver(mouseX, mouseY)) {
                return Optional.of(entry.button);
            }
        }

        return Optional.empty();
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
            button = new ModButton(container, width, parent);
        }

        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(button);
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            button.y = y;
            button.render(matrices, mouseX, mouseY, tickDelta);
        }
    }

    /**
     * Simple button that has a cool tooltip :D
     */
    private final class ModButton extends ButtonWidget implements OrderableTooltip {
        private final String tooltip;

        public ModButton(EntrypointContainer<UkulibAPI> container, int width, Screen parent) {
            super(width / 2 - 100, 0, 200, 20, Text.of(container.getProvider().getMetadata().getName()),
                    b -> client.openScreen(container.getEntrypoint().supplyConfigScreen().apply(parent)));
            this.tooltip = container.getProvider().getMetadata().getDescription();
        }

        @Override
        public Optional<List<OrderedText>> getOrderedTooltip() {
            return Optional.ofNullable(client.textRenderer.wrapLines(Text.of(tooltip), 200));
        }
    }
}
