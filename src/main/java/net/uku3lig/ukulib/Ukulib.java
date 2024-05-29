package net.uku3lig.ukulib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.sync.VersionQueryC2SPacket;
import net.uku3lig.ukulib.sync.VersionQueryS2CPacket;

public class Ukulib implements ModInitializer {
    public static final String MOD_ID = "ukulib";
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();

    @Override
    public void onInitialize() {
        // fabric
        PayloadTypeRegistry.configurationC2S().register(VersionQueryC2SPacket.PACKET_ID, VersionQueryC2SPacket.PACKET_CODEC);
        PayloadTypeRegistry.configurationS2C().register(VersionQueryS2CPacket.PACKET_ID, VersionQueryS2CPacket.PACKET_CODEC);

        // paper
        PayloadTypeRegistry.playC2S().register(VersionQueryC2SPacket.PACKET_ID, VersionQueryC2SPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(VersionQueryS2CPacket.PACKET_ID, VersionQueryS2CPacket.PACKET_CODEC);
    }

    public static Identifier identifier(final String path) {
        return Identifier.of(MOD_ID, path);
    }
}
