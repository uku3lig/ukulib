package net.uku3lig.ukulib.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.Option;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Simple class for various utilities.
 */
@SuppressWarnings("unused")
public class Ukutils {
    /**
     * Creates a {@link Option} which acts as a simple button.
     * Equivalent to {@link Ukutils#createButton(String, Text, Consumer) createButton(key, Text.of(String.valueOf(value)), callback)}.
     *
     * @param key      The translation key of the button text
     * @param value    The value to be displayed after the colon
     * @param callback The action to be performed when the button is clicked
     * @return The generated option
     * @see Ukutils#createButton(String, Text, Consumer)
     */
    public static Option createButton(String key, Object value, Consumer<Screen> callback) {
        return createButton(key, new LiteralText(String.valueOf(value)), callback);
    }

    /**
     * Creates a {@link Option} which acts as a simple button.
     *
     * @param key      The translation key of the button text
     * @param callback The action to be performed when the button is clicked
     * @return The generated option
     * @see Ukutils#createButton(String, Text, Consumer)
     */
    public static Option createButton(String key, Consumer<Screen> callback) {
        return createButton(key, null, callback);
    }

    /**
     * Creates a {@link Option} which acts as a simple button.
     *
     * @param key      The translation key of the button text
     * @param text     The text to be displayed after the colon
     * @param callback The action to be performed when the button is clicked
     * @return The generated option
     */
    public static Option createButton(String key, Text text, Consumer<Screen> callback) {
        return new CyclingOption(key, (opt, amount) -> callback.accept(MinecraftClient.getInstance().currentScreen), (opt, option) -> {
            if (text != null) return option.getDisplayPrefix() + text.asFormattedString();
            else return new TranslatableText(key).asString();
        });
    }

    /**
     * Creates an {@link Option} which acts as a simple button that opens a screen when clicked.
     * Equivalent to {@link Ukutils#createOpenButton(String, Text, UnaryOperator) createOpenButton(key, Text.of(String.valueOf(value)), callback)}.
     *
     * @param key      The translation key of the button text
     * @param value    The value to be displayed after the colon
     * @param callback The getter for the screen to be opened when the button is clicked
     * @return The generated option
     * @see Ukutils#createOpenButton(String, Text, UnaryOperator)
     */
    public static Option createOpenButton(String key, Object value, UnaryOperator<Screen> callback) {
        return createOpenButton(key, new LiteralText(String.valueOf(value)), callback);
    }

    /**
     * Creates a {@link Option} which acts as a simple that opens a screen when clicked.
     *
     * @param key      The translation key of the button text
     * @param callback The getter for the screen to be opened when the button is clicked
     * @return The generated option
     * @see Ukutils#createOpenButton(String, Text, UnaryOperator)
     */
    public static Option createOpenButton(String key, UnaryOperator<Screen> callback) {
        return createOpenButton(key, null, callback);
    }

    /**
     * Creates a {@link Option} which acts as a simple button that opens a screen when clicked.
     *
     * @param key      The translation key of the button text
     * @param text     The text to be displayed after the colon
     * @param callback The getter for the screen to be opened when the button is clicked
     * @return The generated option
     */
    public static Option createOpenButton(String key, Text text, UnaryOperator<Screen> callback) {
        return createButton(key, text, parent -> MinecraftClient.getInstance().openScreen(callback.apply(parent)));
    }

    private static Text getGenericLabel(String key, Text text) {
        return new TranslatableText("options.generic_value", new TranslatableText(key), text);
    }

    /**
     * Creates a done button.
     *
     * @param width  The width of the screen
     * @param height The height of the screen
     * @param parent The parent screen
     * @return The generated button
     */
    public static ButtonWidget doneButton(int width, int height, Screen parent) {
        return new ButtonWidget(width / 2 - 100, height - 27, 200, 20, I18n.translate("gui.done"),
                button -> MinecraftClient.getInstance().openScreen(parent));
    }

    /**
     * Makes text coordinates based on the position of an icon.
     *
     * @param text         The text to be drawn
     * @param screenWidth  The width of the screen
     * @param textRenderer The text renderer
     * @param x            The x coordinate of the icon
     * @param y            The y coordinate of the icon
     * @param width        The width of the icon
     * @param height       The height of the icon
     * @return The tuple of coordinates
     */
    public static Tuple2<Integer, Integer> getTextCoords(String text, int screenWidth, TextRenderer textRenderer, int x, int y, int width, int height) {
        int rx = x - ((screenWidth + width) / 2);
        int textX = x + (width / 2) - (textRenderer.getStringWidth(text) / 2); // center
        int textY = y + height + 2 - textRenderer.fontHeight; // center

        if (Math.abs(rx) >= 2) {
            textY = y + height - (textRenderer.fontHeight / 2); // left/right

            if (rx < 0) textX = x + width + 2; // left
            else textX = x - 2 - textRenderer.getStringWidth(text); // right
        }

        return new Tuple2<>(textX, textY);
    }

    /**
     * Makes text coordinates based on the position of a standard 16x16 icon.
     *
     * @param text         The text to be drawn
     * @param screenWidth  The width of the screen
     * @param textRenderer The text renderer
     * @param x            The x coordinate of the icon
     * @param y            The y coordinate of the icon
     * @return The tuple of coordinates
     * @see Ukutils#getTextCoords(String, int, TextRenderer, int, int, int, int)
     */
    public static Tuple2<Integer, Integer> getTextCoords(String text, int screenWidth, TextRenderer textRenderer, int x, int y) {
        return getTextCoords(text, screenWidth, textRenderer, x, y, 16, 16);
    }

    /**
     * Simple 2-tuple.
     *
     * @param <T1> The type of the first element
     * @param <T2> The type of the second element
     */
    @Getter
    @AllArgsConstructor
    public static class Tuple2<T1, T2> {
        private T1 t1;
        private T2 t2;
    }

    private Ukutils() {
    }
}
