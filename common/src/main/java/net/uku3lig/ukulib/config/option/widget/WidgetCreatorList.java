package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
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
public class WidgetCreatorList extends ContainerObjectSelectionList<WidgetCreatorList.@NotNull ButtonEntry> {
    /**
     * Creates an empty widget list
     *
     * @param minecraftClient The Minecraft client instance
     * @param width           The width of the list
     * @param layout          The containing {@link HeaderAndFooterLayout}, used to compute the size
     */
    public WidgetCreatorList(Minecraft minecraftClient, int width, HeaderAndFooterLayout layout) {
        super(minecraftClient, width, layout.getContentHeight(), layout.getHeaderHeight(), 25);
    }

    /**
     * Add two entries. If <code>other</code> is <code>null</code>, <code>first</code> will *not* take up the full width.
     *
     * @param first The first widget creator
     * @param other The second widget creator; nullable
     */
    public void addEntries(WidgetCreator first, @Nullable WidgetCreator other) {
        this.addEntry(new ButtonEntry(first, other));
    }

    /**
     * Add a single, wide entry.
     *
     * @param creator The widget creator
     */
    public void addWideEntry(WidgetCreator creator) {
        this.addEntry(new ButtonEntry(creator));
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
        return 310;
    }

    /**
     * A widget entry, made of one or two creators.
     */
    public class ButtonEntry extends ContainerObjectSelectionList.Entry<@NotNull ButtonEntry> {
        private final List<AbstractWidget> widgets;

        /**
         * Creates an entry.
         *
         * @param first The first entry
         * @param other The second entry; nullable
         */
        public ButtonEntry(WidgetCreator first, @Nullable WidgetCreator other) {
            AbstractWidget firstWidget = first.createWidget(150, 20);
            this.widgets = other == null ? List.of(firstWidget) : List.of(firstWidget, other.createWidget(150, 20));
        }

        /**
         * Creates a single, wide entry.
         *
         * @param creator The entry
         */
        public ButtonEntry(WidgetCreator creator) {
            this.widgets = List.of(creator.createWidget(310, 20));
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
        public void renderContent(@NotNull GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int leftOffset = 0;
            int left = WidgetCreatorList.this.getWidth() / 2 - 155;

            for (var widget : this.widgets) {
                widget.setPosition(left + leftOffset, this.getContentY());
                widget.render(graphics, mouseX, mouseY, deltaTicks);
                leftOffset += 160;
            }
        }
    }
}
