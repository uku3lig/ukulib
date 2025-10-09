package net.uku3lig.ukulib.config.option;

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

/**
 * An option type based on a finite amount of cycling values of the same type.
 *
 * @param <T> The type of the value
 */
public class CyclingOption<T> implements WidgetCreator {
    /**
     * Boolean to text converter.
     */
    public static final Function<Boolean, Text> BOOL_TO_TEXT = b -> Boolean.TRUE.equals(b) ? ScreenTexts.ON : ScreenTexts.OFF;

    private final String key;
    private final Collection<T> values;
    private final T initialValue;
    private final Consumer<T> setter;
    private final Function<T, Text> valueToText;
    private final SimpleOption.TooltipFactory<T> tooltipFactory;
    private final boolean active;

    /**
     * Constructs a new generic cycling option.
     *
     * @param key            The translation key
     * @param values         The possible option values
     * @param initialValue   The initial displayed value
     * @param setter         The callback for the modified value
     * @param valueToText    The function that converts the value to human-readable text
     * @param tooltipFactory The tooltip factory
     * @param active         Whether the button can be clicked
     */
    public CyclingOption(String key, Collection<T> values, T initialValue, Consumer<T> setter, Function<T, Text> valueToText, SimpleOption.TooltipFactory<T> tooltipFactory, boolean active) {
        this.key = key;
        this.values = values;
        this.initialValue = initialValue;
        this.setter = setter;
        this.valueToText = valueToText;
        this.tooltipFactory = tooltipFactory;
        this.active = active;
    }

    /**
     * Constructs a new generic cycling option.
     *
     * @param key            The translation key
     * @param values         The possible option values
     * @param initialValue   The initial displayed value
     * @param setter         The callback for the modified value
     * @param valueToText    The function that converts the value to human-readable text
     * @param tooltipFactory The tooltip factory
     */
    public CyclingOption(String key, Collection<T> values, T initialValue, Consumer<T> setter, Function<T, Text> valueToText, SimpleOption.TooltipFactory<T> tooltipFactory) {
        this(key, values, initialValue, setter, valueToText, tooltipFactory, true);
    }

    /**
     * Constructs a new generic cycling option, with no tooltip.
     *
     * @param key          The translation key
     * @param values       The possible option values
     * @param initialValue The initial displayed value
     * @param setter       The callback for the modified value
     * @param valueToText  The function that converts the value to human-readable text
     * @see CyclingOption#CyclingOption(String, Collection, Object, Consumer, Function, SimpleOption.TooltipFactory)
     */
    public CyclingOption(String key, Collection<T> values, T initialValue, Consumer<T> setter, Function<T, Text> valueToText) {
        this(key, values, initialValue, setter, valueToText, SimpleOption.emptyTooltip());
    }

    /**
     * Creates a new boolean option.
     *
     * @param key            The translation key
     * @param initialValue   The initial boolean value
     * @param setter         The callback for the modified value
     * @param tooltipFactory The tooltip factory
     * @return The newly constructed option
     */
    public static CyclingOption<Boolean> ofBoolean(String key, boolean initialValue, Consumer<Boolean> setter, SimpleOption.TooltipFactory<Boolean> tooltipFactory) {
        return new CyclingOption<>(key, List.of(Boolean.TRUE, Boolean.FALSE), initialValue, setter, BOOL_TO_TEXT, tooltipFactory);
    }

    /**
     * Creates a new boolean option, with an empty tooltip.
     *
     * @param key          The translation key
     * @param initialValue The initial boolean value
     * @param setter       The callback for the modified value
     * @return The newly constructed option
     * @see CyclingOption#ofBoolean(String, boolean, Consumer, SimpleOption.TooltipFactory)
     */
    public static CyclingOption<Boolean> ofBoolean(String key, boolean initialValue, Consumer<Boolean> setter) {
        return ofBoolean(key, initialValue, setter, SimpleOption.emptyTooltip());
    }

    /**
     * Creates a new option from a {@link TranslatableOption}.
     *
     * @param key            The translation key
     * @param values         The values to be displayed
     * @param initialValue   The initial value
     * @param setter         The callback for the modified value
     * @param tooltipFactory The tooltip factory
     * @param <T>            The type of the option
     * @return The newly constructed option
     */
    public static <T extends TranslatableOption> CyclingOption<T> ofTranslatable(
            String key, Collection<T> values, T initialValue,
            Consumer<T> setter, SimpleOption.TooltipFactory<T> tooltipFactory
    ) {
        return new CyclingOption<>(key, values, initialValue, setter, TranslatableOption::getText, tooltipFactory);
    }

