package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

/**
 * A text widget that can have a line before the text.
 */
public class LinedTextWidget extends StringWidget {
    private final boolean drawLine;
    private final int padding;

    /**
     * Constructor.
     *
     * @param x        the x position
     * @param y        the y position
     * @param width    the width
     * @param height   the height
     * @param message  the text
     * @param font     the text renderer
     * @param drawLine whether to draw a line before the text
     * @param padding  the padding between the line and the text
     */
    public LinedTextWidget(int x, int y, int width, int height, Component message, Font font, boolean drawLine, int padding) {
        super(x, y, width, height, message, font);
        this.drawLine = drawLine;
        this.padding = padding;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.renderWidget(graphics, mouseX, mouseY, delta);
        if (drawLine) {
            // draw a line before the text
            int lineWidth = (this.width - this.getFont().width(this.getMessage())) / 2 - (2 * this.padding);
            int lineY = this.getY() + this.height / 2;

            graphics.hLine(this.getX() + this.padding, this.getX() + this.padding + lineWidth, lineY, 0xFFFFFFFF);
            graphics.hLine(this.getX() + this.width - this.padding - lineWidth, this.getX() + this.width - this.padding, lineY, 0xFFFFFFFF);
        }
    }
}
