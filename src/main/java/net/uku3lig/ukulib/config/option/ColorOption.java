package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.option.widget.ColorInputWidget;

import java.util.function.IntConsumer;

/**
 * An option to select a color.
 */
public class ColorOption implements WidgetCreator {
    private final String suggestionKey;
    private final int initialValue;
    private final IntConsumer setter;
    private final boolean allowAlpha;

    /**
     * Constructor.
     *
     * @param suggestionKey the translation key for the suggestion
     * @param initialValue  the initial value
     * @param setter        the callback to set the value
     * @param allowAlpha    whether to allow changing the alpha value
     */
    public ColorOption(String suggestionKey, int initialValue, IntConsumer setter, boolean allowAlpha) {
        this.suggestionKey = suggestionKey;
        this.initialValue = initialValue;
        this.setter = setter;
        this.allowAlpha = allowAlpha;
    }

    /**
     * Constructor. Equivalent to {@code ColorOption(suggestionKey, initialValue, setter, false)}.
     *
     * @param suggestionKey the translation key for the suggestion
     * @param initialValue  the initial value
     * @param setter        the callback to set the value
     */
    public ColorOption(String suggestionKey, int initialValue, IntConsumer setter) {
        this(suggestionKey, initialValue, setter, false);
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width, int height) {
        String suggestion = Text.translatable(suggestionKey).getString();

        return new ColorInputWidget(x, y, width, height, initialValue, setter, suggestion, allowAlpha);
    }
}
