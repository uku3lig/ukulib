package net.uku3lig.ukulib.config.option.widget;

import net.minecraft.client.gui.DrawContext;

import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.function.Predicate;

/**
 * A widget that allows the user to input a color.
 */
public class ColorInputWidget extends TextInputWidget {
    private static final Predicate<String> IS_COLOR = s -> s.matches("^#?([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$");

    private final boolean allowAlpha;

    /**
     * Constructor.
     *
     * @param x               the x position
     * @param y               the y position
     * @param width           the width
     * @param height          the height
     * @param initialValue    the initial value
     * @param changedListener the listener that is called when the value changes
     * @param suggestion      the suggestion
     * @param allowAlpha      whether to allow changing the alpha value
     */
    public ColorInputWidget(int x, int y, int width, int height, int initialValue, IntConsumer changedListener, String suggestion, boolean allowAlpha) {
        super(x, y, width - height - 2, height, "#" + Integer.toHexString(initialValue).toUpperCase(),
                s -> convert(s, allowAlpha).ifPresent(changedListener::accept), suggestion, IS_COLOR, 9);

        this.allowAlpha = allowAlpha;
    }

    @Override
    public void renderButton(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.renderButton(drawContext, mouseX, mouseY, delta);
        if (!this.isVisible()) return;

        convert(this.getText(), this.allowAlpha).ifPresent(color -> {
            int x = this.getX() + this.width + 2;
            int y = this.getY();
            int size = this.height;
            drawContext.fill(x, y, x + size, y + size, color);
        });
    }

    private static Optional<Integer> convert(String value, boolean allowAlpha) {
        if (!IS_COLOR.test(value)) return Optional.empty();

        try {
            value = value.replace("#", "");
            if (value.length() == 6 || (allowAlpha && value.length() == 8)) {
                int color = Integer.parseUnsignedInt(value, 16);
                if (color <= 0xFFFFFF) color |= (0xFF << 24);

                return Optional.of(color);
            }
        } catch (Exception ignored) {
            // problem? no color for you owo
        }

        return Optional.empty();
    }
}
