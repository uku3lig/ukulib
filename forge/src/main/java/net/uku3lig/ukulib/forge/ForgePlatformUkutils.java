package net.uku3lig.ukulib.forge;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.IModInfo;
import net.uku3lig.ukulib.utils.ModMeta;
import net.uku3lig.ukulib.utils.PlatformUkutils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class ForgePlatformUkutils implements PlatformUkutils {
    @Override
    public Path getConfigPath(String name) {
        return FMLPaths.CONFIGDIR.get().resolve(name);
    }

    @Override
    public void registerKeyBindingInternal(KeyMapping keyMapping) {
        // we do nothing here, see UkulibForge
    }

    @Override
    public Map<ModMeta, UnaryOperator<Screen>> getConfigMods() {
        Map<ModMeta, UnaryOperator<Screen>> mods = new LinkedHashMap<>();
        getContainers().forEach((container, screen) -> mods.put(fromForge(container), screen));
        return mods;
    }

    public static Map<ModContainer, UnaryOperator<Screen>> getContainers() {
        Map<ModContainer, UnaryOperator<Screen>> containers = new LinkedHashMap<>();

        ModList.forEachModInOrder(c -> c.getCustomExtension(ConfigScreenHandler.ConfigScreenFactory.class)
                .map(ConfigScreenHandler.ConfigScreenFactory::screenFunction)
                .ifPresent(factory -> containers.put(c, parent -> factory.apply(Minecraft.getInstance(), parent))));

        return containers;
    }

    private static ModMeta fromForge(ModContainer container) {
        IModInfo info = container.getModInfo();
        Optional<IoSupplier<NativeImage>> icon = info.getLogoFile().flatMap(path -> {
            Path logo = info.getOwningFile().getFile().findResource(path);
            if (!Files.isRegularFile(logo)) return Optional.empty();
            return Optional.of(() -> NativeImage.read(Files.newInputStream(logo)));
        });
        return new ModMeta(container.getModId(), info.getDisplayName(), info.getDescription(), icon);
    }
}
