package net.uku3lig.ukulib.utils;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;

/**
 * A button that doesn't make noise.
 */
@SuppressWarnings("unused")
public class SilentButtonWidget extends ButtonWidget {
    /**
     * Creates a new silent button
     * @param x The x position of the button
     * @param y The y position of the button
     * @param width The width of the button
     * @param height The height of the button
     * @param message The text of the button
     * @param onPress The action to be done when the button is pressed
     * @param tooltipSupplier The tooltip supplier
     */
    protected SilentButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
    }

    /**
     * Creates a new silent button
     * @param x The x position of the button
     * @param y The y position of the button
     * @param width The width of the button
     * @param height The height of the button
     * @param message The text of the button
     * @param onPress The action to be done when the button is pressed
     */
    public SilentButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        this(x, y, width, height, message, onPress, EMPTY);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        // don't play anything
    }
}
