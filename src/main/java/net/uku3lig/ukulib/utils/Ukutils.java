package net.uku3lig.ukulib.utils;

import net.minecraft.client.gui.Screen;
import net.uku3lig.ukulib.config.ConfigOption;
import net.uku3lig.ukulib.mixin.MinecraftAccessor;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Simple class for various utilities.
 */
@SuppressWarnings("unused")
public class Ukutils {
    /**
     * Creates a {@link ConfigOption} which acts as a simple button.
     * Equivalent to {@link Ukutils#createButton(int, String, String, Consumer) createButton(key, Text.of(String.valueOf(value)), callback)}.
     *
     * @param text The translation key of the button text
     * @param value The value to be displayed after the colon
     * @param callback The action to be performed when the button is clicked
     * @return The generated option
     * @see Ukutils#createButton(int, String, String, Consumer)
     */
    public static ConfigOption<Boolean> createButton(int id, String text, Object value, Consumer<Screen> callback) {
        return createButton(id, text, String.valueOf(value), callback);
    }


    /**
     * Creates a {@link ConfigOption} which acts as a simple button.
     * Equivalent to {@link Ukutils#createOpenButton(int, String, String, UnaryOperator) createOpenButton(key, Text.of(String.valueOf(value)), callback)}.
     *
     * @param text The translation key of the button text
     * @param value The value to be displayed after the colon
     * @param callback The getter for the screen to be opened when the button is clicked
     * @return The generated option
     * @see Ukutils#createButton(int, String, String, Consumer)
     */
    public static ConfigOption<Boolean> createOpenButton(int id, String text, Object value, UnaryOperator<Screen> callback) {
        return createOpenButton(id, text, String.valueOf(value), callback);
    }

    /**
     * Creates a {@link ConfigOption} which acts as a simple button.
     *
     * @param text The translation key of the button text
     * @param value The text to be displayed after the colon
     * @param callback The action to be performed when the button is clicked
     * @return The generated option
     */
    public static ConfigOption<Boolean> createButton(int id, String text, String value, Consumer<Screen> callback) {
        return new ConfigOption<>(id, text, false, false, () -> false, b -> value,
                f -> callback.accept(MinecraftAccessor.getInstance().currentScreen));
    }

    /**
     * Creates a {@link ConfigOption} which acts as a simple button that opens a screen when clicked.
     *
     * @param text The translation key of the button text
     * @param value The text to be displayed after the colon
     * @param callback The getter for the screen to be opened when the button is clicked
     * @return The generated option
     */
    public static ConfigOption<Boolean> createOpenButton(int id, String text, String value, UnaryOperator<Screen> callback) {
        return new ConfigOption<>(id, text, false, false, () -> false, b -> value,
                f -> MinecraftAccessor.getInstance().openScreen(callback.apply(MinecraftAccessor.getInstance().currentScreen)));
    }

    private Ukutils() {}
}
