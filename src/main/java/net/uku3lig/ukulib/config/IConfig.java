package net.uku3lig.ukulib.config;

import java.io.Serializable;

/**
 * An interface which represents a config.
 * @param <T> The class itself
 */
public interface IConfig<T extends IConfig<T>> extends Serializable {
    /**
     * The default configuration, set by the developer when implementing the class. Used for serialization.
     * @return The default configuration
     */
    T defaultConfig();
}
