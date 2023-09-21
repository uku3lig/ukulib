package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.option.widget.DoubleSlider;

import java.util.function.IntConsumer;
import java.util.function.IntFunction;

/**
 * An int option, represented by a slider.
 *
 * @see DoubleSlider
 */
public class IntSliderOption implements WidgetCreator {
    /**
     * Converts an int to Text using its String representation.
     */
    public static final IntFunction<Text> DEFAULT_INT_TO_TEXT = i -> Text.of(String.valueOf(i));

    private final String key;
    private final int initialValue;
    private final IntConsumer setter;
    private final IntFunction<Text> valueToText;
    private final int min;
    private final int max;
    private final int step;
    private final SimpleOption.TooltipFactory<Integer> tooltipFactory;

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
    public IntSliderOption(String key, int initialValue, IntConsumer setter, IntFunction<Text> valueToText, int min, int max, int step, SimpleOption.TooltipFactory<Integer> tooltipFactory) {
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
    public IntSliderOption(String key, int initialValue, IntConsumer setter, IntFunction<Text> valueToText, int min, int max, int step) {
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
    public IntSliderOption(String key, int initialValue, IntConsumer setter, IntFunction<Text> valueToText, int min, int max) {
        this(key, initialValue, setter, valueToText, min, max, 1);
    }
    
    @Override
    public ClickableWidget createWidget(int x, int y, int width, int height) {
        return new DoubleSlider(Text.translatable(key), d -> valueToText.apply((int) d), initialValue,
                min, max, step, d -> setter.accept((int) d), d -> tooltipFactory.apply(d.intValue()),
                x, y, width, height);
    }
}
