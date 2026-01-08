package net.uku3lig.ukulib.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.uku3lig.ukulib.config.ConfigManagerReloader;

public class UkulibFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResourceLoader.get(PackType.CLIENT_RESOURCES)
                .registerReloadListener(Identifier.fromNamespaceAndPath("ukulib", "config_reloader"), new ConfigManagerReloader());
    }
}
