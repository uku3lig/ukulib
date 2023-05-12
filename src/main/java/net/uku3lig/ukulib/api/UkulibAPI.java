package net.uku3lig.ukulib.api;

import net.minecraft.client.gui.screen.Screen;

import java.util.Collections;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * This interface is used to integrate your mod with Ukulib.
 */
public interface UkulibAPI {
    /**
     * This method is used to show your mod's config screen in the Ukulib config screen.
     * The lambda parameter is the parent screen.
     * @return A function that takes a parent screen and returns a config screen.
     */
    default UnaryOperator<Screen> supplyConfigScreen() {
        return null;
    }

    /**
     * This method is used to enable or disable the integration with Mod Menu.
     * @return Whether to enable the integration with Mod Menu.
     */
    default boolean enableModMenuIntegration() {
        return true;
    }

    /**
     * The map of the config screens, linked to their mod id.
     *
     * @return the map
     */
    default Map<String, UnaryOperator<Screen>> getProvidedConfigScreens() {
        return Collections.emptyMap();
    }
}
