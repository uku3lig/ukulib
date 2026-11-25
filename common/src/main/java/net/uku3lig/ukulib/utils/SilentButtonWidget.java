package net.uku3lig.ukulib.utils;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * A button that doesn't make noise.
 */
@SuppressWarnings("unused")
public class SilentButtonWidget extends Button.Plain {
    /**
     * Creates a new silent button
     *
     * @param x                 The x position of the button
     * @param y                 The y position of the button
     * @param width             The width of the button
     * @param height            The height of the button
     * @param message           The text of the button
     * @param onPress           The action to be done when the button is pressed
     * @param narrationSupplier The narration supplier
     */
    protected SilentButtonWidget(int x, int y, int width, int height, net.minecraft.network.chat.Component message, OnPress onPress, CreateNarration narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }

    /**
     * Creates a builder for a silent button
     *
     * @param message The text of the button
     * @param onPress The action to be done when the button is pressed
     * @return A new builder
     */
    public static SilentButtonWidget.Builder<?> silentBuilder(Component message, OnPress onPress) {
        return new SilentButtonWidget.Builder<>(message, onPress);
    }

    @Override
    public void playDownSound(@NotNull SoundManager soundManager) {
        // don't play anything
    }

    /**
     * A silent button builder
     */
    public static class Builder<B extends Builder<B>> extends Button.Builder<B> {
        protected Builder(Component message, OnPress onPress) {
            super(message, onPress);
        }

        @Override
        public @NonNull SilentButtonWidget build() {
            SilentButtonWidget buttonWidget = new SilentButtonWidget(this.x, this.y, this.width, this.height, this.message, this.onPress, this.createNarration);
            buttonWidget.setTooltip(this.tooltip);
            return buttonWidget;
        }
    }
}
