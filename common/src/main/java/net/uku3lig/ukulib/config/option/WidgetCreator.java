package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.components.AbstractWidget;

/**
 * An interface used to create clickable widgets
 */
@FunctionalInterface
public interface WidgetCreator {
    /**
     * Creates the widget.
     *
     * @param x      The x position
     * @param y      The y position
     * @param width  The widget width
     * @param height The widget height
     * @return The created widget
     */
    AbstractWidget createWidget(int x, int y, int width, int height);

    /**
     * Makes a widget wide.
     *
     * @return The wide widget
     */
    default WideWidgetCreator wide() {
        return new WideWidgetCreator(this);
    }
}
