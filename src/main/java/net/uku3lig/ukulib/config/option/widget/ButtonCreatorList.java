package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.uku3lig.ukulib.config.option.ButtonCreator;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A list of {@link ButtonCreator}, used to construct a vertical list of widgets
 */
public class ButtonCreatorList extends ElementListWidget<ButtonCreatorList.ButtonEntry> {
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
    public ButtonCreatorList(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.centerListVertically = false;
    }

    /**
     * Add two entries. If <code>other</code> is <code>null</code>, <code>first</code> will take the whole width.
     *
     * @param first The first widget creator
     * @param other The second widget creator; nullable
     */
    public void addEntries(ButtonCreator first, @Nullable ButtonCreator other) {
        this.addEntry(new ButtonEntry(this.getRowWidth(), first, other));
    }

    /**
     * Add an array of widget creators.
     *
     * @param options The widget creator
     * @see ButtonCreatorList#addEntries(ButtonCreator, ButtonCreator)
     */
    public void addAll(ButtonCreator[] options) {
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
        public ButtonEntry(int width, ButtonCreator first, @Nullable ButtonCreator other) {
            ClickableWidget firstButton = first.createButton(width / 2 - 155, 0, 150, 20);
            this.widgets = other == null ? List.of(firstButton) : List.of(firstButton, other.createButton(width / 2 + 5, 0, 150, 20));
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
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            widgets.forEach(w -> {
                w.setY(y);
                w.render(matrices, mouseX, mouseY, tickDelta);
            });
        }
    }
}
