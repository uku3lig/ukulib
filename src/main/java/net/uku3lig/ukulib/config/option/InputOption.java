package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.option.widget.TextInputWidget;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InputOption implements WidgetCreator {
    private final String suggestionKey;
    private final Consumer<String> setter;
    private final Predicate<String> validator;
    private final int maxLength;

    public InputOption(String suggestionKey, Consumer<String> setter, Predicate<String> validator, int maxLength) {
        this.suggestionKey = suggestionKey;
        this.setter = setter;
        this.validator = validator;
        this.maxLength = maxLength;
    }

    public InputOption(String suggestionKey, Consumer<String> setter, Predicate<String> validator) {
        this(suggestionKey, setter, validator, 1000);
    }

    public InputOption(String suggestionKey, Consumer<String> setter) {
        this(suggestionKey, setter, Objects::nonNull);
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width, int height) {
        String suggestion = Text.translatable(suggestionKey).getString();

        return new TextInputWidget(x, y, width, height, setter, suggestion, validator, maxLength);
    }
}
