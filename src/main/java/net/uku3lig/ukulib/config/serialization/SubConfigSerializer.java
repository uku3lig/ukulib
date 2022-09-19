package net.uku3lig.ukulib.config.serialization;

import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.IConfig;
import net.uku3lig.ukulib.config.ISubConfig;

import java.util.function.Function;

public class SubConfigSerializer<T extends ISubConfig<T, P>, P extends IConfig<P>> implements ConfigSerializer<T> {
    private final ConfigManager<P> parentManager;
    private final Function<P, T> getter;

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
