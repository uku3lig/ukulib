package net.uku3lig.ukulib.config.option;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.option.widget.LinedTextWidget;

public class TextOption implements WidgetCreator {
    private final String key;
    private final boolean drawLine;
    private final int padding;

    public TextOption(String key, boolean drawLine, int padding) {
        this.key = key;
        this.drawLine = drawLine;
        this.padding = padding;
    }

    public TextOption(String key, boolean drawLine) {
        this(key, drawLine, 5);
    }

    public TextOption(String key) {
        this(key, false);
    }

    public ClickableWidget createWidget(int x, int y, int width, int height) {
        return new LinedTextWidget(x, y, width, height, Text.translatable(this.key), MinecraftClient.getInstance().textRenderer, this.drawLine, this.padding);
    }
}
