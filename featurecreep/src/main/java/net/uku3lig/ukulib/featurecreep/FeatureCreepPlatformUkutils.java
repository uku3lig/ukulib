package net.uku3lig.ukulib.featurecreep;

import com.mojang.blaze3d.platform.NativeImage;
import featurecreep.api.FCLoaderObtainer;
import featurecreep.api.bg.mc.config.FCKeyBridge;
import featurecreep.api.bg.mc.config.FCMinecraftConfig;
import featurecreep.loader.FCLoaderBasic;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.packs.resources.IoSupplier;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.config.ConfigManagerReloader;
import net.uku3lig.ukulib.utils.ModMeta;
import net.uku3lig.ukulib.utils.PlatformUkutils;
import org.jboss.modules.Module;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.function.UnaryOperator;

public class FeatureCreepPlatformUkutils implements PlatformUkutils {
    static {
        FCMinecraftConfig.registerReloader(() -> new ConfigManagerReloader().onResourceManagerReload(Minecraft.getInstance().getResourceManager()));
    }//For when the game reloads the configs reload so we use this to handle that

    @Override
    public Path getConfigPath(String name) {
        return FCLoaderObtainer.getFCLoaderBasic(FeatureCreepPlatformUkutils.class).getGameProvider().getGameWorkingDirectory().resolve("config").resolve(name);//Historically FC has preferred ./etc/ for configs but ukulib has historically preferred ./config/ so we will use that
    }

    @Override
    public void registerKeyBindingInternal(KeyMapping keyMapping) {
        FCKeyBridge.register(keyMapping);
    }

    @Override
    public Map<ModMeta, UnaryOperator<Screen>> getConfigMods() {
        FCLoaderBasic loader = FCLoaderObtainer.getFCLoaderBasic(FeatureCreepPlatformUkutils.class);
        Map<String, Module> modules = new LinkedHashMap<>();
        loader.getModules().forEach(module -> modules.put(module.getProperty("FeatureCreep-Mod-Id"), module));

        Map<ModMeta, UnaryOperator<Screen>> mods = new LinkedHashMap<>();
        modules.values().forEach(module -> {
            try {
                Iterator<UkulibAPI> apis = ServiceLoader.load(UkulibAPI.class, module.getClassLoader()).iterator();
                while (apis.hasNext()) {
                    UkulibAPI api;
                    try {
                        api = apis.next();
                    } catch (ServiceConfigurationError e) {
                        System.err.println("ukulib skipped an UkulibAPI provider in " + module.getName() + ": " + e.getMessage());
                        continue;
                    }
                    if (!isProvidedBy(api, loader, module)) continue;
                    if (api.supplyConfigScreen() != null) mods.put(metaOf(module), api.supplyConfigScreen());

                    api.getProvidedConfigScreens().forEach((modId, screen) -> {
                        if (screen != null && modules.containsKey(modId)) mods.put(metaOf(modules.get(modId)), screen);
                    });
                }
            } catch (ServiceConfigurationError e) {
                System.err.println("ukulib skipped UkulibAPI providers in " + module.getName() + ": " + e.getMessage());
            }
        });

        return mods;
    }

    //The class loader also sees providers from other app-classpath jars, so only count the ones this module declares. */
    private static boolean isProvidedBy(UkulibAPI api, FCLoaderBasic loader, Module module) {
        var entry = loader.getModuleLoadingMap().get(module.getName());
        CodeSource src = api.getClass().getProtectionDomain().getCodeSource();
        if (entry == null || entry.getSourceFile() == null || src == null) {return false;}
        try {
            File jar = new File(src.getLocation().toURI());
            return jar.getCanonicalFile().equals(entry.getSourceFile().getCanonicalFile());
        } catch (IOException | URISyntaxException | IllegalArgumentException e) {
            return false;
        }
    }

    private static ModMeta metaOf(Module module) {
        return new ModMeta(module.getProperty("FeatureCreep-Mod-Id"), module.getProperty("FeatureCreep-Name"),
                module.getProperty("FeatureCreep-Description"), iconOf(module));
    }

    private static Optional<IoSupplier<NativeImage>> iconOf(Module module) {
        String path = module.getProperty("FeatureCreep-Icon");
        URL icon = path == null ? null : module.getClassLoader().getResource(path);
        if (icon == null) return Optional.empty();
        return Optional.of(() -> {
            try (InputStream in = icon.openStream()) {
                return NativeImage.read(in);
            }
        });
    }
}
