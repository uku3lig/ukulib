package net.uku3lig.ukulib.config.option;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.OptionEnum;

/**
 * An option type based on a finite amount of cycling values of the same type.
 *
 * @param <T> The type of the value
 */
public class CyclingOption<T> implements WidgetCreator {
    /**
     * Boolean to text converter.
     */
    public static final Function<Boolean, Component> BOOL_TO_TEXT = b -> Boolean.TRUE.equals(b) ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF;

    private final String key;
    private final Collection<T> values;
    private final T initialValue;
    private final Consumer<T> setter;
    private final Function<T, Component> valueToText;
    private final OptionInstance.TooltipSupplier<T> tooltipSupplier;
    private final boolean active;

    /**
     * Constructs a new generic cycling option.
     *
     * @param key             The translation key
     * @param values          The possible option values
     * @param initialValue    The initial displayed value
     * @param setter          The callback for the modified value
     * @param valueToText     The function that converts the value to human-readable text
     * @param tooltipSupplier The tooltip supplier
     * @param active          Whether the button can be clicked
     */
    public CyclingOption(String key, Collection<T> values, T initialValue, Consumer<T> setter, Function<T, Component> valueToText, OptionInstance.TooltipSupplier<T> tooltipSupplier, boolean active) {
        this.key = key;
        this.values = values;
        this.initialValue = initialValue;
        this.setter = setter;
        this.valueToText = valueToText;
        this.tooltipSupplier = tooltipSupplier;
        this.active = active;
    }

    /**
     * Constructs a new generic cycling option.
     *
     * @param key             The translation key
     * @param values          The possible option values
     * @param initialValue    The initial displayed value
     * @param setter          The callback for the modified value
     * @param valueToText     The function that converts the value to human-readable text
     * @param tooltipSupplier The tooltip supplier
     */
    public CyclingOption(String key, Collection<T> values, T initialValue, Consumer<T> setter, Function<T, Component> valueToText, OptionInstance.TooltipSupplier<T> tooltipSupplier) {
        this(key, values, initialValue, setter, valueToText, tooltipSupplier, true);
    }

    /**
     * Constructs a new generic cycling option, with no tooltip.
     *
     * @param key          The translation key
     * @param values       The possible option values
     * @param initialValue The initial displayed value
     * @param setter       The callback for the modified value
     * @param valueToText  The function that converts the value to human-readable text
     * @see CyclingOption#CyclingOption(String, Collection, Object, Consumer, Function, OptionInstance.TooltipSupplier)
     */
    public CyclingOption(String key, Collection<T> values, T initialValue, Consumer<T> setter, Function<T, Component> valueToText) {
        this(key, values, initialValue, setter, valueToText, OptionInstance.noTooltip());
    }

    /**
     * Creates a new boolean option.
     *
     * @param key             The translation key
     * @param initialValue    The initial boolean value
     * @param setter          The callback for the modified value
     * @param tooltipSupplier The tooltip supplier
     * @return The newly constructed option
     */
    public static CyclingOption<Boolean> ofBoolean(String key, boolean initialValue, Consumer<Boolean> setter, OptionInstance.TooltipSupplier<Boolean> tooltipSupplier) {
        return new CyclingOption<>(key, List.of(Boolean.TRUE, Boolean.FALSE), initialValue, setter, BOOL_TO_TEXT, tooltipSupplier);
    }

    /**
     * Creates a new boolean option, with an empty tooltip.
     *
     * @param key          The translation key
     * @param initialValue The initial boolean value
     * @param setter       The callback for the modified value
     * @return The newly constructed option
     * @see CyclingOption#ofBoolean(String, boolean, Consumer, OptionInstance.TooltipSupplier)
     */
    public static CyclingOption<Boolean> ofBoolean(String key, boolean initialValue, Consumer<Boolean> setter) {
        return ofBoolean(key, initialValue, setter, OptionInstance.noTooltip());
    }

