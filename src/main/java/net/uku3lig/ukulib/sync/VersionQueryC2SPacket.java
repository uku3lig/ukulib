package net.uku3lig.ukulib.sync;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.Ukulib;

public record VersionQueryC2SPacket(String version) implements CustomPayload {
    public static final Id<VersionQueryC2SPacket> PACKET_ID = new Id<>(Identifier.of(Ukulib.MOD_ID, "version_query_c2s"));
    public static final PacketCodec<PacketByteBuf, VersionQueryC2SPacket> PACKET_CODEC = PacketCodecs.string(50).xmap(VersionQueryC2SPacket::new, VersionQueryC2SPacket::version).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
