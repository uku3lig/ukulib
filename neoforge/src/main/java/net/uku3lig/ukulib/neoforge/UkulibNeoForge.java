package net.uku3lig.ukulib.neoforge;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.resource.ResourcePackLoader;
import net.neoforged.neoforgespi.language.IModInfo;
import net.uku3lig.ukulib.Ukulib;
import net.uku3lig.ukulib.config.ConfigManagerReloader;
import net.uku3lig.ukulib.config.impl.UkulibConfigScreen;
import net.uku3lig.ukulib.neoforge.utils.UkutilsNeoForge;
import net.uku3lig.ukulib.utils.ModMeta;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Mod(value = "ukulib", dist = Dist.CLIENT)
public class UkulibNeoForge {
    public UkulibNeoForge(IEventBus modEventBus, ModContainer container) {
        Ukulib.onPreLaunch(new UkutilsNeoForge());

        modEventBus.register(this);

        container.registerExtensionPoint(IConfigScreenFactory.class, (c, screen) -> new UkulibConfigScreen(screen));

        Ukulib.onInitialize(this::getConfigMods);
    }

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        Ukulib.getUtils().getKeybindings().keySet().forEach(event::register);
    }

    @SubscribeEvent
    public void addClientReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(ResourceLocation.fromNamespaceAndPath("ukulib", "config_reloader"), new ConfigManagerReloader());
    }

    // TODO figure out a way to remove the supplier
    private Map<ModMeta, UnaryOperator<Screen>> getConfigMods() {
        Map<ModMeta, UnaryOperator<Screen>> mods = new LinkedHashMap<>();
        ModList.get().getSortedMods().forEach(c -> {
            Optional<UkulibNFProvider> opt = c.getCustomExtension(UkulibNFProvider.class);
            opt.ifPresent(api -> {
                mods.put(fromNeoForge(c), api.get().supplyConfigScreen());
                // TODO provided screens
                if (api.get().enableModMenuIntegration()) {
                    c.registerExtensionPoint(IConfigScreenFactory.class,
                            (c2, screen) -> api.get().supplyConfigScreen().apply(screen));
                }
            });
        });

        return mods;
    }

    private ModMeta fromNeoForge(ModContainer container) {
        IModInfo info = container.getModInfo();
        Optional<IoSupplier<InputStream>> logo = Optional.empty();

        if (info.getLogoFile().isPresent()) {
            final Pack.ResourcesSupplier resourcePack = ResourcePackLoader.getPackFor(container.getModId())
                    .or(() -> ResourcePackLoader.getPackFor("neoforge"))
                    .orElseThrow(() -> new RuntimeException("Can't find neoforge, WHAT!"));

            PackLocationInfo packInfo = new PackLocationInfo("mod/" + container.getModId(), Component.empty(), PackSource.BUILT_IN, Optional.empty());

            try (PackResources packResources = resourcePack.openPrimary(packInfo)) {
                IoSupplier<InputStream> logoResource = packResources.getRootResource(info.getLogoFile().get().split("[/\\\\]"));
                logo = Optional.ofNullable(logoResource);
            }
        }

        return new ModMeta(container.getModId(), info.getDisplayName(), info.getDescription(), logo);
    }
}
