package net.uku3lig.ukulib.config;

import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.uku3lig.ukulib.config.serialization.ConfigSerializer;
import net.uku3lig.ukulib.config.serialization.TomlConfigSerializer;

import java.io.Serializable;
import java.nio.file.Path;

/**
 * Manages a config, by holding it, saving it and loading it.
 *
 * @param <T> The type of the config
 */
public class ConfigManager<T extends Serializable> {
    private final ConfigSerializer<T> serializer;

    /**
     * The class of the held config
     *
     * @return The class
     */
    @Getter
    private final Class<T> configClass;

    /**
     * The config held by the manager.
     *
     * @return The config
     */
    @Getter
    private T config;

    /**
     * Creates a manager.
     *
     * @param configClass The class of the config
     * @param serializer  The serializer
     * @param config      The initial config
     */
    public ConfigManager(Class<T> configClass, ConfigSerializer<T> serializer, T config) {
        this.configClass = configClass;
        this.serializer = serializer;
        this.config = config;
    }

    /**
     * Creates a manager which provides an initial config by deserializing the file.
     *
     * @param configClass The class of the config
     * @param serializer  The serializer
     */
    public ConfigManager(Class<T> configClass, ConfigSerializer<T> serializer) {
        this(configClass, serializer, serializer.deserialize());
    }

    /**
     * Creates a default config manager, with a default config serializer.
     * The config will be saved to and read from <code>./config/[name].toml</code> (without the brackets).
     *
     * @param configClass The class of the config
     * @param name        The name of the config, used for the filename
     * @param <T>         The type of the config
     * @return The generated config manager
     */
    public static <T extends Serializable> ConfigManager<T> createDefault(Class<T> configClass, String name) {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(name + ".toml");
        return new ConfigManager<>(configClass, new TomlConfigSerializer<>(configClass, path.toFile()));
    }

    /**
     * Saves the stored config.
     */
    public void saveConfig() {
        serializer.serialize(config);
    }

    /**
     * Replaces the current config with a new one, serializing it in the process.
     *
     * @param newConfig The new config
     */
    public void replaceConfig(T newConfig) {
        config = newConfig;
        serializer.serialize(newConfig);
    }

    /**
     * Resets the config to its default, from a default provided by the {@link ConfigSerializer}.
     */
    public void resetConfig() {
        this.serializer.serialize(this.serializer.makeDefault());
    }
}
