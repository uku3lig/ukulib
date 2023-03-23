package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

/**
 * A simple button that does something when pressed.
 */
public class SimpleButton implements ButtonCreator {
    private final Text text;
    private final ButtonWidget.PressAction action;

    /**
     * Creates a simple button.
     *
     * @param text   The text to be displayed
     * @param action The action to run when clicked
     */
    public SimpleButton(Text text, ButtonWidget.PressAction action) {
        this.text = text;
        this.action = action;
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
    public ClickableWidget createButton(int x, int y, int width, int height) {
        return ButtonWidget.builder(text, action)
                .dimensions(x, y, width, height)
                .build();
    }
}
