package net.uku3lig.ukulib;

import lombok.SneakyThrows;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.impl.UkulibServerConfig;
import net.uku3lig.ukulib.sync.VersionQueryC2SPacket;
import net.uku3lig.ukulib.sync.VersionQueryS2CPacket;
import net.uku3lig.ukulib.sync.VersionQueryTask;

public class UkulibServer implements DedicatedServerModInitializer {
    @SneakyThrows(VersionParsingException.class)
    @Override
    public void onInitializeServer() {
        VersionPredicate predicate = VersionPredicate.parse(UkulibServerConfig.getManager().getConfig().getVersionRange());

        ServerConfigurationConnectionEvents.CONFIGURE.register((handler, server) -> {
            if (ServerConfigurationNetworking.canSend(handler, VersionQueryS2CPacket.PACKET_ID)) {
                handler.addTask(new VersionQueryTask());
            } else {
                handler.disconnect(Text.of("Unsupported ukulib version! Please connect with " + predicate));
            }
        });

        ServerConfigurationNetworking.registerGlobalReceiver(VersionQueryC2SPacket.PACKET_ID, (packet, sender) -> {
            Version version = null;
            try {
                version = Version.parse(packet.version());
            } catch (VersionParsingException e) {
                e.printStackTrace();
            }

            if (version != null && predicate.test(version)) {
                sender.networkHandler().completeTask(VersionQueryTask.KEY);
            } else {
                sender.networkHandler().disconnect(Text.of("Unsupported ukulib version (" + packet.version() + ")! Please connect with " + predicate));
            }
        });
    }
}
