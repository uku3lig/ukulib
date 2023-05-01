package net.uku3lig.ukulib.config.option;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.option.widget.LinedTextWidget;

/**
 * An option to display text.
 */
public class TextOption implements WidgetCreator {
    private final String key;
    private final boolean drawLine;
    private final int padding;

    /**
     * Constructor.
     *
     * @param key      the translation key for the text
     * @param drawLine whether to draw a line on each side of the text
     * @param padding  the padding between the text and the line
     */
    public TextOption(String key, boolean drawLine, int padding) {
        this.key = key;
        this.drawLine = drawLine;
        this.padding = padding;
    }

    /**
     * Constructor. Equivalent to {@code TextOption(key, drawLine, 5)}.
     *
     * @param key      the translation key for the text
     * @param drawLine whether to draw a line on each side of the text
     */
    public TextOption(String key, boolean drawLine) {
        this(key, drawLine, 5);
    }

    /**
     * Constructor. Equivalent to {@code TextOption(key, false, 5)}.
     *
     * @param key the translation key for the text
     */
    public TextOption(String key) {
        this(key, false);
    }

    public ClickableWidget createWidget(int x, int y, int width, int height) {
        return new LinedTextWidget(x, y, width, height, Text.translatable(this.key), MinecraftClient.getInstance().textRenderer, this.drawLine, this.padding);
    }
}
