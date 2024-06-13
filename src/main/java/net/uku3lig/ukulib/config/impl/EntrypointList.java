package net.uku3lig.ukulib.config.impl;

import lombok.extern.slf4j.Slf4j;
import net.fabricmc.loader.api.ModContainer;
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.function.UnaryOperator;

/**
 * A widget to display the list of mods that have integrated with ukulib.
 */
@Slf4j
final class EntrypointList extends ElementListWidget<EntrypointList.ModEntry> {
    public EntrypointList(MinecraftClient minecraftClient, int width, int height, int top, int itemHeight) {
        super(minecraftClient, width, height, top, itemHeight);
    }

    public void addAll(Map<ModContainer, UnaryOperator<Screen>> containers, Screen parent) {
        containers.entrySet().stream()
                .map(entry -> new ModEntry(entry.getKey(), entry.getValue(), this.width, parent))
                .forEach(this::addEntry);
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int getScrollbarX() {
        return super.getScrollbarX() + 32;
    }

    public final class ModEntry extends ElementListWidget.Entry<ModEntry> {
        private static final Identifier UNKNOWN_ICON = Identifier.of("ukulib", "unknown.png");
        private static final int ICON_SIZE = 32;

        private final ButtonWidget button;
        private final Identifier iconPath;

        public ModEntry(ModContainer mod, UnaryOperator<Screen> operator, int width, Screen parent) {
            button = ButtonWidget.builder(Text.of(mod.getMetadata().getName()), b -> client.setScreen(operator.apply(parent)))
                    .dimensions(width / 2 - 100, 0, 200, ICON_SIZE)
                    .tooltip(Tooltip.of(Text.of(mod.getMetadata().getDescription())))
                    .build();

            ModMetadata metadata = mod.getMetadata();

            this.iconPath = metadata.getIconPath(ICON_SIZE)
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
                        Identifier identifier = Identifier.of("ukulib", metadata.getId() + "_icon");
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

            drawContext.drawTexture(this.iconPath, button.getX() - ICON_SIZE - 5, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        }
    }
}
