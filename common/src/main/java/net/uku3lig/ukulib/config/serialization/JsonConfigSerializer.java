package net.uku3lig.ukulib.config.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import net.uku3lig.ukulib.utils.PlatformUkutils;
import net.uku3lig.ukulib.utils.ReflectionUtils;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A config serializer that uses JSON via {@link Gson}.
 *
 * @param <T> The type of the config
 */
@Slf4j
public class JsonConfigSerializer<T extends Serializable> implements ConfigSerializer<T> {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Class<T> configClass;
    private final Path path;

    /**
     * Creates a serializer. Uses <code>`name`.json</code> as the file name.
     *
     * @param configClass The class of the config
     * @param name        The name of the config
     */
    public JsonConfigSerializer(Class<T> configClass, String name) {
        this.configClass = configClass;
        this.path = PlatformUkutils.INSTANCE.getConfigPath(name + ".json");
    }

    @Override
    public T deserialize() {
        if (!Files.isRegularFile(path)) {
            Path parent = path.getParent();
            T defaultConfig = makeDefault();

            if (!parent.toFile().mkdirs() && !Files.isDirectory(parent)) {
                log.warn("Could not create directory {}", parent.toAbsolutePath());
            } else {
                serialize(defaultConfig);
            }

            return defaultConfig;
        }

        try {
            return GSON.fromJson(Files.newBufferedReader(path), configClass);
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
            GSON.toJson(config, Files.newBufferedWriter(path));
        } catch (Exception e) {
            log.warn("Could not write config to file", e);
        }
    }

    @Override
    public T makeDefault() {
        return ReflectionUtils.newInstance(configClass);
    }
}
