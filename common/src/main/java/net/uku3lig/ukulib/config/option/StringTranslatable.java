package net.uku3lig.ukulib.config.option;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/**
 * Replacement for Minecraft's former <code>TranslatableOption</code>, using {@link StringRepresentable} as a "backend".
 * This exists because <code>StringIdentifiable</code> doesn't provide any translation capability.
 */
// TODO figure out how to serialize to #getName
public interface StringTranslatable extends StringRepresentable {
    /**
     * Name of the option, also used as its unique identifier for {@link StringRepresentable}.
     *
     * @return The name of the option
     * @see StringRepresentable#getSerializedName()
     */
    String getName();

    /**
     * The translation key of the option
     *
     * @return The translation key
     */
    String getTranslationKey();

    @Override
    default @NotNull String getSerializedName() {
        return getName();
    }

    /**
     * Constructs a default {@link Component} object containing the translated value.
     *
     * @return The text object
     * @see StringTranslatable#getTranslationKey()
     */
    default Component getText() {
        return Component.translatable(getTranslationKey());
    }
}
