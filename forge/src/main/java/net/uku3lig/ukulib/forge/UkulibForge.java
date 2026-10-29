package net.uku3lig.ukulib.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.uku3lig.ukulib.config.ConfigManagerReloader;
import net.uku3lig.ukulib.utils.Ukutils;

@Mod(UkulibForge.MOD_ID)
public final class UkulibForge {
    public static final String MOD_ID = "ukulib";

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static final class ClientEvents {
        private ClientEvents() {
        }

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            Ukutils.getKeybindings().keySet().forEach(event::register);
        }

        @SubscribeEvent
        public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(new ConfigManagerReloader());
        }
    }
}
