package net.uku3lig.ukulib.config.impl;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.TextInputScreen;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Basic implementation of {@link TextInputScreen} used to input a {@link String} of any type.
 */
public class StringInputScreen extends TextInputScreen<String> {
    /**
     * Creates an input screen.
     *
     * @param parent   The parent screen
     * @param title    The title of the screen
     * @param label    The label to be shown above the text input field
     * @param callback The action to be performed when the value is changed
     * @param last     The last known value
     * @param manager  The config manager, used to save the config
     */
    public StringInputScreen(Screen parent, Text title, Text label, Consumer<String> callback, String last, ConfigManager<?> manager) {
        super(parent, title, label, callback, last, manager);
    }

    @Override
    protected Optional<String> convert(String value) {
        return Optional.ofNullable(value);
    }
}
