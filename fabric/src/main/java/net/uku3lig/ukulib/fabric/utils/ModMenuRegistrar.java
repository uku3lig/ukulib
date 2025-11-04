package net.uku3lig.ukulib.fabric.utils;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.config.impl.UkulibConfig;
import net.uku3lig.ukulib.config.impl.UkulibConfigScreen;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is used to integrate mods that use ukulib with Mod Menu.
 */
public class ModMenuRegistrar implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return UkulibConfigScreen::new;
    }

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        if (!UkulibConfig.get().isModMenuIntegration())
            return Collections.emptyMap();

        return FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class).stream()
                .filter(c -> c.getEntrypoint().enableModMenuIntegration() && c.getEntrypoint().supplyConfigScreen() != null)
                .collect(Collectors.toMap(c -> c.getProvider().getMetadata().getId(), c -> parent -> c.getEntrypoint().supplyConfigScreen().apply(parent)));
    }

    /**
     * Empty default constructor, required for the entrypoint to be constructed. See #18
     */
    public ModMenuRegistrar() {
        // see javadoc
    }
}
