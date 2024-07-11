package com.neptunecentury.fixedlevels;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

/**
 * An implementation of the CustomPayload to store config options to encode and send to the client
 *
 * @param curveMode
 * @param baseXPForOneLevel
 * @param curveModeMultiplier
 */
public record ConfigPayload(boolean curveMode, int baseXPForOneLevel,
                            int curveModeMultiplier) implements CustomPayload {
    public static final CustomPayload.Id<ConfigPayload> ID = new CustomPayload.Id<>(FixedLevels.CONFIG_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, ConfigPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, ConfigPayload::curveMode,
            PacketCodecs.INTEGER, ConfigPayload::baseXPForOneLevel,
            PacketCodecs.INTEGER, ConfigPayload::curveModeMultiplier,
            ConfigPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}