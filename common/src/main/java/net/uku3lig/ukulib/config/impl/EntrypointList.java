package net.uku3lig.ukulib.config.impl;

import com.mojang.blaze3d.platform.NativeImage;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.uku3lig.ukulib.utils.ModMeta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;

/**
 * A widget to display the list of mods that have integrated with ukulib.
 */
@Slf4j
final class EntrypointList extends ContainerObjectSelectionList<EntrypointList.@NotNull ModEntry> {
    public EntrypointList(Minecraft client, int width, HeaderAndFooterLayout layout) {
        super(client, width, layout.getContentHeight(), layout.getHeaderHeight(), 36);
        this.centerListVertically = false;
    }

    public void addAll(Map<ModMeta, UnaryOperator<Screen>> containers, Screen parent) {
        containers.entrySet().stream()
                .map(entry -> new ModEntry(entry.getKey(), entry.getValue(), this.width, parent))
                .forEach(this::addEntry);
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int scrollBarX() {
        return super.scrollBarX() + 32;
    }

    public final class ModEntry extends ContainerObjectSelectionList.Entry<@NotNull ModEntry> {
        private static final Identifier UNKNOWN_ICON = Identifier.fromNamespaceAndPath("ukulib", "unknown.png");
        private static final int ICON_SIZE = 32;

        private final Button button;
        private final Identifier iconPath;

        public ModEntry(ModMeta mod, UnaryOperator<Screen> operator, int width, Screen parent) {
            button = Button.builder(Component.literal(mod.name()), _ -> minecraft.setScreen(operator.apply(parent)))
                    .bounds(width / 2 - 100, 0, 200, ICON_SIZE)
                    .tooltip(Tooltip.create(Component.literal(mod.description())))
                    .build();

            Identifier identifier = Identifier.fromNamespaceAndPath("ukulib", mod.id() + "_icon");

            this.iconPath = mod.icon()
                    .flatMap(icon -> {
                        try {
                            NativeImage image = NativeImage.read(Objects.requireNonNull(icon.get()));
                            return Optional.of(new DynamicTexture(identifier::toString, image));
                        } catch (IOException e) {
                            log.warn("Failed to load icon from mod jar", e);
                            return Optional.empty();
                        }
                    })
                    .map(tex -> {
                        Minecraft.getInstance().getTextureManager().register(identifier, tex);
                        return identifier;
                    })
                    .orElse(UNKNOWN_ICON);
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return Collections.singletonList(button);
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return Collections.singletonList(button);
        }

        @Override
        public void renderContent(@NotNull GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int x = (EntrypointList.this.getWidth() - this.button.getWidth()) / 2;
            button.setPosition(x, this.getContentY());
            button.render(graphics, mouseX, mouseY, deltaTicks);

            graphics.blit(RenderPipelines.GUI_TEXTURED, this.iconPath, button.getX() - ICON_SIZE - 5, this.getContentY(), 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        }
    }
}
