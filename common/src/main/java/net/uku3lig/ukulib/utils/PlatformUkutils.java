package net.uku3lig.ukulib.utils;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Modding platform-specific utilities. Mostly for ukulib internal stuff, but feel free to use :-)
 */
public interface PlatformUkutils {
    /**
     * Singleton instance of the class.
     */
    PlatformUkutils INSTANCE = Services.load(PlatformUkutils.class);

    /**
     * Creates a config path for a file name.
     *
     * @param name The name of the file
     * @return The path to the file
     */
    Path getConfigPath(String name);

    /**
     * Registers a keybinding in Minecraft via the modding platform.
     *
     * @param keyMapping The keybinding
     */
    void registerKeyBindingInternal(KeyMapping keyMapping);

    /**
     * Fetches and collects the mods using the ukulib config system.
     *
     * @return All the registered mods
     */
    Map<ModMeta, UnaryOperator<Screen>> getConfigMods();
}
