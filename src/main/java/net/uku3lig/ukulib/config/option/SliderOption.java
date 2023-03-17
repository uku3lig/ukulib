package net.uku3lig.ukulib.config.option;

import lombok.AllArgsConstructor;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.option.widget.DoubleSlider;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;

@AllArgsConstructor
public class SliderOption implements ButtonCreator {
    public static final DoubleFunction<Text> DEFAULT_VALUE_TO_TEXT = d -> Text.of(String.format("%.2f", d));
    public static final DoubleFunction<Text> PERCENT_VALUE_TO_TEXT = d -> Text.of(String.format("%.0f%%", d * 100));
    public static final DoubleFunction<Text> INTEGER_VALUE_TO_TEXT = d -> Text.of(String.format("%.0f", d));


    private final String key;
    private final double initialValue;
    private final DoubleConsumer setter;
    private final DoubleFunction<Text> valueToText;
    private final double min;
    private final double max;
    private final double step;
    private final SimpleOption.TooltipFactory<Double> tooltipFactory;

    public SliderOption(String key, double initialValue, DoubleConsumer setter, DoubleFunction<Text> valueToText, double min, double max, double step) {
        this(key, initialValue, setter, valueToText, min, max, step, SimpleOption.emptyTooltip());
    }

    public SliderOption(String key, double initialValue, DoubleConsumer setter, DoubleFunction<Text> valueToText, double min, double max) {
        this(key, initialValue, setter, valueToText, min, max, 1);
    }

    public SliderOption(String key, double initialValue, DoubleConsumer setter, DoubleFunction<Text> valueToText) {
        this(key, initialValue, setter, valueToText, 0, 1);
    }

    @Override
    public ClickableWidget createButton(int x, int y, int width, int height) {
        return new DoubleSlider(key, valueToText, initialValue, min, max, step, setter, tooltipFactory, x, y, width, height);
    }
}
