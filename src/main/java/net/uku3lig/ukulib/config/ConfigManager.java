package net.uku3lig.ukulib.config;

import lombok.Getter;
import net.uku3lig.ukulib.config.serialization.ConfigSerializer;
import net.uku3lig.ukulib.config.serialization.DefaultConfigSerializer;
import net.uku3lig.ukulib.utils.ReflectionUtils;

import java.io.File;
import java.util.function.Supplier;

/**
 * Manages a config, by holding it, saving it and loading it.
 * @param <T> The type of the config
 */
public class ConfigManager<T extends IConfig<T>> {
    private final ConfigSerializer<T> serializer;

    /**
     * The config held by the manager.
     * @return The config
     */
    @Getter
    private final T config;

    /**
     * Creates a manager.
     * @param serializer The serializer
     * @param config The initial config
     */
    public ConfigManager(ConfigSerializer<T> serializer, T config) {
        this.serializer = serializer;
        this.config = config;
    }

    /**
     * Creates a manager which provides an initial config by deserializing the file.
     * @param serializer The serializer
     */
    public ConfigManager(ConfigSerializer<T> serializer) {
        this(serializer, serializer.deserialize());
    }

    /**
     * Creates a default config manager, with a default config serializer.
     * The config will be saved to and read from <code>./config/[name].toml</code> (without the brackets).
     *
     * @param configClass The class of the config
     * @param name The name of the config, used for the filename
     * @return The generated config manager
     * @param <T> The type of the config
     */
    public static <T extends IConfig<T>> ConfigManager<T> create(Class<T> configClass, String name) {
        String filename = "./config/" + name + ".toml";
        Supplier<T> defaultConfig = () -> ReflectionUtils.newInstance(configClass).defaultConfig();
        return new ConfigManager<>(new DefaultConfigSerializer<>(configClass, new File(filename), defaultConfig));
    }

    /**
     * Saves the stored config.
     */
    public void saveConfig() {
        serializer.serialize(config);
    }
}
