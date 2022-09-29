package net.uku3lig.ukulib.config.serialization;

import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.IConfig;
import net.uku3lig.ukulib.config.ISubConfig;

import java.util.function.Function;

/**
 * Manages the serialization of a sub-config, which is stored inside the main config file, under a table.
 * @param <T> The type of the config
 * @param <P> The type of the parent config
 */
public class SubConfigSerializer<T extends ISubConfig<T, P>, P extends IConfig<P>> implements ConfigSerializer<T> {
    private final ConfigManager<P> parentManager;
    private final Function<P, T> getter;

    /**
     * Creates a sub-config serializer
     * @param parentManager The manager of the parent config
     * @param getter The function used to get the sub-config from the parent
     */
    public SubConfigSerializer(ConfigManager<P> parentManager, Function<P, T> getter) {
        this.parentManager = parentManager;
        this.getter = getter;
    }

    @Override
    public T deserialize() {
        return getter.apply(parentManager.getConfig());
    }

    @Override
    public void serialize(T config) {
        // do nothing, serialization is managed by the parent serializer
    }
}