    /**
     * Creates a new option from a {@link TranslatableOption}, with an empty tooltip.
     *
     * @param key          The translation key
     * @param values       The values to be displayed
     * @param initialValue The initial value
     * @param setter       The callback for the modified value
     * @param <T>          The type of the option
     * @return The newly constructed option
     */
    public static <T extends TranslatableOption> CyclingOption<T> ofTranslatable(String key, Collection<T> values, T initialValue, Consumer<T> setter) {
        return ofTranslatable(key, values, initialValue, setter, SimpleOption.emptyTooltip());
    }

    /**
     * Creates a new option based on an enum's values.
     *
     * @param key            The translation key
     * @param klass          The class type of the enum
     * @param initialValue   The initial value
     * @param setter         The callback for the modified value
     * @param valueToText    The function that converts the value to human-readable text
     * @param tooltipFactory The tooltip factory
     * @param <T>            The type of the enum
     * @return The newly created option
     */
    public static <T extends Enum<T>> CyclingOption<T> ofEnum(
            String key, Class<T> klass, T initialValue,
            Consumer<T> setter, Function<T, Text> valueToText,
            SimpleOption.TooltipFactory<T> tooltipFactory
    ) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, valueToText, tooltipFactory);
    }

    /**
     * Creates a new option based on an enum's values, with an empty tooltip.
     *
     * @param key          The translation key
     * @param klass        The class type of the enum
     * @param initialValue The initial value
     * @param setter       The callback for the modified value
     * @param valueToText  The function that converts the value to human-readable text
     * @param <T>          The type of the enum
     * @return The newly created option
     * @see CyclingOption#ofEnum(String, Class, Enum, Consumer, Function, SimpleOption.TooltipFactory)
     */
    public static <T extends Enum<T>> CyclingOption<T> ofEnum(
            String key, Class<T> klass, T initialValue,
            Consumer<T> setter, Function<T, Text> valueToText
    ) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, valueToText);
    }

    /**
     * Creates a new option based on an enum's values, with an empty tooltip.
     * The value text is the constant's name.
     *
     * @param key          The translation key
     * @param klass        The class type of the enum
     * @param initialValue The initial value
     * @param setter       The callback for the modified value
     * @param <T>          The type of the enum
     * @return The newly created option
     * @see CyclingOption#ofEnum(String, Class, Enum, Consumer, Function, SimpleOption.TooltipFactory)
     * @see CyclingOption#enumNameToText(Enum)
     */
    public static <T extends Enum<T>> CyclingOption<T> ofEnum(String key, Class<T> klass, T initialValue, Consumer<T> setter) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, CyclingOption::enumNameToText);
    }

    /**
     * Creates a new option based on a {@link TranslatableOption translatable} enum's values.
     *
     * @param key            The translation key
     * @param klass          The class type of the enum
     * @param initialValue   The initial value
     * @param setter         The callback for the modified value
     * @param tooltipFactory The tooltip factory
     * @param <T>            The type of the enum
     * @return The newly created option
     */
    public static <T extends Enum<T> & TranslatableOption> CyclingOption<T> ofTranslatableEnum(
            String key, Class<T> klass, T initialValue,
            Consumer<T> setter, SimpleOption.TooltipFactory<T> tooltipFactory
    ) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, TranslatableOption::getText, tooltipFactory);
    }

    /**
     * Creates a new option based on a {@link TranslatableOption translatable} enum's values, with an empty tooltip.
     *
     * @param key          The translation key
     * @param klass        The class type of the enum
     * @param initialValue The initial value
     * @param setter       The callback for the modified value
     * @param <T>          The type of the enum
     * @return The newly created option
     * @see CyclingOption#ofTranslatableEnum(String, Class, Enum, Consumer, SimpleOption.TooltipFactory)
     */
    public static <T extends Enum<T> & TranslatableOption> CyclingOption<T> ofTranslatableEnum(String key, Class<T> klass, T initialValue, Consumer<T> setter) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, TranslatableOption::getText);
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width, int height) {
        CyclingButtonWidget<T> widget = CyclingButtonWidget.builder(valueToText, initialValue)
                .values(values)
                .tooltip(tooltipFactory)
                .build(x, y, width, height, Text.translatable(key), (button, value) -> setter.accept(value));

        widget.active = this.active;
        return widget;
    }

    /**
     * Returns a text that contains the name of the enum constant.
     *
     * @param value The enum constant
     * @param <T>   The type of the enum
     * @return The text
     */
    public static <T extends Enum<T>> Text enumNameToText(T value) {
        return Text.of(value.name());
    }
}
