package net.uku3lig.ukulib.config.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import net.uku3lig.ukulib.utils.ReflectionUtils;
import net.uku3lig.ukulib.utils.Ukutils;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class JsonConfigSerializer<T extends Serializable> implements ConfigSerializer<T> {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Class<T> configClass;
    private final Path path;

    public JsonConfigSerializer(Class<T> configClass, String name) {
        this.configClass = configClass;
        this.path = Ukutils.getConfigPath(name + ".json");
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
