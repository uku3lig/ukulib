package net.uku3lig.ukulib.utils;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.uku3lig.ukulib.api.UkulibAPI;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is used to integrate mods that use ukulib with Mod Menu.
 */
public class ModMenuRegistrar implements ModMenuApi {
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        return FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class).stream()
                .filter(c -> c.getEntrypoint().enableModMenuIntegration())
                .collect(Collectors.toMap(c -> c.getProvider().getMetadata().getId(), c -> parent -> c.getEntrypoint().supplyConfigScreen().apply(parent)));
    }
}
