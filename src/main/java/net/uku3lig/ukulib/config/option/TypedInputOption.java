package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.option.widget.TextInputWidget;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TypedInputOption<T> implements WidgetCreator {
    private final String suggestionKey;
    private final String initialValue;
    private final Consumer<T> setter;
    private final Function<String, Optional<T>> converter;
    private final Predicate<T> validator;
    private final int maxLength;

    /**
     * Constructor.
     *
     * @param suggestionKey the translation key for the suggestion
     * @param initialValue  the initial value
     * @param setter        the callback to set the value
     * @param converter     the converter
     * @param validator     the validator
     * @param maxLength     the maximum length of the input
     */
    public TypedInputOption(String suggestionKey, String initialValue, Consumer<T> setter, Function<String, Optional<T>> converter, Predicate<T> validator, int maxLength) {
        this.suggestionKey = suggestionKey;
        this.initialValue = initialValue;
        this.setter = setter;
        this.converter = converter;
        this.validator = validator;
        this.maxLength = maxLength;
    }

    /**
     * Constructor. Equivalent to {@code InputOption(suggestionKey, initialValue, setter, converter, validator, 1000)}.
     *
     * @param suggestionKey the translation key for the suggestion
     * @param initialValue  the initial value
     * @param setter        the callback to set the value
     * @param converter     the converter
     * @param validator     the validator
     */
    public TypedInputOption(String suggestionKey, String initialValue, Consumer<T> setter, Function<String, Optional<T>> converter, Predicate<T> validator) {
        this(suggestionKey, initialValue, setter, converter, validator, 1000);
    }

    /**
     * Constructor. Equivalent to {@code InputOption(suggestionKey, initialValue, setter, converter, Objects::nonNull, 1000)}.
     *
     * @param suggestionKey the translation key for the suggestion
     * @param initialValue  the initial value
     * @param setter        the callback to set the value
     * @param converter     the converter
     */
    public TypedInputOption(String suggestionKey, String initialValue, Consumer<T> setter, Function<String, Optional<T>> converter) {
        this(suggestionKey, initialValue, setter, converter, Objects::nonNull);
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width, int height) {
        String suggestion = Text.translatable(suggestionKey).getString();

        return new TextInputWidget(x, y, width, height, initialValue, s -> converter.apply(s).ifPresent(setter),
                suggestion, s -> converter.apply(s).map(validator::test).orElse(false), maxLength);
    }
}
