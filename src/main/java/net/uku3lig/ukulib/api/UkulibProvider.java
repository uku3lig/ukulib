package net.uku3lig.ukulib.api;

import net.minecraft.client.gui.screen.Screen;

import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * This interface can be used to provide *other* config screens to ukulib.
 * <p>
 * Do NOT use this to add your own config screen, use {@link UkulibAPI} instead.
 */
public interface UkulibProvider {
    /**
     * The map of the config screens, linked to their mod id.
     *
     * @return the map
     */
    Map<String, UnaryOperator<Screen>> getProvidedConfigScreens();
}
