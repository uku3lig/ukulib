package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.uku3lig.ukulib.config.option.WideWidgetCreator;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A list of {@link WidgetCreator}, used to construct a vertical list of widgets
 */
public class WidgetCreatorList extends ElementListWidget<WidgetCreatorList.ButtonEntry> {
    /**
     * Creates an empty widget list
     *
     * @param minecraftClient The Minecraft client instance
     * @param width           The width of the list
     * @param height          The height of the list
     * @param top             The top position of the list
     * @param bottom          UNUSED PARAMETER, USE OTHER CONSTRUCTOR
     * @param itemHeight      The height of each widget, usually 20
     * @see WidgetCreatorList#WidgetCreatorList(MinecraftClient, int, int, int, int)
     * @deprecated Mojang (rightfully) removed the bottom in 1.20.3, use {@link WidgetCreatorList#WidgetCreatorList(MinecraftClient, int, int, int, int)} instead
     */
    @Deprecated(since = "1.1.0", forRemoval = true)
    public WidgetCreatorList(MinecraftClient minecraftClient, int width, int height, int top, @SuppressWarnings("unused") int bottom, int itemHeight) {
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
     * @see WidgetCreatorList#WidgetCreatorList(MinecraftClient, int, ThreePartsLayoutWidget)
     * @deprecated You should be wrapping the widget in the body of a {@link ThreePartsLayoutWidget}
     */
    @Deprecated(since = "1.10.0", forRemoval = true)
    public WidgetCreatorList(MinecraftClient minecraftClient, int width, int height, int top, int itemHeight) {
        super(minecraftClient, width, height, top, itemHeight);
        this.centerListVertically = false;
    }

    /**
     * Creates an empty widget list
     *
     * @param minecraftClient The Minecraft client instance
     * @param width           The width of the list
     * @param layout          The containing {@link ThreePartsLayoutWidget}, used to compute the size
     */
    public WidgetCreatorList(MinecraftClient minecraftClient, int width, ThreePartsLayoutWidget layout) {
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
    public class ButtonEntry extends ElementListWidget.Entry<ButtonEntry> {
        private final List<ClickableWidget> widgets;

        /**
         * Creates an entry.
         *
         * @param first The first entry
         * @param other The second entry; nullable
         */
        public ButtonEntry(WidgetCreator first, @Nullable WidgetCreator other) {
            ClickableWidget firstWidget = first.createWidget(150, 20);
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

        /**
         * Creates an entry.
         *
         * @param width The width of the entry
         * @param first The first entry
         * @param other The second entry; nullable
         * @see ButtonEntry#ButtonEntry(WidgetCreator, WidgetCreator)
         * @deprecated The width is now computed automatically
         */
        @Deprecated(since = "1.10.0", forRemoval = true)
        public ButtonEntry(@SuppressWarnings("unused") int width, WidgetCreator first, @Nullable WidgetCreator other) {
            this(first, other);
        }

        /**
         * Creates a single, wide entry.
         *
         * @param width   The width of the entry
         * @param creator The entry
         * @see ButtonEntry#ButtonEntry(WidgetCreator)
         * @deprecated The width is now computed automatically
         */
        @Deprecated(since = "1.10.0", forRemoval = true)
        public ButtonEntry(@SuppressWarnings("unused") int width, WidgetCreator creator) {
            this(creator);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return widgets;
        }

        @Override
        public List<? extends Element> children() {
            return widgets;
        }


        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int leftOffset = 0;
            int left = WidgetCreatorList.this.getWidth() / 2 - 155;

            for (var widget : this.widgets) {
                widget.setPosition(left + leftOffset, this.getContentY());
                widget.render(context, mouseX, mouseY, deltaTicks);
                leftOffset += 160;
            }
        }
    }
}
