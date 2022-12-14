package net.uku3lig.ukulib.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Simple class for various utilities.
 */
@SuppressWarnings("unused")
public class Ukutils {
    /**
     * Placeholder for button text.
     */
    public static final Text BUTTON_PLACEHOLDER = Text.of("ukulib:placeholder_owo");


    /**
     * Creates a {@link SimpleOption} which acts as a simple button.
     * Equivalent to {@link Ukutils#createButton(String, Text, Consumer) createButton(key, Text.of(String.valueOf(value)), callback)}.
     *
     * @param key The translation key of the button text
     * @param value The value to be displayed after the colon
     * @param callback The action to be performed when the button is clicked
     * @return The generated option
     * @see Ukutils#createButton(String, Text, Consumer)
     */
    public static SimpleOption<Boolean> createButton(String key, Object value, Consumer<Screen> callback) {
        return createButton(key, Text.of(String.valueOf(value)), callback);
    }

    /**
     * Creates a {@link SimpleOption} which acts as a simple button.
     * @param key The translation key of the button text
     * @param callback The action to be performed when the button is clicked
     * @return The generated option
     * @see Ukutils#createButton(String, Text, Consumer)
     */
    public static SimpleOption<Boolean> createButton(String key, Consumer<Screen> callback) {
        return createButton(key, BUTTON_PLACEHOLDER, callback);
    }

    /**
     * Creates a {@link SimpleOption} which acts as a simple button.
     *
     * @param key The translation key of the button text
     * @param text The text to be displayed after the colon
     * @param callback The action to be performed when the button is clicked
     * @return The generated option
     */
    public static SimpleOption<Boolean> createButton(String key, Text text, Consumer<Screen> callback) {
        return new SimpleOption<>(key, SimpleOption.emptyTooltip(), (optionText, value) -> text,
                SimpleOption.BOOLEAN, true, v -> callback.accept(MinecraftClient.getInstance().currentScreen));
    }

    /**
     * Creates a {@link SimpleOption} which acts as a simple button that opens a screen when clicked.
     * Equivalent to {@link Ukutils#createOpenButton(String, Text, UnaryOperator) createOpenButton(key, Text.of(String.valueOf(value)), callback)}.
     *
     * @param key The translation key of the button text
     * @param value The value to be displayed after the colon
     * @param callback The getter for the screen to be opened when the button is clicked
     * @return The generated option
     * @see Ukutils#createOpenButton(String, Text, UnaryOperator)
     */
    public static SimpleOption<Boolean> createOpenButton(String key, Object value, UnaryOperator<Screen> callback) {
        return createOpenButton(key, Text.of(String.valueOf(value)), callback);
    }

    /**
     * Creates a {@link SimpleOption} which acts as a simple that opens a screen when clicked.
     * @param key The translation key of the button text
     * @param callback The getter for the screen to be opened when the button is clicked
     * @return The generated option
     * @see Ukutils#createOpenButton(String, Text, UnaryOperator)
     */
    public static SimpleOption<Boolean> createOpenButton(String key, UnaryOperator<Screen> callback) {
        return createOpenButton(key, BUTTON_PLACEHOLDER, callback);
    }

    /**
     * Creates a {@link SimpleOption} which acts as a simple button that opens a screen when clicked.
     *
     * @param key The translation key of the button text
     * @param text The text to be displayed after the colon
     * @param callback The getter for the screen to be opened when the button is clicked
     * @return The generated option
     */
    public static SimpleOption<Boolean> createOpenButton(String key, Text text, UnaryOperator<Screen> callback) {
        return new SimpleOption<>(key, SimpleOption.emptyTooltip(), (optionText, value) -> text,
                SimpleOption.BOOLEAN, true, v -> MinecraftClient.getInstance().setScreen(callback.apply(MinecraftClient.getInstance().currentScreen)));
    }

    /**
     * Creates a done button.
     * @param width The width of the screen
     * @param height The height of the screen
     * @param parent The parent screen
     * @return The generated button
     */
    public static ButtonWidget doneButton(int width, int height, Screen parent) {
        return ButtonWidget.builder(ScreenTexts.DONE, button -> MinecraftClient.getInstance().setScreen(parent))
                .dimensions(width / 2 - 100, height - 27, 200, 20)
                .build();
    }

    /**
     * Makes text coordinates based on the position of an icon.
     * @param text The text to be drawn
     * @param screenWidth The width of the screen
     * @param textRenderer The text renderer
     * @param x The x coordinate of the icon
     * @param y The y coordinate of the icon
     * @param width The width of the icon
     * @param height The height of the icon
     * @return The tuple of coordinates
     */
    public static Tuple2<Integer, Integer> getTextCoords(Text text, int screenWidth, TextRenderer textRenderer, int x, int y, int width, int height) {
        int rx = x - ((screenWidth - width) / 2);
        int textX = x + (width / 2) - (textRenderer.getWidth(text) / 2); // center
        int textY = y + height + 2 - textRenderer.fontHeight; // center

        if (Math.abs(rx) >= 2) {
            textY = y + (height / 2) - (textRenderer.fontHeight / 2); // left/right

            if (rx < 0) textX = x + width + 2; // left
            else textX = x - 2 - textRenderer.getWidth(text); // right
        }

        return new Tuple2<>(textX, textY);
    }

    /**
     * Makes text coordinates based on the position of a standard 16x16 icon.
     * @param text The text to be drawn
     * @param screenWidth The width of the screen
     * @param textRenderer The text renderer
     * @param x The x coordinate of the icon
     * @param y The y coordinate of the icon
     * @return The tuple of coordinates
     * @see Ukutils#getTextCoords(Text, int, TextRenderer, int, int, int, int)
     */
    public static Tuple2<Integer, Integer> getTextCoords(Text text, int screenWidth, TextRenderer textRenderer, int x, int y) {
        return getTextCoords(text, screenWidth, textRenderer, x, y, 16, 16);
    }

    /**
     * Returns the value, keeping it between two bounds.
     * @param n The value
     * @param min The bottom bound
     * @param max The top bound
     * @return The bounded value
     */
    public static double bound(double n, double min, double max) {
        if (min > max) {
            double tmp = max;
            max = min;
            min = tmp;
        }

        return Math.min(Math.max(n, min), max);
    }

    /**
     * Returns the value, keeping it between two integer bounds.
     * @param n The value
     * @param min The bottom bound
     * @param max The top bound
     * @return The bounded value
     */
    public static int bound(int n, int min, int max) {
        if (min > max) {
            int tmp = max;
            max = min;
            min = tmp;
        }

        return Math.min(Math.max(n, min), max);
    }

    /**
     * Retrieves the string text from an ordered text.
     * @param text The ordered text
     * @return The value of the text
     */
    public static String getText(OrderedText text) {
        StringBuilder builder = new StringBuilder();
        text.accept((index, style, codePoint) -> {
            builder.append(Character.toChars(codePoint));
            return true;
        });

        return builder.toString();
    }

    /**
     * Simple 2-tuple.
     * @param t1 The first element
     * @param t2 The second element
     * @param <T1> The type of the first element
     * @param <T2> The type of the second element
     */
    public record Tuple2<T1, T2>(T1 t1, T2 t2) {
    }

    private Ukutils() {}
}
