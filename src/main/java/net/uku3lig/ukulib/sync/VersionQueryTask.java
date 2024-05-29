package net.uku3lig.ukulib.sync;

import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayerConfigurationTask;

import java.util.function.Consumer;

public class VersionQueryTask implements ServerPlayerConfigurationTask {
    public static final Key KEY = new Key("ukulib:version_query_task");

    @Override
    public Key getKey() {
        return KEY;
    }

    @Override
    public void sendPacket(Consumer<Packet<?>> sender) {
        sender.accept(ServerConfigurationNetworking.createS2CPacket(VersionQueryS2CPacket.INSTANCE));
    }
}
