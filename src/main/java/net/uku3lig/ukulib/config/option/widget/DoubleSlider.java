package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;

public class DoubleSlider extends SliderWidget {
    private final String key;
    private final double min;
    private final double max;
    private final double step;
    private final DoubleFunction<Text> valueToText;
    private final DoubleConsumer setter;
    private final SimpleOption.TooltipFactory<Double> tooltipFactory;

    public DoubleSlider(
            String key, DoubleFunction<Text> valueToText, double initialValue,
            double min, double max, double step,
            DoubleConsumer setter, SimpleOption.TooltipFactory<Double> tooltipFactory,
            int x, int y, int width, int height
    ) {
        super(x, y, width, height, Text.translatable(key), MathHelper.map(initialValue, min, max, 0, 1));

        if (step <= 0) throw new IllegalArgumentException("step cannot be negative or null");
        if (min > max) throw new IllegalArgumentException("min cannot be greater than max");

        this.key = key;
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
        this.setMessage(GameOptions.getGenericValueText(Text.translatable(this.key), this.valueToText.apply(fixedValue())));
        this.setTooltip(this.tooltipFactory.apply(fixedValue()));
    }

    @Override
    protected void applyValue() {
        this.setter.accept(fixedValue());
    }

    private double fixedValue() {
        double fixed = MathHelper.map(this.value, 0, 1, this.min, this.max);
        double next = MathHelper.ceil(fixed / this.step) * this.step;
        return MathHelper.clamp(next, this.min, this.max);
    }
}
