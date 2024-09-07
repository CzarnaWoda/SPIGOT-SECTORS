package pl.supereasy.sectors.api.packets.api;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import pl.supereasy.sectors.api.netty.enums.BufferType;
import pl.supereasy.sectors.api.netty.enums.SourceType;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Packet {


    public abstract void handlePacket(final PacketHandler handler);
}
