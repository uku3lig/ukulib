package net.uku3lig.ukulib.config;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Reloads all config managers when the resource manager reloads (e.g. on F3+T).
 */
public class ConfigManagerReloader implements ResourceManagerReloadListener {
    private static final Set<ConfigManager<?>> managers = new HashSet<>();

    /**
     * Adds a config manager to be reloaded.
     *
     * @param manager the config manager
     */
    public static void addManager(ConfigManager<?> manager) {
        managers.add(manager);
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager manager) {
        managers.forEach(this::reloadConfig);
    }

    @Override
    public @NotNull String getName() {
        return "ukulib_config_reloader";
    }

    // I FUCKING LOVE JAVA GENERICS
    private <T extends Serializable> void reloadConfig(ConfigManager<T> manager) {
        manager.replaceConfig(manager.getSerializer().deserialize());
    }

    /**
     * Constructor.
     */
    public ConfigManagerReloader() {
    }
}
