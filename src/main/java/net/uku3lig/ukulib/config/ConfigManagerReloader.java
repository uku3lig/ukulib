package net.uku3lig.ukulib.config;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Reloads all config managers when the resource manager reloads (e.g. on F3+T).
 */
public class ConfigManagerReloader implements SimpleSynchronousResourceReloadListener {
    private static final Set<ConfigManager<?>> managers = new HashSet<>();

    static {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ConfigManagerReloader());
    }

    /**
     * Adds a config manager to be reloaded.
     *
     * @param manager the config manager
     */
    public static void addManager(ConfigManager<?> manager) {
        managers.add(manager);
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier("ukulib", "config_reloader");
    }

    @Override
    public void reload(ResourceManager manager) {
        managers.forEach(this::reloadConfig);
    }

    // I FUCKING LOVE JAVA GENERICS
    private <T extends Serializable> void reloadConfig(ConfigManager<T> manager) {
        manager.replaceConfig(manager.getSerializer().deserialize());
    }

    private ConfigManagerReloader() {}
}
