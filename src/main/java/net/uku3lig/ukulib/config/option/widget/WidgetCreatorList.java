package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import org.jetbrains.annotations.Nullable;

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
     * @param bottom          The bottom position of the list
     * @param itemHeight      The height of each widget, usually 20
     */
    public WidgetCreatorList(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.centerListVertically = false;
    }

    /**
     * Add two entries. If <code>other</code> is <code>null</code>, <code>first</code> will take the whole width.
     *
     * @param first The first widget creator
     * @param other The second widget creator; nullable
     */
    public void addEntries(WidgetCreator first, @Nullable WidgetCreator other) {
        this.addEntry(new ButtonEntry(this.getRowWidth(), first, other));
    }

    /**
     * Add an array of widget creators.
     *
     * @param options The widget creator
     * @see WidgetCreatorList#addEntries(WidgetCreator, WidgetCreator)
     */
    public void addAll(WidgetCreator[] options) {
        for (int i = 0; i < options.length; i += 2) {
            this.addEntries(options[i], i < options.length - 1 ? options[i + 1] : null);
        }
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 32;
    }

    /**
     * A widget entry, made of one or two creators.
     */
    public static class ButtonEntry extends ElementListWidget.Entry<ButtonEntry> {
        private final List<ClickableWidget> widgets;

        /**
         * Creates an entry.
         * @param width The width of the entry
         * @param first The first entry
         * @param other The second entry; nullable
         */
        public ButtonEntry(int width, WidgetCreator first, @Nullable WidgetCreator other) {
            ClickableWidget firstWidget = first.createWidget(width / 2 - 155, 0, 150, 20);
            this.widgets = other == null ? List.of(firstWidget) : List.of(firstWidget, other.createWidget(width / 2 + 5, 0, 150, 20));
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
        public void render(DrawableHelper drawableHelper, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            widgets.forEach(w -> {
                w.setY(y);
                w.render(drawableHelper, mouseX, mouseY, tickDelta);
            });
        }
    }
}
