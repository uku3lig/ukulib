package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.option.widget.DoubleSlider;

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
    public static final DoubleFunction<Text> DEFAULT_VALUE_TO_TEXT = d -> Text.of(String.format("%.2f", d));

    /**
     * Displays a double as a percentage, with no further precision.
     */
    public static final DoubleFunction<Text> PERCENT_VALUE_TO_TEXT = d -> Text.of(String.format("%.0f%%", d * 100));

    /**
     * Displays a double as a rounded integer.
     */
    public static final DoubleFunction<Text> INTEGER_VALUE_TO_TEXT = d -> Text.of(String.format("%.0f", d));


    private final String key;
    private final double initialValue;
    private final DoubleConsumer setter;
    private final DoubleFunction<Text> valueToText;
    private final double min;
    private final double max;
    private final double step;
    private final SimpleOption.TooltipFactory<Double> tooltipFactory;

    /**
     * Creates a slider option.
     *
     * @param key            The translation key
     * @param initialValue   The initial value
     * @param setter         The callback for the modified value
     * @param valueToText    The function that converts the value to human-readable text
     * @param min            The minimum value
     * @param max            The maximum value
     * @param step           The step between each value
     * @param tooltipFactory The tooltip factory
     */
    public SliderOption(String key, double initialValue, DoubleConsumer setter, DoubleFunction<Text> valueToText, double min, double max, double step, SimpleOption.TooltipFactory<Double> tooltipFactory) {
        this.key = key;
        this.initialValue = initialValue;
        this.setter = setter;
        this.valueToText = valueToText;
        this.min = min;
        this.max = max;
        this.step = step;
        this.tooltipFactory = tooltipFactory;
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
    public SliderOption(String key, double initialValue, DoubleConsumer setter, DoubleFunction<Text> valueToText, double min, double max, double step) {
        this(key, initialValue, setter, valueToText, min, max, step, SimpleOption.emptyTooltip());
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
    public SliderOption(String key, double initialValue, DoubleConsumer setter, DoubleFunction<Text> valueToText, double min, double max) {
        this(key, initialValue, setter, valueToText, min, max, 1);
    }

    /**
     * Creates a slider option, between 0 (inclusive) and 1 (inclusive), with no step or tooltip.
     *
     * @param key          The translation key
     * @param initialValue The initial value
     * @param setter       The callback for the modified value
     * @param valueToText  The function that converts the value to human-readable text
     */
    public SliderOption(String key, double initialValue, DoubleConsumer setter, DoubleFunction<Text> valueToText) {
        this(key, initialValue, setter, valueToText, 0, 1);
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width, int height) {
        return new DoubleSlider(Text.translatable(key), valueToText, initialValue, min, max, step, setter, tooltipFactory, x, y, width, height);
    }
}
