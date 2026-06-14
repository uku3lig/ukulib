package net.uku3lig.ukulib.neoforge;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.resource.ResourcePackLoader;
import net.neoforged.neoforgespi.language.IModInfo;
import net.uku3lig.ukulib.utils.ModMeta;
import net.uku3lig.ukulib.utils.PlatformUkutils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class NeoForgePlatformUkutils implements PlatformUkutils {
    private static final Logger log = LoggerFactory.getLogger(NeoForgePlatformUkutils.class);

    @Override
    public Path getConfigPath(String name) {
        return FMLPaths.CONFIGDIR.get().resolve(name);
    }

    @Override
    public void registerKeyBindingInternal(KeyMapping keyMapping) {
        // we do nothing here, see UkulibNeoForge
    }

    @Override
    public Map<ModMeta, UnaryOperator<Screen>> getConfigMods() {
        Map<ModMeta, UnaryOperator<Screen>> mods = new LinkedHashMap<>();
        getContainers().forEach((container, screen) -> mods.put(fromNeoForge(container), screen));
        return mods;
    }

    public static Map<ModContainer, UnaryOperator<Screen>> getContainers() {
        Map<ModContainer, UnaryOperator<Screen>> containers = new LinkedHashMap<>();

        ModList.get().getSortedMods().forEach(c -> {
            Optional<UkulibNFProvider> opt = c.getCustomExtension(UkulibNFProvider.class);
            opt.map(UkulibNFProvider::get).ifPresent(api -> {
                containers.put(c, api.supplyConfigScreen());

                api.getProvidedConfigScreens().forEach((id, screen) -> {
                    if (screen != null) {
                        ModList.get().getModContainerById(id).ifPresent(c2 -> containers.put(c2, screen));
                    }
                });
            });
        });

        return containers;
    }

    private ModMeta fromNeoForge(ModContainer container) {
        IModInfo info = container.getModInfo();
        Optional<IoSupplier<@NotNull NativeImage>> logo = Optional.empty();

        if (info.getLogoFile().isPresent()) {
            final Pack.ResourcesSupplier resourcePack = ResourcePackLoader.getPackFor(container.getModId())
                    .or(() -> ResourcePackLoader.getPackFor("neoforge"))
                    .orElseThrow(() -> new RuntimeException("Can't find neoforge, WHAT!"));

            PackLocationInfo packInfo = new PackLocationInfo("mod/" + container.getModId(), Component.empty(), PackSource.BUILT_IN, Optional.empty());

            // this isn't very pretty, ideally you'd use the io supplier to load the image fully
            // however since the PackResources is closed we get an exception, so we have to eagerly load it
            try (PackResources packResources = resourcePack.openPrimary(packInfo)) {
                IoSupplier<@NotNull InputStream> logoResource = packResources.getRootResource(info.getLogoFile().get().split("[/\\\\]"));
                if (logoResource != null) {
                    try {
                        NativeImage image = NativeImage.read(logoResource.get());
                        logo = Optional.of(() -> image);
                    } catch (IOException e) {
                        log.warn("Failed to load icon from mod jar", e);
                    }
                }
            }
        }

        return new ModMeta(container.getModId(), info.getDisplayName(), info.getDescription(), logo);
    }
}
