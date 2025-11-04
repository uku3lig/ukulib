package net.uku3lig.ukulib.utils;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

/**
 * A button that doesn't make noise.
 */
@SuppressWarnings("unused")
public class SilentButtonWidget extends Button {
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
    protected SilentButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }

    /**
     * Creates a builder for a silent button
     *
     * @param message The text of the button
     * @param onPress The action to be done when the button is pressed
     * @return A new builder
     */
    public static Builder silentBuilder(Component message, OnPress onPress) {
        return new Builder(message, onPress);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        // don't play anything
    }

    /**
     * A silent button builder
     */
    public static class Builder {
        private final Component message;
        private final OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private CreateNarration narrationSupplier;

        /**
         * Creates a builder for a silent button
         *
         * @param message The text of the button
         * @param onPress The action to be done when the button is pressed
         */
        public Builder(Component message, OnPress onPress) {
            this.narrationSupplier = Button.DEFAULT_NARRATION;
            this.message = message;
            this.onPress = onPress;
        }

        /**
         * Sets the position
         *
         * @param x The x position
         * @param y The y position
         * @return The modified builder
         */
        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        /**
         * Sets the width
         *
         * @param width The width
         * @return The modified builder
         */
        public Builder width(int width) {
            this.width = width;
            return this;
        }

        /**
         * Sets the size
         *
         * @param width  The width
         * @param height The height
         * @return The modified builder
         */
        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * Sets the dimensions
         *
         * @param x      The x position
         * @param y      The y position
         * @param width  The width
         * @param height The height
         * @return The modified builder
         */
        public Builder dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        /**
         * Sets the tooltip
         *
         * @param tooltip The tooltip
         * @return The modified builder
         */
        public Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        /**
         * Sets the narration supplier
         *
         * @param narrationSupplier The narration supplier
         * @return The modified builder
         */
        public Builder narrationSupplier(CreateNarration narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        /**
         * Build the button
         *
         * @return The button
         */
        public SilentButtonWidget build() {
            SilentButtonWidget buttonWidget = new SilentButtonWidget(this.x, this.y, this.width, this.height, this.message, this.onPress, this.narrationSupplier);
            buttonWidget.setTooltip(this.tooltip);
            return buttonWidget;
        }
    }
}
