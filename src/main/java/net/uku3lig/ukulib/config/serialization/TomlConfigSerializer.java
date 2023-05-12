package net.uku3lig.ukulib.config.serialization;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lombok.extern.slf4j.Slf4j;
import net.uku3lig.ukulib.utils.ReflectionUtils;
import net.uku3lig.ukulib.utils.Ukutils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

/**
 * A default config serializer, which saves the config in a TOML file.
 *
 * @param <T> The type of the config
 */
@Slf4j
public class TomlConfigSerializer<T extends Serializable> implements ConfigSerializer<T> {
    private final Class<T> configClass;
    private final File file;

    /**
     * Creates a serializer.
     *
     * @param configClass The class of the config
     * @param name        The name of the config
     */
    public TomlConfigSerializer(Class<T> configClass, String name) {
        this.configClass = configClass;
        this.file = Ukutils.getConfigPath(name + ".toml");
    }

    /**
     * Reads the config from the file.
     * If the file isn't found or is corrupted, the file is overwritten by the default config.
     *
     * @return The deserialized config
     */
    @Override
    public T deserialize() {
        if (!Files.isRegularFile(file.toPath())) {
            File parent = file.getParentFile();
            T defaultConfig = makeDefault();

            if (!parent.mkdirs() && !Files.isDirectory(parent.toPath())) {
                log.warn("Could not create directory {}", parent.getAbsolutePath());
            } else {
                serialize(defaultConfig);
            }

            return defaultConfig;
        }

        try {
            return new Toml().read(file).to(configClass);
        } catch (Exception e) {
            log.warn("A corrupted configuration file was found, overwriting it with the default config", e);
            T defaultConfig = makeDefault();
            serialize(defaultConfig);
            return defaultConfig;
        }
    }

    @Override
    public void serialize(T config) {
        try {
            new TomlWriter().write(config, file);
        } catch (IOException e) {
            log.warn("Could not write config", e);
        }
    }

    @Override
    public T makeDefault() {
        return ReflectionUtils.newInstance(configClass);
    }
}
