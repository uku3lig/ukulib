package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

/**
 * A simple button that does something when pressed.
 */
public class SimpleButton implements WidgetCreator {
    private final Text text;
    private final ButtonWidget.PressAction action;
    private final boolean active;

    /**
     * Creates a simple button.
     *
     * @param text   The text to be displayed
     * @param action The action to run when clicked
     * @param active Whether the button can be clicked
     */
    public SimpleButton(Text text, ButtonWidget.PressAction action, boolean active) {
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
    public SimpleButton(Text text, ButtonWidget.PressAction action) {
        this(text, action, true);
    }

    /**
     * Creates a simple translated button.
     *
     * @param key    The translation key of the text
     * @param action The action to run when clicked
     */
    public SimpleButton(String key, ButtonWidget.PressAction action) {
        this(Text.translatable(key), action);
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width, int height) {
        ButtonWidget widget = ButtonWidget.builder(text, action)
                .dimensions(x, y, width, height)
                .build();

        widget.active = this.active;
        return widget;
    }
}
