package net.uku3lig.ukulib.fabric;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.packs.resources.IoSupplier;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.utils.ModMeta;
import net.uku3lig.ukulib.utils.PlatformUkutils;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class FabricPlatformUkutils implements PlatformUkutils {
    @Override
    public Path getConfigPath(String name) {
        return FabricLoader.getInstance().getConfigDir().resolve(name);
    }

    @Override
    public void registerKeyBindingInternal(KeyMapping keyMapping) {
        KeyBindingHelper.registerKeyBinding(keyMapping);
    }

    @Override
    public Map<ModMeta, UnaryOperator<Screen>> getConfigMods() {
        Map<ModMeta, UnaryOperator<Screen>> mods = new LinkedHashMap<>();

        FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class)
                .forEach(entry -> {
                    if (entry.getEntrypoint().supplyConfigScreen() != null)
                        mods.put(containerToMeta(entry.getProvider()), entry.getEntrypoint().supplyConfigScreen());

                    entry.getEntrypoint().getProvidedConfigScreens().forEach((modId, screen) -> {
                        if (screen != null) {
                            FabricLoader.getInstance().getModContainer(modId).ifPresent(c -> mods.put(containerToMeta(c), screen));
                        }
                    });
                });

        return mods;
    }

    private static ModMeta containerToMeta(ModContainer container) {
        ModMetadata metadata = container.getMetadata();
        var logo = metadata.getIconPath(32).flatMap(container::findPath).map(IoSupplier::create);
        return new ModMeta(metadata.getId(), metadata.getName(), metadata.getDescription(), logo);
    }
}
