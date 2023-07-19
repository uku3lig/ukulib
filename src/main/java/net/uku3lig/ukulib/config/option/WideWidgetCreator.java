package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ClickableWidget;

/**
 * Used to indicate that the created widget should be *wide*.
 */
public class WideWidgetCreator implements WidgetCreator {
    private final WidgetCreator creator;

    public WideWidgetCreator(WidgetCreator creator) {
        this.creator = creator;
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width, int height) {
        return creator.createWidget(x, y, width, height);
    }
}
