package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.option.widget.TextInputWidget;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An option to input text.
 */
public class InputOption implements WidgetCreator {
    private final String suggestionKey;
    private final String initialValue;
    private final Consumer<String> setter;
    private final Predicate<String> validator;
    private final int maxLength;

    /**
     * Constructor.
     *
     * @param suggestionKey the translation key for the suggestion
     * @param initialValue  the initial value
     * @param setter        the callback to set the value
     * @param validator     the validator
     * @param maxLength     the maximum length of the input
     */
    public InputOption(String suggestionKey, String initialValue, Consumer<String> setter, Predicate<String> validator, int maxLength) {
        this.suggestionKey = suggestionKey;
        this.initialValue = initialValue;
        this.setter = setter;
        this.validator = validator;
        this.maxLength = maxLength;
    }

    /**
     * Constructor. Equivalent to {@code InputOption(suggestionKey, initialValue, setter, validator, 1000)}.
     *
     * @param suggestionKey the translation key for the suggestion
     * @param initialValue  the initial value
     * @param setter        the callback to set the value
     * @param validator     the validator
     */
    public InputOption(String suggestionKey, String initialValue, Consumer<String> setter, Predicate<String> validator) {
        this(suggestionKey, initialValue, setter, validator, 1000);
    }

    /**
     * Constructor. Equivalent to {@code InputOption(suggestionKey, initialValue, setter, Objects::nonNull, 1000)}.
     *
     * @param suggestionKey the translation key for the suggestion
     * @param initialValue  the initial value
     * @param setter        the callback to set the value
     */
    public InputOption(String suggestionKey, String initialValue, Consumer<String> setter) {
        this(suggestionKey, initialValue, setter, Objects::nonNull);
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width, int height) {
        String suggestion = Text.translatable(suggestionKey).getString();

        return new TextInputWidget(x, y, width, height, initialValue, setter, suggestion, validator, maxLength);
    }
}
