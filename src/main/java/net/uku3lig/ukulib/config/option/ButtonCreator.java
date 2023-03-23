package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ClickableWidget;

/**
 * An interface used to create clickable widgets
 */
@FunctionalInterface
public interface ButtonCreator {
    /**
     * Creates the widget.
     *
     * @param x      The x position
     * @param y      The y position
     * @param width  The widget width
     * @param height The widget height
     * @return The created widget
     */
    ClickableWidget createButton(int x, int y, int width, int height);
}
