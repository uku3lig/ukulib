package net.uku3lig.ukulib.neoforge;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.uku3lig.ukulib.config.ConfigManagerReloader;
import net.uku3lig.ukulib.config.impl.UkulibConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

@Mod(value = "ukulib", dist = Dist.CLIENT)
public class UkulibNeoForge {
    public UkulibNeoForge(IEventBus modEventBus, ModContainer container) {
        modEventBus.register(this);
        container.registerExtensionPoint(IConfigScreenFactory.class, (_, screen) -> new UkulibConfigScreen(screen));
    }

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        Ukutils.getKeybindings().keySet().forEach(event::register);
    }

    @SubscribeEvent
    public void addClientReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(Identifier.fromNamespaceAndPath("ukulib", "config_reloader"), new ConfigManagerReloader());
    }

    @SubscribeEvent
    public void onFinishedLoading(FMLLoadCompleteEvent event) {
        NeoForgePlatformUkutils.getContainers().forEach((container, screenFactory) ->
                container.registerExtensionPoint(IConfigScreenFactory.class, (_, screen) -> screenFactory.apply(screen)));
    }
}
