package net.uku3lig.ukulib.config;

import lombok.Getter;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.config.serialization.ConfigSerializer;
import net.uku3lig.ukulib.config.serialization.TomlConfigSerializer;

import java.io.Serializable;
import java.util.Locale;

/**
 * Manages a config, by holding it, saving it and loading it.
 *
 * @param <T> The type of the config
 */
public class ConfigManager<T extends Serializable> implements SimpleSynchronousResourceReloadListener {
    private final ConfigSerializer<T> serializer;

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
     * @param serializer The serializer
     * @param config     The initial config
     */
    public ConfigManager(ConfigSerializer<T> serializer, T config) {
        this.serializer = serializer;
        this.config = config;
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this);
    }

    /**
     * Creates a manager which provides an initial config by deserializing the file.
     *
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
     * @param name        The name of the config, used for the filename
     * @param <T>         The type of the config
     * @return The generated config manager
     */
    public static <T extends Serializable> ConfigManager<T> createDefault(Class<T> configClass, String name) {
        return new ConfigManager<>(new TomlConfigSerializer<>(configClass, name));
    }

    /**
     * Saves the stored config.
     */
    public void saveConfig() {
        this.serializer.serialize(config);
    }

    /**
     * Replaces the current config with a new one, serializing it in the process.
     *
     * @param newConfig The new config
     */
    public void replaceConfig(T newConfig) {
        this.config = newConfig;
        this.serializer.serialize(newConfig);
    }

    /**
     * Resets the config to its default, from a default provided by the {@link ConfigSerializer}.
     */
    public void resetConfig() {
        this.replaceConfig(this.serializer.makeDefault());
    }

    // Config reload stuff :3
    @Override
    public Identifier getFabricId() {
        String className = String.join(".", config.getClass().getPackageName(), config.getClass().getSimpleName());
        return new Identifier("ukulib", className.toLowerCase(Locale.ROOT) + "_reloader");
    }

    @Override
    public void reload(ResourceManager manager) {
        this.replaceConfig(this.serializer.deserialize());
    }
}
