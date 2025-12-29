package net.uku3lig.ukulib.config.option;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.uku3lig.ukulib.config.option.widget.DoubleSlider;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;

/**
 * A <code>double</code> option, displayed by a slider.
 *
 * @see DoubleSlider
 */
public class SliderOption implements WidgetCreator {
    /**
     * Displays a double with 2 decimals of precision.
     */
    public static final DoubleFunction<Component> DEFAULT_VALUE_TO_TEXT = d -> Component.literal(String.format("%.2f", d));

    /**
     * Displays a double as a percentage, with no further precision.
     */
    public static final DoubleFunction<Component> PERCENT_VALUE_TO_TEXT = d -> Component.literal(String.format("%.0f%%", d * 100));

    /**
     * Displays a double as a rounded integer.
     */
    public static final DoubleFunction<Component> INTEGER_VALUE_TO_TEXT = d -> Component.literal(String.format("%.0f", d));


    private final String key;
    private final double initialValue;
    private final DoubleConsumer setter;
    private final DoubleFunction<Component> valueToText;
    private final double min;
    private final double max;
    private final double step;
    private final OptionInstance.TooltipSupplier<@NotNull Double> tooltipSupplier;

    /**
     * Creates a slider option.
     *
     * @param key             The translation key
     * @param initialValue    The initial value
     * @param setter          The callback for the modified value
     * @param valueToText     The function that converts the value to human-readable text
     * @param min             The minimum value
     * @param max             The maximum value
     * @param step            The step between each value
     * @param tooltipSupplier The tooltip supplier
     */
    public SliderOption(String key, double initialValue, DoubleConsumer setter, DoubleFunction<Component> valueToText, double min, double max, double step, OptionInstance.TooltipSupplier<@NotNull Double> tooltipSupplier) {
        this.key = key;
        this.initialValue = initialValue;
        this.setter = setter;
        this.valueToText = valueToText;
        this.min = min;
        this.max = max;
        this.step = step;
        this.tooltipSupplier = tooltipSupplier;
    }

    /**
     * Creates a slider option, with no tooltip.
     *
     * @param key          The translation key
     * @param initialValue The initial value
     * @param setter       The callback for the modified value
     * @param valueToText  The function that converts the value to human-readable text
     * @param min          The minimum value
     * @param max          The maximum value
     * @param step         The step between each value
     */
    public SliderOption(String key, double initialValue, DoubleConsumer setter, DoubleFunction<Component> valueToText, double min, double max, double step) {
        this(key, initialValue, setter, valueToText, min, max, step, OptionInstance.noTooltip());
    }

    /**
     * Creates a slider option, with no step or tooltip.
     *
     * @param key          The translation key
     * @param initialValue The initial value
     * @param setter       The callback for the modified value
     * @param valueToText  The function that converts the value to human-readable text
     * @param min          The minimum value
     * @param max          The maximum value
     */
    public SliderOption(String key, double initialValue, DoubleConsumer setter, DoubleFunction<Component> valueToText, double min, double max) {
        this(key, initialValue, setter, valueToText, min, max, 0.01);
    }

    /**
     * Creates a slider option, between 0 (inclusive) and 1 (inclusive), with no step or tooltip.
     *
     * @param key          The translation key
     * @param initialValue The initial value
     * @param setter       The callback for the modified value
     * @param valueToText  The function that converts the value to human-readable text
     */
    public SliderOption(String key, double initialValue, DoubleConsumer setter, DoubleFunction<Component> valueToText) {
        this(key, initialValue, setter, valueToText, 0, 1);
    }

    @Override
    public AbstractWidget createWidget(int x, int y, int width, int height) {
        return new DoubleSlider(Component.translatable(key), valueToText, initialValue, min, max, step, setter, tooltipSupplier, x, y, width, height);
    }
}
