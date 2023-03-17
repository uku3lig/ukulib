package net.uku3lig.ukulib.config.option;

import lombok.AllArgsConstructor;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.TranslatableOption;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor
public class CyclingOption<T> implements ButtonCreator {
    public static final Function<Boolean, Text> BOOL_TO_TEXT = b -> Boolean.TRUE.equals(b) ? ScreenTexts.ON : ScreenTexts.OFF;

    private final String key;
    private final Collection<T> values;
    private final T initialValue;
    private final Consumer<T> setter;
    private final Function<T, Text> valueToText;
    private final SimpleOption.TooltipFactory<T> tooltipFactory;

    public CyclingOption(String key, Collection<T> values, T initialValue, Consumer<T> setter, Function<T, Text> valueToText) {
        this(key, values, initialValue, setter, valueToText, SimpleOption.emptyTooltip());
    }

    public static CyclingOption<Boolean> ofBoolean(String key, boolean initialValue, Consumer<Boolean> setter, SimpleOption.TooltipFactory<Boolean> tooltipFactory) {
        return new CyclingOption<>(key, List.of(Boolean.TRUE, Boolean.FALSE), initialValue, setter, BOOL_TO_TEXT, tooltipFactory);
    }

    public static CyclingOption<Boolean> ofBoolean(String key, boolean initialValue, Consumer<Boolean> setter) {
        return ofBoolean(key, initialValue, setter, SimpleOption.emptyTooltip());
    }

    public static <T extends Enum<T>> CyclingOption<T> ofEnum(
            String key, Class<T> klass, T initialValue,
            Consumer<T> setter, Function<T, Text> valueToText,
            SimpleOption.TooltipFactory<T> tooltipFactory
    ) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, valueToText, tooltipFactory);
    }

    public static <T extends Enum<T>> CyclingOption<T> ofEnum(
            String key, Class<T> klass, T initialValue,
            Consumer<T> setter, Function<T, Text> valueToText
    ) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, valueToText);
    }

    public static <T extends Enum<T> & TranslatableOption> CyclingOption<T> ofTranslatableEnum(
            String key, Class<T> klass, T initialValue,
            Consumer<T> setter, SimpleOption.TooltipFactory<T> tooltipFactory
    ) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, TranslatableOption::getText, tooltipFactory);
    }

    public static <T extends Enum<T> & TranslatableOption> CyclingOption<T> ofTranslatableEnum(String key, Class<T> klass, T initialValue, Consumer<T> setter) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, TranslatableOption::getText);
    }

    @Override
    public ClickableWidget createButton(int x, int y, int width, int height) {
        CyclingButtonWidget.Builder<T> builder = CyclingButtonWidget.builder(valueToText)
                .values(values)
                .tooltip(tooltipFactory)
                .initially(initialValue);

        return builder.build(x, y, width, height, Text.translatable(key), (button, value) -> setter.accept(value));
    }

    public static <T extends Enum<T>> Text enumValueToText(T value) {
        return Text.of(value.name());
    }
}
