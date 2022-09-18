package net.uku3lig.ukulib.config;

import lombok.Getter;
import net.uku3lig.ukulib.config.serialization.ConfigSerializer;
import net.uku3lig.ukulib.utils.ReflectionUtils;

import java.io.File;
import java.util.function.Supplier;

public class ConfigManager<T extends IConfig<T>> {
    private final ConfigSerializer<T> serializer;
    @Getter
    private final T config;

    public ConfigManager(ConfigSerializer<T> serializer, T config) {
        this.serializer = serializer;
        this.config = config;
    }

    public ConfigManager(ConfigSerializer<T> serializer) {
        this(serializer, serializer.deserialize());
    }

    public static <T extends IConfig<T>> ConfigManager<T> create(Class<T> configClass, String name) {
        String filename = "./config/" + name + ".toml";
        Supplier<T> defaultConfig = () -> ReflectionUtils.newInstance(configClass).defaultConfig();
        return new ConfigManager<>(new ConfigSerializer<>(configClass, new File(filename), defaultConfig));
    }

    public void saveConfig() {
        serializer.serialize(config);
    }
}
