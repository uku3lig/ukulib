package net.uku3lig.ukulib.config.option;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.option.widget.ColorInputWidget;

import java.util.function.IntConsumer;

public class ColorOption implements WidgetCreator {
    private final String suggestionKey;
    private final IntConsumer setter;
    private final boolean allowAlpha;

    public ColorOption(String suggestionKey, IntConsumer setter, boolean allowAlpha) {
        this.suggestionKey = suggestionKey;
        this.setter = setter;
        this.allowAlpha = allowAlpha;
    }

    public ColorOption(String suggestionKey, IntConsumer setter) {
        this(suggestionKey, setter, false);
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width, int height) {
        String suggestion = Text.translatable(suggestionKey).getString();

        return new ColorInputWidget(x, y, width, height, setter, suggestion, allowAlpha);
    }
}
