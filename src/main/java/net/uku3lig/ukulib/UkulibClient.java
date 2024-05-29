package net.uku3lig.ukulib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.uku3lig.ukulib.sync.VersionQueryC2SPacket;
import net.uku3lig.ukulib.sync.VersionQueryS2CPacket;

public class UkulibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        String version = Ukulib.MOD_CONTAINER.getMetadata().getVersion().getFriendlyString();

        // fabric
        ClientConfigurationNetworking.registerGlobalReceiver(VersionQueryS2CPacket.PACKET_ID, (payload, context) -> context.responseSender().sendPacket(new VersionQueryC2SPacket(version)));

        // paper
        ClientPlayNetworking.registerGlobalReceiver(VersionQueryS2CPacket.PACKET_ID, (payload, context) -> context.responseSender().sendPacket(new VersionQueryC2SPacket(version)));
    }
}