    /**
     * Creates a new option from a {@link OptionEnum}.
     *
     * @param key             The translation key
     * @param values          The values to be displayed
     * @param initialValue    The initial value
     * @param setter          The callback for the modified value
     * @param tooltipSupplier The tooltip supplier
     * @param <T>             The type of the option
     * @return The newly constructed option
     */
    public static <T extends OptionEnum> CyclingOption<T> ofTranslatable(
            String key, Collection<T> values, T initialValue,
            Consumer<T> setter, OptionInstance.TooltipSupplier<T> tooltipSupplier
    ) {
        return new CyclingOption<>(key, values, initialValue, setter, OptionEnum::getCaption, tooltipSupplier);
    }

    /**
     * Creates a new option from a {@link OptionEnum}, with an empty tooltip.
     *
     * @param key          The translation key
     * @param values       The values to be displayed
     * @param initialValue The initial value
     * @param setter       The callback for the modified value
     * @param <T>          The type of the option
     * @return The newly constructed option
     */
    public static <T extends OptionEnum> CyclingOption<T> ofTranslatable(String key, Collection<T> values, T initialValue, Consumer<T> setter) {
        return ofTranslatable(key, values, initialValue, setter, OptionInstance.noTooltip());
    }

    /**
     * Creates a new option based on an enum's values.
     *
     * @param key             The translation key
     * @param klass           The class type of the enum
     * @param initialValue    The initial value
     * @param setter          The callback for the modified value
     * @param valueToText     The function that converts the value to human-readable text
     * @param tooltipSupplier The tooltip supplier
     * @param <T>             The type of the enum
     * @return The newly created option
     */
    public static <T extends Enum<T>> CyclingOption<T> ofEnum(
            String key, Class<T> klass, T initialValue,
            Consumer<T> setter, Function<T, Component> valueToText,
            OptionInstance.TooltipSupplier<T> tooltipSupplier
    ) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, valueToText, tooltipSupplier);
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
     * @see CyclingOption#ofEnum(String, Class, Enum, Consumer, Function, OptionInstance.TooltipSupplier)
     */
    public static <T extends Enum<T>> CyclingOption<T> ofEnum(
            String key, Class<T> klass, T initialValue,
            Consumer<T> setter, Function<T, Component> valueToText
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
     * @see CyclingOption#ofEnum(String, Class, Enum, Consumer, Function, OptionInstance.TooltipSupplier)
     * @see CyclingOption#enumNameToText(Enum)
     */
    public static <T extends Enum<T>> CyclingOption<T> ofEnum(String key, Class<T> klass, T initialValue, Consumer<T> setter) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, CyclingOption::enumNameToText);
    }

    /**
     * Creates a new option based on a {@link OptionEnum translatable} enum's values.
     *
     * @param key             The translation key
     * @param klass           The class type of the enum
     * @param initialValue    The initial value
     * @param setter          The callback for the modified value
     * @param tooltipSupplier The tooltip supplier
     * @param <T>             The type of the enum
     * @return The newly created option
     */
    public static <T extends Enum<T> & OptionEnum> CyclingOption<T> ofTranslatableEnum(
            String key, Class<T> klass, T initialValue,
            Consumer<T> setter, OptionInstance.TooltipSupplier<T> tooltipSupplier
    ) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, OptionEnum::getCaption, tooltipSupplier);
    }

    /**
     * Creates a new option based on a {@link OptionEnum translatable} enum's values, with an empty tooltip.
     *
     * @param key          The translation key
     * @param klass        The class type of the enum
     * @param initialValue The initial value
     * @param setter       The callback for the modified value
     * @param <T>          The type of the enum
     * @return The newly created option
     * @see CyclingOption#ofTranslatableEnum(String, Class, Enum, Consumer, OptionInstance.TooltipSupplier)
     */
    public static <T extends Enum<T> & OptionEnum> CyclingOption<T> ofTranslatableEnum(String key, Class<T> klass, T initialValue, Consumer<T> setter) {
        return new CyclingOption<>(key, EnumSet.allOf(klass), initialValue, setter, OptionEnum::getCaption);
    }

    @Override
    public AbstractWidget createWidget(int x, int y, int width, int height) {
        CycleButton<T> widget = CycleButton.builder(valueToText)
                .withValues(values)
                .withTooltip(tooltipSupplier)
                .withInitialValue(initialValue)
                .create(x, y, width, height, Component.translatable(key), (button, value) -> setter.accept(value));

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
    public static <T extends Enum<T>> Component enumNameToText(T value) {
        return Component.literal(value.name());
    }
}
