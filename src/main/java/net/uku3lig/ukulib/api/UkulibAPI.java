package net.uku3lig.ukulib.api;

import net.minecraft.client.gui.screen.Screen;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;

import java.util.function.Function;

/**
 * This interface is used to integrate your mod with Ukulib.
 */
public interface UkulibAPI {
    /**
     * This method is used to show your mod's config screen in the Ukulib config screen.
     * @return A function that takes a parent screen and returns a config screen.
     */
    Function<Screen, AbstractConfigScreen<?>> supplyConfigScreen();
}
