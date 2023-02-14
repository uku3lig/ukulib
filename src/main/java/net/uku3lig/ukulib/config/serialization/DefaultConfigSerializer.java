package net.uku3lig.ukulib.config.serialization;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lombok.extern.log4j.Log4j2;
import net.uku3lig.ukulib.config.IConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.function.Supplier;

/**
 * A default config serializer, which saves the config in a TOML file.
 * @param <T> The type of the config
 */
@Log4j2
public class DefaultConfigSerializer<T extends IConfig<T>> implements ConfigSerializer<T> {
    private final Class<T> configClass;
    private final File file;
    private final Supplier<T> defaultConfig;

    /**
     * Creates a serializer.
     *
     * @param configClass The class of the config
     * @param file The file to save the config into
     * @param defaultConfig The default config
     */
    public DefaultConfigSerializer(Class<T> configClass, File file, Supplier<T> defaultConfig) {
        this.configClass = configClass;
        this.file = file;
        this.defaultConfig = defaultConfig;
    }

    /**
     * Reads the config from the file.
     * If the file isn't found or is corrupted, the file is overwritten by the default config.
     * @return The deserialized config
     */
    public T deserialize() {
        if (!Files.isRegularFile(file.toPath())) {
            File parent = file.getParentFile();

            if (!parent.mkdirs() && !Files.isDirectory(parent.toPath())) {
                log.warn("Could not create directory {}", parent.getAbsolutePath());
            } else {
                serialize(defaultConfig.get());
            }

            return defaultConfig.get();
        }

        try {
            T config = new Toml().read(file).to(configClass);
            if (hasNullFields(config)) throw new NullPointerException("null fields were found");
            return config;
        } catch (Exception e) {
            log.warn("A corrupted configuration file was found, overwriting it with the default config", e);
            serialize(defaultConfig.get());
            return defaultConfig.get();
        }
    }

    public void serialize(T config) {
        try {
            new TomlWriter().write(config, file);
        } catch (IOException e) {
            log.warn("Could not write config", e);
        }
    }

    private boolean hasNullFields(T instance) {
        for (Field f : configClass.getDeclaredFields()) {
            try {
                if (f.get(instance) == null) return true;
            } catch (IllegalAccessException e) {
                log.warn("Could not access field {} in class {}", f.getName(), configClass.getSimpleName());
            }
        }

        return false;
    }
}
