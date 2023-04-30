package net.uku3lig.ukulib.config.impl;

import lombok.extern.slf4j.Slf4j;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.api.UkulibAPI;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

/**
 * A widget to display the list of mods that have integrated with ukulib.
 */
@Slf4j
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
        private static final Identifier UNKNOWN_ICON = new Identifier("ukulib", "unknown.png");

        private final ButtonWidget button;
        private final Identifier iconPath;
        private final int iconSize = 16 * MinecraftClient.getInstance().options.getGuiScale().getValue();

        public ModEntry(EntrypointContainer<UkulibAPI> container, int width, Screen parent) {
            button = ButtonWidget.builder(Text.of(container.getProvider().getMetadata().getName()),
                            b -> client.setScreen(container.getEntrypoint().supplyConfigScreen().apply(parent)))
                    .dimensions(width / 2 - 100, 0, 200, iconSize)
                    .tooltip(Tooltip.of(Text.of(container.getProvider().getMetadata().getDescription())))
                    .build();

            ModContainer mod = container.getProvider();
            ModMetadata metadata = mod.getMetadata();

            this.iconPath = metadata.getIconPath(iconSize)
                    .flatMap(mod::findPath)
                    .flatMap(path -> {
                        try (InputStream inputStream = Files.newInputStream(path)) {
                            NativeImage image = NativeImage.read(Objects.requireNonNull(inputStream));
                            return Optional.of(new NativeImageBackedTexture(image));
                        } catch (IOException e) {
                            log.warn("Failed to load icon from mod jar: {}", path, e);
                            return Optional.empty();
                        }
                    })
                    .map(tex -> {
                        Identifier identifier = new Identifier("ukulib", metadata.getId() + "_icon");
                        MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, tex);
                        return identifier;
                    })
                    .orElse(UNKNOWN_ICON);
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
        public void render(DrawContext drawContext, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            button.setY(y);
            button.render(drawContext, mouseX, mouseY, tickDelta);

            drawContext.drawTexture(this.iconPath, button.getX() - iconSize - 5, y, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }
    }
}
