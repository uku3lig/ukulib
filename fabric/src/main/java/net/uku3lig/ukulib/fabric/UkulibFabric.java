package net.uku3lig.ukulib.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.uku3lig.ukulib.Ukulib;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.config.ConfigManagerReloader;
import net.uku3lig.ukulib.fabric.utils.UkutilsFabric;
import net.uku3lig.ukulib.utils.ModMeta;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class UkulibFabric implements PreLaunchEntrypoint, ModInitializer {
    @Override
    public void onPreLaunch() {
        Ukulib.onPreLaunch(new UkutilsFabric());
    }

    @Override
    public void onInitialize() {
        ResourceLoader.get(PackType.CLIENT_RESOURCES)
                .registerReloader(ResourceLocation.fromNamespaceAndPath("ukulib", "config_reloader"), new ConfigManagerReloader());

        Map<ModMeta, UnaryOperator<Screen>> mods = new LinkedHashMap<>();
        FabricLoader.getInstance().getEntrypointContainers("ukulib", UkulibAPI.class)
                .forEach(entry -> {
                    if (entry.getEntrypoint().supplyConfigScreen() != null)
                        mods.put(this.fromFabric(entry.getProvider()), entry.getEntrypoint().supplyConfigScreen());

                    entry.getEntrypoint().getProvidedConfigScreens().forEach((modId, screen) -> {
                        if (screen != null) {
                            FabricLoader.getInstance().getModContainer(modId)
                                    .ifPresent(c -> mods.put(fromFabric(c), screen));
                        }
                    });
                });

        Ukulib.onInitialize(mods);
    }

    private ModMeta fromFabric(ModContainer mod) {
        ModMetadata metadata = mod.getMetadata();
        Optional<Path> path = metadata.getIconPath(32).flatMap(mod::findPath);

        return new ModMeta(metadata.getId(), metadata.getName(), metadata.getDescription(), path);
    }
}
