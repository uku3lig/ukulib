package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;

/**
 * Implementation of a slider widget for use in {@link net.uku3lig.ukulib.config.option.SliderOption}.
 *
 * @see net.uku3lig.ukulib.config.option.SliderOption
 */
public class DoubleSlider extends SliderWidget {
    private final Text text;
    private final double min;
    private final double max;
    private final double step;
    private final DoubleFunction<Text> valueToText;
    private final DoubleConsumer setter;
    private final SimpleOption.TooltipFactory<Double> tooltipFactory;

    /**
     * Constructs a new slider.
     *
     * @param text           The text
     * @param valueToText    The function that converts the value to human-readable text
     * @param initialValue   The initial value
     * @param min            The minimum value
     * @param max            The maximum value
     * @param step           The step between each value
     * @param setter         The callback for the modified value
     * @param tooltipFactory The tooltip factory
     * @param x              The x position of the widget
     * @param y              The y position of the widget
     * @param width          The width of the widget
     * @param height         The height of the widget
     */
    public DoubleSlider(
            Text text, DoubleFunction<Text> valueToText, double initialValue,
            double min, double max, double step,
            DoubleConsumer setter, SimpleOption.TooltipFactory<Double> tooltipFactory,
            int x, int y, int width, int height
    ) {
        super(x, y, width, height, text, MathHelper.map(initialValue, min, max, 0, 1));

        if (step <= 0) throw new IllegalArgumentException("step cannot be negative or null");
        if (min > max) throw new IllegalArgumentException("min cannot be greater than max");

        this.text = text;
        this.valueToText = valueToText;
        this.min = min;
        this.max = max;
        this.step = step;
        this.setter = setter;
        this.tooltipFactory = tooltipFactory;

        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(GameOptions.getGenericValueText(this.text, this.valueToText.apply(fixedValue())));
        this.setTooltip(this.tooltipFactory.apply(fixedValue()));
    }

    @Override
    protected void applyValue() {
        this.setter.accept(fixedValue());
    }

    /**
     * Maps the value to the given bounds, and then rounds it up to the closest step.
     *
     * @return The fixed value
     */
    private double fixedValue() {
        double fixed = MathHelper.map(this.value, 0, 1, this.min, this.max);
        double next = MathHelper.ceil(fixed / this.step) * this.step;
        return MathHelper.clamp(next, this.min, this.max);
    }
}
