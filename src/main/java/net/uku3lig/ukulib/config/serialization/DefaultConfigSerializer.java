package net.uku3lig.ukulib.config.serialization;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lombok.extern.slf4j.Slf4j;
import net.uku3lig.ukulib.config.IConfig;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Supplier;

@Slf4j
@ApiStatus.Internal
public class DefaultConfigSerializer<T extends IConfig<T>> implements ConfigSerializer<T> {
    private final Class<T> configClass;
    private final File file;
    private final Supplier<T> defaultConfig;

    public DefaultConfigSerializer(Class<T> configClass, File file, Supplier<T> defaultConfig) {
        this.configClass = configClass;
        this.file = file;
        this.defaultConfig = defaultConfig;
    }

    public T deserialize() {
        if (!Files.exists(file.toPath())) {
            return defaultConfig.get();
        }

        try {
            return new Toml().read(file).to(configClass);
        } catch (Exception e) {
            log.warn("A corrupted configuration file was found, overwriting it with the default config");
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
}
