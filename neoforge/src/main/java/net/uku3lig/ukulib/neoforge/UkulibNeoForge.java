package net.uku3lig.ukulib.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.uku3lig.ukulib.Ukulib;
import net.uku3lig.ukulib.config.impl.UkulibConfigScreen;
import net.uku3lig.ukulib.neoforge.utils.UkutilsNeoForge;

@Mod(value = "ukulib", dist = Dist.CLIENT)
public class UkulibNeoForge {
    public UkulibNeoForge(IEventBus modEventBus, ModContainer container) {
        Ukulib.onPreLaunch(new UkutilsNeoForge());

        modEventBus.register(this);

        container.registerExtensionPoint(IConfigScreenFactory.class, (c, screen) -> new UkulibConfigScreen(screen));
    }

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        Ukulib.getUtils().getKeybindings().keySet().forEach(event::register);
    }
}
