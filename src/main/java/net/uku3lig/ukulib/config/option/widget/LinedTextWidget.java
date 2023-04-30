package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class LinedTextWidget extends TextWidget {
    private final boolean drawLine;
    private final int padding;

    public LinedTextWidget(int x, int y, int width, int height, Text message, TextRenderer textRenderer, boolean drawLine, int padding) {
        super(x, y, width, height, message, textRenderer);
        this.drawLine = drawLine;
        this.padding = padding;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderButton(matrices, mouseX, mouseY, delta);
        if (drawLine) {
            // draw a line before the text
            int lineWidth = (this.width - this.getTextRenderer().getWidth(this.getMessage())) / 2 - (2 * this.padding);
            int lineY = this.getY() + this.height / 2;

            drawHorizontalLine(matrices, this.getX() + this.padding, this.getX() + this.padding + lineWidth, lineY, 0xFFFFFFFF);
            drawHorizontalLine(matrices, this.getX() + this.width - this.padding - lineWidth, this.getX() + this.width - this.padding, lineY, 0xFFFFFFFF);
        }
    }
}
