package net.uku3lig.ukulib.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Simple class for various utilities.
 */
@SuppressWarnings("unused")
public class Ukutils {
    /**
     * Creates a {@link SimpleOption} which acts as a simple button.
     * Equivalent to {@link Ukutils#createButton(String, Text, Consumer) createButton(key, Text.empty(), callback)}
     *
     * @param key The translation key of the button text
     * @param callback The action to be performed when the button is clicked
     * @return The generated option
     * @see Ukutils#createButton(String, Text, Consumer)
     */
    public static SimpleOption<Boolean> createButton(String key, Consumer<Screen> callback) {
        return createButton(key, Text.empty(), callback);
    }

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
     * Equivalent to {@link Ukutils#createOpenButton(String, Text, UnaryOperator) createOpenButton(key, Text.of(String.valueOf(value)), callback)}.
     *
     * @param key The translation key of the button text
     * @param value The value to be displayed after the colon
     * @param callback The getter for the screen to be opened when the button is clicked
     * @return The generated option
     * @see Ukutils#createButton(String, Text, Consumer)
     */
    public static SimpleOption<Boolean> createOpenButton(String key, Object value, UnaryOperator<Screen> callback) {
        return createOpenButton(key, Text.of(String.valueOf(value)), callback);
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

    private Ukutils() {}
}
