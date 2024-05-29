package net.uku3lig.ukulib.sync;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.Ukulib;

public class VersionQueryS2CPacket implements CustomPayload {
    public static final VersionQueryS2CPacket INSTANCE = new VersionQueryS2CPacket();
    public static final Id<VersionQueryS2CPacket> PACKET_ID = new Id<>(Identifier.of(Ukulib.MOD_ID, "version_query_s2c"));
    public static final PacketCodec<PacketByteBuf, VersionQueryS2CPacket> PACKET_CODEC = PacketCodec.unit(INSTANCE);


    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
