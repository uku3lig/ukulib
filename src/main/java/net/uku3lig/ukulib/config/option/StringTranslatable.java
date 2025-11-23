package net.uku3lig.ukulib.config.option;

import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

/**
 * Replacement for Minecraft's former <code>TranslatableOption</code>, using {@link StringIdentifiable} as a "backend".
 * This exists because <code>StringIdentifiable</code> doesn't provide any translation capability.
 */
public interface StringTranslatable extends StringIdentifiable {
    /**
     * Name of the option, also used as its unique identifier for {@link StringIdentifiable}.
     *
     * @return The name of the option
     * @see StringIdentifiable#asString()
     */
    String getName();

    /**
     * The translation key of the option
     *
     * @return The translation key
     */
    String getTranslationKey();

    @Override
    default String asString() {
        return getName();
    }

    /**
     * Constructs a default {@link Text} object containing the translated value.
     *
     * @return The text object
     * @see StringTranslatable#getTranslationKey()
     */
    default Text getText() {
        return Text.translatable(getTranslationKey());
    }
}
