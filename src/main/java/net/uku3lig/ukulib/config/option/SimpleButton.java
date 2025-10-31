package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * A simple button that does something when pressed.
 */
public class SimpleButton implements WidgetCreator {
    private final Component text;
    private final Button.OnPress action;
    private final boolean active;

    /**
     * Creates a simple button.
     *
     * @param text   The text to be displayed
     * @param action The action to run when clicked
     * @param active Whether the button can be clicked
     */
    public SimpleButton(Component text, Button.OnPress action, boolean active) {
        this.text = text;
        this.action = action;
        this.active = active;
    }

    /**
     * Creates a simple button.
     *
     * @param text   The text to be displayed
     * @param action The action to run when clicked
     */
    public SimpleButton(Component text, Button.OnPress action) {
        this(text, action, true);
    }

    /**
     * Creates a simple translated button.
     *
     * @param key    The translation key of the text
     * @param action The action to run when clicked
     */
    public SimpleButton(String key, Button.OnPress action) {
        this(Component.translatable(key), action);
    }

    @Override
    public AbstractWidget createWidget(int x, int y, int width, int height) {
        Button widget = Button.builder(text, action)
                .bounds(x, y, width, height)
                .build();

        widget.active = this.active;
        return widget;
    }
}
