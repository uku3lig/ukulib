package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.uku3lig.ukulib.config.option.WideWidgetCreator;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A list of {@link WidgetCreator}, used to construct a vertical list of widgets
 */
public class WidgetCreatorList extends ContainerObjectSelectionList<WidgetCreatorList.ButtonEntry> {
    /**
     * Creates an empty widget list
     *
     * @param minecraftClient The Minecraft client instance
     * @param width           The width of the list
     * @param height          The height of the list
     * @param top             The top position of the list
     * @param bottom          UNUSED PARAMETER, USE OTHER CONSTRUCTOR
     * @param itemHeight      The height of each widget, usually 20
     * @see WidgetCreatorList#WidgetCreatorList(Minecraft, int, int, int, int)
     * @deprecated Mojang (rightfully) removed the bottom in 1.20.3, use {@link WidgetCreatorList#WidgetCreatorList(Minecraft, int, int, int, int)} instead
     */
    @Deprecated(since = "1.1.0", forRemoval = true)
    public WidgetCreatorList(Minecraft minecraftClient, int width, int height, int top, @SuppressWarnings("unused") int bottom, int itemHeight) {
        this(minecraftClient, width, height, top, itemHeight);
    }

    /**
     * Creates an empty widget list
     *
     * @param minecraftClient The Minecraft client instance
     * @param width           The width of the list
     * @param height          The height of the list
     * @param top             The top position of the list
     * @param itemHeight      The height of each widget, usually 20
     */
    public WidgetCreatorList(Minecraft minecraftClient, int width, int height, int top, int itemHeight) {
        super(minecraftClient, width, height, top, itemHeight);
        this.centerListVertically = false;
    }

    /**
     * Add two entries. If <code>other</code> is <code>null</code>, <code>first</code> will *not* take up the full width.
     *
     * @param first The first widget creator
     * @param other The second widget creator; nullable
     */
    public void addEntries(WidgetCreator first, @Nullable WidgetCreator other) {
        this.addEntry(new ButtonEntry(this.width, first, other));
    }

    /**
     * Add a single, wide entry.
     *
     * @param creator The widget creator
     */
    public void addWideEntry(WidgetCreator creator) {
        this.addEntry(new ButtonEntry(this.width, creator));
    }

    /**
     * Add an array of widget creators.
     *
     * @param options The widget creator
     * @see WidgetCreatorList#addEntries(WidgetCreator, WidgetCreator)
     */
    public void addAll(WidgetCreator[] options) {
        Iterator<WidgetCreator> iterator = Arrays.stream(options).iterator();

        while (iterator.hasNext()) {
            WidgetCreator first = iterator.next();
            if (first instanceof WideWidgetCreator wide) {
                this.addWideEntry(wide);
            } else {
                WidgetCreator second = iterator.hasNext() ? iterator.next() : null;
                if (second instanceof WideWidgetCreator wide) {
                    this.addEntries(first, null);
                    this.addWideEntry(wide);
                } else {
                    this.addEntries(first, second);
                }
            }
        }
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int scrollBarX() {
        return super.scrollBarX() + 32;
    }

    /**
     * A widget entry, made of one or two creators.
     */
    public static class ButtonEntry extends ContainerObjectSelectionList.Entry<ButtonEntry> {
        private final List<AbstractWidget> widgets;

        /**
         * Creates an entry.
         *
         * @param width The width of the entry
         * @param first The first entry
         * @param other The second entry; nullable
         */
        public ButtonEntry(int width, WidgetCreator first, @Nullable WidgetCreator other) {
            AbstractWidget firstWidget = first.createWidget(width / 2 - 155, 0, 150, 20);
            this.widgets = other == null ? List.of(firstWidget) : List.of(firstWidget, other.createWidget(width / 2 + 5, 0, 150, 20));
        }

        /**
         * Creates a single, wide entry.
         *
         * @param width   The width of the entry
         * @param creator The entry
         */
        public ButtonEntry(int width, WidgetCreator creator) {
            this.widgets = List.of(creator.createWidget(width / 2 - 155, 0, 310, 20));
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return widgets;
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return widgets;
        }


        @Override
        public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            widgets.forEach(w -> {
                w.setY(this.getContentY());
                w.render(graphics, mouseX, mouseY, deltaTicks);
            });
        }
    }
}
